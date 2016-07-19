package com.kk.dbutils;

import com.kk.dbutils.bean.BeanListTplHandler;
import com.kk.dbutils.bean.BeanTplHandler;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtilsBeanTest {


    private static DataSource dataSource = getDataSource();

    private static DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true");
        dataSource.setUsername("");
        dataSource.setPassword("");
        dataSource.setInitialSize(2);
        dataSource.setMaxActive(2);
        dataSource.setDefaultAutoCommit(true);
        dataSource.setTimeBetweenEvictionRunsMillis(3600000);
        dataSource.setMinEvictableIdleTimeMillis(3600000);
        return dataSource;
    }


    // 返回 bean， 取第一条数据
    // 会进行下划线和驼峰式写法的转换
    private static <T> T queryForBean(String sql, Class<T> clz, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        try {
            T bean = (T) qr.query(
                    sql, new BeanTplHandler(clz), params);
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 返回 bean list
    // 会进行下划线和驼峰式写法的转换
    private static <T> List<T> queryForBeanList(String sql, Class<T> clz, Object... params) {
        QueryRunner qr = new QueryRunner(dataSource);
        List<T> list = new ArrayList<T>();
        try {
            list = (List<T>) qr.query(
                    sql, new BeanListTplHandler(clz), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static void main(String[] args) throws Exception {


        String sql = "select * from eby_user where id = ?";

//        User user = queryForBean(sql, User.class, 195);
//        System.out.println(user);

        List<User> users = queryForBeanList(sql, User.class, 195);
        System.out.println(users.get(0));

    }
}
