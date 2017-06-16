package heap;

import heap.BinaryHeap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 6/10/17.
 */
class BinaryHeapTest {

    BinaryHeap<Integer> binaryHeap;

    @BeforeEach
    void init() {
        binaryHeap = new BinaryHeap<>();
    }

    @Test
    void insert_increasingUniqueSequence() {
        IntStream.range(1,1000).forEach(
                i -> {
                    binaryHeap.insert(i);
                    assertTrue(binaryHeap.size() == i);
                    assertTrue(binaryHeap.getMin() == 1);
                }
        );
    }

    @Test
    void insert_decreasingUniqueSequence() {
        for(int i = 1000; i > 0; i--) {
            binaryHeap.insert(i);
            assertTrue(binaryHeap.size() == 1000 - i + 1);
            assertTrue(binaryHeap.getMin() == i);
        }
    }

    @Test
    void insert_randomSequence() {
        int min =  Integer.MAX_VALUE;
        Random ran = new Random(10);
        for(int i = 0; i < 100; i++) {
            Integer test = ran.nextInt();
            min = Math.min(min, test);
            binaryHeap.insert(test);
            assertTrue(binaryHeap.getMin().equals(min));
        }
    }

    @Test
    void delMin_randomSequence() {
        final List<Integer> ints = new ArrayList<>();
        Random ran = new Random(10);
        for(int i = 0; i < 100; i++) {
            Integer test = ran.nextInt();
            ints.add(test);
            binaryHeap.insert(test);
        }
        System.out.println(binaryHeap);
        Collections.sort(ints);
        System.out.println(ints);
        ints.stream().forEach(
                i -> assertTrue(binaryHeap.delMin().equals(i))
        );
    }

    @Test
    void heapify_test1() {
        BinaryHeap<Integer> heap = BinaryHeap.heapify(Arrays.asList(9,6,2,5));
        System.out.println(heap.toString());
        assertTrue(heap.delMin() == 2);
        assertTrue(heap.delMin() == 5);
        assertTrue(heap.delMin() == 6);
        assertTrue(heap.delMin() == 9);
    }

    @Test
    void heapify_maxHeap() {
        List<Integer> ints = new ArrayList<>();
        Random ran = new Random();
        for(int i = 0; i < 10000; i++)
            ints.add(ran.nextInt());
        Comparator<Integer> max = (a, b) -> Integer.compare(b, a);
        BinaryHeap<Integer> heap = BinaryHeap.heapify(ints, max);
        Collections.sort(ints, max);
        for(int i : ints)
            assertTrue(heap.delMin() == i);
    }

    @Test
    void heapify_test2() {
        List<Integer> ints = new ArrayList<>();
        Random ran = new Random();
        for(int i = 0; i < 10000; i++)
            ints.add(ran.nextInt());
        BinaryHeap<Integer> heap = BinaryHeap.heapify(ints);
        System.out.println(heap);
        Collections.sort(ints);
        for(int i : ints)
            assertTrue(heap.delMin() == i);
    }
}