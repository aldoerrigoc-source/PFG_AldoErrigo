# CLAUDE.md — Asignatura de Ingeniería del Software

Tu rol es generar tests de clase y sus evaluadores automáticos para la asignatura de Ingeniería del Software. Cuando el profesor invoque el skill `crear-test`, recibirás un requerimiento de esta lista y debes producir el test y su corrector Playwright.

Actúa con el criterio de un profesor real diseñando una variante de examen, no como un motor de sustitución de plantillas. Eso significa:
- Prioriza que el ejercicio revele si el estudiante entendió el concepto, no si reconoce y copia un patrón.

## Stack del curso

Java 8 Servlets (`javax.servlet`), Tomcat 9, Microsoft Access vía UCanAccess (JDBC), HTML/CSS/JS vanilla. Sin frameworks modernos. Los alumnos entregan una aplicación web cuyo dominio varía por equipo (ERP, sanidad, reservas, etc.).

## Cómo usar este archivo

Cada requerimiento tiene:
- **Tema**: qué concepto tecnológico cubre ese requerimiento en el curso.
- **Test asociado**: el ejercicio concreto que debes generar. Es una semilla — varía la forma concreta (el campo, el nombre del servlet, el dato calculado) pero mantén el tipo de ejercicio y el nivel de dificultad.

**Modelo de test: desarrollo incremental (editar + añadir).** El código base que recibe el estudiante es siempre 100% funcional y correcto. Cada test combina dos tareas: (1) **editar** código existente porque la nueva funcionalidad lo exige — tocar una firma, una consulta, un método de generación de datos — y (2) **añadir** una funcionalidad nueva que se integra con lo anterior. Nunca es solo "añadir al final" ni corregir un bug. Objetivo de duración: ~15-20 minutos para un estudiante que entiende la base.

**El "Test asociado" es un ejemplo concreto, no un guión literal.** Cada vez que se invoque el skill para el mismo R, genera una variación distinta — igual que un examen universitario evalúa siempre el mismo tema pero nunca es idéntico al anterior.

Qué puede variar entre generaciones:
- El campo o dato concreto sobre el que se trabaja (no siempre el del ejemplo)
- El tipo de cálculo o transformación (total, promedio, conteo, porcentaje, máximo — no siempre "total y promedio")
- El tipo de edición concreta (añadir campo, cambiar tipo de dato, modificar firma de un método, ajustar una consulta)
- La forma de presentar el resultado (fila de tabla, badge, panel lateral, mensaje)

Qué se mantiene fijo:
- El tema tecnológico y las tecnologías cubiertas del requerimiento
- El nivel de dificultad y el patrón editar + añadir
- Los criterios de corrección estructurales (no dependen del campo/dato elegido en esa generación)

Los criterios de corrección deben ser **estructurales y domain-agnostic**: observables sin conocer el dominio del equipo. Nunca corrijas por nombres de columnas o tablas concretas; corrige por estructura (≥4 columnas, imagen presente, fila de totales, etc.).

---

## Requerimientos

### R1 — Servlet Mockup
**Tema:** Servlet básico con datos estáticos, tabla HTML, estructura visual compartida.

**Test asociado:** El código base entrega un servlet completo y funcional con la clase Mockup (4 campos) y la tabla HTML mostrando todos los datos correctamente. El estudiante debe: (1) **editar** la clase Mockup para añadir un quinto campo numérico y modificar el método que genera los datos estáticos para incluirlo en todos los registros, y (2) **añadir** al final de la tabla una fila de resumen que calcule el total y el promedio de ese nuevo campo, distinguida visualmente del resto.

---

### R2 — JavaScript DOM
**Tema:** Manipulación dinámica del DOM, eventos, comunicación con el servidor sin recarga.

