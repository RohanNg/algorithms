package graph;

import heap.BinaryHeap;
import scala.Int;

import java.util.*;

public class MinuminSpanningTree<T> {
    public static class Node<T> {
        private final T val;
        private List<Edge<T>> edges;
        private boolean visited = false;
        public Node(T val) {
            Objects.requireNonNull(val);
            this.val = val;
        }

        private List<Edge<T>> getEdges() {
            return edges == null ? Collections.emptyList() : edges;
        }

        public void connect(Node<T> other, int cost) {
            Edge<T> edge = new Edge<T>(this, other, cost);
            this.addEdge(edge);
            other.addEdge(edge);
        }

        private void addEdge(Edge<T> edge) {
            if(edges == null) edges = new ArrayList<>();
            edges.add(edge);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node<?> node = (Node<?>) o;

            return val.equals(node.val);
        }

        @Override
        public int hashCode() {
            return val.hashCode();
        }
    }

    public static class Edge<T> {
        private final Node<T> a;
        private final Node<T> b;
        private final int cost;
        public Edge(Node<T> a, Node<T> b, int cost) {
            this.a = a;
            this.b = b;
            this.cost = cost;
        }
        public Node<T> getOtherEnd(Node<T> oneEnd) {
            return oneEnd.equals(a) ? b : a;
        }
    }

    private Map<T, Node<T>> nodes;
    private List<Edge<T>> edges;

    public MinuminSpanningTree() {
        this.nodes = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    private void addNode(T value) {
        assertContainNode(value);
        nodes.put(value, new Node<>(value));
    }

    private void assertContainNode(T... value) {
        for(T val : value) {
            if (nodes.containsKey(val)) throw new IllegalArgumentException();
        }
    }

    private void addEdge(T a, T b, int cost) {
        assertContainNode(a, b);
        nodes.get(a).connect(nodes.get(b), cost);
    }

//    private List<Edge<T>> computeMinimumSpanningTree() {
//        if(edges.isEmpty()) return Collections.emptyList();
//        return computeMSTPrim();
//    }

    private static class PrimNode<T> implements Comparable<PrimNode<T>> {
        public final T value;
        public Edge<T> edge;
        public PrimNode(T value, Edge<T> edge) {
            this.value = value;
            this.edge = edge;
        }
        public PrimNode(T value) {
            this.value = value;
        }
        private int getScore() {
            return edge == null ? Integer.MAX_VALUE : edge.cost;
        }
        @Override
        public int compareTo(PrimNode<T> o) {
            return Integer.compare(this.getScore(), o.getScore());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PrimNode<?> primNode = (PrimNode<?>) o;

            return value.equals(primNode.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    private void markAllNodeUnvisited() {
        for(Node<T> node : nodes.values())
            node.visited = false;
    }

//    private List<Edge<T>> computeMSTPrim() {
//        // initialize
//        List<Edge<T>> selectedEdges = new ArrayList<>();
//
//        Set<T> unProcessedVertices = new HashSet<T>(nodes.keySet());
//        Node<T> arbitraryNode = nodes.get(unProcessedVertices.iterator().next());
//        unProcessedVertices.remove(arbitraryNode.val);
//        List<PrimNode<T>> primNodes = new ArrayList<>();
//        for(Edge<T> edge : arbitraryNode.getEdges()) {
//            T otherEnd = edge.getOtherEnd(arbitraryNode).val;
//            primNodes.add(new PrimNode<>(otherEnd, edge));
//            unProcessedVertices.remove(otherEnd);
//        }
//        for(T left : unProcessedVertices)
//            primNodes.add(new PrimNode<>(left));
//
//        BinaryHeap<PrimNode<T>> bh = BinaryHeap.heapify(primNodes);
//
//        // ready to process
//        while(bh.isEmpty()) {
//            PrimNode<T> next = bh.delMin();
//            selectedEdges.add(next.edge);
//
//            Node<T> processed = nodes.get(next.value);
//            processed.visited = true;
//            for(Edge<T> edge : processed.getEdges()) {
//                Node<T> otherEnd = edge.getOtherEnd(processed);
//                if(!otherEnd.visited) {
//                    bh.de
//                }
//
//            }
//        }
//    }

}
