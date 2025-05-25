package org.bil.task.server;

import org.bil.task.server.config.ServerTemplateConfig;
import org.bil.task.server.configurator.HttpTemplateResponseConfiguratorImpl;
import org.bil.task.server.processor.anchor.AnchorTemplateProcessor;
import org.bil.task.server.processor.anchor.AnchorTemplateProcessorImpl;
import org.bil.task.server.processor.request.HttpTemplateRequestProcessor;
import org.bil.task.server.processor.request.HttpTemplateRequestProcessorImpl;
import org.bil.task.server.storage.TemplateStorage;
import org.bil.task.server.storage.TemplateStorageImpl;

public class ServerInitializer {

    private final int port;
    private final TemplateStorage templateStorage;
    private final AnchorTemplateProcessor templateProcessor;
    private final HttpTemplateRequestProcessor requestProcessor;

    public ServerInitializer(int port) {
        this.port = port;
        this.templateStorage = new TemplateStorageImpl();
        this.templateProcessor = new AnchorTemplateProcessorImpl();
        this.requestProcessor = createRequestProcessor();
    }

    public void initialize() {

        templateStorage.loadTemplates();
        ServerTemplateConfig config = new ServerTemplateConfig(port);

        new MiniWebServer(config, requestProcessor).launch();
    }

    private HttpTemplateRequestProcessor createRequestProcessor() {
        return new HttpTemplateRequestProcessorImpl(
                templateStorage,
                templateProcessor,
                new HttpTemplateResponseConfiguratorImpl()
        );
    }

}