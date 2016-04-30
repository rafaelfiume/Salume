package com.rafaelfiume.db.plugin;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static support.Decorators.version;

public class VersionTest {

    @Test
    public void returnsTrueIfaVersionIs_GreaterThan_Another() {
        assertTrue(version("i91", "02").isGreaterThan(version("i02", "05")));
        assertTrue(version("i91", "02").isGreaterThan(version("i91", "01")));
    }

    @Test
    public void returnsFalseIfaVersionIsNot_GreaterThan_Another() {
        assertFalse(version("i91", "02").isGreaterThan(version("i91", "02")));
        assertFalse(version("i55", "02").isGreaterThan(version("i91", "01")));
        assertFalse(version("i55", "02").isGreaterThan(version("i91", "07")));
    }

    @Test
    public void returnsTrueIfaVersionIs_LesserThanOrEqualTo_Another() {
        assertTrue(version("i91", "02").isLesserThanOrEqualTo(version("i91", "02")));
        assertTrue(version("i55", "02").isLesserThanOrEqualTo(version("i91", "01")));
        assertTrue(version("i55", "02").isLesserThanOrEqualTo(version("i91", "07")));
    }

    @Test
    public void returnsFalseIfaVersionIs_LesserThanOrEqualTo_Another() {
        assertFalse(version("i91", "02").isLesserThanOrEqualTo(version("i02", "05")));
        assertFalse(version("i91", "02").isLesserThanOrEqualTo(version("i91", "01")));
    }
}