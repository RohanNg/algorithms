package clustering;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static clustering.BinaryPathTree.Package;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 6/21/17.
 */
class BinaryPathTreeTest {
    @Test
    void buildSearchTree_completeSearchTree() {
        BinaryPathTree<Integer> p2 = new BinaryPathTree();
        p2.buildSearchTree(Arrays.asList(
                new Package<>("000", 0),
                new Package<>("010", 1),
                new Package<>("101", 2),
                new Package<>("001", 3),
                new Package<>("100", 4),
                new Package<>("011", 5),
                new Package<>("111", 6),
                new Package<>("110", 7)
        ));
        assertTrue(p2.findValue("000") == 0);
        assertTrue(p2.findValue("010") == 1);
        assertTrue(p2.findValue("101") == 2);
        assertTrue(p2.findValue("001") == 3);
        assertTrue(p2.findValue("100") == 4);
        assertTrue(p2.findValue("011") == 5);
        assertTrue(p2.findValue("111") == 6);
        assertTrue(p2.findValue("110") == 7);
    }

    @Test
    void buildSearchTree_linkedListTree() {
        BinaryPathTree<String> p2 = new BinaryPathTree();
        List<Package<String>> packages = Arrays.asList(
                new Package<>("01", "a"),
                new Package<>("001", "b"),
                new Package<>("0001", "c"),
                new Package<>("00001", "d"),
                new Package<>("000001", "e"),
                new Package<>("0000001", "f")
        );
        p2.buildSearchTree(packages);
        for(Package<String> p : packages)
            assertTrue(p2.findValue(p.getPath()) == p.getValue());

        for(String path: Arrays.asList("0", "1", "10", "111", "1010", "011", "010", "000000", "0000000", "00000001", "0000010"))
            assertNull(p2.findValue(path));
    }

}