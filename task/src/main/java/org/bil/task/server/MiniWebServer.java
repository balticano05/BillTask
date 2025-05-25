package org.bil.task.server;

import org.bil.task.server.config.ServerTemplateConfig;
import org.bil.task.server.connection.ClientConnection;
import org.bil.task.server.processor.request.HttpTemplateRequestProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MiniWebServer implements WebServer {

    private final ServerTemplateConfig serverTemplateConfig;
    private final HttpTemplateRequestProcessor requestProcessor;

    private volatile boolean isRunning;

    public MiniWebServer(ServerTemplateConfig serverTemplateConfig, HttpTemplateRequestProcessor requestHandler) {
        this.serverTemplateConfig = serverTemplateConfig;
        this.requestProcessor = requestHandler;
        isRunning = true;
    }

    @Override
    public void launch() {
        try (ServerSocket serverSocket = new ServerSocket(serverTemplateConfig.getPort())) {

            System.out.println("Server is listening on port " + serverTemplateConfig.getPort());
            acceptConnections(serverSocket);

        } catch (IOException e) {
            throw new RuntimeException("Server startup failed", e);
        }
    }

    private void acceptConnections(ServerSocket serverSocket) {
        try {

            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                new ClientConnection(clientSocket, requestProcessor).handle();
            }

        } catch (IOException e) {
            System.err.println("Error accepting connection: " + e.getMessage());
        }
    }

    public void stop() {
        isRunning = false;
    }

}