package com.rafaelfiume.salume.web.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStream;
import java.net.URL;
import java.util.jar.Manifest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class StatusPageController {

    @RequestMapping(value = "/status", method = GET)
    public ResponseEntity<String> handle() {

        final String body = new StringBuilder("Salume Supplier is: OK\n") // TODO Retrieve the app name from properties
                .append("Version: ").append(appVersion()).toString();

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private String appVersion() {
        return getManifest(this.getClass()).getMainAttributes().getValue("Implementation-Version");
    }

    public static Manifest getManifest(Class<?> clz) {
        // Really boring stuff to code! Glad I found it in the web... Source: http://stackoverflow.com/questions/1272648/reading-my-own-jars-manifest/29103019#29103019
        String resource = "/" + clz.getName().replace(".", "/") + ".class";
        String fullPath = clz.getResource(resource).toString();
        String archivePath = fullPath.substring(0, fullPath.length() - resource.length());
        if (archivePath.endsWith("\\WEB-INF\\classes") || archivePath.endsWith("/WEB-INF/classes")) {
            archivePath = archivePath.substring(0, archivePath.length() - "/WEB-INF/classes".length()); // Required for wars
        }

        try (InputStream input = new URL(archivePath + "/META-INF/MANIFEST.MF").openStream()) {
            return new Manifest(input);
        } catch (Exception e) {
            throw new RuntimeException("Loading MANIFEST for class " + clz + " failed!", e);
        }
    }
}
