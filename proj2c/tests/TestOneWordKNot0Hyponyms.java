import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import main.AutograderBuddy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TestOneWordKNot0Hyponyms {
    public static final String WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String HYPONYM_FILE = "data/wordnet/hyponyms.txt";
    public static final String MY_WORDS_FILE = "data/ngrams/my_words.csv";
    public static final String MY_SYNSET_FILE = "data/wordnet/my_synsets.txt";
    public static final String MY_HYPONYM_FILE = "data/wordnet/my_hyponyms.txt";

/*
    @Test
    public void testActKNot0() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("CS61A");

        NgordnetQuery nq = new NgordnetQuery(words, 2010, 2020, 4, NgordnetQueryType.HYPONYMS);
        String actual = studentHandler.handle(nq);
        String expected = "[CS170, CS61A, CS61B, CS61C]";
        assertThat(actual).isEqualTo(expected);
    }
*/

    @Test
    public void testKNot0Basic() {
        NgordnetQueryHandler sth = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SYNSET_FILE, HYPONYM_FILE);
        List<String> words = List.of("food", "cake");
        NgordnetQuery nq = new NgordnetQuery(words, 1950, 1990, 5, NgordnetQueryType.HYPONYMS);
        String actual = sth.handle(nq);
        String expected = "[cake, cookie, kiss, snap, wafer]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testKNot0Empty() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                MY_WORDS_FILE, TOTAL_COUNTS_FILE, MY_SYNSET_FILE, MY_HYPONYM_FILE);
        List<String> words = List.of("shepherd", "tool");
        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 2, NgordnetQueryType.HYPONYMS);
        String actual = studentHandler.handle(nq);
        String expected = "[]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testOneWordKNot0HyponymsBasic() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                MY_WORDS_FILE, TOTAL_COUNTS_FILE, MY_SYNSET_FILE, MY_HYPONYM_FILE);
        List<String> words = List.of("organism");
        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 5, NgordnetQueryType.HYPONYMS);
        String actual = studentHandler.handle(nq);
        String expected = "[animal, cat, dog, mammal, pet]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testEmpty() {
        NgordnetQueryHandler sth = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SYNSET_FILE, HYPONYM_FILE);
        List<String> words = List.of("thisworddoesnotexist");
        NgordnetQuery nq = new NgordnetQuery(words, 1950, 1990, 5, NgordnetQueryType.HYPONYMS);
        String actual = sth.handle(nq);
        assertThat(actual).isEqualTo("[]");
    }

}
