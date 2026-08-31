import java.sql.Connection;
import java.sql.DriverManager;
import javax.servlet.*;

public class ConnectionUtils {
    public static Connection getConnection(ServletConfig config) {
        Connection connection = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            // Obtener ruta real del archivo .mdb desde el contexto del servlet
            ServletContext context = config.getServletContext();
            String dbPath = context.getRealPath("Database.mdb");
            System.out.println("Base de datos que está usando Tomcat: " + dbPath);
            String url = "jdbc:ucanaccess://" + dbPath;
            connection = DriverManager.getConnection(url);
            System.out.println("Conexión establecida a Access correctamente.");
        } catch (Exception e) {
            System.out.println("Error conectando a Access:");
            e.printStackTrace();
        }
        return connection;
    }
}
