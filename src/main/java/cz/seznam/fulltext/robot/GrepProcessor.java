package cz.seznam.fulltext.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GrepProcessor implements Processor {
    private String[] regex;
    private List<String> matchedLines = new ArrayList<>();

    @Override
    public void processData(String url, String contentType, int clickCount) {
        String line = url + "\t" + contentType + "\t" + clickCount;
        String combinedLine = String.join("\t", line);

        // Iterate through each regular expression pattern
        for (String pattern : regex) {
            // Compile the regular expression pattern
            Pattern p = Pattern.compile(pattern);

            // Create a Matcher for the combined line
            Matcher m = p.matcher(combinedLine);

            // Check for any matches in the combined line
            if (m.find()) {
                matchedLines.add(line);
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
    public void setArgs(String additionalArgs) {
        // Split the additional arguments into an array of regular expression patterns
        this.regex = additionalArgs.split(" ");
    }
}