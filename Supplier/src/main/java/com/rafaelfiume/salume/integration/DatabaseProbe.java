package com.rafaelfiume.salume.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

// // TODO RF 24/10/15 Move it to Salume-Db?
@Repository
public class DatabaseProbe {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseProbe.class);

    private final String probeDbQuery;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseProbe(DataSource dataSource, @Value("${probe.db.query}") String probeDbQuery) {
        this.probeDbQuery = probeDbQuery;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String connectionStatus() {
        try {
            jdbcTemplate.execute(probeDbQuery);
            return "OK";

        } catch (DataAccessException e) {
            LOG.error("database probe error", e);
            return "FAILING";
        }
    }

}
