package com.rafaelfiume.db.plugin.support;

import com.rafaelfiume.db.plugin.config.DataSourceConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

// TODO RF 23/10/2015 Duplicated from Salume-Db (test remains in Salume-Db only)
public class SimpleJdbcDatabaseSupport implements SimpleDatabaseSupport {

    private final JdbcTemplate jdbcTemplate;

    public SimpleJdbcDatabaseSupport(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void dropDb(String schema) {
        jdbcTemplate.execute("drop schema if exists " + schema + " cascade");
        jdbcTemplate.execute("create schema " + schema);
    }

    @Override
    public void execute(String statement) {
        jdbcTemplate.execute(statement);
    }

    public Boolean queryBoolean(String query) {
        return jdbcTemplate.queryForObject(query, Boolean.class);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void cleanTable(String tableName) {
        jdbcTemplate.execute("delete from " + tableName);
    }

}
