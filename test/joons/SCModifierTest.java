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
import processing.core.PApplet;

/**
 *
 * @author sid
 */
public class SCModifierTest {
    SCModifier instance;
    public SCModifierTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new SCModifier(new PApplet(), "/home/sid/test.sc") ;
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of readSC method, of class SCModifier.
     */
    @Test
    public void testReadSC() {
        System.out.println("readSC");        
        instance.readSC();
    }

    /**
     * Test of lookForTheFirst method, of class SCModifier.
     */
    @Test
    public void testLookForTheFirst() {
        System.out.println("lookForTheFirst");
        instance.readSC();
        String[] keywords = {"name", "debug_caustics"};
        int expResult = 36;
        int result = instance.lookForTheFirst(keywords);
        assertEquals(expResult, result);
    }

    /**
     * Test of lookForTheFirstFrom method, of class SCModifier.
     */
    @Test
    public void testLookForTheFirstFrom() {
        System.out.println("lookForTheFirstFrom");
        instance.readSC();
        String[] keywords = {"type", "pinhole"};
        int indexFrom = 0;
        int expResult = 19;
        int result = instance.lookForTheFirstFrom(keywords, indexFrom);
        assertEquals(expResult, result);
    }

    /**
     * Test of lookForTheLast method, of class SCModifier.
     */
    @Test
    public void testLookForTheLast() {
        System.out.println("lookForTheLast");
        instance.readSC();
        String[] keywords = {"shader", "christmas"};
        int expResult = -1;
        int result = instance.lookForTheLast(keywords);
        assertEquals(expResult, result);
    }
}
