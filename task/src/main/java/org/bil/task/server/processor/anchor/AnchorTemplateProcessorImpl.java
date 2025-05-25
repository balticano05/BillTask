package org.bil.task.server.processor.anchor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnchorTemplateProcessorImpl implements AnchorTemplateProcessor {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("<(\\w+)>");

    @Override
    public String processTemplate(String template, Map<String, String> replacements) {

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {

            String key = matcher.group(1);
            String value = replacements.getOrDefault(key, "");

            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }

        matcher.appendTail(result);

        return result.toString();
    }

}