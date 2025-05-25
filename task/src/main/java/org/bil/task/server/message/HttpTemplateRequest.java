package org.bil.task.server.message;

import java.util.Map;

public class HttpTemplateRequest {

    private final String method;
    private final String path;
    private final Map<String, String> params;

    public HttpTemplateRequest(String method, String path, Map<String, String> params) {
        this.method = method;
        this.path = path;
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

}