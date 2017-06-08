package graph;

import com.google.common.base.MoreObjects;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StronglyConnectedComponent {

    /**
     * Directed graph
     * with adjacency list representation
     */
    public static class Graph<T> {
        public static class Node<T> {
            private final T val;
            private List<Edge<T>> outbound;
            private List<Edge<T>> inbound;
            private boolean seached = false;

            public Node(T val) {
                this.val = val;
            }

            public void addInboundEdge(Edge<T> edge) {
                if (!edge.head.equals(this)) throw new IllegalArgumentException();
                if (inbound == null) {
                    inbound = new ArrayList<>();
                }
                inbound.add(edge);
            }

            public void addOutboundEdge(Edge<T> edge) {
                if (!edge.tail.equals(this)) throw new IllegalArgumentException();
                if (outbound == null) {
                    outbound = new ArrayList<>();
                }
                outbound.add(edge);
            }

            public void setSeached(boolean searched) {
                this.seached = searched;
            }

            public List<Edge<T>> getOutbound() {
                return outbound == null ? Collections.emptyList() : outbound;
            }

            public List<Edge<T>> getInbound() {
                return inbound == null ? Collections.emptyList() : inbound;
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

            @Override
            public String toString() {
                return val.toString();
            }
        }

        public static class Edge<T> {
            private Node<T> tail;
            private Node<T> head;

            private Edge(Node<T> tail, Node<T> head) {
                this.tail = tail;
                this.head = head;
            }

            public static <T> Edge<T> createEdge(Node<T> tail, Node<T> head) {
                Edge<T> edge = new Edge<>(tail, head);
                head.addInboundEdge(edge);
                tail.addOutboundEdge(edge);
                return edge;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Edge<?> edge = (Edge<?>) o;

                if (!head.equals(edge.head)) return false;
                return tail.equals(edge.tail);
            }

            @Override
            public int hashCode() {
                int result = head.hashCode();
                result = 31 * result + tail.hashCode();
                return result;
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                        .add("", tail)
                        .add(" -> ", head)
                        .toString();
            }
        }

        private final HashMap<T, Node<T>> nodes;
        private final Set<Edge<T>> edges;

        public Graph() {
            nodes = new HashMap<>();
            edges = new HashSet<>();
        }

        public void addNode(T val) {
            nodes.put(val, new Node<>(val));
        }

        public void addEdge(T from, T to) {
            if (!nodes.containsKey(from) || !nodes.containsKey(to))
                throw new IllegalArgumentException();
            edges.add(Edge.createEdge(nodes.get(from), nodes.get(to)));
        }

        private void markAllUnsearched() {
            for (Node<?> node : nodes.values())
                node.setSeached(false);
        }

        /**
         * Run DFS for the first DFS Loop, which build
         * the finishing time ordering
         */
        private void sccFirstDFS(Node<T> from, List<Node<T>> finishingTime) {
            from.seached = true;
            for (Edge<T> edge : from.getInbound())
                if (!edge.tail.seached) {
                    sccFirstDFS(edge.tail, finishingTime);
                }
            finishingTime.add(from);
        }

        /**
         * Run DFS for the second DFS Loop, which build
         * the map from the leader to the size of the strongly connected component
         * represented by the leader
         */
        private void sccSecondDFS(Node<T> from, Node<T> leader, Map<Node<T>, Integer> collector) {
            from.seached = true;
            if (!collector.containsKey(leader))
                collector.put(leader, 0);
            collector.put(leader, collector.get(leader) + 1);
            for (Edge<T> edge : from.getOutbound())
                if (!edge.head.seached)
                    sccSecondDFS(edge.head, leader, collector);
        }

        /**
         * Search for strongly connected component in this graph
         *
         * @return a map from the leader component to the size of the
         * strongly connected component represented by the leader
         */
        public Map<Node<T>, Integer> searchForStronglyConnectedComponent() {
            // first DFS loop
            markAllUnsearched();
            List<Node<T>> runtimeOrder = new ArrayList<>(nodes.size());
            for (Node<T> node : nodes.values())
                if (!node.seached)
                    sccFirstDFS(node, runtimeOrder);

            // second DFS loop
            markAllUnsearched();
            Map<Node<T>, Integer> collector = new HashMap<>();
            ListIterator<Node<T>> it = runtimeOrder.listIterator(runtimeOrder.size());
            while (it.hasPrevious()) {
                Node<T> prev = it.previous();
                if (!prev.seached)
                    sccSecondDFS(prev, prev, collector);
            }
            return collector;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("nodes", nodes)
                    .add("edges", edges)
                    .toString();
        }
    }

    public static void main(String[] args) throws Exception {
        Graph<Integer> g = new Graph<>();
        Scanner in = new Scanner(new FileReader("./testData/graph/SCC.txt"));
        IntStream.range(1, 875715).forEach(
                i -> g.addNode(i)
        );

        while (in.hasNextLine()) {
            String[] inp = in.nextLine().trim().split("\\s+");
            System.out.println(Arrays.toString(inp));
            g.addEdge(Integer.valueOf(inp[0]), Integer.valueOf(inp[1]));
        }
        List<Integer> out = g.searchForStronglyConnectedComponent().values().stream().filter(i -> i > 100).collect(Collectors.toList());
        Collections.sort(out);
        System.out.println(out);
    }
}
