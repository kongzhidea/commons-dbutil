package com.kk.dbutils.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.RowProcessor;

/**
 * 进行下划线和驼峰式写法的转换
 *
 * @param <T>
 */
public class BeanTplHandler<T> implements ResultSetHandler<T> {

    /**
     * The Class of beans produced by this handler.
     */
    private final Class<T> type;

    /**
     * The RowProcessor implementation to use when converting rows
     * into beans.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of BeanHandler.
     *
     * @param type The Class that objects returned from <code>handle()</code>
     *             are created from.
     */
    public BeanTplHandler(Class<T> type) {
        this(type, new BeanRowProcessor());
    }

    /**
     * Creates a new instance of BeanHandler.
     *
     * @param type    The Class that objects returned from <code>handle()</code>
     *                are created from.
     * @param convert The <code>RowProcessor</code> implementation
     *                to use when converting rows into beans.
     */
    public BeanTplHandler(Class<T> type, RowProcessor convert) {
        this.type = type;
        this.convert = convert;
    }

    /**
     * Convert the first row of the <code>ResultSet</code> into a bean with the
     * <code>Class</code> given in the constructor.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return An initialized JavaBean or <code>null</code> if there were no
     * rows in the <code>ResultSet</code>.
     * @throws SQLException if a database access error occurs
     * @see org.apache.commons.dbutils.ResultSetHandler#handle(java.sql.ResultSet)
     */
    @Override
    public T handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toBean(rs, this.type) : null;
    }

}
