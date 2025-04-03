package wordnet;

import java.util.*;

public class WordnetGraph {
    private Map<Integer, Set<String>> synsets;
    private Map<Integer, Set<Integer>> adjList;

    public WordnetGraph() {
        synsets = new HashMap<>();
        adjList = new HashMap<>();
    }

    // add a synset(addNode)
    public void addSynset(int id, Set<String> words) {
        synsets.put(id, words);
    }

    // add hyponym(addEdge)
    public void addHyponym(int id, int hyponymId) {
        if(!adjList.containsKey(id)) {
            adjList.put(id, new HashSet<>());
        }
        adjList.get(id).add(hyponymId);
    }

    // get the word set of a specific synset id
    public Set<String> getWords(int id) {
        return synsets.getOrDefault(id, Set.of());
    }

    // get all hyponym words for a word
    public Set<String> getAllHyponyms(String word) {
        Set<String> result = new HashSet<>();
        Set<Integer> wordIds = new HashSet<>();

        // find all synset ID that contain the input word
        for (int id : synsets.keySet()) {
            if (synsets.get(id).contains(word)) {
                wordIds.add(id);
            }
        }

        // BFS graph traversal
        Queue<Integer> queue = new LinkedList<>(wordIds);
        while(!queue.isEmpty()) {
            int currentId = queue.poll();
            // add words to result set
            result.addAll(synsets.getOrDefault(currentId, Set.of()));
            // add current synset's hyponym set id
            if (adjList.containsKey(currentId)) {
                queue.addAll(adjList.get(currentId));
            }
        }
        return result;
    }
}
