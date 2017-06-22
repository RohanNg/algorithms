package clustering;

import unionfind.UnionFind;

import java.io.FileReader;
import java.util.*;

public class KMaxSpacingClustering<T> {


    public static class Graph<T> {
        public static class Node<T> {
            private final T val;
            private final int id;
            public Node(T val, int id) {
                Objects.requireNonNull(val);
                this.val = val;
                this.id = id;
            }

            public Edge<T> connect(Node<T> other, int cost) {
                Edge<T> edge = new Edge<>(this, other, cost);
                return edge;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Node<?> node = (Node<?>) o;

                return val.equals(node.val);
            }

            @Override
            public String toString() {
                return val.toString();
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

            @Override
            public String toString() {
                final StringBuffer sb = new StringBuffer("{");
                sb.append(a);
                sb.append(",").append(b);
                sb.append('}');
                return sb.toString();
            }
        }

        Map<T, Node<T>> nodes;
        List<Edge<T>> edges;
        private int currentId = 0;

        public Graph() {
            this.nodes = new HashMap<>();
            this.edges = new ArrayList<>();
        }

        public void addNode(T node) {
            if(! nodes.containsKey(node)) nodes.put(node, new Node<>(node, currentId++));
        }

        public void addEdge(T a, T b, int cost) {
            if(!nodes.containsKey(a) || !nodes.containsKey(b))
                throw new IllegalArgumentException();
            edges.add(new Edge<>(nodes.get(a), nodes.get(b), cost));
        }

        public int maxSpacingKCluster(final int clusterNum) {
            UnionFind uf = new UnionFind(nodes.size());
            int num = nodes.size();
            Collections.sort(edges, Comparator.comparingInt(i -> i.cost));
            Iterator<Edge<T>> iterator = edges.iterator();
            while(iterator.hasNext()) {
                Edge<T> edge = iterator.next();
                if(!uf.isConnected(edge.a.id, edge.b.id)) {
                    uf.union(edge.a.id, edge.b.id);
                    if(--num == clusterNum) break;
                }
            }

            while(iterator.hasNext()) {
                Edge<T> edge = iterator.next();
                if(!uf.isConnected(edge.a.id, edge.b.id)) {
                    return edge.cost;
                }
            }

            throw new UnsupportedOperationException("Disconnected graph");
        }
    }

    public static void test1() throws Exception {
        Graph<Integer> min = new Graph<>();
        Scanner in = new Scanner(new FileReader("./testData/graph/maxSpacingKClustering.txt"));
        int nodeNum = Integer.valueOf(in.nextLine().trim());
        for(int i = 1; i <= nodeNum; i ++) min.addNode(i);

        while (in.hasNextLine()) {
            String[] edge = in.nextLine().trim().split("\\s+");
            System.out.println(Arrays.toString(edge));
            min.addEdge(Integer.valueOf(edge[0]), Integer.valueOf(edge[1]), Integer.valueOf(edge[2]));
        }
        System.out.println(min.maxSpacingKCluster(4));
    }

    public static void main(String[] args) throws Exception {
        test1();
    }
}
