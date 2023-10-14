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
    private String additionalArgs; // Added a field to store additional arguments

    public TopProcessor() {
        // Initialize a priority queue to keep the top-N URLs based on click counts
        topUrls = new PriorityQueue<>(
            TOP_N, (entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue())
        );
    }

    @Override
    public void processData(String url, String contentType, int clickCount) {
        if (topUrls.size() < TOP_N) {
            // If the priority queue is not full, add the URL directly
            topUrls.offer(new AbstractMap.SimpleEntry<>(url, clickCount));
        } else {
            // If the priority queue is full, compare with the smallest click count
            Map.Entry<String, Integer> smallest = topUrls.peek();
            if (clickCount > smallest.getValue()) {
                // If the current click count is larger, remove the smallest and add the current URL
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
            // Pop elements from the priority queue to obtain sorted entries
            sortedEntries.add(topUrls.poll());
        }
        Collections.reverse(sortedEntries); // Reverse the list to get the top entries first

        sortedEntries.forEach(entry -> System.out.println(entry.getKey() + "\t" + entry.getValue()));
        System.out.println("Top results output complete.");
    }

    @Override
    public void setArgs(String additionalArgs) {
        this.additionalArgs = additionalArgs; // Store additional arguments
        // Add code here for processing additional arguments if necessary
    }
}