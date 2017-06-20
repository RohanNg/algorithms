package unionfind;

/**
 * Lazy Union Find Data Structure
 * implemented with Union by Rank and Path Compression
 */
public class UnionFind {
    private final int[] nodes;
    private final int[] ranks;
    private int count;

    public UnionFind(int componentNum) {
        if(componentNum < 0) throw new IllegalArgumentException("Invalid");
        this.nodes = new int[componentNum];
        this.ranks = new int[componentNum];
        for(int i = 0; i < componentNum; i++) {
            this.nodes[i] = i;
            this.ranks[i] = 1;
        }
        this.count = componentNum;
    }

    private void validateElement(int a) {
        if(a < 0 || a >= nodes.length) throw new IllegalArgumentException();
    }

    public boolean union(int a, int b) {
        validateElement(a); validateElement(b);
        int aLeader = findRec(a);
        int bLeader = findRec(b);
        if(aLeader == bLeader) return false;

        final int rankA = ranks[aLeader];
        final int rankB = ranks[bLeader];
        if(rankA < rankB) {
            ranks[aLeader] = rankA + 1;
            nodes[bLeader] = aLeader;
        } else {
            ranks[bLeader] = rankB + 1;
            nodes[aLeader] = bLeader;
        }
        count --;
        return true;
    }

    public int find(int a) {
        validateElement(a);
        return findRec(a);
    }

    public boolean isConnected(int a, int b) {
        validateElement(a);
        validateElement(b);

        return findRec(a) == findRec(b);
    }

    private int findRec(int a) {
        int leader = nodes[a];
        if(leader == a) return leader;
        else {
            int trueLeader = findRec(leader);
            nodes[a] = trueLeader;
            return trueLeader;
        }
    }

    public int getComponentCount() {
        return count;
    }

}
