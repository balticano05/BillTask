package org.bil.task.server.connection;

import org.bil.task.server.processor.request.HttpTemplateRequestProcessor;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection implements Runnable {

    private final Socket socket;
    private final HttpTemplateRequestProcessor handler;

    public ClientConnection(Socket socket, HttpTemplateRequestProcessor handler) {
        this.socket = socket;
        this.handler = handler;
    }

    public void handle() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try (Socket clientSocket = socket) {
            handler.process(clientSocket);
        } catch (IOException e) {
            System.err.println("Client connection error: " + e.getMessage());
        }
    }

}