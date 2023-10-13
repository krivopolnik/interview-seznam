package cz.seznam.fulltext.robot;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

class TopProcessor implements Processor {
    private static final int TOP_N = 10;
    private PriorityQueue<Map.Entry<String, Integer>> topUrls;
    private String[] additionalArgs; // Добавлено поле для хранения дополнительных аргументов

    public TopProcessor() {
        topUrls = new PriorityQueue<>(
            TOP_N, (entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue())
        );
    }

    @Override
    public void processData(String url, String contentType, int clickCount) {
        if (topUrls.size() < TOP_N) {
            topUrls.offer(new AbstractMap.SimpleEntry<>(url, clickCount));
        } else {
            Map.Entry<String, Integer> smallest = topUrls.peek();
            if (clickCount > smallest.getValue()) {
                topUrls.poll();
                topUrls.offer(new AbstractMap.SimpleEntry<>(url, clickCount));
            }
        }
    }

    @Override
    public void outputResults() {
        System.out.println("Outputting top 10 results...");
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>();
        while (!topUrls.isEmpty()) {
            sortedEntries.add(topUrls.poll());
        }
        Collections.reverse(sortedEntries);

        sortedEntries.forEach(entry -> System.out.println(entry.getKey() + "\t" + entry.getValue()));
        System.out.println("Top results output complete.");
    }

    @Override
    public void setArgs(String[] additionalArgs) {
        this.additionalArgs = additionalArgs; // Сохраняем дополнительные аргументы
        // Добавьте здесь код для обработки дополнительных аргументов, если необходимо
    }
}