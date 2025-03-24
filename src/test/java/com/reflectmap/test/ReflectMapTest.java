package com.reflectmap.test;

import static com.reflectmap.mock.Destinations.*;
import static com.reflectmap.mock.Sources.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reflectmap.ReflectMap;
import com.reflectmap.exception.IncompatibleFieldTypesException;
import org.junit.jupiter.api.Test;

// ----- Test Suite -----

public class ReflectMapTest {

    @Test
    void testCopyFromSourceAToDestination1() {
        SourceA src = new SourceA("Hello World");
        Destination1 dst = new Destination1();

        ReflectMap.map(src, SourceA.class, dst, Destination1.class);
        assertEquals("Hello World", dst.getDestValue());
    }

    @Test
    void testCopyFromSourceBToDestination2() {
        SourceB src = new SourceB(42);
        Destination2 dst = new Destination2();

        ReflectMap.map(src, SourceB.class, dst, Destination2.class);
        assertEquals(42, dst.getDestValue());
    }

    @Test
    void testTypeIncompatibilityThrowsException() {
        SourceA src = new SourceA("Not a number");
        Destination3 dst = new Destination3();

        assertThrows(IncompatibleFieldTypesException.class, () -> ReflectMap.map(src, SourceA.class, dst, Destination3.class));
    }

    @Test
    void testNoMatchingCandidate() {
        SourceB src = new SourceB(123);
        Destination4 dst = new Destination4();

        ReflectMap.map(src, SourceB.class, dst, Destination4.class);
        assertNull(dst.getDestValue(), "Destination4 should remain null if no candidate matches");
    }

    @Test
    void testMultipleAnnotatedFields() {
        SourceA srcA = new SourceA("MultiTest");
        SourceB srcB = new SourceB(7);
        DestinationWithMultipleSources dst = new DestinationWithMultipleSources();

        ReflectMap.map(srcA, SourceA.class, dst, DestinationWithMultipleSources.class);
        ReflectMap.map(srcB, SourceB.class, dst, DestinationWithMultipleSources.class);

        assertEquals(srcA.getValue(), dst.getDestValueA(), "destValueA should be set from SourceA");
        assertEquals(srcB.getValue(), dst.getDestValueB(), "destValueB should be set from SourceB");
    }

    @Test
    void testCopyFromSourceWithInnerToDestinationWithInner() {
        // Test the inner container mapping.
        InnerSourceA innerSrc = new InnerSourceA("InnerHello");
        SourceAWithInner src = new SourceAWithInner(innerSrc);
        DestinationWithInner dst = new DestinationWithInner();

        ReflectMap.map(src, SourceAWithInner.class, dst, DestinationWithInner.class);
        assertEquals(innerSrc.getValue(), dst.getDestValue(),
                "DestinationWithInner should receive the value from the inner container");
    }

    @Test
    void testCopyMultipleSourceFieldsToDestination() {
        Source25 src = new Source25("Hello1", "Hello2", "Hello3", "Hello4", "Hello5",
                "Hello6", "Hello7", "Hello8", "Hello9", "Hello10",
                "Hello11", "Hello12", "Hello13", "Hello14", "Hello15",
                "Hello16", "Hello17", "Hello18", "Hello19", "Hello20",
                "Hello21", "Hello22", "Hello23", "Hello24", "Hello25");
        Destination25 dst = new Destination25();
        ReflectMap.map(src, Source25.class, dst, Destination25.class);
        assertEquals(src.getValue1(), dst.getDestValue1());
        assertEquals(src.getValue2(), dst.getDestValue2());
        assertEquals(src.getValue3(), dst.getDestValue3());
        assertEquals(src.getValue4(), dst.getDestValue4());
        assertEquals(src.getValue5(), dst.getDestValue5());
        assertEquals(src.getValue6(), dst.getDestValue6());
        assertEquals(src.getValue7(), dst.getDestValue7());
        assertEquals(src.getValue8(), dst.getDestValue8());
        assertEquals(src.getValue9(), dst.getDestValue9());
        assertEquals(src.getValue10(), dst.getDestValue10());
        assertEquals(src.getValue11(), dst.getDestValue11());
        assertEquals(src.getValue12(), dst.getDestValue12());
        assertEquals(src.getValue13(), dst.getDestValue13());
        assertEquals(src.getValue14(), dst.getDestValue14());
        assertEquals(src.getValue15(), dst.getDestValue15());
        assertEquals(src.getValue16(), dst.getDestValue16());
        assertEquals(src.getValue17(), dst.getDestValue17());
        assertEquals(src.getValue18(), dst.getDestValue18());
        assertEquals(src.getValue19(), dst.getDestValue19());
        assertEquals(src.getValue20(), dst.getDestValue20());
        assertEquals(src.getValue21(), dst.getDestValue21());
        assertEquals(src.getValue22(), dst.getDestValue22());
        assertEquals(src.getValue23(), dst.getDestValue23());
        assertEquals(src.getValue24(), dst.getDestValue24());
        assertEquals(src.getValue25(), dst.getDestValue25());
    }
}