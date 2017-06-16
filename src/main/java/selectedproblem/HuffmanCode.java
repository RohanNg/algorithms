package selectedproblem;

import com.google.common.base.MoreObjects;
import heap.BinaryHeap;

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

    /** Register new unique symbol having given frequency to be encoded
     * @throws IllegalArgumentException if duplicate symbol found
     * */
    private void addSymbol(final T value, final double frequency) {
        Leaf<T> leaf = new Leaf<>(value, frequency);
        // because equals and hashcode for leaf are defined on T only,
        // the following check guarantee to discover duplicate
        if(!leaves.add(leaf)) throw new IllegalArgumentException();
    }

    public Map<T, String> getEncodingRule() {
        // version with priority queue

        // simple case
        if(leaves.size() == 0) return new HashMap<>();
        if(leaves.size() == 0) return new HashMap<T, String>() {
            {put(leaves.iterator().next().value, "0");}
        };

        BinaryHeap<Tree<T>> heap = BinaryHeap.heapify(leaves);
        while(heap.size() > 1) {
            Tree<T> l = heap.delMin();
            Tree<T> r = heap.delMin();
            heap.insert(new Node<>(l, r, l.frequency() + r.frequency()));
        }

        HashMap<T, String> code = new HashMap<>();
        traverseTreeAndBuildCode(heap.getMin(), code, "");
        return code;
    }

    private void traverseTreeAndBuildCode(Tree<T> tree, Map<T, String> map, String code) {
        if(tree.isLeaf()) map.put(tree.getSymbol(), code);
        else {
            traverseTreeAndBuildCode(tree.left(), map, code + '0');
            traverseTreeAndBuildCode(tree.right(), map, code + '1');
        }
    }

    public static void main(String[] agrs) {
        HuffmanCode<String> h = new HuffmanCode<>();
        h.addSymbol("a", 60);
        h.addSymbol("b", 20);
        h.addSymbol("c", 10);
        h.addSymbol("d", 5);
        h.addSymbol("e", 5);
        h.addSymbol("g", 1);
        h.addSymbol("h", 0.5);
        System.out.println(h.getEncodingRule());
    }
}
