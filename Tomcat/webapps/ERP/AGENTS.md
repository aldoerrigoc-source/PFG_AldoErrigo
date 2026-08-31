# AGENTS.md — ERP Project Onboarding
Read this file at the start of every session before touching any code.

---

## 1. Project Context
You are a software engineer building a web-based ERP (Enterprise Resource Planning) system for a small/medium company. The application is built as a Java Servlet web application running on Apache Tomcat with a Microsoft Access database. No frameworks, no build tools beyond javac.

The ERP has two user roles: **Administrator** (full access including user management) and **Employee** (full access to all operational modules except user management).

Runtime versions: JDK 1.8 (Java 8), Tomcat 9.0.89, UCanAccess 5.0.0.

---

## 2. Project Structure
Always work inside `webapps/ERP/`. Never modify other webapps or Tomcat config files unless explicitly asked.

```
Tomcat_compartido/
└── webapps/
    └── ERP/
        ├── index.html              # Entry point — lists all implemented functions
        ├── style.css               # Shared stylesheet
        ├── Database.mdb            # Access database (do not move)
        └── WEB-INF/
            ├── web.xml             # Servlet declarations + URL mappings
            └── classes/
                ├── *.java          # Source files
                └── *.class         # Compiled bytecode
```

File categories:
- **Servlets**: extend `HttpServlet`, handle HTTP requests (e.g. `StockList`, `StockEdit`)
- **DAO classes** (`*Data`): static SQL helpers returning data (e.g. `ProductoData`)
- **Utilities**: `ConnectionUtils` — handles DB connection via UCanAccess

---

## 3. Rules

- **Always ask before making complex or destructive changes.**
- **Show your plan before executing changes** that affect more than one file.
- **Keep summaries concise** — after editing, describe what changed briefly.
- **Surface inconsistencies** instead of silently fixing them.
- **Match existing patterns** in each file. Do not refactor without being asked.
- **Edit narrowly** — do not touch code outside the scope of the request.
- **Always generate HTML inside the servlet** using `out.println(...)`. Never create separate `.html` files for servlet views.
- **Never compile, run, or execute anything** (no javac, no Tomcat commands, no terminal build/run steps). The developer compiles and tests manually outside this session. Just write the code and stop.


---

## 4. Tech Stack — Constraints You Must Respect

- **Servlet API**: Always use `javax.servlet.*`. Never `jakarta.servlet.*` — it will not compile on Java 8.
- **Default package**: No `package` declaration in any `.java` file. Do not add one.
- **Database driver**: `net.ucanaccess.jdbc.UcanaccessDriver`. Do not introduce other drivers.
- **No new dependencies** without explicit request. JSON is hand-built; no Jackson, Gson, etc.
- **No annotations**: Register all new servlets in `web.xml` only. Do not use `@WebServlet`.
- **Java 8 only**: Do not use features added after Java 8 (`var`, `records`, `Map.of()`, etc.).
- **Access SQL quirks**:
  - Table names with spaces need square brackets: `[My Table]`
  - Coerce numeric IDs to strings with: `id+'' = ?`
  - Date functions follow Access syntax: `FORMAT(fecha, 'yyyy-mm')`, not standard SQL.

---

## 5. Database Schema

### Productos
| Column     | Type        | Notes        |
|------------|-------------|--------------|
| id         | AUTOINCREMENT | Primary key |
| nombre     | TEXT(100)   |              |
| categoria  | TEXT(50)    |              |
| cantidad   | INTEGER     | Stock units  |
| coste      | DOUBLE      | Unit cost    |
| imagen_url | TEXT(255)   | Image path   |

### Usuarios
| Column   | Type      | Notes                    |
|----------|-----------|--------------------------|
| id       | AUTOINCREMENT | Primary key          |
| nombre   | TEXT(100) |                          |
| email    | TEXT(100) |                          |
| password | TEXT(100) |                          |
| rol      | TEXT(20)  | 'admin' or 'employee'    |

### Movimientos
| Column       | Type          | Notes                     |
|--------------|---------------|----------------------------|
| id           | AUTOINCREMENT | Primary key               |
| producto_id  | INTEGER       | FK to Productos.id        |
| tipo         | TEXT(10)      | 'entrada' or 'salida'     |
| cantidad     | INTEGER       |                            |
| fecha        | DATETIME      |                            |
| motivo       | TEXT(255)     |                            |

### InventarioFisico
| Column           | Type          | Notes                  |
|------------------|---------------|--------------------------|
| id               | AUTOINCREMENT | Primary key            |
| producto_id      | INTEGER       | FK to Productos.id     |
| cantidad_sistema | INTEGER       |                         |
| cantidad_fisica  | INTEGER       |                         |
| discrepancia     | INTEGER       |                         |
| fecha            | DATETIME      |                         |

---

## 6. Code Conventions

### Servlet skeleton (HTML response)
```java
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.Connection;

public class MyServlet extends HttpServlet {
    Connection connection;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        connection = ConnectionUtils.getConnection(config);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // write HTML with out.println(...)
    }
}
```

### Servlet skeleton (JSON / AJAX)
```java
public class MyJsonServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try (Connection conn = ConnectionUtils.getConnection(getServletConfig())) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Productos WHERE id+'' = ?");
            ps.setString(1, request.getParameter("id"));
            ResultSet rs = ps.executeQuery();
            // build JSON by string concatenation
        } catch (Exception e) {
            out.print("{\"error\":\"Internal error\"}");
        }
    }
}
```

### ConnectionUtils pattern
```java
import java.sql.*;
import javax.servlet.ServletConfig;

public class ConnectionUtils {
    public static Connection getConnection(ServletConfig config) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String path = config.getServletContext().getRealPath("/Database.mdb");
            return DriverManager.getConnection("jdbc:ucanaccess://" + path);
        } catch (Exception e) {
            throw new RuntimeException("DB connection failed", e);
        }
    }
}
```

### Database access pattern
- DB logic lives in `*Data` classes as `public static` methods taking `Connection` as first argument.
- Always use `PreparedStatement`. Never concatenate user input into SQL.
- Return types: `Vector<T>` for lists, plain objects for single rows.

### Naming conventions
- Servlets: PascalCase English (`StockList`, `StockEdit`, `UserLogin`).
- DAO classes: `XData` (e.g. `ProductoData`, `UsuarioData`).
- Field names in DAOs: Spanish (`nombre`, `categoria`, `cantidad`).

### Registering new servlets
Always register in `web.xml`. URL pattern = class name.

```xml
<servlet>
    <servlet-name>StockList</servlet-name>
    <servlet-class>StockList</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>StockList</servlet-name>
    <url-pattern>/StockList</url-pattern>
</servlet-mapping>
```

---

## 7. Building and Running
1. Open CMD at `Tomcat_compartido/` and run `sj.bat` to set up the classpath.
2. Edit `.java` files in `webapps/ERP/WEB-INF/classes/`.
3. Compile from the `classes/` directory: `javac MyServlet.java`
4. Start Tomcat: `catalina.bat run`
5. Access the app at: `http://localhost:8080/ERP/`