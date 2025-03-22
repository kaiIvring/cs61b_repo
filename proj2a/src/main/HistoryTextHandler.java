package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    public String toString(List<String> words, int startYear, int endYear) {
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word).append(": {");

            TimeSeries ts = map.weightHistory(word, startYear, endYear);
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
