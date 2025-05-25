package org.bil.task.server.storage;

public interface TemplateStorage {

    void loadTemplates();

    String getTemplate(String name);

}