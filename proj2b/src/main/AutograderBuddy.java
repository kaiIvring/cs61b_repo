package main;

import browser.NgordnetQueryHandler;
import wordnet.Wordnet;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(String wordFile, String countFile, String synsetFile, String hyponymFile) {
        Wordnet wn = new Wordnet(synsetFile, hyponymFile);
        HyponymsHandler hy = new HyponymsHandler(wn);
        return hy;
    }
}
