package com.labs.dm.jkvdb;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author daniel
 */
public class UtilsTest {
    
    
    @Test
    public void testMain() throws Exception {
        int pid = Utils.pid();
        assertTrue(pid >= 1024);
        assertTrue(pid <= 65536);
    }
    
}