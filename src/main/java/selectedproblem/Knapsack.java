package selectedproblem;

import java.io.FileReader;
import java.util.*;

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

    private double findOptimalSolutionSlow(final int weight) {
        // 2D array
        // y axis: i in [0,n ] indicate sub problem containing item 0,1,2,3...i in collection of n item
        // x axis: w in [0,w  ] indicate sub problem having weight w
        final double[][] subProblemsOptimalValue = new double[items.size() + 1][];
        for (int i = 0; i <= items.size(); i++)
            subProblemsOptimalValue[i] = new double[weight + 1];

        for (int subProblemSize = 1; subProblemSize <= items.size(); subProblemSize++) {
            for (int subProblemWeight = 0; subProblemWeight <= weight; subProblemWeight++) {
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
        }
        //        int currSize = items.size();
        //        int currWeight = weight;
        // reconstruct result
//        List<Item> result = new ArrayList<>();
//        while(currSize > 0 && currWeight >= 0) {
//            if(subProblemsOptimalValue[currSize][currWeight] != subProblemsOptimalValue[currSize - 1][currWeight]) {
//                // it must be the case the item at current size has been added
//                result.add(items.get(currSize - 1));
//                currWeight = weight - items.get(currSize - 1).weight;
//            }
//            currSize--;
//        }

        return subProblemsOptimalValue[items.size()][weight];
    }

    private double findOptimalSolutionSlick(final int weight) {
        // solutions to sub problem containing i - 1 items, with weight [0, weight]
        double[] subProblemsA = new double[weight + 1];
        // solution to sub problem containing i - 1 items, with weight [0, weight]
        double[] subProblemsB = new double[weight + 1];

        // only compute knapsack(i, w) where w > skip[i]
        final int[] skip = new int[items.size() + 1];

        // process big weight first to maximize ignorable sub problem
        Collections.sort(items, (i1, i2) -> Integer.compare(i2.weight, i1.weight));

        // building skip[]
        ListIterator<Item> lt = items.listIterator(items.size());
        int currWeight = weight - 1;
        skip[items.size()] = weight - 1;
        int i = items.size() - 1;
        while (lt.hasPrevious()) {
            currWeight -= lt.previous().weight;
            skip[i--] = Math.max(-1, currWeight);
        }
        System.out.println("Skip array: ");
        System.out.println(Arrays.toString(skip));

        for (int a = 0; a < items.size(); a++) {
            Item item = items.get(a);
            for (int subProblemWeight = skip[a] + 1; subProblemWeight <= weight; subProblemWeight++) {
                int residualWeight = subProblemWeight - item.weight;
                if (residualWeight < 0) {
                    subProblemsB[subProblemWeight] = subProblemsA[subProblemWeight];
                } else {
                    subProblemsB[subProblemWeight] =
                            Math.max(
                                    subProblemsA[subProblemWeight],
                                    subProblemsA[residualWeight] + item.value
                            );
                }
            }
            double[] swapper = subProblemsA;
            subProblemsA = subProblemsB;
            subProblemsB = swapper;
        }
        return subProblemsA[weight];
    }

    /**
     * Compute maximized value as solution to 0-1 knapsack problem.
     *
     * File describes a knapsack instance must have the following format:
     * <p>
     * [knapsack_size][number_of_items]
     * <p>
     * [value_1] [weight_1]
     * <p>
     * [value_2] [weight_2]
     *
     * @param filePath file to Knapsack problem instance
     * @return maximized value, solution to 0-1 knapsack problem
     * @throws Exception if given invalid file path
     */
    public static double solveKnapsackProblem(String filePath) throws Exception {
        Scanner in = new Scanner(new FileReader(filePath));

        Knapsack ks = new Knapsack();

        String[] info = in.nextLine().trim().split("\\s+");
        final int knapsackSize = Integer.valueOf(info[0]);
        final int numberOfItem = Integer.valueOf(info[1]);
        System.out.println("knapsack capacity: " + knapsackSize + ", #items: " + numberOfItem);
        int i = 1;
        while (in.hasNextLine()) {
            String[] item = in.nextLine().trim().split("\\s+");
            ks.addItem(i++, Integer.valueOf(item[0]), Integer.valueOf(item[1]));
        }
        return (ks.findOptimalSolutionSlick(knapsackSize));
    }

    public static void main(String[] args) throws Exception {
        double res = solveKnapsackProblem("./testData/graph/knapsack_big.txt");
        System.out.println(res);
    }
}
