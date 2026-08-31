import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Servlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html; charset=UTF-8");
        //out.println("Hello Servlet World v1");
        
        try {
            ServletContext context = getServletConfig( ).getServletContext();
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String url=new String("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};Dbq=" + context.getRealPath("database.mdb"));
            System.out.println("url: " + url);
            Connection connection = DriverManager.getConnection(url);
            
            String sql = request.getParameter("sql");
            System.out.println("sql: " + sql);
            
            Statement statement=connection.createStatement();
            if (sql.toUpperCase().startsWith("SELECT")) {
                ResultSet result=statement.executeQuery(sql);
                ResultSetMetaData rmeta=result.getMetaData();
                int numColumns=rmeta.getColumnCount();
                for(int i=1;i<=numColumns;++i) {
                    if(i<numColumns)
                        System.out.print(rmeta.getColumnName(i)+"|");
                    else
                        System.out.println(rmeta.getColumnName(i));
                }
                
                StringBuffer outSb = new StringBuffer("");
                String c = "\"";
                
                while(result.next()) {
                    for(int i=1;i<=numColumns;++i) {
                        if (i == 1) {
                            outSb.append("{");
                        }
                        outSb.append(      c + rmeta.getColumnName(i)    + c + ":" + c + result.getString(rmeta.getColumnName(i))  + c) ;
                        if (i<numColumns) {
                            outSb.append(",");
                        } else {
                            outSb.append("}");
                        }
                    }
                    outSb.append(",");
                }
                String outp = outSb.toString();
                System.out.println ("Without StandardCharsets");
                if (outp.length() > 0) {
                    outp = outp.substring(0, outp.length() - 1);
                }
                outp ="{" + c + "records" + c + ":[" + outp + "]}";
                outp = outp.replaceAll("(\r\n|\r)", "\\n");
                out.println(outp);
            } else {
                int nrows = statement.executeUpdate(sql);
                System.out.println ("Numero de registros modificados: " + nrows);
                out.println (nrows);
            }
            connection.close();
        } catch(SQLException ex) {
            System.out.println("Error de SQLException:" + ex);
            ex.printStackTrace();
        } catch(Exception ex) {
            System.out.println("Error de Exception:" + ex);
            ex.printStackTrace();
        }
    }
}