package org.bil.task.server.processor.anchor;

import java.util.Map;

public interface AnchorTemplateProcessor {
    String processTemplate(String template, Map<String, String> replacements);
}