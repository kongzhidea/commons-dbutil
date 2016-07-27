package com.kk.dbaccess.op;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Op
 */
public abstract class Op {
    protected Log logger = LogFactory.getLog(this.getClass());

    protected String sql;
    protected DataSource dataSource;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final String getSql() {
        if (logger.isDebugEnabled()) {
            logger.debug("getSQL: " + sql);
        }
        return sql;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final void log(Log logger) {
        if (logger.isDebugEnabled()) {
            logger.debug(getSql());
        }
    }

    public void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                stmt.setObject(i + 1, params[i]);
            } else {
                int sqlType = Types.VARCHAR;
                stmt.setNull(i + 1, sqlType);
            }
        }
    }

    public abstract void setParam(PreparedStatement ps) throws SQLException;

    public abstract Object parse(ResultSet rs) throws SQLException;
}