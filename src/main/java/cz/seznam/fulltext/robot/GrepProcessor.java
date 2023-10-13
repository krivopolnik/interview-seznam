package cz.seznam.fulltext.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


class GrepProcessor implements Processor {
    private String[] regex;
    private List<String> matchedLines = new ArrayList<>();
    
    @Override
    public void processData(String url, String contentType, int clickCount) {
        for (String pattern : regex) {
            if (Pattern.matches(".*\\b" + pattern + "\\b.*", url)) {
                matchedLines.add(url + "\t" + contentType + "\t" + clickCount);
            }
        }
    }
    

    @Override
    public void outputResults() {
        System.out.println("Outputting grep results...");
        matchedLines.forEach(System.out::println);
        System.out.println("Grep results output complete.");
    }

    @Override
    public void setArgs(String[] additionalArgs) {
        this.regex = additionalArgs; // Сохраняем дополнительные аргументы в массив регулярных выражений
    }
}

