package com.kk.dbaccess.op.mapper;

import com.google.common.base.CaseFormat;
import com.kk.dbaccess.util.JdbcUtils;
import org.apache.commons.lang.reflect.FieldUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 根据 clz类型来获取值
 *
 * @param <T>
 */
public class TemplateRowMapper<T> {
    private Class<T> clz;

    public TemplateRowMapper(Class<T> clz) {
        this.clz = clz;
    }

    public T mapRow(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        Object ret = null;
        try {
            ret = clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= columnCount; ++i) {
            String key = this.getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
            try {
                Field field = FieldUtils.getField(clz, key, true);
                if (field == null && key.contains("_")) {
                    // 下划线与驼峰式写法转换
                    key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);
                    field = FieldUtils.getField(clz, key, true);
                }
//                System.out.println(key + "," + field);
                if (field == null) {
                    continue;
                }
                Class fieldType = field.getType();
                Object value = JdbcUtils.getResultSetValue(rs, i, fieldType);
                setPropertyByIntrospector(ret, key, value);
            } catch (Exception e) {
                System.err.println(String.format("set bean error,type=%s,key=%s", clz.getName(), key));
                e.printStackTrace();
            }
        }
        return (T) ret;
    }

    /**
     * 　Introspector类:
     * 　　将JavaBean中的属性封装起来进行操作。在程序把一个类当做JavaBean来看，
     * 就是调用Introspector.getBeanInfo()方法，得到的BeanInfo对象封装了把这个类当做JavaBean看的结果信息，即属性的信息。
     * 　　getPropertyDescriptors()，获得属性的描述，可以采用遍历BeanInfo的方法，来查找、设置类的属性。
     */
    private void setPropertyByIntrospector(Object obj, Object prop, Object value) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(clz);
        // 所有的 内省方法
        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();
        if (proDescrtptors != null && proDescrtptors.length > 0) {
            for (PropertyDescriptor propDesc : proDescrtptors) {
                if (propDesc.getName().equals(prop)) {
                    Method method = propDesc.getWriteMethod();
                    method.invoke(obj, value);
                    break;
                }
            }
        }
    }

    private String getColumnKey(String columnName) {
        return columnName;
    }

    private Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }
}

