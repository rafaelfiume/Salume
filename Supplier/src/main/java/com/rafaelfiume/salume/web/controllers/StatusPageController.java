package com.rafaelfiume.salume.web.controllers;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.jar.Manifest;


import static javax.servlet.http.HttpServletResponse.SC_OK;

public class StatusPageController extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain; charset=utf-8");
        response.setStatus(SC_OK);

        PrintWriter out = response.getWriter();

        out.println("Salume Supplier is: OK\n"); // TODO Retrieve the app name from properties
        out.println("Version: " + appVersion());

        baseRequest.setHandled(true);
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
