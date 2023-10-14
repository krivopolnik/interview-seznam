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
        // Initialize the processorClassMap with supported processor names and class names
        processorClassMap.put("top", TOP_PROCESSOR);
        processorClassMap.put("contentType", CONTENT_TYPE_PROCESSOR);
        processorClassMap.put("grep", GREP_PROCESSOR);
    }

    public static void main(String[] args) {
        try {
            // Process command line arguments and store them in a CommandLineArgs object
            CommandLineArgs commandLineArgs = processCommandLineArgs(args);

            if (commandLineArgs != null) {
                // Process the data using the specified processor
                process(commandLineArgs.processorClassName, commandLineArgs.additionalArgs, commandLineArgs.inputFileName);
            }
        } catch (Exception e) {
            logError("An error occurred: " + e.getMessage(), e);
        }
    }

    // Process command line arguments and return a CommandLineArgs object
    private static CommandLineArgs processCommandLineArgs(String[] args) throws IllegalArgumentException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: java Runner <processorName> <inputFileName>");
        }

        String processorName = args[0];
        String processorClassName = processorClassMap.get(processorName);
        String additionalArgs = "";
        String inputFileName = "";

        if (processorClassName != null) {
            if ("grep".equals(processorName) && args.length < 3) {
                throw new IllegalArgumentException("Error: Additional arguments are missing for the 'grep' processor.");
            } else if ("grep".equals(processorName)) {
                additionalArgs = args[1];
                inputFileName = args[2];
            } else {
                inputFileName = args[1];
            }

            if (!processorClassMap.containsKey(processorName)) {
                throw new IllegalArgumentException("Error: Invalid processor name.");
            }
    
            File inputFile = new File(inputFileName);
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("Error: Input file does not exist.");
            }
    
            return new CommandLineArgs(processorName, processorClassName, additionalArgs, inputFileName);
        } else {
            throw new IllegalArgumentException("Error: Invalid processor name.");
        }
    }

    private static class CommandLineArgs {
        String processorName;
        String processorClassName;
        String additionalArgs;
        String inputFileName;

        public CommandLineArgs(String processorName, String processorClassName, String additionalArgs, String inputFileName) {
            this.processorName = processorName;
            this.processorClassName = processorClassName;
            this.additionalArgs = additionalArgs;
            this.inputFileName = inputFileName;
        }
    }

    // Process the data using the specified processor
    static void process(String processorName, String additionalArgs, String inputFileName) {
        try {
            // Create an instance of the selected processor
            Processor processor = ProcessorFactory.createProcessor(processorName, additionalArgs);

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length == EXPECTED_PARTS_COUNT) {
                        String url = parts[0];
                        String contentType = parts[1];
                        int clickCount = Integer.parseInt(parts[2]);
                        // Process data with the processor
                        processor.processData(url, contentType, clickCount);
                    }
                }

                // If the processor is GrepProcessor, set its arguments
                if (processor instanceof GrepProcessor) {
                    ((GrepProcessor) processor).setArgs(additionalArgs);
                }

                processor.outputResults();
            }
        } catch (IOException e) {
            logError("Error reading the input file: " + e.getMessage(), e);
        }
    }

    // Log error messages with optional exceptions
    static void logError(String message, Exception e) {
        System.err.println("Error: " + message);
        if (e != null) {
            e.printStackTrace();
        }
    }
}