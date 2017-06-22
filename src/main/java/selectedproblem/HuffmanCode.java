package selectedproblem;

import com.google.common.base.MoreObjects;
import edu.princeton.cs.algs4.In;
import heap.BinaryHeap;

import java.io.FileReader;
import java.util.*;

/**
 * Created by rohan on 6/16/17.
 */
public class HuffmanCode<T> {
    // Tree<T> = Leaf(T: symbol, double: frequency) + Node(Tree<T> left, Tree<T> right, double: frequency)
    private static interface Tree<T> extends Comparable<Tree<T>> {
        double frequency();

        @Override
        default int compareTo(Tree<T> o) {
            return Double.compare(this.frequency(), o.frequency());
        }

        boolean isLeaf();

        boolean isNode();

        T getSymbol();

        Tree<T> left();

        Tree<T> right();
    }

    private static class Leaf<T> implements Tree<T> {
        private final T value;
        private final double fred;

        public Leaf(T value, double fred) {
            Objects.requireNonNull(value);
            this.value = value;
            this.fred = fred;
        }

        @Override
        public double frequency() {
            return fred;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public boolean isNode() {
            return false;
        }

        @Override
        public T getSymbol() {
            return value;
        }

        @Override
        public Tree<T> left() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Tree<T> right() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Leaf<?> leaf = (Leaf<?>) o;

            return value.equals(leaf.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("value", value)
                    .add("fred", fred)
                    .toString();
        }
    }

    private static class Node<T> implements Tree<T> {
        private final Tree<T> left;
        private final Tree<T> right;
        private final double fred;

        public Node(Tree<T> left, Tree<T> right, double fred) {
            this.left = left;
            this.right = right;
            this.fred = fred;
        }

        @Override
        public double frequency() {
            return fred;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public boolean isNode() {
            return true;
        }

        @Override
        public T getSymbol() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Tree<T> left() {
            return left;
        }

        @Override
        public Tree<T> right() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node<?> node = (Node<?>) o;

            if (Double.compare(node.fred, fred) != 0) return false;
            if (left != null ? !left.equals(node.left) : node.left != null) return false;
            return right != null ? right.equals(node.right) : node.right == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = left != null ? left.hashCode() : 0;
            result = 31 * result + (right != null ? right.hashCode() : 0);
            temp = Double.doubleToLongBits(fred);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("left", left)
                    .add("right", right)
                    .add("fred", fred)
                    .toString();
        }
    }

    private final Set<Leaf<T>> leaves;

    public HuffmanCode() {
        leaves = new HashSet<>();
    }

    /**
     * Register new unique symbol having given frequency to be encoded
     *
     * @throws IllegalArgumentException if duplicate symbol found
     */
    private void addSymbol(final T value, final double frequency) {
        Leaf<T> leaf = new Leaf<>(value, frequency);
        // because equals and hashcode for leaf are defined on T only,
        // the following check guarantee to discover duplicate
        if (!leaves.add(leaf)) throw new IllegalArgumentException();
    }

    public Map<T, String> getEncodingRuleUsingHeap() {
        // version with priority queue

        // simple case
        if (leaves.size() == 0) return new HashMap<>();
        if (leaves.size() == 0) return new HashMap<T, String>() {
            {
                put(leaves.iterator().next().value, "0");
            }
        };

        BinaryHeap<Tree<T>> heap = BinaryHeap.heapify(leaves);
        while (heap.size() > 1) {
            Tree<T> l = heap.delMin();
            Tree<T> r = heap.delMin();
            heap.insert(new Node<>(l, r, l.frequency() + r.frequency()));
        }

        HashMap<T, String> code = new HashMap<>();
        traverseTreeAndBuildCode(heap.getMin(), code, "");
        return code;
    }

    public Map<T, String> getEncodingRuleUsingQueues() {
        // version using two queue
        // simple case
        if (leaves.size() == 0) return new HashMap<>();
        if (leaves.size() == 0) return new HashMap<T, String>() {
            {
                put(leaves.iterator().next().value, "0");
            }
        };

        List<Tree<T>> list = new ArrayList<>(leaves);
        Collections.sort(list);
        Queue<Tree<T>> a = new LinkedList<>(list);
        Queue<Tree<T>> b = new LinkedList<>();
        while (a.size() + b.size() >= 2) {
            Tree<T> s1 = takeSmallest(a, b);
            Tree<T> s2 = takeSmallest(a, b);
            Tree<T> meta = new Node<>(s1, s2, s1.frequency() + s2.frequency());
            b.add(meta);
        }
        HashMap<T, String> code = new HashMap<>();
        traverseTreeAndBuildCode(b.peek(), code, "");
        return code;
    }

    /**
     * Take a tree with smallest frequency from the two queue
     */
    private Tree<T> takeSmallest(Queue<Tree<T>> a, Queue<Tree<T>> b) {
        if (a.isEmpty() && b.isEmpty()) throw new IllegalArgumentException();
        if (a.isEmpty()) return b.poll();
        if (b.isEmpty()) return a.poll();
        if (a.peek().compareTo(b.peek()) <= 0) return a.poll();
        else return b.poll();
    }

    private void traverseTreeAndBuildCode(Tree<T> tree, Map<T, String> map, String code) {
        if (tree.isLeaf()) map.put(tree.getSymbol(), code);
        else {
            traverseTreeAndBuildCode(tree.left(), map, code + '0');
            traverseTreeAndBuildCode(tree.right(), map, code + '1');
        }


    }

    /**
     * Compute Huffman encoding rule for instance of problem defined in given file.
     * Given file describes an instance of the problem. It has the following format:
     * <p>
     * [number_of_symbols #n]
     * <p>
     * [weight of symbol #1]
     * <p>
     * [weight of symbol #2]
     * <p> ....
     * [weight of symbol #n]
     *
     * @return a map: i -> string describe decoding rule of symbol #i for all i in [1, n]
     */
    public static Map<Integer, String> computeEncodingRule(String filePath) throws Exception {
        Scanner in = new Scanner(new FileReader(filePath));

        Integer symbolNum = Integer.valueOf(in.nextLine().trim());
        System.out.println("#symbol: " + symbolNum );

        HuffmanCode<Integer> huffmanCode = new HuffmanCode<>();

        int i = 1;
        while (in.hasNextLine()) {
            Integer freq = Integer.valueOf(in.nextLine().trim());
            huffmanCode.addSymbol(i++, freq);
        }

        Map<Integer, String> res = huffmanCode.getEncodingRuleUsingHeap();

        return res;
    }

    public static void main(String[] agrs) throws Exception{
//        HuffmanCode<String> h = new HuffmanCode<>();
//        h.addSymbol("a", 60);
//        h.addSymbol("b", 20);
//        h.addSymbol("c", 10);
//        h.addSymbol("d", 5);
//        h.addSymbol("e", 3);
//        h.addSymbol("g", 1);
//        h.addSymbol("h", 0.5);
//        h.addSymbol("i", 67);
//        h.addSymbol("k", 392);
//        h.addSymbol("l", 3);
//        h.addSymbol("m", 389570);
//        System.out.println(h.getEncodingRuleUsingQueues());
//        System.out.println(h.getEncodingRuleUsingHeap());
        Map<Integer, String> res = computeEncodingRule("./testData/graph/huffman.txt");
        System.out.println("Max encoding length: " + res.values().stream().map(e -> e.length()).max(Integer::compareTo));
        System.out.println("Min encoding length: " + res.values().stream().map(e -> e.length()).min(Integer::compareTo));
    }
}
