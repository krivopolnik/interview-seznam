package cz.seznam.fulltext.robot;

public interface Processor {
    void processData(String url, String contentType, int clickCount);
    void setArgs(String[] additionalArgs);
    void outputResults();
}
