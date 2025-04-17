import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import main.AutograderBuddy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TestCommonAncestors {
    public static final String WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String MY_WORDS_FILE = "data/ngrams/my_words.csv";
    public static final String MY_SYNSET_FILE = "data/wordnet/my_synsets.txt";
    public static final String MY_HYPONYM_FILE = "data/wordnet/my_hyponyms.txt";

    /** This is an example from the spec for a common-ancestors query on the word "adjustment".
     * You should add more tests for the other spec examples! */
    @Test
    public void testSpecAdjustment() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("adjustment");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[adjustment, alteration, event, happening, modification, natural_event, occurrence, occurrent]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testCommonAncestorsOneWord() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                MY_WORDS_FILE, TOTAL_COUNTS_FILE, MY_SYNSET_FILE, MY_HYPONYM_FILE);
        List<String> words = List.of("shepherd");
        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 3, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[animal, mammal, shepherd]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testCommonAncestorsBasic() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                MY_WORDS_FILE, TOTAL_COUNTS_FILE, MY_SYNSET_FILE, MY_HYPONYM_FILE);
        List<String> words = List.of("shepherd", "cat");
        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 2, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[animal, mammal]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testCommonAncestorsEmpty() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                MY_WORDS_FILE, TOTAL_COUNTS_FILE, MY_SYNSET_FILE, MY_HYPONYM_FILE);
        List<String> words = List.of("shepherd", "tool");
        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 2, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[]";
        assertThat(actual).isEqualTo(expected);
    }

    // TODO: Add more unit tests (including edge case tests) here.

    // TODO: Create similar unit test files for the k != 0 cases.
}
