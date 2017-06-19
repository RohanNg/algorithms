package heap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class AugmentedBinaryHeap<T extends AugmentedBinaryHeap.Element> {

    public static abstract class Element {
        private int idx;
        public void setPosition(int i) {
            this.idx = i;
        }
        public int getPosition() {
            return idx;
        }
    }

    private Element[] vals;
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

        for(int i = 0; i < size(); i++)
            assert i == vals[i].getPosition();
    }

    private void checkParent(int i) {
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;
        if (leftChild < size ) assert lt(i, leftChild);
        if (rightChild < size) assert lt(i, rightChild);
    }

    public AugmentedBinaryHeap(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        vals = new Element[1];
        this.size = 0;
        this.comparator = comparator;
    }

    private void resize(int newSize) {
        Element[] copy = new Element[newSize];
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
        return comparator.compare((T) vals[i], (T) vals[t]) <= 0;
    }

    private void swap(final int i,final int t) {
        Element tVal = vals[t];
        Element iVal = vals[i];
        tVal.setPosition(i);
        iVal.setPosition(t);
        vals[i] = tVal;
        vals[t] = iVal;
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
        if (size == vals.length) resize(size * 4);

        vals[size] = value;
        value.setPosition(size);

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
        return delAt(0);
    }

    public T delAt(int index) {
        if(index < 0 || index >= size())
            throw new IllegalArgumentException("Index out of bound");

        if (size == vals.length/4) resize(vals.length/2);

        Element deleted = vals[index];

        if(index == size() - 1) {
            vals[--size] = null;
            return (T) deleted;
        } else {
            Element last = vals[size - 1];
            last.setPosition(index);
            vals[index] = last;
            vals[--size] = null;
            swim(last.getPosition());
            sink(last.getPosition());
            checkRep();
            return (T) deleted;
        }
    }

    public static <T extends Element> AugmentedBinaryHeap<T> heapify(Collection<? extends T> elem, Comparator<? super T> comparator) {
        Element[] values = new Element[elem.size()];
        int i = 0;
        for(T e : elem) values[i++] = e;
        AugmentedBinaryHeap<T> bh = new AugmentedBinaryHeap<>(comparator);
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
