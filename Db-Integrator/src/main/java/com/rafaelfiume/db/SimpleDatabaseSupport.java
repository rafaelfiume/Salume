package com.rafaelfiume.db;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class SimpleDatabaseSupport {

    private final JdbcTemplate jdbcTemplate;

    public SimpleDatabaseSupport(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void dropDb(String schema) {
        jdbcTemplate.execute("drop schema if exists " + schema + " cascade;");
        jdbcTemplate.execute("create schema " + schema + ";");
    }

    public void execute(String statement) {
        jdbcTemplate.execute(statement);
    }

    public Boolean queryBoolean(String query) {
        return jdbcTemplate.queryForObject(query, Boolean.class);
    }

}
