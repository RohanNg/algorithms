package graph;
import com.sun.istack.internal.NotNull;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomContraction {

    public static class Graph<T> {
        public interface Node<T> {
            boolean isSingle();

            T value();

            Set<Node<T>> values();

            List<Edge<T>> edges();

            void addEdge(Edge<T> edge);

            default Edge<T> connect(Node<T> other) {
                final Edge<T> edge = new Edge<>(this, other);
                this.addEdge(edge);
                other.addEdge(edge);
                return edge;
            }

            default Node<T> contract(Node<T> other) {

                final Set<Node<T>> values = new HashSet<>();
                if (this.isSingle()) values.add(this);
                else values.addAll(this.values());

                if (other.isSingle()) values.add(other);
                else values.addAll(other.values());

                final Node<T> contractedNode = new ContractedNode<>(values);

                for (Edge<T> edge : this.edges()) {
                    if (!edge.contains(other)) {
                        edge.replace(this, contractedNode);
                        contractedNode.addEdge(edge);
                    }
                }

                for (Edge<T> edge : other.edges()) {
                    if (!edge.contains(this)) {
                        edge.replace(other, contractedNode);
                        contractedNode.addEdge(edge);
                    }
                }

                this.removeAllEdges();
                other.removeAllEdges();

                return contractedNode;
            }

            void removeAllEdges();
        }

        public static class ContractedNode<T> implements Node<T> {
            private Set<Node<T>> values;
            private List<Edge<T>> edges;

            public ContractedNode(Set<Node<T>> values, List<Edge<T>> edges) {
                for (Node<T> node : values) {
                    if (!node.isSingle()) throw new IllegalArgumentException();
                }
                this.values = values;
                this.edges = edges;
            }

            public ContractedNode(Set<Node<T>> values) {
                this(values, new ArrayList<>());
            }

            @Override
            public boolean isSingle() {
                return false;
            }

            @Override
            public T value() {
                throw new UnsupportedOperationException("Contracted.value");
            }

            @Override
            public Set<Node<T>> values() {
                return values;
            }

            @Override
            public List<Edge<T>> edges() {
                return edges;
            }

            @Override
            public void addEdge(Edge<T> edge) {
                if (!edge.contains(this)) throw new RuntimeException();
                this.edges.add(edge);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                ContractedNode<?> that = (ContractedNode<?>) o;

                return values.equals(that.values);
            }

            @Override
            public int hashCode() {
                return values.hashCode();
            }

            @Override
            public String toString() {
                return new StringBuilder()
                        .append("Contracted {")
                        .append("value = ")
                        .append(values.toString())
                        .append("}")
                        .toString();
            }

            @Override
            public void removeAllEdges() {
                edges.clear();
            }
        }

        public static class SingleNode<T> implements Node<T> {

            private final T val;
            private final List<Edge<T>> edges;

            public SingleNode(final T val) {
                this(val, new ArrayList<>());
            }

            public SingleNode(final T val, List<Edge<T>> edges) {
                this.val = val;
                this.edges = edges;
            }

            @Override
            public boolean isSingle() {
                return true;
            }

            @Override
            public T value() {
                return val;
            }

            @Override
            public Set<Node<T>> values() {
                throw new UnsupportedOperationException("SingleNode.values");
            }

            @Override
            public List<Edge<T>> edges() {
                return edges;
            }

            @Override
            public void addEdge(Edge<T> edge) {
                if (!edge.contains(this)) throw new RuntimeException();
                this.edges.add(edge);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                SingleNode<?> that = (SingleNode<?>) o;

                return val.equals(that.val);
            }

            @Override
            public int hashCode() {
                return val.hashCode();
            }

            @Override
            public String toString() {
                return new StringBuilder()
                        .append("Single{ ")
                        .append(val.toString())
                        .append(" }")
                        .toString();
            }

            @Override
            public void removeAllEdges() {
                edges.clear();
            }
        }

        private static class Edge<T> {
            private Node<T> a;
            private Node<T> b;

            public Edge(@NotNull Node<T> a, @NotNull Node<T> b) {
                this.a = a;
                this.b = b;
            }

            public void replace(Node<T> old, Node<T> newNode) {
                if (a.equals(old)) {
                    a = newNode;
                } else if (b.equals(old)) {
                    b = newNode;
                } else throw new IllegalArgumentException("Old is not member of this edge");
            }

            public boolean contains(Node<T> node) {
                return a.equals(node) || b.equals(node);
            }

            public boolean isSelfLoop() {
                return a.equals(b);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Edge<?> edge = (Edge<?>) o;

                if (a.equals(edge.a)) return b.equals(edge.b);
                else return (a.equals(edge.b) && b.equals(edge.a));
            }

            @Override
            public int hashCode() {
                int result = 39;
                result = 31 * result + b.hashCode() * a.hashCode();
                return result;
            }

            @Override
            public String toString() {
                return new StringBuilder()
                        .append("Edge { ")
                        .append(a.toString())
                        .append(", ")
                        .append(b.toString())
                        .append(" }")
                        .toString();
            }

            public Node<T> getA() {
                return a;
            }

            public Node<T> getB() {
                return b;
            }

            public Node<T> contract() {
                return a.contract(b);
            }
        }

        private final Map<T, Node<T>> nodes;
        private final List<Edge<T>> edges;

        public Graph() {
            nodes = new HashMap<>();
            edges = new LinkedList<>();
        }

        public int attempMinCut() {
            Random ran = new Random();
            // do a copy
            final Map<T, Node<T>> verticesMap = new HashMap<>();
            final List<Edge<T>> eds = new ArrayList<>();
            for(Node<T> n : nodes.values()) {
                verticesMap.put(n.value(), new SingleNode<>(n.value()));
            }
            for(Edge<T> edge : edges) {
                eds.add(verticesMap.get(edge.getA().value()).connect(verticesMap.get(edge.getB().value())));
            }
            final Set<Node> vertices = new HashSet<>(verticesMap.values());
            while(vertices.size() > 2) {
                Edge<T> removed = eds.remove(ran.nextInt(eds.size()));
                // remove self loop
                eds.removeIf( p -> p.equals(removed));
                vertices.remove(removed.a);
                vertices.remove(removed.b);
                vertices.add(removed.contract());
            }
            return eds.size();
        }

        public void addNode(T value) {
            java.util.Objects.requireNonNull(value);
            nodes.put(value, new SingleNode<>(value));
        }

        public void addEdge(T a, T b) {
            if (!nodes.containsKey(a) || !nodes.containsKey(b))
                throw new IllegalArgumentException("Invalid node value");

            edges.add(nodes.get(a).connect(nodes.get(b)));
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("Graph {\n ")
                    .append(nodes.values().toString())
                    .append("\n ")
                    .append(edges.toString())
                    .append("\n}")
                    .toString();
        }
    }

    public static void main(String[] args) throws Exception {
        Graph<Integer> g = new Graph<>();
        Scanner in = new Scanner(new FileReader("./testData/sort/KargerMinCut.txt"));
        IntStream.range(1, 201).forEach(
                i -> g.addNode(i)
        );

        while (in.hasNextLine()) {
            List<Integer> input = Stream
                    .of(in.nextLine().trim().split("\\s+"))
                    .map( i -> Integer.valueOf(i))
                    .collect(Collectors.toList());
            System.out.println(input);
            Iterator<Integer> it = input.iterator();
            int fist = it.next();
            while(it.hasNext()) {
                int next = it.next();
                if (next > fist)
                    g.addEdge(fist, next);
                // if next < first
                // connection next <-> first has already been added
            }
        }
        int[] res = IntStream.range(0,100).map(
                ii -> {
            int min = Integer.MAX_VALUE;
            // n = #vertices = 200
            // Attempt 100 time
            // Pr[100 trial fail] <= e^(-100/200^2) = 99.75
            for (int i = 0; i < 100; i++) {
                int val = g.attempMinCut();
                if (val < min) min = val;
            }
            return min;
        }
        ).toArray();
        System.out.println("Number of crossing edges for min cut: " + res);
    }
}
