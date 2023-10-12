package cz.seznam.fulltext.robot;

import java.io.*;
import java.util.*;
import java.util.regex.*;


public class Runner {
    private static final Map<String, String> processorClassMap = new HashMap<>();
    private static final int EXPECTED_PARTS_COUNT = 3;

    static {
        processorClassMap.put("top", "cz.seznam.fulltext.robot.TopProcessor");
        processorClassMap.put("contentType", "cz.seznam.fulltext.robot.ContentTypeProcessor");
        processorClassMap.put("grep", "cz.seznam.fulltext.robot.GrepProcessor");
    }

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.println("Usage: java Runner <processorName> <inputFileName>");
                System.exit(1);
            }

            String processorName = args[0];
            String processorClassName = processorClassMap.get(processorName);

            if (processorClassName != null) {
                process(processorClassName, args[1]);
            } else {
                System.out.println("Error: Invalid processor name.");
                System.exit(1);
            }
        } catch (Exception e) {
            logError("An error occurred: " + e.getMessage(), e);
        }
    }

    static void process(String processorClassName, String inputFileName) {
        try {
            Class<?> processorClass = Class.forName(processorClassName);
            Processor processor = (Processor) processorClass.newInstance();
    
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length == EXPECTED_PARTS_COUNT) {
                        String url = parts[0];
                        String contentType = parts[1];
                        int clickCount = Integer.parseInt(parts[2]);
                        processor.processData(url, contentType, clickCount);
                    }
                }
    
                processor.outputResults();
            } catch (IOException e) {
                logError("Error reading the input file: " + e.getMessage(), e);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            logError("Error creating processor instance: " + e.getMessage(), e);
        }
    }    
    
    static void logError(String message, Exception e) {
        System.err.println("Error: " + message);
        if (e != null) {
            e.printStackTrace();
        }
    }
}

interface Processor {
    void processData(String url, String contentType, int clickCount);
    void outputResults();
}


class TopProcessor implements Processor {

    private static final int TOP_N = 10;
    private PriorityQueue<Map.Entry<String, Integer>> topUrls;

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
}



class ContentTypeProcessor implements Processor {
    private Map<String, Integer> contentTypeCounts = new HashMap<>();

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
}


class GrepProcessor implements Processor {
    private String regex;
    private List<String> matchedLines = new ArrayList<>();

    public GrepProcessor() {
        // Пустой конструктор
    }
    
    public GrepProcessor(String regex) {
        this.regex = regex;
    }

    @Override
    public void processData(String url, String contentType, int clickCount) {
        if (Pattern.matches(regex, url)) {
            matchedLines.add(url + "\t" + contentType + "\t" + clickCount);
        }
    }

    @Override
    public void outputResults() {
        System.out.println("Outputting grep results...");
        matchedLines.forEach(System.out::println);
        System.out.println("Grep results output complete.");
    }
}
