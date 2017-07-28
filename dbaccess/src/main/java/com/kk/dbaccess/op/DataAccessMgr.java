package com.kk.dbaccess.op;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kk.dbaccess.util.JdbcUtils;
import com.kk.dbaccess.util.NumberUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * datasource管理器，通过它可以实现db访问，进行db操作<br>
 * <p>
 * Class.forName(“com.mysql.jdbc.Driver”)
 * 在java 6中，引入了service provider(ServiceLoader)的概念，即可以在配置文件中配置service（可能是一个interface或者abstract class）的provider（即service的实现类）。配置路径是：/META-INF/services/下面。
 * 在jdk6中，其实是可以不用调用Class.forName来加载mysql驱动的，因为mysql的驱动程序jar包中已经包含了java.sql.Driver配置文件，并在文件中添加了com.mysql.jdbc.Driver.但在JDK6之前版本，还是要调用这个方法。
 */
public class DataAccessMgr {
    private static DataAccessMgr instance = new DataAccessMgr();

    public static DataAccessMgr getInstance() {
        return instance;
    }

    /**
     * logger
     */
    protected Log logger = LogFactory.getLog(this.getClass());

    private DataAccessMgr() {
        super();
    }

    private void closeConnection(final Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResultSet(final ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param rs
     * @param st
     * @param conn
     */
    private void closeRSC(final ResultSet rs, final Statement st,
                          final Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("res close", e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("prestatement  close", e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeStatement(final Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insert(final OpUpdate op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());
            op.setParam(ps);
            int count = ps.executeUpdate();
            if (count > 0) {
                if (ps != null) {
                    ps.close();
                }
                return true;
            } else
                return false;

        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    public int[] batchUpdate(final OpUpdate op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());
            op.setParam(ps);
            return ps.executeBatch();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    public long insertReturnId(final OpUpdate op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());
            op.setParam(ps);
            int count = ps.executeUpdate();
            if (count > 0) {
                if (ps != null) {
                    ps.close();
                }
                ps = conn.prepareStatement("select last_insert_id();");
                rs = ps.executeQuery();
                if (rs.next())
                    return rs.getLong(1);
                else
                    return 0;
            } else
                return 0;
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    //最好指定clz，否则返回值一定要和数据库中的column值类型相对应， 作用与queryInt,queryLong等作用一致。
    // 返回int，string，date等类型， 不支持bean
    public <T> T queryObject(final OpUniq<T> op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        T result;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());
            op.setParam(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                op.add(op.parse(rs));
            }
            if (rs.next()) {
                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
            }
            return (T) op.getResult();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    // 对queryObject() 返回int的封装
    public int queryInteger(final OpUniq op) throws SQLException {
        op.setClz(Integer.class);
        Number number = (Number) this.queryObject(op);
        return number != null ? number.intValue() : 0;
    }

//    public int queryInt(final OpUniq op) throws SQLException {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Connection conn = null;
//        int result = 0;
//        try {
//            conn = op.getConnection();
//            ps = conn.prepareStatement(op.getSql());
//            op.setParam(ps);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                Object o = op.parse(rs, Integer.class);
//                result = NumberUtil.parseInt(o);
//            } else
//                return 0;
//            if (rs.next()) {
//                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
//            }
//            return result;
//        } finally {
//            closeRSC(rs, ps, conn);
//        }
//    }
//
//    public long queryLong(final OpUniq op) throws SQLException {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Connection conn = null;
//        long result = 0;
//        try {
//            conn = op.getConnection();
//            ps = conn.prepareStatement(op.getSql());
//            op.setParam(ps);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                Object o = op.parse(rs, Long.class);
//                result = NumberUtil.parseLong(o);
//            } else
//                return 0;
//            if (rs.next()) {
//                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
//            }
//            return result;
//        } finally {
//            closeRSC(rs, ps, conn);
//        }
//    }
//
//    public double queryDouble(final OpUniq op) throws SQLException {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Connection conn = null;
//        double result = 0;
//        try {
//            conn = op.getConnection();
//            ps = conn.prepareStatement(op.getSql());
//            op.setParam(ps);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                Object o = op.parse(rs, Double.class);
//                result = NumberUtil.parseDouble(o);
//            } else
//                return 0;
//            if (rs.next()) {
//                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
//            }
//            return result;
//        } finally {
//            closeRSC(rs, ps, conn);
//        }
//    }

    public Date queryDate(final OpUniq op) throws SQLException {
        op.setClz(Date.class);
        return (Date) queryObject(op);
    }

//    public Date queryDate2(final OpUniq op) throws SQLException {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Connection conn = null;
//        Date result = null;
//        try {
//            conn = op.getConnection();
//            ps = conn.prepareStatement(op.getSql());
//            op.setParam(ps);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                Object o = op.parse(rs, Date.class);
//                result = NumberUtil.parseDate(o);
//            } else
//                return null;
//            if (rs.next()) {
//                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
//            }
//            return result;
//        } finally {
//            closeRSC(rs, ps, conn);
//        }
//    }

    public BigDecimal queryBigDecimal(final OpUniq op) throws SQLException {
        op.setClz(BigDecimal.class);
        return (BigDecimal) queryObject(op);
    }

    // 需要重写 OpUniq.parse方法
    public Object queryUnique(final OpUniq op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());
            op.setParam(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                op.add(op.parse(rs));
            }
            if (rs.next()) {
                logger.error("----------【error sql】----------------");
                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
                logger.error("wrong sql is:" + op.getSql());
                logger.error("wrong ps is:" + ps);
            }
            return op.getResult();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    // 返回对象
    public <T> T queryUniqueBean(final OpBean<T> op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());
            op.setParam(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                op.add(op.parse(rs));
            }
            if (rs.next()) {
                logger.error("----------【error sql】----------------");
                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
                logger.error("wrong sql is:" + op.getSql());
                logger.error("wrong ps is:" + ps);
            }
            if (op.getResult().size() > 0) {
                return op.getResult().get(0);
            } else {
                return null;
            }
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    public List<Map<String, Object>> queryMapList(final OpMap op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());

            op.setParam(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                op.add(op.parse(rs));
            }
            return op.getResult();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    public Map<String, Object> queryMap(final OpMap op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());

            op.setParam(ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                op.add(op.parse(rs));
            }
            if (rs.next()) {
                logger.error("----------【error sql】----------------");
                logger.error("Non Unique Result Error: wrong sql syntax or database not consistence!");
                logger.error("wrong sql is:" + op.getSql());
                logger.error("wrong ps is:" + ps);
            }
            if (op.getResult().size() > 0) {
                return op.getResult().get(0);
            } else {
                return null;
            }
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    // List<Integer>等，最好指定clz，否则返回值一定要和数据库中的column值类型相对应
    public <T> List<T> queryPrimitiveList(final OpList<T> op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());

            op.setParam(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                op.add(op.parse(rs));
            }
            return op.getResult();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    // 返回对象列表
    public <T> List<T> queryBeanList(final OpBean<T> op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());

            op.setParam(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                op.add(op.parse(rs));
            }
            return op.getResult();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    // 需要重写 OpList.parse方法
    public List queryList(final OpList op) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());

            op.setParam(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                op.add(op.parse(rs));
            }
            return op.getResult();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

    public int update(final OpUpdate op) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = op.getConnection();
            ps = conn.prepareStatement(op.getSql());
            op.setParam(ps);
            return ps.executeUpdate();
        } finally {
            closeRSC(rs, ps, conn);
        }
    }

}
