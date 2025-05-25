package org.bil.task.server.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateStorageImpl implements TemplateStorage {

    private final Map<String, String> templates = new ConcurrentHashMap<>();

    @Override
    public void loadTemplates() {
        try {

            templates.put("template1", Files.readString(Path.of("src/main/resources/template1.txt")));
            templates.put("template2", Files.readString(Path.of("src/main/resources/template2.txt")));
            templates.put("template3", Files.readString(Path.of("src/main/resources/template3.txt")));

        } catch (IOException e) {
            System.err.println("Failed to load templates: " + e.getMessage());
            throw new RuntimeException("Template loading failed", e);
        }
    }

    @Override
    public String getTemplate(String name) {
        return templates.getOrDefault(name, "");
    }

}