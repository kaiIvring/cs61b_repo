package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.TreeMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    // word -> TimeSeries -> year, counts
    private TreeMap<String, TimeSeries> wordsFile;
    // year -> counts
    private TreeMap<Integer, Double> countsFile;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordsFile = new TreeMap<>();
        countsFile = new TreeMap<>();

        // words file
        In inWords = new In(wordsFilename);
        while (!inWords.isEmpty()) {
            String nextLine = inWords.readLine();
            String[] splitLine = nextLine.split("\t");

            String word = splitLine[0];
            int year = Integer.parseInt(splitLine[1]);
            double count = Double.parseDouble(splitLine[2]);

            wordsFile.putIfAbsent(word, new TimeSeries());
            wordsFile.get(word).put(year, count);
        }

        // counts file
        In inCounts = new In(countsFilename);
        while (!inCounts.isEmpty()) {
            String nextLine = inCounts.readLine();
            String[] splitLine = nextLine.split(",");

            int year = Integer.parseInt(splitLine[0]);
            double totalCount = Double.parseDouble(splitLine[1]);

            countsFile.put(year, totalCount);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordsFile.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordsFile.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordsFile.containsKey(word)) {
            return new TimeSeries();
        }
        return new TimeSeries(wordsFile.get(word), MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries ts = new TimeSeries();
        for (int year : countsFile.keySet()) {
            ts.put(year, countsFile.get(year));
        }
        return ts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordsFile.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = countHistory(word, startYear, endYear);
        TimeSeries result = new TimeSeries();

        for (int year : ts.keySet()) {
            if (countsFile.containsKey(year)) {
                result.put(year, ts.get(year) / countsFile.get(year));
            }
        }
        return result;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!wordsFile.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = countHistory(word);
        TimeSeries result = new TimeSeries();

        for (int year : ts.keySet()) {
            if (ts.containsKey(year)) {
                result.put(year, ts.get(year) / countsFile.get(year));
            }
        }
        return result;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            if (!wordsFile.containsKey(word)) {
                continue;
            }
            TimeSeries ts = weightHistory(word, startYear, endYear);
            for (int year : ts.keySet()) {
                result.put(year, result.getOrDefault(year, 0.0) + ts.get(year));
            }
        }
        return result;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            if (!wordsFile.containsKey(word)) {
                continue;
            }
            TimeSeries ts = weightHistory(word);
            for (int year : ts.keySet()) {
                result.put(year, result.getOrDefault(year, 0.0) + ts.get(year));
            }
        }
        return result;
    }
}
