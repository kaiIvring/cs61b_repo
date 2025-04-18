package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import wordnet.Wordnet;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private Wordnet wn;
    public HyponymsHandler(Wordnet wn) {
        this.wn = wn;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        Set<String> hyponym = wn.getHyponyms(words.get(0));
        for (int i = 1; i < words.toArray().length; i++) {
            Set<String> hypoSet = wn.getHyponyms(words.get(i));
            hyponym.retainAll(hypoSet);
        }

        // sort the hyponyms set alphabetically
        List<String> sortedHyponyms = new ArrayList<>(hyponym);
        Collections.sort(sortedHyponyms);

        return sortedHyponyms.toString();
    }
}
