package com.kk.dbaccess.op;

import com.kk.dbaccess.op.mapper.ColumnMapRowMapper;
import com.kk.dbaccess.op.mapper.SingleColumnRowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OpList<T> extends AbstractOp {
    private List<T> result = new ArrayList<T>();

    private Class<T> clz;

    public OpList() {
    }

    public OpList(DataSource dataSource, String sql) {
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public OpList(DataSource dataSource, String sql, Object... params) {
        this.dataSource = dataSource;
        this.sql = sql;
        this.params = params;
    }

    public OpList(Class<T> clz) {
        this.clz = clz;
    }

    public OpList(Class<T> clz, DataSource dataSource, String sql) {
        this.clz = clz;
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public OpList(Class<T> clz, DataSource dataSource, String sql, Object... params) {
        this.clz = clz;
        this.dataSource = dataSource;
        this.sql = sql;
        this.params = params;
    }

    public Class<T> getClz() {
        return clz;
    }

    public void setClz(Class<T> clz) {
        this.clz = clz;
    }

    public List<T> getResult() {
        return result;
    }

    public void add(T t) {
        this.result.add(t);
    }

    //返回 list<map>可使用OpMap
    // 返回list<bean>可使用obBeanList

    // 返回list<string>,list<int>等方式时候直接使用即可。 最好指定clz，否则返回值一定要和数据库中的column值类型相对应
    @Override
    public T parse(ResultSet rs) throws SQLException {
        SingleColumnRowMapper<T> rowMapper = new SingleColumnRowMapper<T>(clz);
        return rowMapper.mapRow(rs);
    }
}
