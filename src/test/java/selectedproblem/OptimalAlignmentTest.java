package selectedproblem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by rohan on 6/16/17.
 */
class OptimalAlignmentTest {


    private void test(String a, String b, String expectA, String expectB) {
        OptimalAlignment.Tuple<String> tup = OptimalAlignment.getOptimalAlignment(a,b,1,1);
        assertTrue(tup.getE1().equals(expectA), "Expect" + tup.getE1() + " != " + expectA);
        assertTrue(tup.getE2().equals(expectB), "Expect" + tup.getE2() + " != " + expectB);
    }

    @Test
    void testOptimalAlignment_exactSequence() {
        test("qwerty", "qwerty","qwerty", "qwerty");
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedAsFirst() {
        test("123456789", "abc123456789",
                "___123456789",
                "abc123456789");
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedAsLast() {
        test("123456789", "123456789abc",
                "123456789___",
                "123456789abc");
    }

    @Test
    void testOptimalAlignment_gapInOneSequence_gapAddedInBetween() {
        test("123456789", "123a4b56c78d9",
                "123_4_56_78_9",
                "123a4b56c78d9");
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
    void testOptimalAlignment_gapInTwoSequence_gapAddedInBetween() {
        test("abc13579", "ac123456789",
                "abc1_3_5_7_9",
                "a_c123456789");
    }

    @Test
    void testOptimalAlignment_gapInTwoSequence_gapAddedInBetweenAndFirst() {
        test("aaaabc13579", "bc123456789",
                "aaaabc1_3_5_7_9",
                "____bc123456789");
    }

    @Test
    void testOptimalAlignment_gapInTwoSequence_gapAddedInBetweenAndLast() {
        test("abc13579xy", "abc123456789xyz",
                "abc1_3_5_7_9xy_",
                "abc123456789xyz");
    }

    @Test
    void testOptimalAlignment_gapInTwoSequence_gapAddedInBetweenAndLastAndFirst() {
        test("aaaabc13579xy", "bc123456789xyzt",
                "aaaabc1_3_5_7_9xy__",
                "____bc123456789xyzt");
    }
}