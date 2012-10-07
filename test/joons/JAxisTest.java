/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package joons;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Joon Hyub Lee
 */
public class JAxisTest {
    
    public JAxisTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of rotateAxis method, of class JAxis.
     */
    @Test
    public void testRotateAxis() {
        System.out.println("rotateAxis");
        JVector u = null;
        float th = 0.0F;
        JAxis instance = new JAxis();
        instance.rotateAxis(u, th);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rotateX method, of class JAxis.
     */
    @Test
    public void testRotateX() {
        System.out.println("rotateX");
        float th = 0.0F;
        JAxis instance = new JAxis();
        instance.rotateX(th);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rotateY method, of class JAxis.
     */
    @Test
    public void testRotateY() {
        System.out.println("rotateY");
        float th = 0.0F;
        JAxis instance = new JAxis();
        instance.rotateY(th);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rotateZ method, of class JAxis.
     */
    @Test
    public void testRotateZ() {
        System.out.println("rotateZ");
        float th = 0.0F;
        JAxis instance = new JAxis();
        instance.rotateZ(th);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getXAxis method, of class JAxis.
     */
    @Test
    public void testGetXAxis() {
        System.out.println("getXAxis");
        JAxis instance = new JAxis();
        JVector expResult = null;
        JVector result = instance.getXAxis();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getYAxis method, of class JAxis.
     */
    @Test
    public void testGetYAxis() {
        System.out.println("getYAxis");
        JAxis instance = new JAxis();
        JVector expResult = null;
        JVector result = instance.getYAxis();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getZAxis method, of class JAxis.
     */
    @Test
    public void testGetZAxis() {
        System.out.println("getZAxis");
        JAxis instance = new JAxis();
        JVector expResult = null;
        JVector result = instance.getZAxis();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
