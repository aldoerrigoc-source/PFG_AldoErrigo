# PFG_AldoErrigo

Repositorio del Trabajo de Fin de Grado: **Análisis de utilización de IA generativa en Ingeniería del Software**.

El proyecto valida un sistema asistido por IA para la asignatura de Ingeniería del Software. Incluye una aplicación ERP construida con ayuda de IA (Roo Code + Claude Sonnet), un sistema de corrección automática de entregas con Playwright, y un receptor de notas accesible desde el navegador.

---
## Archivos no incluidos en el repositorio

La carpeta `Programs/JavaStack/` **no está en el repositorio** porque contiene
binarios que superan el límite de 100 MB por archivo impuesto por GitHub.

Incluye: JDK 17, Apache Maven 3.9 y Apache Tomcat 9 (necesarios para
compilar y ejecutar el corrector Playwright).

### Cómo obtenerlos

| Componente | Descarga |
|---|---|
| JDK 17 | https://adoptium.net (Temurin 17) |
| Apache Maven 3.9 | https://maven.apache.org/download.cgi |
| Apache Tomcat 9 | https://tomcat.apache.org/download-90.cgi |

Una vez descargados, colocarlos en `Programs/JavaStack/` respetando la
estructura de rutas que usa `corregir.bat`.

---

## Estructura del repositorio

```
PFG_AldoErrigo/
├── Pruebas/                          # Herramientas del profesor para crear y gestionar tests
├── Programs/PlaywrightCodex/         # Motor de corrección automática
└── Tomcat/
    ├── sj.bat                        # Script para configurar el PATH con Java8
    └── webapps/
        ├── ERP/                      # Aplicación ERP construida con IA
        ├── notasReceptor/            # Servlet que muestra las notas en el navegador
        ├── descomprimir.ps1          # Script para desplegar entregas de alumnos
        └── aerrigochan@alumni.unav.es.zip  # Ejemplo de entrega de alumno de un test
```

---

## Componentes principales

### 1. Aplicación ERP (`Tomcat/webapps/ERP/`)

Módulo de Inventario y Almacén construido con Roo Code y Claude Sonnet. Incluye gestión de stock, movimientos e inventario físico.

Tecnología: Java Servlets, Apache Tomcat 9, Microsoft Access (UCanAccess), Java 8.

Archivos relevantes:
- `AGENTS.md` — contexto de IA usado durante el desarrollo
- `Database.mdb` — base de datos Microsoft Access
- `WEB-INF/web.xml` — registro de servlets
- `WEB-INF/classes/` — código fuente de los servlets (ConnectionUtils, Producto, StockList, StockEdit, Movimiento, InventarioFísico y sus clases de datos)

---

### 2. Sistema de corrección automática (`Pruebas/` + `Programs/PlaywrightCodex/`)

El profesor crea un test con su descripción y código base para el alumno (`Pruebas/`). El alumno entrega su aplicación como un zip. El corrector lanza Playwright, abre el navegador, navega a la app del alumno desplegada en Tomcat y evalúa cada criterio automáticamente, generando un CSV con las notas.

#### Crear un test (`Pruebas/`)

- `CLAUDE.md` — contexto para el agente de IA que genera los tests
- `skills/crear-test/SKILL.md` — skill que genera el evaluador, el BRIEF y el código base a partir de un requisito
- `test-r1/` — Test R1: catálogo de vinilos (Servlet Mockup)
  - `BRIEF.md` — enunciado para el alumno
  - `WebContent/` — código base que recibe el alumno
  - `evaluador/EvaluadorR1.java` — evaluador Playwright (4 criterios: tabla, total, media, distinción visual)
- `test-r2/` — Test R2: inventario de plantas (JavaScript + edición inline)
  - `BRIEF.md` — enunciado para el alumno
  - `WebContent/` — código base que recibe el alumno
  - `evaluador/EvaluadorR2.java` — evaluador Playwright (criterios de edición inline, guardado, actualización)

#### Ejecutar la corrección (`Programs/PlaywrightCodex/`)

- `pom.xml` — proyecto Maven con la dependencia de Playwright
- `corregir.bat` — script que compila y lanza el evaluador
- `ClienteNotas.java` — envía el CSV de notas al receptor vía HTTP POST
- `resultado_r1.csv`, `resultado_r2.csv` — ejemplos de salida del corrector

---

### 3. Despliegue de entregas (`Tomcat/webapps/`)

- `descomprimir.ps1` — extrae los zips de los alumnos en subcarpetas de `webapps/`, genera un índice en `ROOT/index.html`
- `aerrigochan@alumni.unav.es.zip` — ejemplo de entrega real usada para probar el sistema

Cada zip se despliega como una aplicación web independiente accesible en `http://localhost:8082/<alumno>/`.

---

### 4. Receptor de notas (`Tomcat/webapps/notasReceptor/`)

Servlet que recibe las notas enviadas por `ClienteNotas.java` y las muestra en una tabla HTML ordenada alfabéticamente, accesible en `http://localhost:8082/notasReceptor/notas`.

---

## Flujo completo de una corrección

1. El alumno entrega su zip. El profesor lo coloca en `Tomcat/webapps/`.
2. Se ejecuta `descomprimir.ps1` para extraer y registrar al alumno en el índice.
3. Se arranca el servidor Tomcat. Se verifica que la app del alumno carga en `http://localhost:8082/<alumno>/`.
4. Desde `Programs/PlaywrightCodex/`, se ejecuta:
   ```
   .\corregir.bat EvaluadorR1
   ```
5. Playwright evalúa la app de cada alumno y genera `resultado_r1.csv`.
6. Se envían las notas al receptor:
   ```
   java ClienteNotas resultado_r1.csv R1
   ```
7. Se consultan las notas en `http://localhost:8082/notasReceptor/notas`.

## Correctores de otras asignaturas

Estas dos carpetas contienen correctores automáticos desarrollados para prácticas de otras asignaturas. Son independientes del sistema Playwright y entre sí.

### 5. Corrector de SQL (`corrector-sql/`)

Corrige entregas de consultas SQL ejecutándolas directamente contra una base de datos Microsoft Access (`Biblio.accdb`) y comparando los resultados fila a fila con una consulta de referencia. Cada alumno entrega un archivo `.sql` con sus consultas separadas por comentarios `-- ITEM N`.

- `src/` — código fuente del corrector en Java (proyecto Maven)
- `pom.xml` — dependencias (UCanAccess para leer el `.accdb`)
- `entregas/` — carpeta donde se colocan los `.sql` de los alumnos (un archivo por alumno)
- `resultados.csv` — salida con estado `OK`, `FALLO`, `ERROR` o `FALTA` por cada item y alumno

Para ejecutar:
```
mvn compile exec:java
```

Antes de correr, ajustar la ruta de `Biblio.accdb` en `CorrectorSQL.java`.

---

### 6. Corrector de VBA (`corrector-vba/`)

Corrige macros VBA automatizando Excel real (sin ventanas visibles). Coloca una fórmula conocida en una celda, ejecuta la macro del alumno y comprueba el resultado en las celdas de salida. Cada alumno entrega un archivo `.xlsm` con su macro.

- `corrector_practica3.py` — script Python que automatiza Excel vía `pywin32`
- `entregas/` — carpeta donde se colocan los `.xlsm` de los alumnos (un archivo por alumno)
- `resultados_vba.csv` — salida con el resultado por alumno

Requisito: Microsoft Excel instalado y `pywin32` instalado (`pip install pywin32`).

Para ejecutar:
```
python corrector_practica3.py ./entregas --salida resultados_vba.csv
```

