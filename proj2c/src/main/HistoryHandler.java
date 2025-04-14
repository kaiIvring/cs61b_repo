package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap map;
    public HistoryHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        List<String> wordsExist = new ArrayList<>();

        List<TimeSeries> lts = new ArrayList<>();
        for (String word : words) {
            TimeSeries ts = map.weightHistory(word, startYear, endYear);
            // if input word does not exist
            if (ts.isEmpty()) {
                System.out.println("word: " + "\"" + word + "\"" + " does not exist!");
                continue;
            }

            // if input has duplicates, skip it
            if (wordsExist.contains(word)) {
                System.out.println("word: " + "\"" + word + "\"" + " has already exist!");
                continue;
            }
            wordsExist.add(word);
            lts.add(ts);
        }

        XYChart chart = Plotter.generateTimeSeriesChart(wordsExist, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
