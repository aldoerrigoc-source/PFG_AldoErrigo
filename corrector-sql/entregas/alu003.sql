-- ITEM 1
-- Devuelve el codigo y el nombre de los autores nacidos despues de 1950.
SELECT Au_Id, Author FROM Authors WHERE [Year Born] > 1950;

-- ITEM 2
-- Devuelve el nombre, ciudad y codigo postal de los editores cuya ciudad
-- no sea "New York", ordenados de forma descendente por codigo postal.
SELECT Name, City, Zip FROM Publishers WHERE City <> 'New York' ORDER BY Zip DESC;

-- ITEM 3
-- Devuelve cada autor con el numero de libros que ha publicado, mostrando
-- solo los que tienen mas de un libro, ordenados por esa cantidad de forma
-- descendente.
SELECT Authors.Author, COUNT(*) AS num FROM Authors
INNER JOIN [Title Author] ON Authors.Au_ID = [Title Author].Au_ID
GROUP BY Authors.Author HAVING COUNT(*) > 1 ORDER BY COUNT(*) DESC;

-- ITEM 4
-- Devuelve el titulo, el nombre del autor y el nombre del editor de cada libro.
SELECT Titles.Title, Authors.Author, Publishers.Name
FROM (Titles INNER JOIN [Title Author] ON Titles.ISBN = [Title Author].ISBN)
INNER JOIN Authors ON [Title Author].Au_ID = Authors.Au_ID
INNER JOIN Publishers ON Titles.PubID = Publishers.PubID;