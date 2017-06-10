package graph;

import com.google.common.base.MoreObjects;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
            private double shortestPath = Double.MAX_VALUE;

            private void setShortestPath(double shortestPath) {
                this.shortestPath = shortestPath;
            }

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
            private final Node<T> tail;
            private final Node<T> head;
            private final double length;

            private Edge(Node<T> tail, Node<T> head) {
                this(tail, head, 0);
            }

            private Edge(Node<T> tail, Node<T> head, double length) {
                this.tail = tail;
                this.head = head;
                this.length = length;
            }


            public static <T> Edge<T> createEdge(Node<T> tail, Node<T> head) {
                return createEdge(tail, head, 0);
            }

            public static <T> Edge<T> createEdge(Node<T> tail, Node<T> head, double length) {
                Edge<T> edge = new Edge<>(tail, head, length);
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

        public void addEdge(T from, T to, double length) {
            if (!nodes.containsKey(from) || !nodes.containsKey(to))
                throw new IllegalArgumentException();
            edges.add(Edge.createEdge(nodes.get(from), nodes.get(to), length));
        }

        private void markAllUnsearched() {
            for (Node<?> node : nodes.values())
                node.setSeached(false);
        }

        private void resetShortestPath() {
            for (Node<T> node : nodes.values())
                node.setShortestPath(Double.MAX_VALUE);
        }

        /**
         * Run DFS for the first DFS Loop, which build
         * the finishing time ordering
         */
        private void runSccFirstDFS(Node<T> from, List<Node<T>> finishingTime) {
            from.seached = true;
            for (Edge<T> edge : from.getInbound())
                if (!edge.tail.seached) {
                    runSccFirstDFS(edge.tail, finishingTime);
                }
            finishingTime.add(from);
        }

        /**
         * Run DFS for the second DFS Loop, which build
         * the map from the leader to the size of the strongly connected component
         * represented by the leader
         */
        private void runSccSecondDFS(Node<T> from, Node<T> leader, Map<Node<T>, Integer> collector) {
            from.seached = true;
            if (!collector.containsKey(leader))
                collector.put(leader, 0);
            collector.put(leader, collector.get(leader) + 1);
            for (Edge<T> edge : from.getOutbound())
                if (!edge.head.seached)
                    runSccSecondDFS(edge.head, leader, collector);
        }

        /**
         * Search for strongly connected component in this graph
         *
         * @return a map from the leader component to the size of the
         * strongly connected component represented by the leader
         */
        public Map<Node<T>, Integer> computeForStronglyConnectedComponent() {
            // first DFS loop
            markAllUnsearched();
            List<Node<T>> runtimeOrder = new ArrayList<>(nodes.size());
            for (Node<T> node : nodes.values())
                if (!node.seached)
                    runSccFirstDFS(node, runtimeOrder);

            // second DFS loop
            markAllUnsearched();
            Map<Node<T>, Integer> collector = new HashMap<>();
            ListIterator<Node<T>> it = runtimeOrder.listIterator(runtimeOrder.size());
            while (it.hasPrevious()) {
                Node<T> prev = it.previous();
                if (!prev.seached)
                    runSccSecondDFS(prev, prev, collector);
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

        private Comparator<Edge<T>> EDGE_COMPARATOR =
                (Edge<T> o1, Edge<T> o2) -> {
                    if (o1.tail.shortestPath == Double.MAX_VALUE ||
                            o2.tail.shortestPath == Double.MAX_VALUE)
                        throw new IllegalArgumentException();
                    return Double.compare(o1.tail.shortestPath + o1.length, o2.tail.shortestPath + o2.length);
                };


        public Map<T, Double> computeShortestPath(T val) {
            Map<T, Double> shortestPath = new HashMap<>();
            MinPQ<Edge<T>> pr = new MinPQ<>(edges.size(), EDGE_COMPARATOR);
            Node<T> init = nodes.get(val);
            init.seached = true;
            init.setShortestPath(0.0);
            shortestPath.put(val, 0.0);
            for(Edge<T> edge : init.getOutbound()) {
                pr.insert(edge);
            }

            while(!pr.isEmpty()) {
                Edge<T> min = pr.delMin();
                assert min.tail.seached;
                if (!min.head.seached) {
                    Node<T> head = min.head;
                    double minL = min.tail.shortestPath + min.length;
                    head.setShortestPath(minL);
                    min.head.seached = true;
                    shortestPath.put(head.val, minL);
                    // add to priority queue
                    for(Edge<T> edge: head.getOutbound())
                        if (!edge.head.seached)
                            pr.insert(edge);
                }
            }
            return shortestPath;
        }
    }

    public static void testSCC() throws Exception {
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
        List<Integer> out = g.computeForStronglyConnectedComponent().values().stream().filter(i -> i > 100).collect(Collectors.toList());
        Collections.sort(out);
    }

    public static void main(String[] args) throws Exception {
        Graph<Integer> g = new Graph<>();
        IntStream.range(1, 201).forEach(
                i -> g.addNode(i)
        );
        Scanner in = new Scanner(new FileReader("./testData/graph/dijkstraData.txt"));
        while (in.hasNextLine()) {
            List<String> input = Stream
                    .of(in.nextLine().trim().split("\\s+"))
                    .collect(Collectors.toList());
            Iterator<String> it = input.listIterator(0);
            int fist = Integer.valueOf(it.next());
            while(it.hasNext()) {
                String[] next = it.next().split(",");
                g.addEdge(fist, Integer.valueOf(next[0]), Integer.valueOf(next[1]));
            }
        }
        Map<Integer, Double> map = g.computeShortestPath(1);
        IntStream.of(7,37,59,82,99,115,133,165,188,197).forEach(
                i -> System.out.print(map.get(i).intValue() + ",")
        );
    }
}
