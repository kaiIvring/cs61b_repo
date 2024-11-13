import org.junit.jupiter.api.*;

import java.util.Comparator;
import deque.MaxArrayDeque61B;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MaxArrayDeque61BTest {
    private static class StringLengthComparator implements Comparator<String> {
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    // Greatest number is the max item
    private static class NaturalOrderComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    // Least number is the "max" item
    private static class ReverseOrderComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    private static class FirstCharComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            if (s1 == null && s2 != null) {
                return -1;
            } else if (s2 == null && s1 != null) {
                return 1;
            } else if (s1 == null && s2 == null){
                return 0;
            }

            char c1 = s1.charAt(0);
            char c2 = s2.charAt(0);
            return Character.compare(c2, c1);
        }
    }

    @Test
    public void basicTest() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new StringLengthComparator());
        mad.addFirst("");
        mad.addFirst("2");
        mad.addFirst("fury road");
        assertThat(mad.max()).isEqualTo("fury road");
    }

    @Test
    public void naturalNumberBasicTest() {
        MaxArrayDeque61B<Integer> mad = new MaxArrayDeque61B<>(new NaturalOrderComparator());
        mad.addFirst(2);
        mad.addFirst(88);
        mad.addFirst(9);
        assertThat(mad.max()).isEqualTo(88);
    }

    @Test
    public void maxOnEmptyDequeTest() {
        MaxArrayDeque61B<Integer> mad = new MaxArrayDeque61B<>(new NaturalOrderComparator());
        assertThat(mad.max()).isNull();
    }

    @Test
    public void singleElementTest() {
        MaxArrayDeque61B<Integer> mad = new MaxArrayDeque61B<>(new NaturalOrderComparator());
        mad.addFirst(42);
        assertThat(mad.max()).isEqualTo(42);
    }

    @Test
    public void allEqualElementsTest() {
        MaxArrayDeque61B<Integer> mad = new MaxArrayDeque61B<>(new NaturalOrderComparator());
        mad.addFirst(10);
        mad.addFirst(10);
        mad.addFirst(10);
        assertThat(mad.max()).isEqualTo(10);
    }

    @Test
    public void mixedPositiveAndNegativeTest() {
        MaxArrayDeque61B<Integer> mad = new MaxArrayDeque61B<>(new NaturalOrderComparator());
        mad.addFirst(-10);
        mad.addFirst(0);
        mad.addFirst(10);
        mad.addFirst(-20);
        assertThat(mad.max()).isEqualTo(10);
    }

    @Test
    public void emptyStringVsStringTest() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new StringLengthComparator());
        mad.addFirst("");
        mad.addFirst("word");
        assertThat(mad.max()).isEqualTo("word");
    }

    @Test
    public void reverseOrderTest() {
        MaxArrayDeque61B<Integer> mad = new MaxArrayDeque61B<>(new ReverseOrderComparator());
        mad.addFirst(1);
        mad.addFirst(3);
        mad.addFirst(2);

        assertThat(mad.max()).isEqualTo(1);
    }

    @Test
    public void testFirstCharComparator() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new FirstCharComparator());

        mad.addFirst("Banana");
        mad.addFirst("Apple");
        mad.addFirst("Cherry");

        assertThat(mad.max()).isEqualTo("Apple");
    }

    @Test
    public void testFirstCharComparatorWithNull() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new FirstCharComparator());

        mad.addFirst("Banana");
        mad.addFirst("Apple");
        mad.addFirst(null);

        assertThat(mad.max()).isEqualTo("Apple");
    }

    @Test
    public void testFirstCharComparatorWithOnlyNull() {
        MaxArrayDeque61B<String> mad = new MaxArrayDeque61B<>(new FirstCharComparator());

        mad.addFirst(null);
        mad.addFirst(null);

        assertThat(mad.max()).isEqualTo(null);
    }
}