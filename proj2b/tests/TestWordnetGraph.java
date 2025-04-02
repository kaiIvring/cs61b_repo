import org.junit.jupiter.api.Test;
import wordnet.WordnetGraph;

import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class TestWordnetGraph {
    @Test
    public void testAddSynsetAndGetWords() {
        WordnetGraph wg = new WordnetGraph();
        wg.addSynset(1, Set.of("happy"));
        wg.addSynset(2, Set.of("sad", "unhappy"));

        assertThat(wg.getWords(1)).containsExactly("happy");
        assertThat(wg.getWords(2)).containsExactly("sad", "unhappy");
        assertThat(wg.getWords(3)).isEmpty();
    }

    @Test
    public void testAddHyponymAndGetAllHyponyms() {
        WordnetGraph wg = new WordnetGraph();
        // add synsets
        wg.addSynset(1, Set.of("animal", "creature"));
        wg.addSynset(2, Set.of("dog"));
        wg.addSynset(3, Set.of("cat"));
        wg.addSynset(4, Set.of("wolf"));
        wg.addSynset(5, Set.of("tiger"));

        // add hyponym relation
        wg.addHyponym(1, 2); // animal → dog
        wg.addHyponym(1, 3); // animal → cat
        wg.addHyponym(2, 4); // dog → wolf
        wg.addHyponym(3, 5); // cat → tiger

        Set<String> hyponymsAnimal = wg.getAllHyponyms("animal");
        assertThat(hyponymsAnimal).containsExactly("animal", "dog", "cat", "wolf", "tiger", "creature");

        Set<String> hyponymsDog = wg.getAllHyponyms("dog");
        assertThat(hyponymsDog).containsExactly("dog", "wolf");

        Set<String> hyponymsCat = wg.getAllHyponyms("cat");
        assertThat(hyponymsCat).containsExactly("cat", "tiger");

        Set<String> hyponymsUnknown = wg.getAllHyponyms("fish");
        assertThat(hyponymsUnknown).isEmpty();
    }

    
}
