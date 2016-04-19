package com.rafaelfiume.db.plugin.support;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class SimpleJdbcDatabaseSupport {

    private final JdbcTemplate jdbcTemplate;

    public SimpleJdbcDatabaseSupport(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void dropAndCreate(String schema) {
        jdbcTemplate.execute("drop schema if exists " + schema + " cascade");
        jdbcTemplate.execute("create schema " + schema);
    }

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

    public void drop(String schema) {
        jdbcTemplate.execute("drop schema if exists " + schema + " cascade");
    }

}
