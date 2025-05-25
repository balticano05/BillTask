package org.bil.task.server.configurator;

import org.bil.task.server.message.HttpTemplateResponse;

import java.io.PrintWriter;

public interface HttpTemplateResponseConfigurator {
    void configure(HttpTemplateResponse response, PrintWriter out);
}