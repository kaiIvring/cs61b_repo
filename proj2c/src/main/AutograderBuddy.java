package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import wordnet.Wordnet;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        Wordnet wn = new Wordnet(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);

        HyponymsHandler hh = new HyponymsHandler(wn, ngm);
        return hh;
    }
}
