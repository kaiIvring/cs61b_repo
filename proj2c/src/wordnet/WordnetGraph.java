package wordnet;

import net.sf.saxon.serialize.BinaryTextDecoder;

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

    // get all ancestors for a word
    public Set<String> getAllAncestors(String word) {
        Set<String> result = new HashSet<>();
        Set<Integer> startIds = new HashSet<>();

        // find all synset ID for the word
        for (Map.Entry<Integer, Set<String>> entry : synsets.entrySet()) {
            if (entry.getValue().contains(word)) {
                startIds.add(entry.getKey());
            }
        }

        // construct a reverse graph
        // reverseGraph: an adjList (Integer -> IntegerSet)
        Map<Integer, Set<Integer>> reverseGraph = new HashMap<>();
        for (Map.Entry<Integer, Set<Integer>> entry : adjList.entrySet()) {
            int from = entry.getKey(); // hypernym
            for (int to : entry.getValue()) { // hyponyms
                reverseGraph.computeIfAbsent(to, k -> new HashSet<>()).add(from);
                /*
                // add an edge: to -> from (reverse graph)
                    if (!reverseGraph.containsKey(to)) {
                        reverseGraph.put(to, new HashSet<>());
                    }
                    reverseGraph.get(to).add(from);
                 */
            }
        }

        // BFS through the reverse graph
        Queue<Integer> queue = new LinkedList<>(startIds);
        Set<Integer> visited = new HashSet<>(startIds);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.addAll(synsets.getOrDefault(current, Set.of()));

            // if current node has ancestor
            if (reverseGraph.containsKey(current)) {
                for (int parent : reverseGraph.get(current)) {
                    if (!visited.contains(parent)) {
                        queue.add(parent);
                        visited.add(parent);
                    }
                }
            }
        }
        return result;
    }
}
