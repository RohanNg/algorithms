package selectedproblem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static selectedproblem.Knapsack.solveKnapsackProblem;

/**
 * Created by rohan on 6/22/17.
 */
class KnapsackTest {
    @Test
    void solveKnapsackProblem_test1() throws Exception{
        assertTrue(solveKnapsackProblem("./testData/graph/knapsack_big.txt") == 4243395.0);
    }

    @Test
    void solveKnapsackProblem_test2() throws Exception {
        System.out.println(solveKnapsackProblem("./testData/graph/knapsack_small.txt") == 2493893.0);
    }

}