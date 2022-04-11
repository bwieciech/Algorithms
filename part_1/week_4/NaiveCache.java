import java.util.ArrayDeque;
import java.util.Deque;

public class NaiveCache<T> {
    /*
    Since the grader explicitly disallows implementing the hashcode method, a naive visited node cache is
    implemented. Note that this is k * dimension^2 for boards in the worst case, although the equals method
    in the Board class is optimized to make this rather fast in practice. As we want to catch duplication of states that
    are near to each other, a small k should still give substantial performance gains.
     */
    private final int cacheSize;
    private final Deque<T> items;

    public NaiveCache(int cacheSize) {
        this.cacheSize = cacheSize;
        this.items = new ArrayDeque<>();
    }

    public boolean contains(T item) {
        for (T other : this.items) {
            if (item.equals(other)) {
                return true;
            }
        }
        return false;
    }

    public void push(T item) {
        this.items.addLast(item);
        while (this.items.size() > this.cacheSize) {
            this.items.removeFirst();
        }
    }
}