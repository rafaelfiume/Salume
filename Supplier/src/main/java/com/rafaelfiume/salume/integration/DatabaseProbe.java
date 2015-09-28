package com.rafaelfiume.salume.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class DatabaseProbe { // TODO RF 28/09/15 An interface would not be bad

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseProbe.class);

    // Note: Have a look at http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#jdbc-choose-style
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseProbe(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String connectionStatus() { // TODO RF 28/09/15 Returning an enum representing possible status would not be bad either
        try {
            jdbcTemplate.execute("SELECT * FROM DUAL");
            return "OK";

        } catch (DataAccessException e) {
            LOG.error("database probe error", e);
            return "FAILING";
        }
    }

}
