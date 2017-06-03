package sorting.mergeSort;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MergeSort {
    public static void sort(Integer[] array) {
        final Integer[] copied = Arrays.copyOf(array, array.length);
        sortRecOptimized(array, copied, 0, array.length, 0);
    }

    private static void sortRecCrude(Integer[] src, Integer[] des, int from, int until) {
        if (until - from <= 1) {
        } else {
            int mid = (until + from) / 2;
            sortRecCrude(src, des, from, mid);
            sortRecCrude(src, des, mid, until);
            merge(src, des, from, mid, until);
            System.arraycopy(des, from, src, from, until - from);
        }
    }

    private static void sortRecOptimized(Integer[] a, Integer[] b, int from, int until, final int level) {
        if (until - from <= 1) {
            // user insertion sort for better performance
        } else {
            int mid = (until + from) / 2;
            sortRecOptimized(b, a, from, mid, level + 1);
            sortRecOptimized(b, a, mid, until, level + 1);
            merge(b, a, from, mid, until);
        }
    }

    /**
     * Merge by coping element from src to des
     */
    private static void merge(final Integer[] src, final Integer[] des, final int from, final int middle, final int util) {
        int i = from;
        int j = middle;
        int k = from;
        while (k < util) {
            if (j == util || (i < middle && src[i] <= src[j])) {
                des[k++] = src[i++];
            } else {
                des[k++] = src[j++];
            }
        }
    }

    public static long countInversion(Integer[] array) {
        final Integer[] copied = Arrays.copyOf(array, array.length);
        return countSplitInversion(array, copied, 0, array.length, 0);
    }

    private static long countSplitInversion(Integer[] a, Integer[] b, int from, int until, final int level) {
        if (until - from <= 1) {
            return 0;
            // user insertion sort for better performance
        } else {
            int mid = (until + from) / 2;
            long left = countSplitInversion(b, a, from, mid, level + 1);
            long right = countSplitInversion(b, a, mid, until, level + 1);
            long splitInversion = mergeForSplitInversion(b, a, from, mid, until);
            return left + right + splitInversion;
        }
    }

    /**Merge from src to dest, return split inversion between moved array segments [from, middle) [middle, until)*/
    private static long mergeForSplitInversion(final Integer[] src, final Integer[] des, final int from, final int middle, final int util) {
        int i = from;
        int j = middle;
        int k = from;
        long inversionCount = 0;
        while (k < util) {
            if (j == util || (i < middle && src[i] <= src[j])) {
                des[k++] = src[i++];
            } else {
                des[k++] = src[j++];
                inversionCount += middle - i;
            }
        }
        return inversionCount;
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new FileReader("./testData/sort/IntegerArray100k.txt"));
        List<Integer> ints = new ArrayList<>();
        while(in.hasNext()) {
            ints.add(Integer.valueOf(in.next()));
        }
        Integer[] input = ints.toArray(new Integer[100000]);
        Integer[] b = Arrays.copyOf(input, input.length);
        Arrays.sort(b);
        System.out.println("Inversion count result: " + countInversion(input));
    }
}