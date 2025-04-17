package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
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
        NgordnetQueryType type = q.ngordnetQueryType();

        Set<String> result;

        if (type == NgordnetQueryType.HYPONYMS) {
            // getAllHyponyms for a word
            result = wn.getHyponyms(words.get(0));
            for (int i = 1; i < words.size(); i++) {
                result.retainAll(wn.getHyponyms(words.get(i)));
            }
        } else if (type == NgordnetQueryType.ANCESTORS) {
            // getAllAncestors for a word
            result = wn.getAncestors(words.get(0));
            for (int i = 1; i < words.size(); i++) {
                result.retainAll(wn.getAncestors(words.get(i)));
            }
        } else {
            return "[]";
        }

        // on need to handle k
        if (k == 0 || result.size() <= k) {
            // sort the hyponyms set alphabetically
            List<String> sorted = new ArrayList<>(result);
            Collections.sort(sorted);
            return sorted.toString();
        }

        // handling k != 0
        Map<String, Integer> countMap = new HashMap<>();
        for (String word : result) {
            // word counts
            TimeSeries ts = ngm.countHistory(word, startYear, endYear);
            int count = 0;
            for (int year : ts.keySet()) {
                count += ts.get(year);
            }
            countMap.put(word, count);
        }

        // sort the countMap
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(countMap.entrySet());
        sortedEntries.sort((a, b) -> b.getValue() - a.getValue());

        // get the TopK entries' keys
        List<String> topK = new ArrayList<>();
        for (int i = 0; i < k && i < sortedEntries.size(); i++) {
            topK.add(sortedEntries.get(i).getKey());
        }

        // sort the result alphabetically
        Collections.sort(topK);
        return topK.toString();
    }
}
