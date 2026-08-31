import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import java.util.Locale;
import java.io.*;
import java.util.List;

public class EvaluadorR1 {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
            );
            Page page = browser.newPage();

            String urlBase = "http://localhost:8082/";
            page.navigate(urlBase);

            @SuppressWarnings("unchecked")
            List<String> urlsAlumnos = (List<String>) page.evaluate(
                "() => Array.from(document.querySelectorAll('a')).map(a => a.href)"
            );

            try (PrintWriter writer = new PrintWriter(new File("resultado_r1.csv"))) {

                writer.print(java.time.LocalDateTime.now() + "\n");
                writer.println("Alumno;p1-Accesible;p2-DevelopedBy;p3-TablaFilas;p4-CabeceraTH;p5-Min4Col;p6-Consistente;p7-Stock;p8-StockValores;p9-Imagen;p10-SrcPatron;Total");

                for (String fullUrlAlumno : urlsAlumnos) {
                    if (fullUrlAlumno == null
                            || fullUrlAlumno.contains("javascript:")
                            || fullUrlAlumno.equals(urlBase)) continue;

                    String targetUrl = fullUrlAlumno;
                    if (!targetUrl.endsWith("/")) targetUrl += "/";
                    targetUrl += "ItemList";

                    try {
                        page.navigate(targetUrl);
                        page.waitForLoadState(LoadState.NETWORKIDLE);

                        String bodyText = page.innerText("body").toLowerCase();
                        boolean esError = bodyText.contains("http status 404")
                                       || bodyText.contains("http status 500")
                                       || bodyText.contains("exception");
                        double p1 = esError ? 0 : 1.0;

                        String titleText = page.title().toLowerCase();
                        String h2Text = page.locator("h2").count() > 0
                                ? page.locator("h2").first().innerText().toLowerCase() : "";
                        String textoTitulo = titleText + " " + h2Text;
                        double p2 = 0;
                        if (textoTitulo.contains("developed by")) {
                            p2 = 0.5;
                            if (!textoTitulo.contains("nombre apellido")
                                    && !textoTitulo.contains("name surname")) {
                                p2 = 1.0;
                            }
                        }

                        double p3 = 0;
                        if (page.locator("table").count() > 0) {
                            p3 = 0.5;
                            if (page.locator("table tr").count() >= 2) p3 = 1.0;
                        }

                        long numTH = ((Number) page.locator("table th").count()).longValue();
                        double p4 = numTH >= 4 ? 1.0 : (numTH > 0 ? 0.5 : 0);
                        double p5 = numTH >= 4 ? 1.0 : 0;

                        double p6 = 0;
                        if (numTH > 0) {
                            long filasMal = ((Number) page.evaluate(
                                "() => {" +
                                "  let filas = Array.from(document.querySelectorAll('table tr'));" +
                                "  if (filas.length < 2) return filas.length;" +
                                "  let cols = filas[0].querySelectorAll('th,td').length;" +
                                "  return filas.slice(1).filter(f => f.querySelectorAll('td').length !== cols).length;" +
                                "}"
                            )).longValue();
                            if (filasMal == 0 && page.locator("table tr").count() > 1) p6 = 1.0;
                        }

                        boolean stockEnCabecera = (boolean) page.evaluate(
                            "() => Array.from(document.querySelectorAll('table th'))" +
                            "      .some(th => th.innerText.toLowerCase().includes('stock'))"
                        );
                        double p7 = stockEnCabecera ? 1.0 : 0;

                        double p8 = 0;
                        if (stockEnCabecera) {
                            int idxStock = ((Number) page.evaluate(
                                "() => Array.from(document.querySelectorAll('table th'))" +
                                "      .findIndex(th => th.innerText.toLowerCase().includes('stock'))"
                            )).intValue();

                            if (idxStock >= 0) {
                                @SuppressWarnings("unchecked")
                                List<String> valores = (List<String>) page.evaluate(
                                    "(idx) => Array.from(document.querySelectorAll('table tr'))" +
                                    "  .slice(1)" +
                                    "  .map(f => { let c = f.querySelectorAll('td'); return c[idx] ? c[idx].innerText.trim() : ''; })",
                                    idxStock
                                );
                                boolean noTodosCero = valores.stream().anyMatch(v -> !v.equals("0") && !v.isEmpty());
                                boolean sonDistintos = valores.stream().distinct().count() > 1;
                                if (noTodosCero)  p8 += 0.5;
                                if (sonDistintos) p8 += 0.5;
                            }
                        }

                        double p9 = 0;
                        long totalFilasDatos = page.locator("table tr").count() - 1;
                        if (totalFilasDatos > 0) {
                            long filasConImg = ((Number) page.evaluate(
                                "() => Array.from(document.querySelectorAll('table tr'))" +
                                "      .slice(1)" +
                                "      .filter(f => f.querySelector('img') !== null).length"
                            )).longValue();
                            if (filasConImg > 0)               p9 = 0.5;
                            if (filasConImg == totalFilasDatos) p9 = 1.0;
                        }

                        double p10 = 0;
                        if (page.locator("table tr:not(:first-child) img").count() > 0) {
                            String src = page.locator("table tr:not(:first-child) img")
                                            .first().getAttribute("src");
                            if (src != null) {
                                if (src.contains("img/"))      p10 += 0.5;
                                if (src.contains("producto_")
                                 || src.matches(".*_\\d+\\.(png|jpg|jpeg)")) p10 += 0.5;
                            }
                        }

                        double totalFinal = p1+p2+p3+p4+p5+p6+p7+p8+p9+p10;

                        writer.print(fullUrlAlumno + ";");
                        writer.printf(Locale.GERMANY,
                            "%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f\n",
                            p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, totalFinal);

                    } catch (Exception e) {
                        writer.println(fullUrlAlumno + ";Error: " + e.getMessage() + ";;;;;;;;;;0");
                    }
                }
            }

            browser.close();
            System.out.println("Evaluacion R1 finalizada. Revisa resultado_r1.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}