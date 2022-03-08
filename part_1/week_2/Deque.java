import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private final Item value;
        private Node next;
        private Node prev;

        private Node(Item value) {
            this(value, null, null);
        }

        private Node(Item value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    private int size;
    private Node first;
    private Node last;

    public Deque() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        this.assertArgumentNotNull(item);

        Node newNode = new Node(item, this.first, null);
        if (this.size == 0) {
            this.last = newNode;
        } else {
            this.first.prev = newNode;
        }
        this.first = newNode;
        this.size++;
    }

    public void addLast(Item item) {
        this.assertArgumentNotNull(item);

        Node newNode = new Node(item, null, this.last);
        if (this.size == 0) {
            this.first = newNode;
        } else {
            this.last.next = newNode;
        }
        this.last = newNode;
        this.size++;
    }

    public Item removeFirst() {
        this.assertDequeNotEmpty();

        Node item = this.first;
        this.first = this.first.next;
        if (this.size == 1) {
            this.last = null;
        } else {
            this.first.prev = null;
        }
        this.size--;
        return item.value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        this.assertDequeNotEmpty();

        Node item = this.last;
        this.last = this.last.prev;
        if (this.size == 1) {
            this.first = null;
        } else {
            this.last.next = null;
        }
        this.size--;
        return item.value;
    }

    public Iterator<Item> iterator() {
        return new IteratorImpl();
    }

    // (required)
    public static void main(String[] args) {
        Deque.printExceptionStackTracesOnIllegalActions();
        Deque.printCheckAddRemoveSequence();
        Deque.printIteration();
    }

    private class IteratorImpl implements Iterator<Item> {
        private Node curr = first;

        @Override
        public boolean hasNext() {
            return this.curr != null;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("Called next() on iterator without next value");
            }
            Item value = this.curr.value;
            this.curr = this.curr.next;
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private void assertArgumentNotNull(Item value) {
        if (value == null) {
            throw new IllegalArgumentException("Null arguments not supported");
        }
    }

    private void assertDequeNotEmpty() {
        if (this.size == 0) {
            throw new NoSuchElementException("Cannot remove element from empty deque");
        }
    }
    
    private static <Item> void printExceptionStackTracesOnIllegalActions() {
        Deque<Integer> dq = new Deque<>();
        try {
            dq.addFirst(null);
        } catch (IllegalArgumentException exc) {
            exc.printStackTrace();
        }
        try {
            dq.addLast(null);
        } catch (IllegalArgumentException exc) {
            exc.printStackTrace();
        }
        try {
            dq.removeFirst();
        } catch (NoSuchElementException exc) {
            exc.printStackTrace();
        }
        try {
            dq.removeLast();
        } catch (NoSuchElementException exc) {
            exc.printStackTrace();
        }
    }
    
    private static <Item> void printCheckAddRemoveSequence() {
        Deque<String> dq = new Deque<>();
        System.out.printf("Size: %s\n", dq.size());
        dq.addFirst("A");
        Deque.printRemoveLast(dq);
        dq.addLast("B");
        dq.addFirst("C");
        dq.addLast("D");
        Deque.printRemoveFirst(dq);
        Deque.printRemoveFirst(dq);
        Deque.printRemoveFirst(dq);
        dq.addLast("E");
        dq.addFirst("F");
        dq.addLast("G");
        Deque.printRemoveLast(dq);
        Deque.printRemoveLast(dq);
        Deque.printRemoveLast(dq);
        dq.addLast("H");
        dq.addLast("I");
        dq.addLast("J");
        Deque.printRemoveFirst(dq);
        Deque.printRemoveLast(dq);
        Deque.printRemoveFirst(dq);
    }

    private static <Item> void printIteration() {
        Deque<Boolean> dq = new Deque<>();
        dq.addLast(true);
        dq.addLast(false);
        dq.addFirst(false);
        dq.addLast(false);
        dq.addFirst(true);
        dq.addFirst(false);
        dq.removeLast();
        dq.removeFirst();
        System.out.println("Printing with foreach:");
        for (boolean it : dq) {
            System.out.println(it);
        }
        System.out.println("Printing using explicit iterator:");
        Iterator<Boolean> iter = dq.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        System.out.println("Calling next() on empty iterator:");
        try {
            iter.next();
        } catch (NoSuchElementException exc) {
            exc.printStackTrace();
        }
    }
    
    private static <Item> void printRemoveLast(Deque<Item> dq) {
        System.out.printf("Removing last: %s\n", dq.removeLast());
    }

    private static <Item> void printRemoveFirst(Deque<Item> dq) {
        System.out.printf("Removing first: %s\n", dq.removeFirst());
    }
}