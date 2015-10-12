package com.rafaelfiume.db.config;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

public class DataSourceConfig {

    // TODO RF 10/10/2015 Triplicated from Supplier module

    public DataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }

}
