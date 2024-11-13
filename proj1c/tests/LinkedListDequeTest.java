import deque.LinkedListDeque61B;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.truth.Truth.assertThat;

public class LinkedListDequeTest {
    @Test
    public void LLDIteratorTestBasic() {
        LinkedListDeque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        int[] expected = {3, 2, 1};
        int index = 0;
        for (int item : lld1) {
            assertThat(item).isEqualTo(expected[index]);
            index++;
        }
        assertThat(index).isEqualTo(3);
    }

    @Test
    public void LLDIteratorEmptyDequeTest() {
        LinkedListDeque61B<Integer> lld1 = new LinkedListDeque61B<>();
        Iterator<Integer> iterator = lld1.iterator();
        assertThat(iterator.hasNext()).isFalse();

        try {
            iterator.next();
            assertThat(false).isTrue(); // Should not reach here.
        } catch (NoSuchElementException e) {
            assertThat(e).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    public void LLDIteratorAfterAddAndRemoveTest() {

        LinkedListDeque61B<Integer> lld1 = new LinkedListDeque61B<>();

        lld1.addLast(10);
        lld1.addLast(20);
        lld1.addLast(30);
        lld1.removeFirst();
        lld1.addFirst(5);

        int[] expected = {5, 20, 30};
        int index = 0;

        for (int item : lld1) {
            assertThat(item).isEqualTo(expected[index]);
            index++;
        }
        assertThat(index).isEqualTo(3); // Ensure we iterated through all elements
    }

    @Test
    public void equalsBasicTest() {
        LinkedListDeque61B<Integer> lld1 = new LinkedListDeque61B<>();
        LinkedListDeque61B<Integer> lld2 = new LinkedListDeque61B<>();

        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        lld2.addFirst(1);
        lld2.addFirst(2);
        lld2.addFirst(3);

        assertThat(lld1.equals(lld2)).isTrue();
    }

    @Test
    public void equalsFalseTest() {
        LinkedListDeque61B<Integer> lld1 = new LinkedListDeque61B<>();
        LinkedListDeque61B<Integer> lld2 = new LinkedListDeque61B<>();

        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        lld2.addFirst(9);
        lld2.addFirst(2);
        lld2.addFirst(3);

        assertThat(lld1.equals(lld2)).isFalse();
    }

    @Test
    public void equalsEmptyDequeTest() {
        LinkedListDeque61B<Integer> lld1 = new LinkedListDeque61B<>();
        LinkedListDeque61B<Integer> lld2 = new LinkedListDeque61B<>();

        assertThat(lld1.equals(lld2)).isTrue();
    }

    @Test
    public void toStringBasicTest() {
        LinkedListDeque61B<String> lld1 = new LinkedListDeque61B<>();

        lld1.addLast("java");
        lld1.addLast("is");
        lld1.addLast("great");

        assertThat(lld1.toString()).isEqualTo("[java, is, great]");
    }

    @Test
    public void toStringEmptyDequeTest() {
        LinkedListDeque61B<String> lld1 = new LinkedListDeque61B<>();
        assertThat(lld1.toString()).isEqualTo("[]");
    }

    @Test
    public void toStringOneElementTest() {
        LinkedListDeque61B<String> lld1 = new LinkedListDeque61B<>();
        lld1.addLast("java");
        assertThat(lld1.toString()).isEqualTo("[java]");
    }
}
