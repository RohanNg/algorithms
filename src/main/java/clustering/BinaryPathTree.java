package clustering;
import edu.princeton.cs.algs4.In;
import unionfind.UnionFind;

import java.io.FileReader;
import java.util.*;


public class BinaryPathTree<T> {
    public interface Tree<T> {
        boolean isLeaf();
        boolean isNode();
        T getSymbol();
        Tree<T> left();
        Tree<T> right();
    }

    public static class Package<T> {
        private final String path;
        private final T value;
        public Package(String path, T value) {
            this.path = path;
            this.value = value;
        }

        public String getPath() {
            return path;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Package{");
            sb.append("path='").append(path).append('\'');
            sb.append(", value=").append(value);
            sb.append('}');
            return sb.toString();
        }

        public T getValue() {


            return value;
        }
    }

    private static class Node<T> implements Tree<T> {
        private final Tree<T> left;
        private final Tree<T> right;

        public Node(Tree<T> left, Tree<T> right) {
            this.left = left;
            this.right = right;
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
        public String toString() {
            final StringBuffer sb = new StringBuffer("{");
            sb.append(left);
            sb.append(",").append(right);
            sb.append('}');
            return sb.toString();
        }
    }
    private static class Leaf<T> implements Tree<T> {

        private final T value;

        public Leaf(T value) {
            Objects.requireNonNull(value);
            this.value = value;
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
        public String toString() {
            return value.toString();
        }
    }

    private Tree<T> root = new Node<>(null, null);

    public void buildSearchTree(List<Package<T>> nodes) {
        for(Package<T> node : nodes) {
            root = buildSearchTree(root, node.path.getBytes(), 0, node.value);
        }
    }

    public void buildSearchTree(Package<T> node) {
        root = buildSearchTree(root, node.path.getBytes(), 0, node.value);
    }

    private Tree<T> buildSearchTree(Tree<T> curr, byte[] route, int currentIndex, T value) {
        if (currentIndex == route.length - 1) {
            if(route[currentIndex] == '1')
                return new Node<>(curr == null ? null : curr.left(), new Leaf<>(value));
            else
                return new Node<>(new Leaf<>(value), curr == null ? null : curr.right());
        } else {
            if(route[currentIndex] == '1')
                return new Node<>(
                        curr == null ? null : curr.left(),
                        buildSearchTree(curr == null ? null : curr.right(), route, currentIndex + 1, value));
            else
                return new Node<>(
                        buildSearchTree(curr == null ? null : curr.left(),route, currentIndex + 1, value),
                        curr == null ? null : curr.right());
        }
    }

    public T findValue(String direction) {
        if (direction.isEmpty()) return null;
        return findValue(root, direction.getBytes(), 0);
    }

    public T findValue(byte[] direction) {
        if (direction.length == 0) return null;
        return findValue(root, direction, 0);
    }

    private T findValue(Tree<T> current, byte[] direction, int index) {
        if(index < direction.length - 1) {
            if(current == null) return null;
            else if (direction[index] == '1') return findValue(current.right(), direction, index + 1);
            else return findValue(current.left(), direction, index + 1);
        } else {
            // index = direction.length - 1
            if(current == null) return null;
            if(current.isLeaf()) return null;
            if (direction[index] == '1') {
                if(current.right() != null && current.right().isLeaf()) return current.right().getSymbol();
                else return null;
            } else {
                if(current.left() != null && current.left().isLeaf()) return current.left().getSymbol();
                else return null;
            }
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }


    public static int getManhattanDistance(byte[] a, byte[] b) {
        int count = 0;
        for( int i = 0; i < a.length; i++) {
            if(a[i] != b[i]) count ++;
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        BinaryPathTree<Integer> binaryPathTree = new BinaryPathTree<>();
        Scanner in = new Scanner(new FileReader("./testData/graph/clustering_big.txt"));
        Map<Integer, Package<Integer>> packages = new HashMap<>();

        String[] info = in.nextLine().trim().split("\\s+");
        final int nodeNum = Integer.valueOf(info[0]);
        System.out.println("#node: " + info[0] + ", #bits/path: " + info[1]);

        class Edge {
            final int a;
            final int b;
            public Edge(int a, int b) {
                if(a == b) throw new RuntimeException();
                this.a = Math.min(a, b);
                this.b = Math.max(a, b);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Edge edge = (Edge) o;

                if (a != edge.a) return false;
                return b == edge.b;
            }

            @Override
            public int hashCode() {
                int result = a;
                result = 31 * result + b;
                return result;
            }
        }

        List<Edge> length0Edges = new ArrayList<>();

        int i = 0;
        while (in.hasNextLine()) {
            String path = in.nextLine().trim().replace(" ","");
            assert path.length() == Integer.valueOf(info[1]);
            Integer val = binaryPathTree.findValue(path);
            if(val != null) {
                length0Edges.add(new Edge(val, i));
            } else {
                Package<Integer> pack = new Package<>(path, i);
                packages.put(i, pack);
                binaryPathTree.buildSearchTree(pack);
            }
            i++;
        }

        for(Package<Integer> p : packages.values() )
            assert binaryPathTree.findValue(p.getPath()) == p.getValue();

        Set<Edge> length1Edges = new HashSet<>();
        for(Package<Integer> p : packages.values()) {
            final byte[] path = p.getPath().getBytes();
            for(int x = 0; x < path.length; x++) {
                final byte[] copy = Arrays.copyOf(path, path.length);
                assert Arrays.equals(path, copy);
                if(copy[x] == '0') copy[x] = '1';
                else copy[x] = '0';

                Integer friend = binaryPathTree.findValue(copy);
                if(friend != null) length1Edges.add(new Edge(p.getValue(), friend));
            }
        }

        Set<Edge> length2Edges = new HashSet<Edge>();
        for(Package<Integer> p : packages.values()) {
            final byte[] path = p.getPath().getBytes();
            for(int x = 0; x < path.length; x++) {
                for( int y = x + 1; y < path.length; y ++) {
                    final byte[] copy = Arrays.copyOf(path, path.length);
                    assert Arrays.equals(path, copy);
                    if(copy[x] == '0') copy[x] = '1';
                    else copy[x] = '0';

                    if(copy[y] == '0') copy[y] = '1';
                    else copy[y] = '0';

                    Integer friend = binaryPathTree.findValue(copy);
                    if(friend != null) length2Edges.add(new Edge(p.getValue(), friend));
                }
            }
        }
//
//        for(Edge e : length0Edges) {
//            assert getManhattanDistance(packages.get(e.a).getPath().getBytes(), packages.get(e.b).getPath().getBytes()) == 0;
//        }

        for(Edge e : length1Edges) {
            assert getManhattanDistance(packages.get(e.a).getPath().getBytes(), packages.get(e.b).getPath().getBytes()) == 1;
        }

        for(Edge e : length2Edges) {
            assert getManhattanDistance(packages.get(e.a).getPath().getBytes(), packages.get(e.b).getPath().getBytes()) == 2;
        }

        UnionFind uf = new UnionFind(nodeNum);
        for(Edge e : length0Edges)
            uf.union(e.a, e.b);
        for(Edge e : length1Edges)
            uf.union(e.a, e.b);
        for(Edge e : length2Edges)
            uf.union(e.a, e.b);
        System.out.println(uf.getComponentCount());
    }
}
