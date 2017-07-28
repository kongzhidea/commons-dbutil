# commons-dbutil

```
Class.forName(“com.mysql.jdbc.Driver”)
在jdk6中，其实是可以不用调用Class.forName来加载mysql驱动的，因为mysql的驱动程序jar包中已经包含了java.sql.Driver配置文件，并在文件中添加了com.mysql.jdbc.Driver.但在JDK6之前版本，还是要调用这个方法。
```
