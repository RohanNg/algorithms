import org.junit.jupiter.api.Test;
import selectedproblem.Discrete;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Created by rohan on 6/10/17.
 */
class DiscreteTest {
    @Test
    void medianMaintenance_test1() {
        assertEquals(Arrays.asList(7,3,3,3,3,3), Discrete.medianMaintenance(Arrays.asList(7,3,1,6,2,8)));
        assertEquals(Arrays.asList(1,1,2,2,3,3,4,4,5), Discrete.medianMaintenance(Arrays.asList(1,2,3,4,5,6,7,8,9)));
        assertEquals(Arrays.asList(8,2,5,2,5,4,5,5,5), Discrete.medianMaintenance(Arrays.asList(8,2,5,1,9,4,6,7,3)));
    }

}