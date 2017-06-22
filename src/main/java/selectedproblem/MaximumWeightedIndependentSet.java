package selectedproblem;

import java.io.FileReader;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by rohan on 6/22/17.
 */
public class MaximumWeightedIndependentSet {
    /**
     * Compute Maximum Weighted Independent Set for a Path Graph
     *
     * Input file describes the weights of the vertices in a path graph
     * (with the weights listed in the order in which vertices appear in the path).
     * It has the following format:
     * <p>
     * [number_of_vertices (n)]
     * <p>
     * [weight of vertex #1]
     * <p>
     * [weight of vertex #2]
     * <p>
     * ...
     * [weight of vertex #n]
     *
     * @return a set containing item i iff vertex #i is the solution to given problem instance
     */
    public static Set<Integer> computeWISForPathGraph(String filePath) throws Exception{
        final Scanner in = new Scanner(new FileReader(filePath));

        final Integer symbolNum = Integer.valueOf(in.nextLine().trim());
        System.out.println("#symbol: " + symbolNum );

        // SOLUTION with Dynamic Programming
        final int[] input = new int[symbolNum + 1];
        /**
         * Array that hold result to solution of sub problem.
         * Concretely:
         * subProblems[i] = MaxWIS for set containing input[1], input[2]... input[i]
         * subProblem[0] = MaxWIS for empty set
         */
        final int[] subProblems = new int[symbolNum + 1];

        int i = 1;
        while (in.hasNextLine()) {
            Integer weight = Integer.valueOf(in.nextLine().trim());
            input[i++] = weight;
        }

        subProblems[0] = 0;
        subProblems[1] = input[1];
        for(int x = 2; x <= symbolNum; x++)
            subProblems[x] = Math.max(subProblems[x-1], subProblems[x-2] + input[x]);

        Set<Integer> result = new HashSet<>();
        // reconstruct
        int probSize = symbolNum;
        while(probSize >= 0) {
            if(subProblems[probSize] == subProblems[probSize - 1]) {
                probSize--;
            } else {
                result.add(probSize);
                probSize -= 2;
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        Set<Integer> res = computeWISForPathGraph("./testData/graph/maxWeightedIndependentSet.txt");
        IntStream.of(1, 2, 3, 4, 17, 117, 517, 997).forEach(
                i -> System.out.print(res.contains(i) ? "1" : "0")
        );
    }
}
