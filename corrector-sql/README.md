# Corrector de test de SQL (Biblio.accdb)

## Antes de ejecutar

1. Ajusta `RUTA_BIBLIO` en `CorrectorSQL.java` con la ruta real de tu Biblio.accdb.
2. Crea la carpeta `entregas/` en la raíz del proyecto.
3. Dentro, un fichero `.sql` por estudiante (nombre del fichero = identificador del alumno), con este formato:

```sql
-- ITEM 1
SELECT Au_Id, Author FROM Authors WHERE [Year Born] > 1950;

-- ITEM 2
SELECT Name, City, Zip FROM Publishers WHERE City <> 'New York' ORDER BY Zip DESC;

-- ITEM 3
SELECT Authors.Author, COUNT(*) AS num FROM Authors
INNER JOIN [Title Author] ON Authors.Au_ID = [Title Author].Au_ID
GROUP BY Authors.Author HAVING COUNT(*) > 1 ORDER BY COUNT(*) DESC;

-- ITEM 4
SELECT Titles.Title, Authors.Author, Publishers.Name
FROM (Titles INNER JOIN [Title Author] ON Titles.ISBN = [Title Author].ISBN)
INNER JOIN Authors ON [Title Author].Au_ID = Authors.Au_ID
INNER JOIN Publishers ON Titles.PubID = Publishers.PubID;
```

## Ejecutar

```
mvn compile exec:java
```

## Qué hace

Por cada estudiante y cada item:
1. Ejecuta la consulta del estudiante con un timeout de 3 segundos (en un hilo aparte, cancelable).
2. Ejecuta la consulta de referencia.
3. Compara las filas resultado (no el texto SQL). Si el item no requiere orden específico, ordena ambos conjuntos antes de comparar para no penalizar un orden distinto pero válido.
4. Escribe una fila en `resultados.csv` con estado `OK`, `FALLO`, `ERROR` (sintaxis o timeout) o `FALTA` (no entregado).

## Limitaciones conocidas 

- La comparación de alias de columna no se hace por nombre, solo por posición y valor. Si el alumno nombra distinto una columna calculada, no afecta al resultado.
- No hay tolerancia para columnas adicionales no pedidas: si el alumno devuelve una columna de más, se cuenta como FALLO. 
- El timeout de 3s vía `Future.cancel(true)` interrumpe el hilo, pero UCanAccess/Jackcess no siempre responde a la interrupción de forma inmediata; en consultas realmente pesadas puede tardar algo más en liberar el hilo. 

