package org.bil.task.server.processor.request;

import org.bil.task.server.configurator.HttpTemplateResponseConfigurator;
import org.bil.task.server.message.HttpTemplateRequest;
import org.bil.task.server.message.HttpTemplateResponse;
import org.bil.task.server.processor.anchor.AnchorTemplateProcessor;
import org.bil.task.server.storage.TemplateStorage;
import org.bil.task.server.utils.QueryParserUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class HttpTemplateRequestProcessorImpl implements HttpTemplateRequestProcessor {

    private final TemplateStorage templateStorage;
    private final AnchorTemplateProcessor templateProcessor;
    private final HttpTemplateResponseConfigurator responseConfigurator;

    public HttpTemplateRequestProcessorImpl(TemplateStorage templateStorage,
                                            AnchorTemplateProcessor templateProcessor,
                                            HttpTemplateResponseConfigurator responseConfigurator) {
        this.templateStorage = templateStorage;
        this.templateProcessor = templateProcessor;
        this.responseConfigurator = responseConfigurator;
    }

    @Override
    public void process(Socket clientSocket) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {

            HttpTemplateRequest request = extractRequest(in);
            HttpTemplateResponse response = processRequest(request);

            responseConfigurator.configure(response, out);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpTemplateRequest extractRequest(BufferedReader in) throws IOException {

        String requestLine = in.readLine();

        if (requestLine == null || requestLine.isEmpty()) {
            return new HttpTemplateRequest(null, null, Collections.emptyMap());
        }

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 2) {
            return new HttpTemplateRequest(null, null, Collections.emptyMap());
        }

        String pathAndQuery = URLDecoder.decode(requestParts[1], StandardCharsets.UTF_8);
        String[] pathQuery = pathAndQuery.split("\\?", 2);
        String path = pathQuery[0];
        String templateName = path.startsWith("/") ? path.substring(1) : path;

        Map<String, String> params = QueryParserUtils.parseQueryParameter(pathQuery.length > 1 ? pathQuery[1] : null);

        return new HttpTemplateRequest(requestParts[0], templateName, params);
    }

    private HttpTemplateResponse processRequest(HttpTemplateRequest request) {

        if (request.getMethod() == null || request.getMethod().isEmpty()) {
            return new HttpTemplateResponse(400, "Bad Request", null);
        }

        String templateContent = templateStorage.getTemplate(request.getPath());

        if (templateContent == null || templateContent.isEmpty()) {
            return new HttpTemplateResponse(404, "Not Found", null);
        }

        try {

            String processedContent = templateProcessor.processTemplate(
                    templateContent,
                    request.getParams()
            );

            return new HttpTemplateResponse(200, "OK", processedContent);

        } catch (RuntimeException e) {
            return new HttpTemplateResponse(500, "Internal Server Error", "Template processing error");
        }

    }

}