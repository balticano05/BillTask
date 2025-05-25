package org.bil.task.server.configurator;

import org.bil.task.server.message.HttpTemplateResponse;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HttpTemplateResponseConfiguratorImpl implements HttpTemplateResponseConfigurator {

    @Override
    public void configure(HttpTemplateResponse response, PrintWriter out) {

        out.printf("HTTP/1.1 %d %s%n", response.getStatusCode(), response.getStatus());
        out.println("Content-Type: text/html; charset=UTF-8");

        if (response.getContent() != null && !response.getContent().isEmpty()) {
            out.printf("Content-Length: %d%n%n", response.getContent().getBytes(StandardCharsets.UTF_8).length);
            out.println(response.getContent());
        } else {
            out.println();
        }

    }

}