# commons-dbutil

```
Class.forName(“com.mysql.jdbc.Driver”)
在java 6中，引入了service provider的概念，即可以在配置文件中配置service（可能是一个interface或者abstract class）的provider（即service的实现类）。配置路径是：/META-INF/services/下面。
在jdk6中，其实是可以不用调用Class.forName来加载mysql驱动的，因为mysql的驱动程序jar包中已经包含了java.sql.Driver配置文件，并在文件中添加了com.mysql.jdbc.Driver.但在JDK6之前版本，还是要调用这个方法。
```
