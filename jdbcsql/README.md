### [jdbcsql](http://jdbcsql.sourceforge.net/) 
* java快速执行单行sql命令。

```
jdbcsql is a small command-line tool written in JAVA and can be used on all platforms, for which JRE 8 is available.
To connect to a specific DBMS the tool uses its JDBC driver. The current version for download supports the following DBMS: mysql, oracle and postgresql. 
Other systems can easily be added by the user. The result of the executed 'select' query is displayed in CSV format (by default complying to rfc4180,
but other standards are supported too). When there is an error the tool stops with exit code 1 and the error message is output on stderr.
jdbcsql is created with a main purpose to be used in shell-scripts.

Relatively easy to configurate, this tool is suitable for queries ‘select’, ‘update’ and ‘delete’.
I think that is not suitable for a large number of requests, like 'insert'.

usage

 

$ java -jar jdbcsql.zip
jdbcsql execute queries in diferent databases such as mysql, oracle, postgresql and etc.
Query with resultset output over stdout in CSV format.

usage: jdbcsql [OPTION]... SQL
 -?,--help			show this help, then exit
 -d,--dbname 			database name to connect
 -f,--csv-format 		Output CSV format (EXCEL, MYSQL,
				RFC-4180 and TDF). Default is RFC-4180 
 -h,--host 			database server host
 -H,--hide-headers		hide headers on output
 -m,--management-system 	database management system (mysql,
				oracle, postgresql ...)
 -p,--port 			database server port
 -P,--password 			database password
 -s,--separator 		column separator (default: "\t")
 -U,--usernme 			database user name



Adding DBMS

I will now show an example of how to add support for Microsoft SQL Server. For this purpose we need JDBC driver sqljdbc4.jar.
Add sqljdbc4.jar file in the root directory of the archive jdbcsql.zip

Add the name of the driver sqljdbc4.jar in the file jdbcsql.zip/META-INF/MANIFEST.MF in the field Rsrc-Class-Path: ./ commons-cli-1.2.jar commons-csv-1.1.jar postgresql-9.3-1102-jdbc4.jar ...

Add the following lines in the file Jdbcsql.zip/JDBCConfig.properties:

# sqlserver settings
sqlserver_driver = com.microsoft.sqlserver.jdbc.SQLServerDriver
sqlserver_url = jdbc:sqlserver://host:port;databaseName=dbname
The prefix sqlserver (randomly choosen by us) becomes an argument of the option -m.
To construct correctly url for jdbc the tool will automatically replace 'host', 'port' et 'dbname' in the string 'jdbc:sqlserver://host:port;databaseName=dbname' respectively with the arguments of the options -h, -p and -d.
The command which will run request to Microsoft SQL Server will look like this:

java -jar jdbcsql.zip -m sqlserver -h 127.0.0.1 -d dbtest -U sqluser -P ***** 'select * from table'

In the manner shown you can add support in tool-and jdbcsql for each DBMS as long as it has a JDBC driver.

Examples

java -jar jdbcsql.zip -m postgresql -h localhost -d dbtest -U postgres -P ***** 'select * from table'

java -jar jdbcsql.zip -m postgresql -h pgsql.host.com -d dbtest -U postgres -P ***** -s ';' 'select * from table'

Note

For DBMS Oracle (and Sybase, for example) the port is mandatory i.e. the -p option is required. Maybe it is so in other DBMS as well.

```