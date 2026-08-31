import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evaluator for R1 - Servlet Mockup (vinyl catalog).
 *
 * p1: the base catalog table is intact and functional (rows + images).
 * p2: a summary row shows the correct total price.
 * p3: a summary row shows the correct average price.
 * p4: the summary row is visually distinguished from the data rows.
 */
public class EvaluadorR1 {

    private static final String BASE_URL = "http://localhost:8082/";

    // Expected values, derived from the fixed mock data in the base code.
    private static final int EXPECTED_ROWS = 6;
    private static final double EXPECTED_TOTAL = 132.94;
    private static final double EXPECTED_AVERAGE = 22.16;
    private static final double TIGHT_EPSILON = 0.05;
    private static final double LOOSE_EPSILON = 1.0;

    // Context path of each student's deployed WAR on Tomcat.
    private static final String[] STUDENTS = {
        "alumno1", "alumno2", "alumno3"
    };

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true));

            try (PrintWriter csv = new PrintWriter(new FileWriter("resultado_r1.csv"))) {
                csv.println("student;p1_base;p2_total;p3_average;p4_visual;grade");

                for (String student : STUDENTS) {
                    double p1 = 0, p2 = 0, p3 = 0, p4 = 0;

                    try (BrowserContext context = browser.newContext()) {
                        Page page = context.newPage();

                        page.navigate(BASE_URL + student + "/");
                        page.click("#linkCatalog");
                        page.waitForSelector("#catalogTable");

                        long rowCount = ((Number) page.evaluate(
                            "() => document.querySelectorAll('#catalogTable tr').length - 1")).longValue();
                        long imgCount = ((Number) page.evaluate(
                            "() => document.querySelectorAll('#catalogTable img').length")).longValue();

                        if (rowCount >= EXPECTED_ROWS && imgCount >= EXPECTED_ROWS) {
                            p1 = 1.0;
                        } else if (rowCount > 0 && imgCount > 0) {
                            p1 = 0.5;
                        }

                        String tableText = String.valueOf(page.evaluate(
                            "() => document.querySelector('#catalogTable').innerText"));

                        p2 = scoreClosestValue(tableText, EXPECTED_TOTAL);
                        p3 = scoreClosestValue(tableText, EXPECTED_AVERAGE);

                        long visualDistinct = ((Number) page.evaluate(
                            "() => {" +
                            "  const rows = Array.from(document.querySelectorAll('#catalogTable tr')).slice(1);" +
                            "  if (rows.length < " + (EXPECTED_ROWS + 1) + ") return 0;" +
                            "  const normalRow = rows[0];" +
                            "  const lastRow = rows[rows.length - 1];" +
                            "  const nStyle = getComputedStyle(normalRow);" +
                            "  const lStyle = getComputedStyle(lastRow);" +
                            "  const boldDiff = parseInt(lStyle.fontWeight) > parseInt(nStyle.fontWeight);" +
                            "  const bgDiff = lStyle.backgroundColor !== nStyle.backgroundColor;" +
                            "  return (boldDiff || bgDiff) ? 1 : 0;" +
                            "}")).longValue();
                        p4 = (visualDistinct == 1) ? 1.0 : 0.0;

                    } catch (Exception e) {
                        System.out.println("Error evaluating student " + student + ": " + e.getMessage());
                    }

                    double grade = (p1 + p2 + p3 + p4) / 4.0 * 10.0;

                    csv.println(student + ";" +
                        format(p1) + ";" + format(p2) + ";" + format(p3) + ";" + format(p4) + ";" +
                        format(grade));
                }
            }

            browser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds, among all decimal numbers present in the text, the one closest
     * to the target value, then scores it: 1.0 within a tight tolerance
     * (correct), 0.5 within a loose tolerance (attempted but off), 0
     * otherwise (missing or unrelated).
     */
    private static double scoreClosestValue(String text, double target) {
        Matcher m = Pattern.compile("\\d+[.,]\\d+").matcher(text);
        double bestDiff = Double.MAX_VALUE;

        while (m.find()) {
            double value = Double.parseDouble(m.group().replace(",", "."));
            double diff = Math.abs(value - target);
            if (diff < bestDiff) {
                bestDiff = diff;
            }
        }

        if (bestDiff <= TIGHT_EPSILON) {
            return 1.0;
        } else if (bestDiff <= LOOSE_EPSILON) {
            return 0.5;
        }
        return 0.0;
    }

    private static String format(double value) {
        return String.format(Locale.GERMANY, "%.2f", value);
    }
}
