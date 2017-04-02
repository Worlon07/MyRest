package com.company.my.rest;

import org.junit.Test;

import static org.junit.Assert.*;

public class PathConditionTest {

    @Test
    public void testGetPathConditionsOneCondition() {
        PathCondition[] conditions = PathCondition.getPathConditions("/{condition}");
        assertEquals(2, conditions.length);
        assertEquals("", conditions[0].getName());
        assertEquals(false, conditions[0].isCondition());
        assertEquals("condition", conditions[1].getName());
        assertEquals(true, conditions[1].isCondition());
    }

    @Test
    public void testGetPathConditionsMixedCondition() {
        PathCondition[] conditions = PathCondition.getPathConditions("/{condition1}/noCondition1/noCondition2/{condition2}");
        assertEquals(5, conditions.length);
        assertEquals("", conditions[0].getName());
        assertEquals(false, conditions[0].isCondition());
        assertEquals("condition1", conditions[1].getName());
        assertEquals(true, conditions[1].isCondition());
        assertEquals("noCondition1", conditions[2].getName());
        assertEquals(false, conditions[2].isCondition());
        assertEquals("noCondition2", conditions[3].getName());
        assertEquals(false, conditions[3].isCondition());
        assertEquals("condition2", conditions[4].getName());
        assertEquals(true, conditions[4].isCondition());
    }

    @Test
    public void testPathEqualsNoConditions() {
        PathCondition[] conditions = PathCondition.getPathConditions("/noCondition");
        assertTrue("Paths should be equals", PathCondition.pathEquals("/noCondition".split("/"), conditions));
        assertFalse("Paths should not be equals", PathCondition.pathEquals("/noCondition1".split("/"), conditions));
    }

    @Test
    public void testPathEqualsMixedConditions() {
        PathCondition[] conditions = PathCondition.getPathConditions("/{condition1}/noCondition1/noCondition2/{condition2}");
        assertTrue("Paths should be equals",
                PathCondition.pathEquals("/condition1/noCondition1/noCondition2/condition2".split("/"), conditions));
        assertFalse("Paths should not be equals",
                PathCondition.pathEquals("/condition1/noCondition2/noCondition1/condition2".split("/"), conditions));
    }

}
