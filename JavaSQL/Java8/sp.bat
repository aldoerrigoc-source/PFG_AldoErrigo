REM variables for Java programas: Java, Java DB, Squirrel-sql, and Tomcat
SET JAVA_PC=%cd:~%
SET JAVA_HOME=%JAVA_PC%\jdk1.8.0_131
SET DERBY_HOME=%JAVA_HOME%\db
SET SQUIRREL_HOME=%JAVA_PC%\squirrelsql-4.0.0-standard
SET CATALINA_HOME=%JAVA_PC%\apache-tomcat-8.5.24
SET PATH=%JAVA_HOME%\bin;%DERBY_HOME%\bin;%SQUIRREL_HOME%;%CATALINA_HOME%\bin;%PATH%
SET CLASSPATH=%CLASSPATH%;.;%DERBY_HOME%\lib\derby.jar;%DERBY_HOME%\lib\derbynet.jar;%DERBY_HOME%\lib\derbyclient.jar;%DERBY_HOME%\lib\derbytools.jar;%DERBY_HOME%\lib\derbyoptionaltools.jar;%CATALINA_HOME%\lib\servlet-api.jar;%CATALINA_HOME%\lib\Jama-1.0.2.jar;.

