package cz.seznam.fulltext.robot;

import java.io.*;
import java.util.*;

public class Runner {
    private static final Map<String, String> processorClassMap = new HashMap<>();
    private static final int EXPECTED_PARTS_COUNT = 3;
    private static final String TOP_PROCESSOR = "cz.seznam.fulltext.robot.TopProcessor";
    private static final String CONTENT_TYPE_PROCESSOR = "cz.seznam.fulltext.robot.ContentTypeProcessor";
    private static final String GREP_PROCESSOR = "cz.seznam.fulltext.robot.GrepProcessor";

    static {
        processorClassMap.put("top", TOP_PROCESSOR);
        processorClassMap.put("contentType", CONTENT_TYPE_PROCESSOR);
        processorClassMap.put("grep", GREP_PROCESSOR);
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
                String[] processorArgs;
    
                if ("grep".equals(processorName) && args.length < 3) {
                    System.out.println("Error: Additional arguments are missing for the 'grep' processor.");
                    System.exit(1);
                }
    
                if (args.length > 2) {
                    processorArgs = Arrays.copyOfRange(args, 2, args.length);
                } else {
                    processorArgs = new String[0]; // Пустой массив аргументов
                }
    
                process(processorClassName, args[1], processorArgs);
            } else {
                System.out.println("Error: Invalid processor name.");
                System.exit(1);
            }
        } catch (Exception e) {
            logError("An error occurred: " + e.getMessage(), e);
        }
    }
    

    static void process(String processorName, String inputFileName, String[] additionalArgs) {
        Processor processor = ProcessorFactory.createProcessor(processorName, additionalArgs);
    
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
    
            if (processor instanceof GrepProcessor) {
                ((GrepProcessor) processor).setArgs(additionalArgs);
            }
    
            processor.outputResults();
        } catch (IOException e) {
            logError("Error reading the input file: " + e.getMessage(), e);
        }
    }
    

    static void logError(String message, Exception e) {
        System.err.println("Error: " + message);
        if (e != null) {
            e.printStackTrace();
        }
    }
}