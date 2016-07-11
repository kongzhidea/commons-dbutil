package com.kk.dbutils;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A：loadDriver(StringdriveClassName): 这一方法装载并注册JDBC驱动程序，如果成功就返回TRUE,不需要去捕捉ClassNotFoundException异常。通过返回值判断驱动程序是否加载成功。
 * <p/>
 * B：close方法：DbUtils类提供了三个重载的关闭方法。这些方法检查所提供的参数是不是NULL，如果不是的话，它们就关闭连接(Connection)、声明（Statement）或者结果集（ResultSet）对象。
 * <p/>
 * C：closeQuietly方法: closeQuietly这一方法不仅能在连接、声明或者结果集为NULL情况下避免关闭，还能隐藏一些在程序中抛出的SQLException。如果你不想捕捉这些异常的话，这对你是非常有用的。在重载closeQuietly方法时，特别有用的一个方法是closeQuietly(Connection conn,Statement stmt,ResultSet rs)，使用这一方法，你最后的块就可以只需要调用这一方法即可。
 * <p/>
 * D: commitAndCloseQuietly(Connection conn)方法和commitAndClose (Connection conn)方法：这两个方法用来提交连接，然后关闭连接，不同的是commitAndCloseQuietly(Connection conn)方法关闭连接时不向上抛出在关闭时发生的一些SQL异常而commitAndClose (Connection conn)方法向上抛出SQL异常。
 * <p/>
 * <p/>
 * 传入 datasource ，  推荐！！！！！
 * <p/>
 * <p/>
 * ArrayHandler：把结果集中的第一行数据转成对象数组。
 * ArrayListHandler：把结果集中的每一行数据都转成一个对象数组，再存放到List中。
 * BeanHandler：将结果集中的第一行数据封装到一个对应的JavaBean实例中。
 * BeanListHandler：将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里。
 * ----------------Bean的getter与setter方法的名字与结果集的列名必须
 * ColumnListHandler：将结果集中某一列的数据存放到List中。
 * KeyedHandler：将结果集中的每一行数据都封装到一个Map里，然后再根据指定的key把每个Map再存放到一个Map里。
 * MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
 * MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List。
 * ScalarHandler：将结果集中某一条记录的其中某一列的数据存成Object。
 */
public class DBUtilsTest {


    private static DataSource dataSource = getDataSource();

