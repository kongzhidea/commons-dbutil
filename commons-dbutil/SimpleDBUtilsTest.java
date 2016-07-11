package com.kk.dbutils;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * A：loadDriver(StringdriveClassName): 这一方法装载并注册JDBC驱动程序，如果成功就返回TRUE,不需要去捕捉ClassNotFoundException异常。通过返回值判断驱动程序是否加载成功。
 * <p/>
 * B：close方法：DbUtils类提供了三个重载的关闭方法。这些方法检查所提供的参数是不是NULL，如果不是的话，它们就关闭连接(Connection)、声明（Statement）或者结果集（ResultSet）对象。
 * <p/>
 * C：closeQuietly方法: closeQuietly这一方法不仅能在连接、声明或者结果集为NULL情况下避免关闭，还能隐藏一些在程序中抛出的SQLException。如果你不想捕捉这些异常的话，这对你是非常有用的。在重载closeQuietly方法时，特别有用的一个方法是closeQuietly(Connection conn,Statement stmt,ResultSet rs)，使用这一方法，你最后的块就可以只需要调用这一方法即可。
 * <p/>
 * D: commitAndCloseQuietly(Connection conn)方法和commitAndClose (Connection conn)方法：这两个方法用来提交连接，然后关闭连接，不同的是commitAndCloseQuietly(Connection conn)方法关闭连接时不向上抛出在关闭时发生的一些SQL异常而commitAndClose (Connection conn)方法向上抛出SQL异常。
 * <p/>
 * 传入 connection， 不推荐
 */
public class SimpleDBUtilsTest {
    private static Connection conn = getConnection2();

    public static Connection getConnection() {
        return conn;
    }

    public static Connection getConnection2() {
        String url = "jdbc:mysql://0.0.0.0:3306/eby_test?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true";
        String driverClassName = "com.mysql.jdbc.Driver";
        String username = "";
        String password = "";
        Connection conn = null;
        DbUtils.loadDriver(driverClassName);
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //关闭数据库连接
    private static void clone(Connection conn) {
        DbUtils.closeQuietly(conn);
    }

    // 返回 List<Map>
    private static List<Map<String, Object>> queryForMapList(String sql, Object... params) {
        QueryRunner qr = new QueryRunner();
        List<Map<String, Object>> list = null;
        try {
            list = (List) qr.query(getConnection(), sql, new MapListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 返回 Map， 取第一条数据
    private static Map<String, Object> queryForMap(String sql, Object... params) {
        QueryRunner qr = new QueryRunner();
        Map<String, Object> map = null;
        try {
            map = qr.query(getConnection(), sql, new MapHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // 不会进行下划线和驼峰式写法的转换，  简单的key与bean.field映射
    private static <T> List<T> queryForBeanList(String sql, Class<T> clz, Object... params) {
        QueryRunner qr = new QueryRunner();
        List<T> list = new ArrayList<T>();
        try {
            list = (List<T>) qr.query(getConnection(),
                    sql, new BeanListHandler(clz), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 返回 bean， 取第一条数据
    // 不会进行下划线和驼峰式写法的转换，  简单的key与bean.field映射
    private static <T> T queryForBean(String sql, Class<T> clz, Object... params) {
        QueryRunner qr = new QueryRunner();
        try {
            T bean = (T) qr.query(getConnection(),
                    sql, new BeanHandler(clz), params);
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 更新操作
    public static int update(String sql, Object... params) {
        QueryRunner qr = new QueryRunner();
        int n = 0;
        try {
            n = qr.update(getConnection(), sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }


    public static void main(String[] args) throws Exception {


//        String sql = "select * from eby_user where id = 195";
//        String sql = "select * from eby_user where id = ?";
//        List<Map<String, Object>> list = queryForMapList(sql);
//        for (Map<String, Object> map : list) {
//            System.out.println(map);
//        }

        String sql = "update eby_user set privs=? where id = ?";
        System.out.println(update(sql, "中文", 195));

//        List<User> list = queryForBeanList(sql, User.class);
//        //输出查询结果
//        for (User user : list) {
//            System.out.println(user);
//        }
//
//        Map<String, Object> obj = queryForMap(sql, 195);
//        System.out.println(obj);

//        User user = queryForBean(sql, User.class, 195);
//        User user = queryForBean(sql, User.class);
//        System.out.println(user);
    }
}
