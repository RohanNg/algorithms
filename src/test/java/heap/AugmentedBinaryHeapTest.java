package heap;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rohan on 6/19/17.
 */
class AugmentedBinaryHeapTest {

    private static class AugmentedInt extends AugmentedBinaryHeap.Element {
        private final int value;

        public AugmentedInt(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AugmentedInt that = (AugmentedInt) o;

            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("{");
            sb.append(value);
            sb.append('}');
            return sb.toString();
        }
    }

    @Test
    void delAt() {
        AugmentedBinaryHeap<AugmentedInt> abh = new AugmentedBinaryHeap<>(Comparator.comparingInt(i -> i.getValue()));
        Random ran = new Random();

        for (int e = 0; e <= 1000; e++) {
            List<AugmentedInt> ints = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                AugmentedInt num = new AugmentedInt(ran.nextInt());
                ints.add(num);
                abh.insert(num);
            }
            System.out.println(Arrays.toString(ints.stream().mapToInt(i -> i.getValue()).toArray()));
            for (AugmentedInt i : ints) {
                assertTrue(i.equals(abh.delAt(i.getPosition())));
            }
            assertTrue(abh.isEmpty());

        }

    }

    @Test
    void delAt_test2() {
        AugmentedBinaryHeap<AugmentedInt> abh = new AugmentedBinaryHeap<>(Comparator.comparingInt(i -> i.getValue()));

        List<AugmentedInt> ints = Arrays.asList(9, 4, 3, 8, 7, 6).stream().map(i -> new AugmentedInt(i)).collect(Collectors.toList());
        for(AugmentedInt num : ints)
            abh.insert(num);

        for (AugmentedInt i : ints)
            assertTrue(i.equals(abh.delAt(i.getPosition())));

        System.out.println(Arrays.toString(ints.stream().mapToInt(i -> i.getValue()).toArray()));
        assertTrue(abh.isEmpty());
    }

}