import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CatalogServlet extends HttpServlet {

    private static Vector<Record> getRecords() {
        Vector<Record> records = new Vector<Record>();
        records.add(new Record(1, "Abbey Road", "The Beatles", "img/vinyl.svg", 24.99));
        records.add(new Record(2, "Rumours", "Fleetwood Mac", "img/vinyl.svg", 19.50));
        records.add(new Record(3, "Thriller", "Michael Jackson", "img/vinyl.svg", 22.75));
        records.add(new Record(4, "Back in Black", "AC/DC", "img/vinyl.svg", 18.00));
        records.add(new Record(5, "Nevermind", "Nirvana", "img/vinyl.svg", 21.30));
        records.add(new Record(6, "The Dark Side of the Moon", "Pink Floyd", "img/vinyl.svg", 26.40));
        return records;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Vector<Record> records = getRecords();

        out.println("<html><head><title>Vinyl Catalog</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 80%; margin: 20px auto; }");
        out.println("th, td { border: 1px solid #999; padding: 8px; text-align: center; }");
        out.println("th { background-color: #333; color: white; }");
        out.println("img { width: 50px; height: 50px; }");
        out.println("</style></head><body>");
        out.println("<h1>Vinyl Catalog</h1>");
        out.println("<table id=\"catalogTable\">");
        out.println("<tr><th>Cover</th><th>Title</th><th>Artist</th><th>Price ($)</th></tr>");

        for (Record r : records) {
            out.println("<tr>");
            out.println("<td><img src=\"" + r.getCover() + "\" alt=\"cover\"></td>");
            out.println("<td>" + r.getTitle() + "</td>");
            out.println("<td>" + r.getArtist() + "</td>");
            out.println("<td>" + String.format("%.2f", r.getPrice()) + "</td>");
            out.println("</tr>");
        }

        // TODO (student): add a summary row here with the total and the
        // average of the price column, visually distinguished from the
        // rest of the table rows.

        out.println("</table>");
        out.println("</body></html>");
    }
}
