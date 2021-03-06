package heap;


import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

public class BinaryHeap<T extends Comparable<T>> {

    private Object[] vals;
    private int size;
    private Comparator<? super T> comparator;

    // RI:  binary heap using array:
    //      root at 0
    //      node i
    //          + as a parent, it has two child at (2i + 1) and (2i + 2)
    //          + as a child, it has parent at [(i - 1)/2]
    //      parent <= child
    private void checkRep() {
        for (int i = 0; i < size - 2; i++)
            checkParent(i);
        for (int i = size; i < vals.length; i++)
            assert vals[i] == null;
        assert size <= vals.length;
        assert size >= 0;
    }
    private void checkParent(int i) {
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;
        if (leftChild < size && !lt(i, leftChild)) {
            System.out.println(vals[i] + " " + vals[leftChild]);
            throw new RuntimeException();
        }
        if (rightChild < size) assert lt(i, rightChild);
    }

    public BinaryHeap() {
        vals = new Object[1];
        this.size = 0;
    }

    public BinaryHeap(Comparator<? super T> comparator) {
        vals = new Object[1];
        this.size = 0;
        this.comparator = comparator;
    }

    private void resize(int newSize) {
        Object[] copy = new Object[newSize];
        System.arraycopy(vals, 0, copy, 0, size);
        vals = copy;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /**
     * Check if vals[i] is less than vals[t]
     */
    private boolean lt(int i, int t) {
        if(comparator != null) return comparator.compare((T) vals[i],(T) vals[t]) <= 0;
        else return ((T) vals[i]).compareTo((T) vals[t]) <= 0;
    }

    private void swap(int i, int t) {
        Object tVal = vals[t];
        vals[t] = vals[i];
        vals[i] = tVal;
    }


    /**Bubble up element at index i */
    private void swim(int i) {
        int parent = (i - 1) / 2;
        if (!lt(parent, i)) swap(i, parent);
        if (parent != 0) swim(parent);
    }

    /** Bubble down element at index i*/
    private void sink(int i) {
        int lChild = i * 2 + 1;
        int rChild = i * 2 + 2;
        int selected = -1;
        if (lChild < size) {
            if(rChild < size) selected = lt(lChild, rChild) ? lChild : rChild;
            else selected = lChild;
        }
        if(selected != -1 && !lt(i, selected)) {
            swap(i, selected);
            sink(selected);
        }
    }

    /** Insert a value into this heap
     *  Running time O(log(n))
     * */
    public void insert(T value) {
        if (size == vals.length)
            resize(size * 4);
        vals[size] = value;
        swim(size++);
        checkRep();
    }

    public T getMin() {
        checkRep();
        return (T) vals[0];
    }

    /**
     * Delete minimum element in this heap
     * Running time O(log(n))
     */
    public T delMin() {
        if (size == vals.length/4) resize(vals.length/2);
        Object min = vals[0];
        vals[0] = vals[size - 1];
        vals[--size] = null;
        sink(0);
        checkRep();
        return (T) min;
    }

    public static <T extends Comparable<T>> BinaryHeap<T> heapify(Collection<? extends T> elem) {
        return heapify(elem, null);
    }

    public static <T extends Comparable<T>> BinaryHeap<T> heapify(Collection<? extends T> elem, Comparator<? super T> comparator) {
        Object[] values = new Object[elem.size()];
        int i = 0;
        for(T e : elem) values[i++] = e;
        BinaryHeap<T> bh = new BinaryHeap<>();
        bh.comparator = comparator;
        bh.vals = values;
        bh.size = elem.size();
        for(int o = elem.size()/2; o >= 0; o--)
            bh.sink(o);
        return bh;
    }

    @Override
    public String toString() {
        return Arrays.toString(vals);
    }
}
