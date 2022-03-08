import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;
    private int size;

    public RandomizedQueue() {
        this.arr = (Item[]) new Object[4];
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void enqueue(Item item) {
        this.assertArgumentNotNull(item);

        this.arr[this.size++] = item;
        if (this.size == this.arr.length) {
            this.arr = this.copyResize(this.arr, this.arr.length * 2);
        }
    }

    public Item dequeue() {
        this.assertDequeNotEmpty();

        int idx = StdRandom.uniform(this.size);
        Item elem = this.arr[idx];
        this.swap(this.size - 1, idx, this.arr);
        this.arr[this.size - 1] = null;
        this.size--;

        if ((this.arr.length > 4) && (this.size <= this.arr.length / 4)) {
            this.arr = this.copyResize(this.arr, this.arr.length / 2);
        }

        return elem;
    }

    public Item sample() {
        this.assertDequeNotEmpty();

        int idx = StdRandom.uniform(this.size);
        return this.arr[idx];
    }

    public Iterator<Item> iterator() {
        return new IteratorImpl();
    }

    // required
    public static void main(String[] args) {
        RandomizedQueue.enqueueAndDequeueUntilEmptyThrowsException();
        RandomizedQueue.printRandomizedQueueElementIteration();
    }

    private Item[] copyResize(Item[] array, int targetSize) {
        Item[] newArr = (Item[]) new Object[targetSize];
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                continue;
            }
            newArr[i] = array[i];
        }
        return newArr;
    }

    private void swap(int i, int j, Item[] array) {
        Item tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private class IteratorImpl implements Iterator<Item> {

        private int idx;
        private final int[] idxsShuffled;

        private IteratorImpl() {
            this.idx = 0;
            this.idxsShuffled = StdRandom.permutation(size);
        }

        @Override
        public boolean hasNext() {
            return this.idx < this.idxsShuffled.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Called next() on iterator without next value");
            }
            int currIdx = this.idxsShuffled[this.idx++];
            return arr[currIdx];
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
            throw new NoSuchElementException("Cannot dequeue element from empty randomized queue");
        }
    }

    private static void enqueueAndDequeueUntilEmptyThrowsException() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        try {
            rq.enqueue(null);
        } catch (IllegalArgumentException exc) {
            exc.printStackTrace();
        }
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        rq.enqueue(6);
        rq.enqueue(7);
        System.out.println("Printing samples");
        RandomizedQueue.printSample(rq);
        RandomizedQueue.printSample(rq);
        RandomizedQueue.printSample(rq);
        RandomizedQueue.printSample(rq);
        RandomizedQueue.printSample(rq);
        RandomizedQueue.printSample(rq);
        RandomizedQueue.printSample(rq);
        RandomizedQueue.printSample(rq);
        System.out.println("Removing samples");
        RandomizedQueue.printDequeue(rq);
        RandomizedQueue.printDequeue(rq);
        RandomizedQueue.printDequeue(rq);
        RandomizedQueue.printDequeue(rq);
        RandomizedQueue.printDequeue(rq);
        RandomizedQueue.printDequeue(rq);
        System.out.printf("isEmpty()? %s\n", rq.isEmpty());
        RandomizedQueue.printDequeue(rq);
        System.out.printf("isEmpty()? %s\n", rq.isEmpty());
        try {
            System.out.println(rq.dequeue());
        } catch (NoSuchElementException exc) {
            exc.printStackTrace();
        }
    }

    private static <Item> void printDequeue(RandomizedQueue<Item> rq) {
        System.out.printf("Dequeued: %s\n", rq.dequeue());
    }

    private static <Item> void printSample(RandomizedQueue<Item> rq) {
        System.out.printf("Sampled: %s\n", rq.sample());
    }

    private static void printRandomizedQueueElementIteration() {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("ABC");
        rq.enqueue("DEF");
        rq.enqueue("GHI");
        rq.enqueue("JKL");
        rq.enqueue("MNO");
        rq.enqueue("PQR");
        rq.enqueue("STU");
        rq.enqueue("VWX");
        rq.enqueue("YZ");
        System.out.println("Randomized queue iteration #1");
        for (String it : rq) {
            System.out.println(it);
        }
        System.out.println("Randomized queue iteration #2");
        for (String it : rq) {
            System.out.println(it);
        }
        System.out.println("Printing using explicit iterator:");
        Iterator<String> iter = rq.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
        System.out.println("Calling next() on empty iterator:");
        try {
            iter.next();
        } catch (NoSuchElementException exc) {
            exc.printStackTrace();
        }
        System.out.printf("Size: %s\n", rq.size());
    }
}