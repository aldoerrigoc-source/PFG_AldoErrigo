import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

/**
 * Corrector automatico del test de SQL sobre Biblio.accdb.
 *
 * Estructura de carpetas esperada:
 *   entregas/
 *     alu001.sql
 *     alu002.sql
 *     ...
 *
 * Cada fichero .sql de un estudiante debe contener las 4 consultas
 * separadas por marcadores de comentario:
 *
 *   -- ITEM 1
 *   SELECT ...;
 *   -- ITEM 2
 *   SELECT ...;
 *   -- ITEM 3
 *   SELECT ...;
 *   -- ITEM 4
 *   SELECT ...;
 *
 * Salida: resultados.csv en la carpeta raiz, con una fila por (alumno, item).
 *
 * Uso: mvn compile exec:java
 *      (ajustar RUTA_BIBLIO abajo antes de ejecutar)
 */
public class CorrectorSQL {

    // ---- Configuracion ----
    static final String RUTA_BIBLIO = "C:\\Users\\Aldoa\\OneDrive\\Desktop\\PFG\\Tec.Info\\Biblio.accdb"; // AJUSTAR
    static final String CARPETA_ENTREGAS = "entregas";
    static final String CSV_SALIDA = "resultados.csv";
    static final int TIMEOUT_SEGUNDOS = 3;

    // Consultas de referencia del test (ver diseño del test de SQL)
    // El booleano indica si el orden de las filas importa para ese item.
    static final Map<Integer, String> REFERENCIA = new LinkedHashMap<>();
    static final Map<Integer, Boolean> ORDEN_IMPORTA = new LinkedHashMap<>();
    static {
        REFERENCIA.put(1,
            "SELECT Au_Id, Author FROM Authors WHERE [Year Born] > 1950");
        ORDEN_IMPORTA.put(1, false);

        REFERENCIA.put(2,
            "SELECT Name, City, Zip FROM Publishers WHERE City <> 'New York' ORDER BY Zip DESC");
        ORDEN_IMPORTA.put(2, true);

        REFERENCIA.put(3,
            "SELECT Authors.Author, COUNT(*) AS cantidad_libros " +
            "FROM Authors INNER JOIN [Title Author] ON Authors.Au_ID = [Title Author].Au_ID " +
            "GROUP BY Authors.Author HAVING COUNT(*) > 1 ORDER BY COUNT(*) DESC");
        ORDEN_IMPORTA.put(3, true);

        REFERENCIA.put(4,
            "SELECT Titles.Title, Authors.Author, Publishers.Name " +
            "FROM (Titles INNER JOIN [Title Author] ON Titles.ISBN = [Title Author].ISBN) " +
            "INNER JOIN Authors ON [Title Author].Au_ID = Authors.Au_ID " +
            "INNER JOIN Publishers ON Titles.PubID = Publishers.PubID");
        ORDEN_IMPORTA.put(4, false);
    }

    static final Pattern ITEM_MARKER = Pattern.compile("--\\s*ITEM\\s*(\\d+)", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) throws Exception {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

        File carpetaEntregas = new File(CARPETA_ENTREGAS);
        File[] ficherosEstudiantes = carpetaEntregas.listFiles((dir, name) -> name.endsWith(".sql"));

        if (ficherosEstudiantes == null || ficherosEstudiantes.length == 0) {
            System.out.println("No se encontraron entregas en " + CARPETA_ENTREGAS);
            return;
        }

        Locale.setDefault(Locale.GERMANY);
        try (PrintWriter csv = new PrintWriter(new FileWriter(CSV_SALIDA))) {
            csv.println("alumno;item;resultado;detalle");

            for (File fichero : ficherosEstudiantes) {
                String alumno = fichero.getName().replace(".sql", "");
                Map<Integer, String> consultasAlumno = parsearItems(fichero);

                for (int item : REFERENCIA.keySet()) {
                    String consultaAlumno = consultasAlumno.get(item);
                    Fila resultado = corregirItem(alumno, item, consultaAlumno);
                    csv.println(alumno + ";" + item + ";" + resultado.estado + ";" + resultado.detalle);
                    System.out.println(alumno + " - item " + item + ": " + resultado.estado);
                }
            }
        }

        System.out.println("Corregido. Resultados en " + CSV_SALIDA);
    }

