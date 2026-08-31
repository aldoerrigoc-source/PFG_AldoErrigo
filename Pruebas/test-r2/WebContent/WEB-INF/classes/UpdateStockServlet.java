import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateStockServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        int value = Integer.parseInt(request.getParameter("value"));

        for (Plant p : Data.inventory) {
            if (p.getId() == id) {
                p.setStock(value);
                break;
            }
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().print("OK");
    }
}
