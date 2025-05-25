package org.bil.task.server.config;

public class ServerTemplateConfig {

    private final int port;

    public ServerTemplateConfig(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

}