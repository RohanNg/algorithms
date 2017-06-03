package sorting.mergeSort;

import org.junit.jupiter.api.Test;
import scala.Int;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static sorting.mergeSort.MergeSort.*;

/**
 * Created by rohan on 6/3/17.
 */
class MergeSortTest {
    @Test
    void sort() {

    }

    @Test
    void countInversion_emptyInput() {
        Integer[] in = new Integer[0];
        assertTrue(in.length == 0);
        assertTrue(countInversion(new Integer[0]) == 0);
    }

    @Test
    void countInversion_inputSize_1() {
        Integer[] in = {1};
        assertTrue(countInversion(in) == 0);
    }

    @Test
    void countInversion_inputSize_2() {
        Integer[] in = {1,2};
        assertTrue(countInversion(in) == 0);

        Integer[] in2 = {2, 1};
        assertTrue(countInversion(in2) == 1);
    }

    @Test
    void countInversion_inputSizeMany_0inversion() {
        Integer[] in = {1,2,3,4,5,6,7,8,9,10,11};
        assertTrue(countInversion(in) == 0);
    }

    @Test
    void countInversion_inputSizeMany_maxInversion() {
        Integer[] in = {10,9,8,7,6,5,4,3,2,1,0};
        assertTrue(countInversion(in) == (in.length*(in.length - 1)/2));
    }

    @Test
    void countInversion_inputSizeMany_manyInversion() {
        Integer[] in = {10,9,7,8,6,4,5,3,2,1,0};
        assertTrue(countInversion(in) == (in.length*(in.length - 1)/2) - 2);
    }
}