import com.google.common.collect.Lists;
import queues.BinaryHeap;

import java.io.FileReader;
import java.util.*;

/**
 * Created by rohan on 6/10/17.
 */
public class Discrete {

    /**
     * Compute median such that
     * <pre>
     *      For all index i in range [1, input.size) read from the input
     *      output[i] = median of the numbers: input[0], input[1] ... input[i]
     *  </pre>
     */
    public static List<Integer> medianMaintenance(List<Integer> input) {
        BinaryHeap<Integer> low = new BinaryHeap<>((i1, i2) -> Integer.compare(i1, i2)*-1);
        BinaryHeap<Integer> high = new BinaryHeap<>();
        // invariant: |low.size - high.size| <= 1
        if(input.isEmpty()) return Collections.emptyList();
        if(input.size() == 1) return Lists.newArrayList(input.get(0));
        List<Integer> output = Lists.newArrayList(input.get(0), Math.min(input.get(0), input.get(1)));
        if(input.size() == 2) return output;
        Iterator<Integer> iterator = input.iterator();
        int a = iterator.next();
        int b = iterator.next();
        if(b > a) {
            low.insert(a);
            high.insert(b);
        } else {
            low.insert(b);
            high.insert(a);
        }
        while(iterator.hasNext()) {
            final int i = iterator.next();
            if(i < low.getMin()) {
                if(low.size() > high.size()) high.insert(low.delMin());
                low.insert(i);
            } else if ( i > high.getMin()) {
                if(high.size() > low.size()) low.insert(high.delMin());
                high.insert(i);
            } else {
                if(low.size() <= high.size()) low.insert(i);
                else high.insert(i);
            }
            // extract
            if( high.size() > low.size()) output.add(high.getMin());
            else output.add(low.getMin());
            assert Math.abs(low.size() - high.size()) <= 1;
            assert low.getMin() <= high.getMin();
        }
        return output;
    }

    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(new FileReader("./testData/graph/MedianMaintenance.txt"));
        List<Integer> input = new ArrayList<>();
        while (in.hasNext())
            input.add(Integer.valueOf(in.next()));

        List<Integer> output = medianMaintenance(input);
        long sum = 0;
        for(Integer i : output) sum += i;
        System.out.println(sum);
    }
}
