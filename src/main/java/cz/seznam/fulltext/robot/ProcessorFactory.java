package cz.seznam.fulltext.robot;

public class ProcessorFactory {

    private static final String TOP_PROCESSOR = "cz.seznam.fulltext.robot.TopProcessor";
    private static final String CONTENT_TYPE_PROCESSOR = "cz.seznam.fulltext.robot.ContentTypeProcessor";
    private static final String GREP_PROCESSOR = "cz.seznam.fulltext.robot.GrepProcessor";

    public static Processor createProcessor(String processorName, String additionalArgs) {
        if (TOP_PROCESSOR.equals(processorName)) {
            return new TopProcessor();
        } else if (CONTENT_TYPE_PROCESSOR.equals(processorName)) {
            return new ContentTypeProcessor();
        } else if (GREP_PROCESSOR.equals(processorName)) {
            GrepProcessor grepProcessor = new GrepProcessor();
            grepProcessor.setArgs(additionalArgs);
            return grepProcessor;
        } else {
            throw new IllegalArgumentException("Invalid processor name: " + processorName);
        }
    }
}