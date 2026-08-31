# Skill: crear-test

Invocación: "Dame el test del R{N}"

Fuente de verdad: el campo "Test asociado" del requerimiento en CLAUDE.md. Es una idea, no un guión — cada generación produce un test concreto distinto pero con el mismo rigor.

## Produce exactamente dos cosas

1. **Código base** que recibe el estudiante: scaffold funcional y correcto en todo lo que no es objeto del ejercicio. El código base entrega funcionalidad completa y correcta; p1 evalúa que esa base sigue intacta. El resto de puntos evalúan exclusivamente la extensión pedida.

2. **Evaluador** `EvaluadorR{N}.java`: sigue el esqueleto de abajo, navega desde `http://localhost:8082/`, evalúa con variables `p1, p2...`, escribe `resultado_r{N}.csv` con `;` y `Locale.GERMANY`.

## Contrato de selectores (código base ↔ evaluador)

El código base y el evaluador comparten los **mismos nombres de selectores**. Defínelos una sola vez antes de generar nada (ej. `#inventoryTable`, `.edit-btn[data-id]`, `.save-btn[data-id]`, `#stock-input-<id>`, `#stock-<id>`, `#editCounter`) y genera **ambos** artefactos a partir de esa lista. Si el código base usa `#input-` y el evaluador espera `#stock-input-`, todo falla. Este desajuste es el fallo más caro: fíjalo primero.

## Reglas del código base
- Java 8, `javax.servlet.*`, sin package, sin @WebServlet, sin librerías externas
- Dominio neutro (nunca el de ningún grupo real)
- El brief para el estudiante describe comportamiento visible, nunca menciona clases ni métodos
- Nunca contiene bugs intencionales. Toda la parte no evaluada debe funcionar sin fallos.
- Expone exactamente los selectores del contrato.

## Reglas del evaluador (estructura R1: nunca se cuelga)
- **Descubre alumnos desde el índice**, nunca una lista fija: itera sobre los `<a>` de `localhost:8082/`.
- **Timeout corto global**: `page.setDefaultTimeout(3000)` al inicio. Sin esto, un selector inexistente cuelga 30s.
- **Cada criterio en su propio try/catch**: si un `pN` falla, se queda en 0 y el evaluador pasa al siguiente. Un fallo nunca tumba a los demás criterios ni al alumno entero.
- **Ve directo al servlet objetivo** (`url + "/Servlet"`); evita navegar por clicks intermedios si puedes ir directo.
- **try/catch por alumno** que escribe una fila de error y hace `continue`: nunca abortar el CSV entero por un alumno.
- Cast numérico: `((Number) page.evaluate(...)).longValue()` — nunca `(long)` directo.
- p1 confirma que la base funciona (no que "la página carga").
- La tarea de extensión tiene peso real en la nota, no es un punto residual.
- Los valores plenos de los `pN` **suman 10**; `Total` = suma de columnas.
- Todo en inglés (CSV, comentarios, consola).

## Esqueleto del evaluador (armazón fijo; solo cambia el bloque de criterios)

```java
try (Playwright playwright = Playwright.create()) {
    Browser browser = playwright.chromium().launch(
        new BrowserType.LaunchOptions().setHeadless(false));
    Page page = browser.newPage();
    page.setDefaultTimeout(3000);                 // fallar rapido

    String urlBase = "http://localhost:8082/";
    page.navigate(urlBase);

    @SuppressWarnings("unchecked")
    List<String> alumnos = (List<String>) page.evaluate(
        "() => Array.from(document.querySelectorAll('a')).map(a => a.href)");

    try (PrintWriter writer = new PrintWriter(new File("resultado_rN.csv"))) {
        writer.print(java.time.LocalDateTime.now() + "\n");
        writer.println("Alumno;p1-...;p2-...;Total");

        for (String alumno : alumnos) {
            if (alumno == null || alumno.contains("javascript:")
                || alumno.equals(urlBase)) continue;

            String target = alumno.endsWith("/") ? alumno : alumno + "/";
            target += "SERVLET";                  // ir directo al servlet

            double p1 = 0, p2 = 0;                // cada criterio empieza en 0
            try {
                page.navigate(target);
                page.waitForLoadState(LoadState.NETWORKIDLE);
            } catch (Exception e) {
                writer.println(alumno + ";Error nav;;0");
                continue;                         // alumno inaccesible -> fila error
            }

            // ---- CRITERIOS: cada uno aislado ----
            try { /* p1: base intacta (inspeccion) */ } catch (Exception ignored) {}
            try { /* p2: extension pedida */         } catch (Exception ignored) {}
            // ... mas criterios, cada uno en su try/catch ...

            double total = p1 + p2;               // columnas suman 10
            writer.print(alumno + ";");
            writer.printf(Locale.GERMANY, "%.2f;%.2f;%.2f\n", p1, p2, total);
        }
    }
    browser.close();
} catch (Exception e) { e.printStackTrace(); }
```

## Output
Archivos en carpeta `test-r{N}/`.