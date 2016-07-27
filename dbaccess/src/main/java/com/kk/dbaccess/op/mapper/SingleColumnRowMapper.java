
package com.kk.dbaccess.op.mapper;

import com.kk.dbaccess.util.JdbcUtils;
import com.kk.dbaccess.util.NumberUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class SingleColumnRowMapper<T> {

    private Class<T> requiredType;


    /**
     * Create a new SingleColumnRowMapper.
     *
     * @see #setRequiredType
     */
    public SingleColumnRowMapper() {
    }

    /**
     * Create a new SingleColumnRowMapper.
     *
     * @param requiredType the type that each result object is expected to match
     */
    public SingleColumnRowMapper(Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    /**
     * Set the type that each result object is expected to match.
     * <p>If not specified, the column value will be exposed as
     * returned by the JDBC driver.
     */
    public void setRequiredType(Class<T> requiredType) {
        this.requiredType = requiredType;
    }


    @SuppressWarnings("unchecked")
    public T mapRow(ResultSet rs) throws SQLException {
        // Validate column count.
        ResultSetMetaData rsmd = rs.getMetaData();
        int nrOfColumns = rsmd.getColumnCount();
        if (nrOfColumns != 1) {
            throw new IllegalArgumentException("rs.column number is greater than 1");
        }

        // Extract column value from JDBC ResultSet.
        Object result = getColumnValue(rs, 1, this.requiredType);
        if (result != null && this.requiredType != null && !this.requiredType.isInstance(result)) {
            // Extracted value does not match already: try to convert it.
            try {
                return (T) convertValueToRequiredType(result, this.requiredType);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Type mismatch affecting row number and column type '" +
                                rsmd.getColumnTypeName(1) + "': " + ex.getMessage());
            }
        }
        return (T) result;
    }

    protected Object getColumnValue(ResultSet rs, int index, Class requiredType) throws SQLException {
        if (requiredType != null) {
            return JdbcUtils.getResultSetValue(rs, index, requiredType);
        } else {
            // No required type specified -> perform default extraction.
            return getColumnValue(rs, index);
        }
    }

    protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }

    protected Object convertValueToRequiredType(Object value, Class requiredType) {
        if (String.class.equals(requiredType)) {
            return value.toString();
        } else if (Number.class.isAssignableFrom(requiredType)) {
            if (value instanceof Number) {
                // Convert original Number to target Number class.
                return NumberUtil.convertNumberToTargetClass(((Number) value), requiredType);
            } else {
                // Convert stringified value to target Number class.
                return NumberUtil.parseNumber(value.toString(), requiredType);
            }
        } else {
            throw new IllegalArgumentException(
                    "Value [" + value + "] is of type [" + value.getClass().getName() +
                            "] and cannot be converted to required type [" + requiredType.getName() + "]");
        }
    }

}
