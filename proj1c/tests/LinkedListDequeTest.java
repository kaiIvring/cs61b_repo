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
            assertThat(false).isTrue();// Should not reach here.
        } catch (NoSuchElementException e) {
            assertThat(e).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    public void LLDIteratorAfterAddAndRemoveTest() {

        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        deque.addLast(10);
        deque.addLast(20);
        deque.addLast(30);
        deque.removeFirst();
        deque.addFirst(5);

        int[] expected = {5, 20, 30};
        int index = 0;

        for (int item : deque) {
            assertThat(item).isEqualTo(expected[index]);
            index++;
        }
        assertThat(index).isEqualTo(3); // Ensure we iterated through all elements
    }
}
