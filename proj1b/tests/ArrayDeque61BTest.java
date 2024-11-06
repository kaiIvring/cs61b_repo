import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

//     @Test
//     @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
//     void noNonTrivialFields() {
//         List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
//                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
//                 .toList();
//
//         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
//     }

    @Test
    public void addFirstBasicTest() {

        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);

        assertThat(ad1.toList()).containsExactly(3, 2, 1).inOrder();
    }

    @Test
    public void addFirstFullArrayTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);
        ad1.addFirst(4);
        ad1.addFirst(5);
        ad1.addFirst(6);
        ad1.addFirst(7);
        ad1.addFirst(8);

        assertThat(ad1.toList()).containsExactly(8, 7, 6, 5, 4, 3, 2, 1).inOrder();

    }

    @Test
    public void addFirstTriggerResizeTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);
        ad1.addFirst(4);
        ad1.addFirst(5);
        ad1.addFirst(6);
        ad1.addFirst(7);
        ad1.addFirst(8);
        ad1.addFirst(9);

        assertThat(ad1.toList()).containsExactly(9, 8, 7, 6, 5, 4, 3, 2, 1).inOrder();
    }

    @Test
    public void addLastBasicTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);

        assertThat(ad1.toList()).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void addLastFullArrayTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);

        assertThat(ad1.toList()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8).inOrder();

    }

    @Test
    public void addLastTriggerResizeTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);

        assertThat(ad1.toList()).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9).inOrder();

    }

    @Test
    public void addLastAfterRemoveToEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.removeLast();
        ad1.removeLast();
        ad1.addLast(1);
        ad1.addLast(2);

        assertThat(ad1.toList()).containsExactly(1, 2).inOrder();
    }

    @Test
    public void addFirstAfterRemoveToEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.removeLast();
        ad1.removeLast();
        ad1.addFirst(1);
        ad1.addFirst(2);

        assertThat(ad1.toList()).containsExactly(2, 1).inOrder();
    }

    @Test
    public void addFirstAndLastTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addFirst(5);
        ad1.addFirst(6);
        ad1.addFirst(7);
        ad1.addFirst(8);
        ad1.addFirst(9);

        assertThat(ad1.toList()).containsExactly(9, 8, 7, 6, 5, 1, 2, 3, 4).inOrder();

    }

    @Test
    public void getBasicTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);

        assertThat(ad1.get(1)).isEqualTo(2);
    }

    @Test
    public void getOutOfBoundTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);

        assertThat(ad1.get(100)).isEqualTo(null);
    }

    @Test
    public void getNegativeIndexTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        assertThat(ad1.get(-1)).isEqualTo(null);
    }

    @Test
    public void isEmptyTrueTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        assertThat(ad1.isEmpty()).isTrue();
    }

    @Test
    public void isEmptyFalseTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);
        assertThat(ad1.isEmpty()).isFalse();
    }

    @Test
    public void sizeBasicTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);

        assertThat(ad1.size()).isEqualTo(6);
    }

    @Test
    public void sizeEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        assertThat(ad1.size()).isEqualTo(0);
    }

    @Test
    public void sizeResizeTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);

        assertThat(ad1.size()).isEqualTo(9);
    }

    @Test
    public void removeFirstBasicTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();

        assertThat(ad1.toList()).containsExactly(4).inOrder();
    }

    @Test
    public void removeEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();

        assertThat(ad1.removeFirst()).isEqualTo(null);
        assertThat(ad1.removeLast()).isEqualTo(null);
    }

    @Test
    public void removeFirstToEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();

        assertThat(ad1.toList()).containsExactly().inOrder();
        assertThat(ad1.size()).isEqualTo(0);// sizeAfterRemoveToEmptyTest
    }

    @Test
    public void removeFirstTriggerResize() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        ad1.removeFirst();
        assertThat(ad1.toList()).containsExactly( 7, 8, 9);
    }

    @Test
    public void removeLastToEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();

        assertThat(ad1.toList()).containsExactly().inOrder();

    }

    @Test
    public void removeLastBasicTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();

        assertThat(ad1.toList()).containsExactly(1, 2, 3);
    }

    @Test
    public void removeLastTriggerResize() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.addLast(4);
        ad1.addLast(5);
        ad1.addLast(6);
        ad1.addLast(7);
        ad1.addLast(8);
        ad1.addLast(9);
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        ad1.removeLast();
        assertThat(ad1.toList()).containsExactly(1, 2, 3);
    }

    @Test
    public void toStringEmptyTest() {
        Deque61B<Integer> ad1 = new ArrayDeque61B<>();
        assertThat(ad1.toList()).containsExactly().inOrder();
    }
}
