package org.bil.task.server.message;

public class HttpTemplateResponse {

    private final int statusCode;
    private final String status;
    private final String content;

    public HttpTemplateResponse(int statusCode, String status, String content) {
        this.statusCode = statusCode;
        this.status = status;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

}