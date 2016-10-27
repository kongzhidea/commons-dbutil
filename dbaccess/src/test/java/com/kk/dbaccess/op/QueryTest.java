package com.kk.dbaccess.op;

import com.kk.dbaccess.model.User;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class QueryTest extends BaseTest {

    @Test
    public void testQueryBase() throws SQLException {
        String sql = "select status from user where username=?";

//        logger.info(dataAccessMgr.queryInt(new OpUniq(dataSource,
//                sql, "kzh")));

        int o1 = dataAccessMgr.queryObject(new OpUniq<Integer>(Integer.class, dataSource,
                sql, "kzh"));
        logger.info("int=" + o1);
        String o2 = dataAccessMgr.queryObject(new OpUniq<String>(dataSource,
                sql, "kzh"));
        logger.info("string=" + o2);
        int o3 = dataAccessMgr.queryInteger(new OpUniq(dataSource,
                sql, "kzh"));
        logger.info("int=" + o3);

        logger.info("long=" + dataAccessMgr.queryObject(new OpUniq<Long>(dataSource,
                sql, "kzh")));

        logger.info("double=" + dataAccessMgr.queryObject(new OpUniq<Double>(dataSource,
                sql, "kzh")));

        logger.info("decimal=" + dataAccessMgr.queryBigDecimal(new OpUniq(dataSource,
                sql, "kzh")));

        logger.info("username=" + dataAccessMgr.queryDate(new OpUniq(dataSource,
                "select ctime from user where username=?", "kzh")).toLocaleString());

        User user = (User) dataAccessMgr.queryUnique(new OpUniq(dataSource,
                "select * from user where username=?", "kzh") {
            @Override
            public Object parse(ResultSet rs) throws SQLException {
                User u = new User();//可以使用name和index两种方式，index从1开始
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRealname(rs.getString("realname"));
                u.setStatus(rs.getInt("status"));
                u.setCtime(rs.getDate("ctime")); //  java.sql.Date
                u.setCityName(rs.getString("city_name"));
                return u;
            }
        });
        logger.info("op.uniq.parse.user=" + user);

        user = dataAccessMgr.queryUniqueBean(new OpBean<User>(User.class, dataSource,
                "select * from user where username=?", "kzh"));
        logger.info("op.bean.user=" + user);

        logger.info("op.map.list=" + dataAccessMgr.queryMapList(new OpMap(dataSource,
                "select * from user where realname like ?", "%孔%")));

        logger.info("op.map=" + dataAccessMgr.queryMap(new OpMap(dataSource,
                "select * from user where username =?", "kzh")));

        List<Integer> list = dataAccessMgr.queryPrimitiveList(new OpList(Integer.class, dataSource,
                "select id from user where realname like ?", "%孔%"));
        logger.info("int.list=" + list);

        List<String> list2 = dataAccessMgr.queryPrimitiveList(new OpList(String.class, dataSource,
                "select username from user where realname like ?", "%孔%"));
        logger.info("string.list=" + list2);

        List<String> list3 = dataAccessMgr.queryPrimitiveList(new OpList(dataSource,
                "select username from user where realname like ?", "%孔%"));
        logger.info("string.list=" + list3);

        List<Date> list4 = dataAccessMgr.queryPrimitiveList(new OpList(dataSource,
                "select ctime from user where realname like ?", "%孔%"));
        logger.info("date.list" + list4);

        List<User> list5 = dataAccessMgr.queryBeanList(new OpBean(User.class, dataSource,
                "select * from user where realname like ?", "%孔%"));
        logger.info("bean.list=" + list5);

        List<String> list6 = dataAccessMgr.queryList(new OpList(dataSource,
                "select * from user where realname like ?", "%孔%") {
            @Override
            public Object parse(ResultSet rs) throws SQLException {
                return rs.getString("realname");
            }
        });
        logger.info("list.parse=" + list6);
    }

    @Test
    public void testPrime() {
        logger.info(Integer.class.isPrimitive()); // false
        logger.info(Double.class.isPrimitive());
        logger.info(Float.class.isPrimitive());
        logger.info(Long.class.isPrimitive());
        logger.info(BigDecimal.class.isPrimitive());

        logger.info(int.class.isPrimitive()); // true
    }
}
