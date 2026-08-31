import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import java.util.Locale;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EVALUADOR AUTOMATICO - Test Practico R2 (JavaScript DOM)
 * Misma estructura que EvaluadorR1: una sola pagina, descubre alumnos desde el
 * indice, cada criterio aislado y con timeout corto -> nunca se cuelga.
 *
 * Corrige lo mismo que la version anterior:
 *   p1: la tabla base sigue intacta (filas + imagenes).
 *   p2: al editar aparece un input precargado con el valor actual.
 *   p3: guardar persiste el valor (sin recarga) y el contador de sesion marca 1.
 *   p4: el contador acumula a 2 tras una segunda edicion, sin recarga.
 */
public class EvaluadorR2 {

    // Filas usadas para editar (datos del codigo base neutro).
    private static final int ROW_1_ID = 2;
    private static final int ROW_1_NEW_VALUE = 45;
    private static final int ROW_2_ID = 3;
    private static final int ROW_2_NEW_VALUE = 7;

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
            );
            Page page = browser.newPage();
            page.setDefaultTimeout(3000); // fallar rapido: nada de esperas de 30s

            String urlBase = "http://localhost:8082/";
            page.navigate(urlBase);

            @SuppressWarnings("unchecked")
            List<String> urlsAlumnos = (List<String>) page.evaluate(
                "() => Array.from(document.querySelectorAll('a')).map(a => a.href)"
            );

            try (PrintWriter writer = new PrintWriter(new File("resultado_r2.csv"))) {

                writer.print(java.time.LocalDateTime.now() + "\n");
                writer.println("Alumno;p1-Base;p2-InputEdit;p3-GuardaYContador;p4-ContadorAcumula;Total");

                for (String fullUrlAlumno : urlsAlumnos) {
                    if (fullUrlAlumno == null
                            || fullUrlAlumno.contains("javascript:")
                            || fullUrlAlumno.equals(urlBase)) continue;

                    String targetUrl = fullUrlAlumno;
                    if (!targetUrl.endsWith("/")) targetUrl += "/";
                    targetUrl += "inventory";

                    // Cada criterio vale 2.5 (0 / 1.25 / 2.5) -> las 4 columnas suman 10, como en R1.
                    double p1 = 0, p2 = 0, p3 = 0, p4 = 0;

                    try {
                        page.navigate(targetUrl);
                        page.waitForLoadState(LoadState.NETWORKIDLE);
                    } catch (Exception e) {
                        writer.println(fullUrlAlumno + ";Error nav: " + firstLine(e) + ";;;;0");
                        continue;
                    }

                    // Marca para detectar una recarga completa mas tarde.
                    try { page.evaluate("() => { window.__noReload = true; }"); } catch (Exception ignored) {}

                    // --- p1: la tabla base esta intacta (solo inspeccion) ---
                    try {
                        long rows = ((Number) page.evaluate(
                            "() => document.querySelectorAll('#inventoryTable tr').length - 1")).longValue();
                        long imgs = ((Number) page.evaluate(
                            "() => document.querySelectorAll('#inventoryTable img').length")).longValue();
                        if (rows >= 4 && imgs >= 4) p1 = 2.5;
                        else if (rows >= 1)         p1 = 1.25;
                    } catch (Exception ignored) {}

                    // Valor inicial de la fila 1 (para comparar en p2).
                    String stockBefore = "";
                    try {
                        stockBefore = String.valueOf(page.evaluate(
                            "(id) => { const e = document.getElementById('stock-'+id); return e ? e.textContent.trim() : ''; }",
                            ROW_1_ID));
                    } catch (Exception ignored) {}

                    // --- p2: al editar aparece input precargado ---
                    try {
                        page.click(".edit-btn[data-id=\"" + ROW_1_ID + "\"]");
                        page.waitForSelector("#stock-input-" + ROW_1_ID);
                        String inputValue = String.valueOf(page.evaluate(
                            "(id) => document.getElementById('stock-input-'+id).value", ROW_1_ID));
                        if (num(inputValue) >= 0 && num(inputValue) == num(stockBefore)) p2 = 2.5;
                    } catch (Exception ignored) {}

                    // --- p3: guardar persiste + contador == 1 ---
                    try {
                        if (page.locator("#stock-input-" + ROW_1_ID).count() == 0) {
                            page.click(".edit-btn[data-id=\"" + ROW_1_ID + "\"]");
                            page.waitForSelector("#stock-input-" + ROW_1_ID);
                        }
                        page.fill("#stock-input-" + ROW_1_ID, String.valueOf(ROW_1_NEW_VALUE));
                        page.click(".save-btn[data-id=\"" + ROW_1_ID + "\"]");
                        page.waitForFunction(
                            "(a) => document.getElementById('stock-'+a.id).textContent.trim() === String(a.value)",
                            args(ROW_1_ID, ROW_1_NEW_VALUE));
                        boolean saved = String.valueOf(page.evaluate(
                            "(id) => document.getElementById('stock-'+id).textContent.trim()", ROW_1_ID))
                            .equals(String.valueOf(ROW_1_NEW_VALUE));
                        Integer c = counter(page);
                        if (saved && c != null && c == 1) p3 = 2.5;
                        else if (saved)                   p3 = 1.25;
                    } catch (Exception ignored) {}

                    // --- p4: el contador acumula a 2, sin recarga ---
                    try {
                        page.click(".edit-btn[data-id=\"" + ROW_2_ID + "\"]");
                        page.waitForSelector("#stock-input-" + ROW_2_ID);
                        page.fill("#stock-input-" + ROW_2_ID, String.valueOf(ROW_2_NEW_VALUE));
                        page.click(".save-btn[data-id=\"" + ROW_2_ID + "\"]");
                        page.waitForFunction(
                            "(a) => document.getElementById('stock-'+a.id).textContent.trim() === String(a.value)",
                            args(ROW_2_ID, ROW_2_NEW_VALUE));
                        Integer c = counter(page);
                        boolean noReload = false;
                        try { noReload = (Boolean) page.evaluate("() => window.__noReload === true"); } catch (Exception ignored) {}
                        if (noReload && c != null && c == 2) p4 = 2.5;
                        else if (c != null && c >= 2)        p4 = 1.25;
                    } catch (Exception ignored) {}

                    double total = p1 + p2 + p3 + p4;

                    writer.print(fullUrlAlumno + ";");
                    writer.printf(Locale.GERMANY, "%.2f;%.2f;%.2f;%.2f;%.2f\n", p1, p2, p3, p4, total);
                }
            }

            browser.close();
            System.out.println("Evaluacion R2 finalizada. Revisa resultado_r2.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> args(int id, int value) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("id", id);
        m.put("value", value);
        return m;
    }

    private static Integer counter(Page page) {
        try {
            Object el = page.evaluate(
                "() => { const e = document.getElementById('editCounter'); return e ? e.textContent : null; }");
            if (el == null) return null;
            Matcher m = Pattern.compile("\\d+").matcher(String.valueOf(el));
            return m.find() ? Integer.parseInt(m.group()) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static int num(String s) {
        if (s == null) return -1;
        Matcher m = Pattern.compile("\\d+").matcher(s);
        return m.find() ? Integer.parseInt(m.group()) : -1;
    }

    private static String firstLine(Exception e) {
        String msg = e.getMessage();
        if (msg == null) return e.getClass().getSimpleName();
        int nl = msg.indexOf('\n');
        return nl > 0 ? msg.substring(0, nl) : msg;
    }
}