package deque;

import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque61B(Comparator<T> c) {
        super();
        this.comparator = c;
    }

    // Use the default comparator to search the max item.
    public T max() {
        if (this.isEmpty()) {
            return null;
        }

        T maxItem = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            T currItem = this.get(i);
            if (comparator.compare(maxItem, currItem) < 0) {
                maxItem = currItem;
            }
        }
        return maxItem;
    }

    // Use the passing comparator to search
    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }

        T maxItem = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            T currItem = this.get(i);
            if (c.compare(maxItem, currItem) < 0) {
                maxItem = currItem;
            }
        }
        return maxItem;
    }
}
