import edu.princeton.cs.algs4.In;
import org.apache.hc.core5.annotation.Internal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDeque61BTest {

    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque61B<String> lld1 = new LinkedListDeque61B<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }

    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque61B<String> lld1 = new LinkedListDeque61B<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    // Below, you'll write your own tests for LinkedListDeque61B.
    @Test
    public void toListEmptyTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        List<Integer> expected = new ArrayList<>();
        assertThat(lld1.toList()).isEqualTo(expected);
    }

    @Test
    public void toListBasicTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        List<Integer> expected = new ArrayList<>();
        expected.add(3);
        expected.add(2);
        expected.add(1);

        assertThat(lld1.toList()).isEqualTo(expected);
    }
    
    @Test
    public void addFirstAfterRemoveToEmpty() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.addFirst(4);
        lld1.addFirst(3);
        assertThat(lld1.toList()).containsExactly(3,4).inOrder();
    }

    @Test
    public void isEmptyTestBasic() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        assertThat(lld1.isEmpty()).isTrue();
    }

    @Test
    public void isEmptyFalseTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst(1);
        assertThat(lld1.isEmpty()).isFalse();
    }

    @Test
    public void sizeTestBasic() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addLast(4);
        int expectedSize = 3;
        assertThat(lld1.size()).isEqualTo(expectedSize);
    }

    @Test
    public void sizeZeroTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        int expectedSize = 0;
        assertThat(lld1.size()).isEqualTo(expectedSize);
    }

    @Test
    public void sizeAfterRemoveToEmpty() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst(1);
        lld1.addFirst(1);
        lld1.addFirst(1);
        lld1.addFirst(1);

        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();
        lld1.removeFirst();

        int expectedSize = 0;
        assertThat(lld1.size()).isEqualTo(expectedSize);
    }

    @Test
    public void sizeRemoveFromEmpty() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.removeFirst();
        int expectedSize = 0;
        assertThat(lld1.size()).isEqualTo(expectedSize);
    }

    @Test
    public void getBasicTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        assertThat(lld1.get(1)).isEqualTo(2);
    }

    @Test
    public void getOutOfBoundTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        assertThat(lld1.get(1)).isEqualTo(null);
    }

    @Test
    public  void getNegativeIndexTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        assertThat(lld1.get(-11)).isEqualTo(null);
    }

    @Test
    public void getRecursiveBasicTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        assertThat(lld1.getRecursive(1)).isEqualTo(2);
    }

    @Test
    public void getRecursiveOutOfBoundTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        assertThat(lld1.getRecursive(10086)).isEqualTo(null);
    }

    @Test
    public void getRecursiveNegativeIndexTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        assertThat(lld1.getRecursive(-11)).isEqualTo(null);
    }

    @Test
    public void removeLastBasicTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addLast(7);
        lld1.removeLast();
        assertThat(lld1.toList()).containsExactly(1,2,3).inOrder();
    }

    @Test
    public void removeFirstBasicTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addLast(7);
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly(2,3,7).inOrder();
    }

    @Test
    public void removeLastEmpty() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        assertThat(lld1.removeLast()).isEqualTo(null);
    }

    @Test
    public void removeFirstEmpty() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        assertThat(lld1.removeFirst()).isEqualTo(null);
    }

    @Test
    public void removeFirstAndLastTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(3);
        lld1.addFirst(5);
        lld1.addLast(7);
        lld1.addFirst(9);
        lld1.removeLast();
        lld1.removeFirst();
        assertThat(lld1.toList()).containsExactly(5,3).inOrder();
    }

}