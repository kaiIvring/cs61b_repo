package wordnet;

import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Wordnet {
    private WordnetGraph graph;
    public Wordnet(String synsetFile, String hyponymFile) {
        graph = new WordnetGraph();
        // sysnsetFile
        In inSynset = new In(synsetFile);
        while (!inSynset.isEmpty()) {
            String nextLine = inSynset.readLine();
            String[] splitLine = nextLine.split(",");

            int id = Integer.parseInt(splitLine[0]);
            Set<String> words = new HashSet<>(Arrays.asList(splitLine[1].split(" ")));

            graph.addSynset(id, words);
        }

        // hyponymFile
        In inHyponym = new In(hyponymFile);
        while (!inHyponym.isEmpty()) {
            String nextLine = inHyponym.readLine();
            String[] splitLine = nextLine.split(",");

            int id = Integer.parseInt(splitLine[0]);
            for (int i = 1; i < splitLine.length; i++) {
                graph.addHyponym(id, Integer.parseInt(splitLine[i]));
            }
        }
    }
    public Set<String> getHyponyms(String word) {
        return graph.getAllHyponyms(word);
    }
    public Set<String> getAncestors(String word) {
        return graph.getAllAncestors(word);
    }
}
