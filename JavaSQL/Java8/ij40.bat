REM variables for Java programas: Java, Java DB and Squirrel-sql
SET JAVA_PC=%cd:~%
REM SET JAVA_PC=%DRIVE%Derby
SET JAVA_HOME=%JAVA_PC%\jdk1.8.0_131
SET DERBY_HOME=%JAVA_HOME%\db
SET SQUIRREL_HOME=%JAVA_PC%\squirrelsql-4.0.0-standard
SET PATH=%JAVA_HOME%\bin;%DERBY_HOME%\bin;%SQUIRREL_HOME%;%PATH%
SET CLASSPATH=%CLASSPATH%;.;%DERBY_HOME%\lib\derby.jar;%DERBY_HOME%\lib\derbynet.jar;%DERBY_HOME%\lib\derbyclient.jar;%DERBY_HOME%\lib\derbytools.jar;%DERBY_HOME%\lib\derbyoptionaltools.jar
ij.bat
