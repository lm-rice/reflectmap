package com.reflectmap.test;

import static com.reflectmap.mock.Destinations.*;
import static com.reflectmap.mock.Sources.*;
import static org.junit.jupiter.api.Assertions.*;

import com.reflectmap.ReflectMap;
import com.reflectmap.exception.FieldsNotFoundException;
import com.reflectmap.exception.IncompatibleFieldTypesException;
import org.junit.jupiter.api.Test;

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
        assertThrows(FieldsNotFoundException.class, () -> ReflectMap.map(src, SourceB.class, dst, Destination4.class));
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
        Source3Fields src = new Source3Fields("Hello1", "Hello2", "Hello3");
        Destination3Fields dst = new Destination3Fields();
        ReflectMap.map(src, Source3Fields.class, dst, Destination3Fields.class);
        assertEquals(src.getValue1(), dst.getDestValue1());
        assertEquals(src.getValue2(), dst.getDestValue2());
        assertEquals(src.getValue3(), dst.getDestValue3());
    }
}