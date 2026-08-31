-- ITEM 1
SELECT Au_Id, Author FROM Authors WHERE [Year Born] > 1990;
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