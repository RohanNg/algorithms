package selectedproblem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by rohan on 6/16/17.
 */
class OptimalAlignmentTest {

    @Test
    void testOptimalAlignment_exactSequence() {
        OptimalAlignment.Tuple<String> tup = OptimalAlignment.getOptimalAlignment("abc","abc",1,1);
        assertTrue(tup.getE1().equals("abc"));
        assertTrue(tup.getE2().equals("abc"));
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedAsFirst() {
        test("123456789", "abc123456789",
                "___123456789",
                "abc123456789");
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedAsLast() {
        OptimalAlignment.Tuple<String> tup = OptimalAlignment.getOptimalAlignment("123456789","123456789abc",1,1);
        assertTrue(tup.getE1().equals("123456789___"));
        assertTrue(tup.getE2().equals("123456789abc"));
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedInBetween() {
        OptimalAlignment.Tuple<String> tup = OptimalAlignment.getOptimalAlignment("123456789","123a4b56c78d9",1,1);
        assertTrue(tup.getE1().equals("123_4_56_78_9"));
        assertTrue(tup.getE2().equals("123a4b56c78d9"));
    }

    private void test(String a, String b, String expectA, String expectB) {
        OptimalAlignment.Tuple<String> tup = OptimalAlignment.getOptimalAlignment(a,b,1,1);
        assertTrue(tup.getE1().equals(expectA), tup.getE1() + " != " + expectA);
        assertTrue(tup.getE2().equals(expectB), tup.getE2() + " != " + expectB);
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedInBetweenAndLast() {
        test("123456789", "1a23b45cd6e7f8gg9h",
                "1_23_45__6_7_8__9_",
                "1a23b45cd6e7f8gg9h");

        test("123456789", "1a23b45cd6e7f8gg9hhhh",
                "1_23_45__6_7_8__9____",
                "1a23b45cd6e7f8gg9hhhh");
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedInBetweenAndFirst() {
        test("123456789", "xxx1a23b45cd6e7f8gg9",
                "___1_23_45__6_7_8__9",
                "xxx1a23b45cd6e7f8gg9");
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedInBetweenAndFirstAndLast() {
        test("123456789", "xxx1a23b45cd6e7f8gg9yyy",
                "___1_23_45__6_7_8__9___",
                "xxx1a23b45cd6e7f8gg9yyy");
    }

    @Test
    void testOptimalAlignment_gapInTwoSequence_gapAddedInBetweenAndFirstAndLast() {
        test("abc13579", "ac123456789",
                "abc1_3_5_7_9",
                "a_c123456789");
    }
}