    private static DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://0.0.0.0:3306/eby_test?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true");
        dataSource.setUsername("");
        dataSource.setPassword("");
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(2);
        dataSource.setDefaultAutoCommit(true);
        dataSource.setTimeBetweenEvictionRunsMillis(3600000);
        dataSource.setMinEvictableIdleTimeMillis(3600000);
        return dataSource;
    }

    // 返回 List<Object[]>，Object[] 中按照查询先后顺序设置值。
    private static List<Object[]> queryForArrayList(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        List<Object[]> list = null;
        try {
            list = (List) qr.query(sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 返回 List<Object[]>，Object[] 中按照查询先后顺序设置值。 取第一条数据
    private static Object[] queryForArray(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        Object[] objects = null;
        try {
            objects = qr.query(sql, new ArrayHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * 也可以 这样指定field，如下：
     * String sql = "select * from users";
     * List list = (List) qr.query(sql, new ColumnListHandler("id"));
     */
    // 返回某固定列
    public static <T> List<T> queryForColumnList2(String sql, Object... params) throws SQLException {
        QueryRunner qr = new QueryRunner(dataSource);
        List list = (List) qr.query(sql, new ColumnListHandler<T>(), params);
        return list;
    }

    // 返回固定列
    public static List queryForColumnList(String sql, Object... params) throws SQLException {
        QueryRunner qr = new QueryRunner(dataSource);
        List list = (List) qr.query(sql, new ColumnListHandler(), params);
        return list;
    }

    // 返回 List<Map>
    private static List<Map<String, Object>> queryForMapList(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        List<Map<String, Object>> list = null;
        try {
            list = (List) qr.query(sql, new MapListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 返回 Map， 取第一条数据
    private static Map<String, Object> queryForMap(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        Map<String, Object> map = null;
        try {
            map = qr.query(sql, new MapHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    // 不会进行下划线和驼峰式写法的转换，  简单的key与bean.field映射
    private static <T> List<T> queryForBeanList(String sql, Class<T> clz, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        List<T> list = new ArrayList<T>();
        try {
            list = (List<T>) qr.query(
                    sql, new BeanListHandler(clz), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 返回 bean， 取第一条数据
    // 不会进行下划线和驼峰式写法的转换，  简单的key与bean.field映射
    private static <T> T queryForBean(String sql, Class<T> clz, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        try {
            T bean = (T) qr.query(
                    sql, new BeanHandler(clz), params);
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 更新操作， 删除操作
    //DBUtils执行插入操作的时候，无法返回自增主键，在MySQL中，执行完了一个插入SQL后，接着执行SELECT LAST_INSERT_ID()语句，就可以获取到自增主键。
    public static int update(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        int n = 0;
        try {
            n = qr.update(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }

    /**
     * 获得第一个查询第一行第一列
     *
     * @param sql
     * @param params
     * @return
     */
    public static Object getAnAttr(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        Object s = null;
        try {
            s = qr.query(sql, new ScalarHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    // 查询返回 int
    public static int queryForInt(String sql, Object... params) {
        Object o = getAnAttr(sql, params);
        if (o == null) {
            return 0;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? 1 : 0;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            Long l = (Long) o;
            return l.intValue();
        }
        if (o instanceof Float) {
            return ((Float) o).intValue();
        }

        if (o instanceof Double) {
            return ((Double) o).intValue();
        }

        String s = (String) o;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 查询返回 long
    public static long queryForLong(String sql, Object... params) {
        Object o = getAnAttr(sql, params);
        if (o == null) {
            return 0;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? 1 : 0;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            Long l = (Long) o;
            return l;
        }
        if (o instanceof Float) {
            return ((Float) o).longValue();
        }

        if (o instanceof Double) {
            return ((Double) o).longValue();
        }

        String s = (String) o;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }


    // 查询返回 long
    public static double queryForDouble(String sql, Object... params) {
        Object o = getAnAttr(sql, params);
        if (o == null) {
            return 0;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? 1 : 0;
        }
        if (o instanceof Byte) {
            return (Byte) o;
        }
        if (o instanceof Short) {
            return (Short) o;
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            Long l = (Long) o;
            return l;
        }
        if (o instanceof Float) {
            return ((Float) o).doubleValue();
        }

        if (o instanceof Double) {
            return (Double) o;
        }
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).doubleValue();
        }

        String s = (String) o;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static BigDecimal queryForDecimal(String sql, Object... params) {
        Object o = getAnAttr(sql, params);
        if (o == null) {
            return null;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o) ? new BigDecimal(1) : new BigDecimal(0);
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }
        BigDecimal decimal = new BigDecimal(o.toString());
        return decimal;
    }


    public static String queryForString(String sql, Object... params) {
        Object o = getAnAttr(sql, params);
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        }
        if (o instanceof java.sql.Date) {
            return defaultDate((java.sql.Date) o);
        }
        if (o instanceof java.sql.Timestamp) {
            return defaultTime((java.sql.Timestamp) o);
        }
        return o.toString();
    }

    // 返回日期
    public static Date queryForDate(String sql, Object... params) {
        Object o = getAnAttr(sql, params);
        if (o == null) {
            return null;
        }
        if (o instanceof java.sql.Date) {
            return new Date(((java.sql.Date) o).getTime());
        }
        if (o instanceof java.sql.Timestamp) {
            return new Date(((java.sql.Timestamp) o).getTime());
        }
        return null;
    }


    /*****************************/

    // 查询返回 int， 如果数据库字段为long或者 count()查询，则 报错：long无法转为int
    @Deprecated
    public static int queryForIntDeprecated(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        try {
            Integer ret = qr.query(
                    sql, new ScalarHandler<Integer>(), params);
            return ret == null ? 0 : ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 查询返回 long , count() 返回long； 如果数据库字段为int，则 报错：int无法转为long
    @Deprecated
    public static long queryForLongDeprecated(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        try {
            Long ret = qr.query(
                    sql, new ScalarHandler<Long>(), params);
            return ret == null ? 0 : ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    // 查询返回 string， 无法查询date,datetime类型
    @Deprecated
    public static String queryForStringDeprecated(String sql, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        try {
            String ret = qr.query(
                    sql, new ScalarHandler<String>(), params);
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /****************************/
    public static final String formatDefaultTime = "yyyy-MM-dd HH:mm:ss";

    public static final String formatDefaultDate = "yyyy-MM-dd";

    /**
     * yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String defaultDate(Date date) {
        return format(date, new SimpleDateFormat(formatDefaultDate));
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String defaultTime(Date date) {
        return format(date, new SimpleDateFormat(formatDefaultTime));
    }

    public static String format(Date aDate, SimpleDateFormat aFormat) {
        if (aDate == null || aFormat == null) {
            return "";
        }
        synchronized (aFormat) {
            return aFormat.format(aDate);
        }
    }

    public static void main(String[] args) throws Exception {


//        String sql = "select * from eby_user where id = 195";
//        String sql = "select * from eby_user where id = ?";

//        List<Object[]> list = queryForArrayList(sql, 195);
//        for (Object[] objects : list) {
//            for (Object obj : objects) {
//                System.out.print(obj + ",");
//            }
//            System.out.println();
//        }
//
//        Object[] objects = queryForArray(sql, 195);
//        for (Object obj : objects) {
//            System.out.print(obj + ",");
//        }
//        System.out.println();

//        List<Map<String, Object>> list = queryForMapList(sql);
//        for (Map<String, Object> map : list) {
//            System.out.println(map);
//        }

//        String sql = "update eby_user set privs=? where id = ?";
//        System.out.println(update(sql, "s", 195));
//        System.out.println(update(sql, new Object[]{"bj", 195}));

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

//        String sql = "select count(id) from eby_user  ";
//        System.out.println(queryForString(sql));
//        System.out.println(queryForLongDeprecated(sql));
//        System.out.println(queryForInt(sql));
//        System.out.println(queryForLong(sql));
//
//        sql = "select ss from eby_user where id = 195  ";
//        System.out.println(queryForInt(sql));
//        System.out.println(queryForIntDeprecated(sql));
//        System.out.println(queryForLong(sql));
//
//        sql = "select username from eby_user where id = 195  ";
//        System.out.println(queryForString(sql));
//
//        sql = "select status from eby_user where id = 195  ";
//        System.out.println(queryForDecimal(sql));
//        System.out.println(queryForInt(sql));
//        System.out.println(queryForString(sql));
//
//        sql = "select ctime from eby_user where id = 195  ";
//        System.out.println(queryForDate(sql));
//        System.out.println(queryForString(sql));


//        sql = "select original_price from eby_order where id = 11";
//        System.out.println(queryForString(sql));
//        System.out.println(queryForDouble(sql));
//        System.out.println(queryForDecimal(sql));

        String sql = "select ctime from eby_user where id < 50";
        List<String> list2 = queryForColumnList2(sql);
        System.out.println(list2);
        List list = queryForColumnList(sql);
        System.out.println(list);

    }
}
