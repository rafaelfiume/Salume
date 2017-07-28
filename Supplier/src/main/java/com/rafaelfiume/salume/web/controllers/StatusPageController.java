package com.rafaelfiume.salume.web.controllers;

import com.rafaelfiume.salume.db.DatabaseProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;

import static java.lang.System.lineSeparator;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@SuppressWarnings("unused")
public class StatusPageController {

    private final String appName;

    private final DatabaseProbe databaseProbe;

    @Autowired
    public StatusPageController(@Value("${app.name}") String appName, DatabaseProbe databaseProbe) {
        this.appName = appName;
        this.databaseProbe = databaseProbe;
    }

    @RequestMapping(value = "/status", method = GET, produces = "text/plain")
    public ResponseEntity<String> handle() {

        final String dbStatus = databaseProbe.connectionStatus();

        final String body = new StringBuilder(appName).append(" is: ").append(dbStatus)
                .append(lineSeparator())
                .append("Version: ").append(appVersion())
                .append(lineSeparator())
                .append("Database connections is: ").append(dbStatus)
                .toString();

        return new ResponseEntity<>(body, OK);
    }

    private String appVersion() {
        return System.getenv("BUILD_NUMBER");
    }

}