    /** Ejecuta y compara un item, con timeout y manejo de errores aislado. */
    static Fila corregirItem(String alumno, int item, String consultaAlumno) {
        if (consultaAlumno == null || consultaAlumno.trim().isEmpty()) {
            return new Fila("FALTA", "el alumno no entrego este item");
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try (Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + RUTA_BIBLIO)) {

            Future<List<List<String>>> futureAlumno = executor.submit(() -> ejecutar(conn, consultaAlumno));
            List<List<String>> filasAlumno;
            try {
                filasAlumno = futureAlumno.get(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS);
            } catch (TimeoutException te) {
                futureAlumno.cancel(true);
                return new Fila("ERROR", "timeout (" + TIMEOUT_SEGUNDOS + "s) - posible bucle o consulta mal formada");
            } catch (ExecutionException ee) {
                return new Fila("ERROR", "sintaxis invalida: " + causaLimpia(ee));
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return new Fila("ERROR", "ejecucion interrumpida");
            }

            List<List<String>> filasReferencia = ejecutar(conn, REFERENCIA.get(item));

            boolean ordenImporta = ORDEN_IMPORTA.get(item);
            boolean correcto = compararResultados(filasAlumno, filasReferencia, ordenImporta);

            return correcto
                ? new Fila("OK", "")
                : new Fila("FALLO", "resultado no coincide (" + filasAlumno.size() + " filas obtenidas, "
                                     + filasReferencia.size() + " esperadas)");

        } catch (SQLException e) {
            return new Fila("ERROR", "error de conexion/BD: " + e.getMessage());
        } finally {
            executor.shutdownNow();
        }
    }

    /** Ejecuta una consulta SELECT y devuelve las filas como listas de strings. */
    static List<List<String>> ejecutar(Connection conn, String sql) throws SQLException {
        List<List<String>> filas = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int numCols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> fila = new ArrayList<>();
                for (int i = 1; i <= numCols; i++) {
                    Object valor = rs.getObject(i);
                    fila.add(valor == null ? "NULL" : valor.toString().trim());
                }
                filas.add(fila);
            }
        }
        return filas;
    }

    /** Compara dos conjuntos de filas. Si el orden no importa, se normalizan (ordenan) antes de comparar. */
    static boolean compararResultados(List<List<String>> a, List<List<String>> b, boolean ordenImporta) {
        if (a.size() != b.size()) return false;
        if (a.isEmpty()) return true;
        if (a.get(0).size() != b.get(0).size()) return false; // mismo numero de columnas

        List<String> filasA = new ArrayList<>();
        List<String> filasB = new ArrayList<>();
        for (List<String> f : a) filasA.add(String.join("|", f));
        for (List<String> f : b) filasB.add(String.join("|", f));

        if (!ordenImporta) {
            Collections.sort(filasA);
            Collections.sort(filasB);
        }
        return filasA.equals(filasB);
    }

    /** Extrae los 4 items de un fichero de entrega separado por marcadores "-- ITEM n". */
    static Map<Integer, String> parsearItems(File fichero) throws IOException {
        String contenido = new String(Files.readAllBytes(fichero.toPath()));
        Map<Integer, String> items = new LinkedHashMap<>();

        Matcher m = ITEM_MARKER.matcher(contenido);
        List<int[]> posiciones = new ArrayList<>(); // [numItem, posInicioContenido]
        List<Integer> numsItem = new ArrayList<>();
        List<Integer> posInicio = new ArrayList<>();
        while (m.find()) {
            numsItem.add(Integer.parseInt(m.group(1)));
            posInicio.add(m.end());
        }
        for (int i = 0; i < numsItem.size(); i++) {
            int inicio = posInicio.get(i);
            int fin = (i + 1 < posInicio.size()) ? contenidoIndexOfMarker(contenido, posInicio, i + 1) : contenido.length();
            String consulta = contenido.substring(inicio, fin).trim();
            // Quitar el ; final si lo tiene, UCanAccess no lo necesita en executeQuery
            if (consulta.endsWith(";")) consulta = consulta.substring(0, consulta.length() - 1).trim();
            items.put(numsItem.get(i), consulta);
        }
        return items;
    }

    static int contenidoIndexOfMarker(String contenido, List<Integer> posInicio, int idx) {
        Matcher m = ITEM_MARKER.matcher(contenido);
        int count = 0;
        while (m.find()) {
            if (count == idx) return m.start();
            count++;
        }
        return contenido.length();
    }

    static String causaLimpia(ExecutionException ee) {
        Throwable causa = ee.getCause();
        return causa != null ? causa.getMessage() : ee.getMessage();
    }

    static class Fila {
        String estado;
        String detalle;
        Fila(String estado, String detalle) {
            this.estado = estado;
            this.detalle = detalle;
        }
    }
}
