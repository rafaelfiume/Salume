package com.rafaelfiume.db.plugin.database;

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
        drop(schema);
        create(schema);
    }

    public void create(String schema) {
        jdbcTemplate.execute("create schema " + schema);
    }

    public void drop(String schema) {
        jdbcTemplate.execute("drop schema if exists " + schema + " cascade");
    }

    public void execute(String statement) {
        jdbcTemplate.execute(statement);
    }

    public void update(String statement, Object... args) {
        jdbcTemplate.update(statement, args);
    }

    public Boolean queryBoolean(String query) {
        return jdbcTemplate.queryForObject(query, Boolean.class);
    }

    public String queryString(String query) {
        return jdbcTemplate.queryForObject(query, String.class);
    }

    public <T> T queryObject(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void cleanTable(String tableName) {
        jdbcTemplate.execute("delete from " + tableName);
    }

}
