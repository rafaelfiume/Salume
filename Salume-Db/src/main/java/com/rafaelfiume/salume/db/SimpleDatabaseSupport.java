package com.rafaelfiume.salume.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SimpleDatabaseSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
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
