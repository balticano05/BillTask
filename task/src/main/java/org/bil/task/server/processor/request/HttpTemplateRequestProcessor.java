package org.bil.task.server.processor.request;

import java.net.Socket;

public interface HttpTemplateRequestProcessor {
    void process(Socket clientSocket);
}