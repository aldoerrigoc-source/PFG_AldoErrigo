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
 * Evaluator for R2 - JavaScript DOM (nursery inventory).
 *
 * The base code only renders a read-only table. The student must build the
 * whole inline-edit flow (edit button -> input -> save -> fetch -> DOM
 * update) plus a session edit counter.
 *
 * p1: the base read-only table renders correctly (rows + images + values).
 * p2: clicking Edit shows an input pre-filled with the current stock value.
 * p3: Save persists the new value (cell updates, no reload) and the edit
 *     counter reads 1 right after the first successful save.
 * p4: the counter keeps accumulating (reaches 2) after a second successful
 *     save, still without any page reload.
 */
public class EvaluadorR2 {

    private static final String BASE_URL = "http://localhost:8082/";

    // Row used for the first edit: id=2 ("Lavender"), initial stock=30.
    private static final int ROW_1_ID = 2;
    private static final int ROW_1_NEW_VALUE = 45;

    // Row used for the second edit: id=3 ("Aloe Vera"), initial stock=18.
    private static final int ROW_2_ID = 3;
    private static final int ROW_2_NEW_VALUE = 7;

    // Context path of each student's deployed WAR on Tomcat.
    private static final String[] STUDENTS = {
        "student1", "student2", "student3"
    };

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true));

            try (PrintWriter csv = new PrintWriter(new FileWriter("resultado_r2.csv"))) {
                csv.println("student;p1_base;p2_edit_input;p3_save_and_counter;p4_counter_accumulates;grade");

                for (String student : STUDENTS) {
                    double p1 = 0, p2 = 0, p3 = 0, p4 = 0;

                    try (BrowserContext context = browser.newContext()) {
                        Page page = context.newPage();

                        page.navigate(BASE_URL + student + "/");
                        page.click("#linkInventory");
                        page.waitForSelector("#inventoryTable");

                        // Mark this load so we can detect a full page reload later.
                        page.evaluate("() => { window.__noReloadMarker = true; }");

                        // --- p1: base read-only table is intact ---
                        long rowCount = ((Number) page.evaluate(
                            "() => document.querySelectorAll('#inventoryTable tr').length - 1")).longValue();
                        long imgCount = ((Number) page.evaluate(
                            "() => document.querySelectorAll('#inventoryTable img').length")).longValue();
                        String stockBefore = String.valueOf(page.evaluate(
                            "(id) => document.getElementById('stock-' + id).textContent.trim()", ROW_1_ID));

                        boolean baseStructureOk = rowCount >= 4 && imgCount >= 4;
                        boolean baseValueOk = extractNumber(stockBefore) == 30;

                        if (baseStructureOk && baseValueOk) {
                            p1 = 1.0;
                        } else if (baseStructureOk) {
                            p1 = 0.5;
                        }

                        // --- p2: Edit shows a pre-filled input ---
                        page.click(".edit-btn[data-id=\"" + ROW_1_ID + "\"]");
                        page.waitForSelector("#stock-input-" + ROW_1_ID);

                        String inputValue = String.valueOf(page.evaluate(
                            "(id) => document.getElementById('stock-input-' + id).value", ROW_1_ID));

                        p2 = (extractNumber(inputValue) == extractNumber(stockBefore)) ? 1.0 : 0.0;

                        // --- p3: Save persists the value and the counter starts reporting 1 ---
                        page.fill("#stock-input-" + ROW_1_ID, String.valueOf(ROW_1_NEW_VALUE));
                        page.click(".save-btn[data-id=\"" + ROW_1_ID + "\"]");
                        page.waitForFunction(
                            "(args) => document.getElementById('stock-' + args.id).textContent.trim() === String(args.value)",
                            twoArgs(ROW_1_ID, ROW_1_NEW_VALUE));

                        boolean savedOk = String.valueOf(page.evaluate(
                            "(id) => document.getElementById('stock-' + id).textContent.trim()", ROW_1_ID))
                            .equals(String.valueOf(ROW_1_NEW_VALUE));

                        Integer counterAfterFirstSave = readCounter(page);

                        if (savedOk && counterAfterFirstSave != null && counterAfterFirstSave == 1) {
                            p3 = 1.0;
                        } else if (savedOk) {
                            p3 = 0.5;
                        }

                        // --- p4: counter accumulates across a second successful save ---
                        page.click(".edit-btn[data-id=\"" + ROW_2_ID + "\"]");
                        page.waitForSelector("#stock-input-" + ROW_2_ID);
                        page.fill("#stock-input-" + ROW_2_ID, String.valueOf(ROW_2_NEW_VALUE));
                        page.click(".save-btn[data-id=\"" + ROW_2_ID + "\"]");
                        page.waitForFunction(
                            "(args) => document.getElementById('stock-' + args.id).textContent.trim() === String(args.value)",
                            twoArgs(ROW_2_ID, ROW_2_NEW_VALUE));

                        Integer counterAfterSecondSave = readCounter(page);
                        boolean noReload = ((Boolean) page.evaluate("() => window.__noReloadMarker === true"));

                        if (noReload && counterAfterSecondSave != null && counterAfterSecondSave == 2) {
                            p4 = 1.0;
                        } else if (counterAfterSecondSave != null && counterAfterSecondSave >= 2) {
                            p4 = 0.5;
                        }

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

    private static java.util.Map<String, Object> twoArgs(int id, int value) {
        java.util.Map<String, Object> args = new java.util.HashMap<String, Object>();
        args.put("id", id);
        args.put("value", value);
        return args;
    }

    private static Integer readCounter(Page page) {
        Object el = page.evaluate(
            "() => { const e = document.getElementById('editCounter'); return e ? e.textContent : null; }");
        if (el == null) {
            return null;
        }
        Matcher m = Pattern.compile("\\d+").matcher(String.valueOf(el));
        if (m.find()) {
            return Integer.parseInt(m.group());
        }
        return null;
    }

    private static int extractNumber(String text) {
        Matcher m = Pattern.compile("\\d+").matcher(text);
        return m.find() ? Integer.parseInt(m.group()) : -1;
    }

    private static String format(double value) {
        return String.format(Locale.GERMANY, "%.2f", value);
    }
}
