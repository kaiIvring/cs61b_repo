import org.junit.jupiter.api.Test;
import deque.ArrayDeque61B;
import static com.google.common.truth.Truth.assertThat;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque61BTest {

    @Test
    public void iteratorTestBasic() {
        ArrayDeque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);

        int[] expected = {3, 2, 1};
        int index = 0;
        for (int item : ad1) {
            assertThat(item).isEqualTo(expected[index]);
            index++;
        }
        assertThat(index).isEqualTo(3);
    }

    @Test
    public void iteratorEmptyDequeTest() {
        ArrayDeque61B<Integer> ad1 = new ArrayDeque61B<>();
        Iterator<Integer> iterator = ad1.iterator();
        assertThat(iterator.hasNext()).isFalse();

        // Test if calling next on empty iterator throws exception
        try {
            iterator.next();
            assertThat(false).isTrue();
        } catch (NoSuchElementException e) {
            assertThat(e).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    public void iteratorAfterAddAndRemoveTest() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();

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
