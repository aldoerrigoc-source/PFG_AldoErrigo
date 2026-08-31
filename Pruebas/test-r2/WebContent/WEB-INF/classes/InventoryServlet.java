import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InventoryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Nursery Inventory</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 70%; margin: 20px auto; }");
        out.println("th, td { border: 1px solid #999; padding: 8px; text-align: center; }");
        out.println("th { background-color: #333; color: white; }");
        out.println("img { width: 40px; height: 40px; }");
        out.println("</style></head><body>");
        out.println("<h1>Nursery Inventory</h1>");

        out.println("<table id=\"inventoryTable\">");
        out.println("<tr><th>Image</th><th>Name</th><th>Stock</th></tr>");

        for (Plant p : Data.inventory) {
            out.println("<tr>");
            out.println("<td><img src=\"" + p.getImage() + "\" alt=\"plant\"></td>");
            out.println("<td>" + p.getName() + "</td>");
            out.println("<td id=\"stock-" + p.getId() + "\" data-id=\"" + p.getId() + "\">" + p.getStock() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
        out.println("<script src=\"js/script.js\"></script>");
        out.println("</body></html>");
    }
}
