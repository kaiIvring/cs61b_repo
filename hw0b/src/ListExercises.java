import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

public class ListExercises {

    /** Returns the total sum in a list of integers */
	public static int sum(List<Integer> L) {
        // TODO: Fill in this function.
        int sum = 0;
        for (int i : L) {
            sum += i;
        }
        return sum;
    }

    /** Returns a list containing the even numbers of the given list */
    public static List<Integer> evens(List<Integer> L) {
        // TODO: Fill in this function.
        List<Integer> evenList = new ArrayList<>();
        for (int k : L) {
            if (k % 2 == 0) {
                evenList.add(k);
            }
        }
        return evenList;
    }

    /** Returns a list containing the common item of the two given lists */
    public static List<Integer> common(List<Integer> L1, List<Integer> L2) {
        // TODO: Fill in this function.
        List<Integer> commonItem = new ArrayList<>();
        for(int j = 0; j < L1.size(); j++) {
            for (int m = 0; m < L2.size(); m++) {
                if (L1.get(j) == L2.get(m)) {
                    commonItem.add(L1.get(j));
                }
            }
        }
        return commonItem;
    }


    /** Returns the number of occurrences of the given character in a list of strings. */
    public static int countOccurrencesOfC(List<String> words, char c) {
        // TODO: Fill in this function.
        int cnt = 0;
        for (String str : words) {
            for (char ch : str.toCharArray()) {
                if (ch == c) {
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
