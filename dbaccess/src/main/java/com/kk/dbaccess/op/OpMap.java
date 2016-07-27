package com.kk.dbaccess.op;

import com.kk.dbaccess.op.mapper.ColumnMapRowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OpMap extends AbstractOp {
    private List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

    public OpMap() {
    }

    public OpMap(DataSource dataSource, String sql) {
        this.dataSource = dataSource;
        this.sql = sql;
    }

    public OpMap(DataSource dataSource, String sql, Object... params) {
        this.dataSource = dataSource;
        this.sql = sql;
        this.params = params;
    }

    public List<Map<String, Object>> getResult() {
        return result;
    }

    public void add(Map<String, Object> map) {
        this.result.add(map);
    }

    @Override
    public Map<String, Object> parse(ResultSet rs) throws SQLException {
        ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
        return rowMapper.mapRow(rs);
    }
}
