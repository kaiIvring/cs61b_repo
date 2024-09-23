import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapExercises {
    /** Returns a map from every lower case letter to the number corresponding to that letter, where 'a' is
     * 1, 'b' is 2, 'c' is 3, ..., 'z' is 26.
     */
    public static Map<Character, Integer> letterToNum() {
        // TODO: Fill in this function.
        Map<Character, Integer> letterToNum = new HashMap<>();
        for (char letter = 'a'; letter <= 'z'; letter++) {
            letterToNum.put(letter, letter - 'a' + 1);
        }

        return letterToNum;
    }

    /** Returns a map from the integers in the list to their squares. For example, if the input list
     *  is [1, 3, 6, 7], the returned map goes from 1 to 1, 3 to 9, 6 to 36, and 7 to 49.
     */
    public static Map<Integer, Integer> squares(List<Integer> nums) {
        // TODO: Fill in this function.
        Map<Integer, Integer> squaresMap = new HashMap<>();
        for (int i : nums) {
            squaresMap.put(i, i*i);
        }
        return squaresMap;
    }

    /** Returns a map of the counts of all words that appear in a list of words. */
    public static Map<String, Integer> countWords(List<String> words) {
        // TODO: Fill in this function.
        // original solution
//        Map<String, Integer> countMap = new HashMap<>();
//        for (int i = 0; i < words.size(); i++) {
//            int cnt = 1;
//            for (int j = 0; j < words.size(); j++) {
//                if (i != j) {
//                    if(words.get(i).equals(words.get(j))) {
//                        cnt++;
//                    }
//                }
//            }
//            countMap.put(words.get(i), cnt);
//        }
//        return countMap;

        //improved solution
        Map<String, Integer> countMap = new HashMap<>();
        for (String str : words) {
            countMap.put(str, countMap.getOrDefault(str,0) + 1);
        }
        return countMap;
    }
}
