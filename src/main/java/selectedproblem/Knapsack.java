package selectedproblem;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class Knapsack {
    private static class Item {
        private final Object i;
        private final double value;
        private final int weight;
        public Item(Object i, double value, int weight) {
            this.i = i;
            this.value = value;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "( " + i + ", v = " + value + ")";
        }
    }

    private final List<Item> items;

    public Knapsack() {
        items = new ArrayList<>();
    }

    public void addItem(Object content, double value, final int weight) {
        items.add(new Item(content, value, weight));
    }

    public List<Item> findOptimalSolution(final int weight) {
        // 2D array
        // y axis: i in [0,n-1] indicate sub problem containing item 0,1,2,3...i
        // x axis: w in [0,w  ] indicate sub problem having weight w
        final double[][] subProblemsOptimalValue = new double[items.size() + 1][];
        for(int i = 0; i <= items.size(); i++)
            subProblemsOptimalValue[i] = new double[weight + 1];

        for(int subProblemSize = 1; subProblemSize <= items.size(); subProblemSize ++)
            for( int subProblemWeight = 0; subProblemWeight <= weight; subProblemWeight ++) {
                int residualWeight = subProblemWeight - items.get(subProblemSize - 1).weight;
                if (residualWeight < 0) {
                    subProblemsOptimalValue[subProblemSize][subProblemWeight] =
                            subProblemsOptimalValue[subProblemSize - 1][subProblemWeight];
                } else {
                    subProblemsOptimalValue[subProblemSize][subProblemWeight] =
                            Math.max(
                                    subProblemsOptimalValue[subProblemSize - 1][subProblemWeight],
                                    subProblemsOptimalValue[subProblemSize - 1][residualWeight] + items.get(subProblemSize - 1).value
                            );
                }
            }

        // reconstruct result
        List<Item> result = new ArrayList<>();
        int currSize = items.size();
        int currWeight = weight;
        while(currSize > 0 && currWeight >= 0) {
            if(subProblemsOptimalValue[currSize][currWeight] != subProblemsOptimalValue[currSize - 1][currWeight]) {
                // it must be the case the item at current size has been added
                result.add(items.get(currSize - 1));
                currWeight = weight - items.get(currSize - 1).weight;
            }
            currSize--;
        }

        return result;
    }

    public static void main(String[] args) {
        Knapsack ks = new Knapsack();
        ks.addItem("a", 10, 4);
        ks.addItem("b",5 , 3);
        ks.addItem("c", 3, 2);
        ks.addItem("d", 2,1);
        System.out.println(ks.findOptimalSolution(9));
    }
}
