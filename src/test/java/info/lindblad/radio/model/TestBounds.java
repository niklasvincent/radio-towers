package info.lindblad.radio.model;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestBounds extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestBounds(String testName) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestBounds.class);
    }

    /**
     * Test that two points, one inside and one outside the bounds
     * are treated correctly.
     */
    public void testContains() {
        Bounds bounds = new Bounds(10, 10);
        Point insidePoint = new Point(5, 5);
        Point outsidePoint = new Point(10, 10);
        assertTrue(bounds.contains(insidePoint));
        assertFalse(bounds.contains(outsidePoint));
    }

    /**
     * Test that providing a negative X size results in an exception
     */
    public void testIllegalXSize() {
        try {
            new Bounds(-1, 1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }
    }

    /**
     * Test that providing a negative Y size results in an exception
     */
    public void testIllegalYSize() {
        try {
            new Bounds(1, -1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        }
    }

}
