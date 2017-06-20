package unionfind;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 6/20/17.
 */
class UnionFindTest {

    @Test
    void union_reduceComponentCount() {
        UnionFind uf = new UnionFind(100);
        Random ran = new Random();
        for(int i = 0; i < 1000; i++) {
            int a = ran.nextInt(100);
            int b = ran.nextInt(100);
            int currentSize = uf.getComponentCount();
            if(uf.union(a, b)) assertTrue(currentSize - 1 == uf.getComponentCount());
        }
    }

    @Test
    void find() {
        UnionFind uf = new UnionFind(100);
        IntStream.of(1,3,5,7).forEach(
                i -> uf.union(i,9)
        );
        IntStream.of(0,2,4,6).forEach(
                i -> uf.union(i,8)
        );
        assertTrue(uf.isConnected(1,1));
        assertTrue(uf.isConnected(1,3));
        assertTrue(uf.isConnected(1,5));
        assertTrue(uf.isConnected(1,7));
        assertTrue(uf.isConnected(1,9));
        assertTrue(uf.isConnected(3,3));
        assertTrue(uf.isConnected(3,7));
        assertTrue(uf.isConnected(3,9));
        assertTrue(uf.isConnected(5,5));
        assertTrue(uf.isConnected(5,7));
        assertTrue(uf.isConnected(5,9));
        assertTrue(uf.isConnected(7,7));
        assertTrue(uf.isConnected(7,9));
        assertTrue(uf.isConnected(9,9));

        assertTrue(uf.isConnected(0,0));
        assertTrue(uf.isConnected(0,2));
        assertTrue(uf.isConnected(0,4));
        assertTrue(uf.isConnected(0,6));
        assertTrue(uf.isConnected(0,8));
        assertTrue(uf.isConnected(2,2));
        assertTrue(uf.isConnected(2,4));
        assertTrue(uf.isConnected(2,6));
        assertTrue(uf.isConnected(2,8));
        assertTrue(uf.isConnected(4,4));
        assertTrue(uf.isConnected(4,6));
        assertTrue(uf.isConnected(4,8));
        assertTrue(uf.isConnected(6,6));
        assertTrue(uf.isConnected(6,8));
        assertTrue(uf.isConnected(8,8));

        for(int i = 0; i < 8; i++) {
            assertFalse(uf.isConnected(i, i+1));
            assertFalse(uf.isConnected(i + 1, i));
        }
    }

    @Test
    void unionFind_randomTest() {
        final int n = 10000;
        UnionFind uf = new UnionFind(n);
        Random ran = new Random();
        for(int i = 0; i < n; i++) {
            int a = ran.nextInt(n);
            int b = ran.nextInt(n);
            int count = uf.getComponentCount();
            if(uf.union(a, b)) {
                assertTrue(count == uf.getComponentCount() + 1);
                assertTrue(uf.isConnected(a, b));
                assertTrue(uf.isConnected(b, a));
            } else {
                assertTrue(count == uf.getComponentCount());
            }
        }
    }
}