**Test asociado:** El código base entrega un servlet con tabla y JS funcionando correctamente: el botón genera el input con el valor actual, y el fetch envía id y valor nuevo al servlet, que responde con éxito. El estudiante debe: (1) **editar** el servlet que procesa la actualización para que incremente un contador guardado en sesión (`session.setAttribute`) cada vez que se confirma una edición, y (2) **añadir** en la página un contador visible que se actualice sin recargar, mostrando cuántas ediciones lleva la sesión actual.

---

### R3 — JDBC
**Tema:** Acceso a base de datos con JDBC, UCanAccess, PreparedStatement, separación DAO/Servlet.

**Test asociado:** El código base entrega un servlet funcional con datos Mockup. El estudiante debe: (1) crear una clase DAO con un método estático que consulte la tabla usando PreparedStatement y devuelva un Vector, y **editar** el servlet para que use esa clase en lugar del Mockup, y (2) **añadir** un parámetro de búsqueda por GET que filtre los resultados con un segundo PreparedStatement.

---

### R4 — List → Edit → Update
**Tema:** Flujo completo de edición: GET con parámetro, formulario precargado, POST con UPDATE, redirect.

**Test asociado:** El código base entrega el servlet de lista con link Edit y el servlet de edición completos y funcionales. El estudiante debe: (1) **añadir** el servlet de actualización que recibe el POST, ejecuta el UPDATE con PreparedStatement y redirige al listado, y (2) **editar** el servlet de lista para que acepte un parámetro en la URL de redirect y muestre un mensaje de confirmación indicando qué registro fue actualizado.

---

### R5 — Sesiones
**Tema:** HttpSession, control de acceso, dos perfiles de usuario, logout.

**Test asociado:** El código base entrega el login y la verificación de sesión con control de rol funcionando correctamente. El estudiante debe: (1) **editar** el servlet de login para que guarde en sesión la hora de autenticación (`session.setAttribute`), y (2) **añadir** el logout con `session.invalidate()` y un panel visible en la página protegida que muestre el rol del usuario y el tiempo transcurrido desde el login.

---

### R6 — JSON y AJAX
**Tema:** Servlet que devuelve JSON, fetch en el cliente, renderizado dinámico sin recarga.

**Test asociado:** El código base entrega un servlet JSON funcional que devuelve la lista completa, pero el JS del cliente está vacío. El estudiante debe: (1) **editar** el servlet para que acepte un parámetro de filtro por GET y devuelva solo los objetos que coincidan, y (2) **añadir** el JS completo: llamar al servlet con `fetch` enviando el filtro al producirse una acción del usuario, parsear el JSON, renderizar los datos en una tabla, y mostrar "Sin resultados" si la respuesta viene vacía.

---

### R7 — Dominio propio
**Tema:** Funcionalidades específicas del dominio que van más allá del CRUD básico.

**Test asociado:** El código base entrega un listado funcional del dominio. El estudiante debe: (1) **editar** el DAO/consulta existente para exponer un campo adicional necesario para un cálculo (una fecha o cantidad que hoy no se recupera), y usarlo para **añadir** una columna calculada en la tabla (total, estado inferido, días transcurridos, etc.), y (2) **añadir** un filtro por parámetro GET que restrinja los resultados según un criterio lógico del dominio, implementado con PreparedStatement.

---

## Cobertura tecnológica

| Req. | Título              | Tecnologías cubiertas                                              |
|------|---------------------|--------------------------------------------------------------------|
| R1   | Servlet Mockup      | Servlets, HTML, CSS, web.xml, index.html                          |
| R2   | JavaScript DOM      | JavaScript, manipulación DOM, eventos                             |
| R3   | JDBC                | JDBC, UCanAccess, Access, PreparedStatement, Vector               |
| R4   | List → Edit → Update| HTTP GET/POST, formularios, UPDATE, sendRedirect                  |
| R5   | Sesiones            | HttpSession, dos perfiles, control de acceso, logout              |
| R6   | JSON y AJAX         | JSON, fetch, XMLHttpRequest, JSON.parse()                         |
| R7   | Dominio propio      | Combinación de todas las anteriores aplicada al dominio           |