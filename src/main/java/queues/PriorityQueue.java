package queues;

public class PriorityQueue<T extends Comparable<T>> {

//    private java.util.PriorityQueue<Integer> ints;
//
//    // Implementation use binary heap data structure
//    private T[] values;
//    // RI:
//    //      Structure:
//    //          root values[1]
//    //          children-parent relation:
//    //              node values[i] has two children: values[2i] and values[2i+1]
//    //              parent of values[i] is values[floor(i/2)]
//    //          values[i] <= math.min(values[2i+1], values[2i])
//
//    private int size;
//
//    public PriorityQueue(int size) {
//        ints = new java.util.PriorityQueue<>();
//        ints.poll()
//        values = (T[]) new Object[size];
//        size = 0;
//    }
//
//    public void enqueue() {
//
//    }
//
//    public void dequeue() {
//
//    }
//
//    private int leftChild(int i) {
//        return 2*i;
//    }
//
//    private int rightChild(int i) {
//        return 2*i + 1;
//    }
//
//    private int parent(int i) {
//        return i/2;
//    }
}
