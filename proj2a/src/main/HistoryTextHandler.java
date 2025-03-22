package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.ArrayList;
import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    // format: "word: {data1, data2, ..., data_n}"
    public String toString(List<String> words, int startYear, int endYear) {
        StringBuilder sb = new StringBuilder();
        List<String> wordsList = new ArrayList<>();
        for (String word : words) {
            TimeSeries ts = map.weightHistory(word, startYear, endYear);
            // deal with empty TimeSeries
            if (ts.isEmpty()) {
                System.out.println("word: " + "\"" + word + "\"" + " does not exist!");
                continue;
            }

            // deal with duplicates
            if (wordsList.contains(word)) {
                System.out.println("word: " + "\"" + word + "\"" + " has already exist!");
                continue;
            }
            wordsList.add(word);

            sb.append(word).append(": {");

            int count = 0;
            for (int year : ts.keySet()) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append(year).append("=").append(ts.get(year));
                count++;
            }
            sb.append("}\n");
        }

        return sb.toString();
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String response = toString(words, startYear, endYear);
        return response;

    }
}
