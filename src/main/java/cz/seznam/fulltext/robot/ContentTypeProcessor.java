package cz.seznam.fulltext.robot;

import java.util.HashMap;
import java.util.Map;

class ContentTypeProcessor implements Processor {
    private Map<String, Integer> contentTypeCounts = new HashMap<>();
    private String[] additionalArgs;

    @Override
    public void processData(String url, String contentType, int clickCount) {
        contentTypeCounts.put(contentType, contentTypeCounts.getOrDefault(contentType, 0) + 1);
    }

    @Override
    public void outputResults() {
        System.out.println("Outputting content type results...");
        contentTypeCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(entry.getKey() + "\t" + entry.getValue()));
        System.out.println("Content type results output complete.");
    }

    @Override
    public void setArgs(String[] additionalArgs) {
        this.additionalArgs = additionalArgs; // Сохраняем дополнительные аргументы
        // Добавьте здесь код для обработки дополнительных аргументов, если необходимо
    }
}
