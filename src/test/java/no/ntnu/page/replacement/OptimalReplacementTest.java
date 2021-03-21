package no.ntnu.page.replacement;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Optimal page replacement algorithm
 * @author Girts Strazdins, 2016-03-11 (only a template)
 */
public class OptimalReplacementTest {
    /**
     * Test of process method, of class OptimalReplacement.
     */
    @Test
    public void testProcess() {
        System.out.println("Optimal Replacement process test");
        OptimalReplacement algo = new OptimalReplacement();

        // Use the book example with 3 frames and the given reference string
        algo.setup(3);
        String ref = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
        int replacements = algo.process(ref);
        String frameStatus = algo.getFrameStatus();
        assertEquals(6, replacements);
        assertEquals("7, 0, 1", frameStatus);


        // Tests cases where input string is shorter than or
        // equal to number of frames

        // empty string
        replacements = algo.process("");
        frameStatus = algo.getFrameStatus();
        assertEquals(0, replacements);
        assertEquals("7, 0, 1", frameStatus);

        // one value
        replacements = algo.process("4");
        frameStatus = algo.getFrameStatus();
        assertEquals(1, replacements);
        assertEquals("4, 0, 1", frameStatus);

        // two values
        replacements = algo.process("1,2");
        frameStatus = algo.getFrameStatus();
        assertEquals(1, replacements);
        assertEquals("2, 0, 1", frameStatus);

        // three values, same size as number of frames
        replacements = algo.process("1,4,3");
        frameStatus = algo.getFrameStatus();
        assertEquals(2, replacements);
        assertEquals("3, 0, 1", frameStatus);

        // Test when all numbers in input string are loaded,
        //  and no replacements should be made
        replacements = algo.process("0,1,3");
        frameStatus = algo.getFrameStatus();
        assertEquals(0, replacements);
        assertEquals("3, 0, 1", frameStatus);
        // TODO - add additional Unit tests here

        //todo test with multi digit page number?
        //todo test with
    }
   
}
