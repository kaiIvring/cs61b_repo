package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import wordnet.Wordnet;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private Wordnet wn;
    private NGramMap ngm;
    public HyponymsHandler(Wordnet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int k = q.k();
        int startYear = q.startYear();
        int endYear = q.endYear();

        // getAllHyponyms for a word
        Set<String> hyponym = wn.getHyponyms(words.get(0));
        for (int i = 1; i < words.toArray().length; i++) {
            Set<String> hypoSet = wn.getHyponyms(words.get(i));
            hyponym.retainAll(hypoSet);
        }

        // handling k != 0
        if (hyponym.size() > k && k != 0) {
            // store every word with its counts: hyponymMap
            Map<String, Integer> hyponymMap = new HashMap<>();
            for (String word : hyponym) {
                TimeSeries ts = ngm.countHistory(word, startYear, endYear);
                int count = 0;
                for (int year : ts.keySet()) {
                    count += ts.get(year);
                }
                hyponymMap.put(word, count);
            }
            List<Map.Entry<String, Integer>> entries = new ArrayList<>(hyponymMap.entrySet());
            entries.sort((a, b) -> b.getValue() - a.getValue());

            List<String> topK = new ArrayList<>();
            for (int i = 0; i < k && i < entries.size(); i++) {
                topK.add(entries.get(i).getKey());
            }

            Collections.sort(topK);
            return topK.toString();
        }

        // sort the hyponyms set alphabetically
        List<String> sortedHyponyms = new ArrayList<>(hyponym);
        Collections.sort(sortedHyponyms);

        return sortedHyponyms.toString();
    }
}
