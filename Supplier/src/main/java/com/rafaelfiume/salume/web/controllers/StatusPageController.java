package com.rafaelfiume.salume.web.controllers;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static javax.servlet.http.HttpServletResponse.SC_OK;

public class StatusPageController extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain; charset=utf-8");
        response.setStatus(SC_OK);

        PrintWriter out = response.getWriter();

        out.println("Status Page\n");
        out.println("Salume Supplier is: OK"); // TODO Retrieve the app name from properties

        baseRequest.setHandled(true);
    }
}
