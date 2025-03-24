package poc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

// ----- Test Suite -----

public class FieldMapperTest {

    @Test
    void testCopyFromSourceAToDestination1() {
        Sources.SourceA src = new Sources.SourceA("Hello World");
        Destinations.Destination1 dst = new Destinations.Destination1();
        FieldMapper.map(Destinations.Destination1.class, src, dst);
        assertEquals("Hello World", dst.getDestValue(),
                "Destination1 should receive the value from SourceA");
    }

    @Test
    void testCopyFromSourceBToDestination2() {
        // Destination2 candidate order: SourceA and SourceB.
        // Provide a SourceB instance.
        Sources.SourceB src = new Sources.SourceB(42);
        Destinations.Destination2 dst = new Destinations.Destination2();
        FieldMapper.map(Destinations.Destination2.class, src, dst);
        // The copier should pick the SourceB candidate since src is an instance of SourceB.
        assertEquals(42, dst.getDestValue(),
                "Destination2 should receive the int value from SourceB");
    }

    @Test
    void testTypeIncompatibilityThrowsException() {
        // Destination3 expects an int but SourceA.value is a String.
        Sources.SourceA src = new Sources.SourceA("Not a number");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> FieldMapper.map(Destinations.Destination3.class, src, new Destinations.Destination3()));
        assertTrue(ex.getMessage().contains("Incompatible field types"),
                "Exception message should indicate incompatible field types");
    }

    @Test
    void testNoMatchingCandidate() {
        // Destination4 maps only from SourceA, but we supply SourceB.
        Sources.SourceB src = new Sources.SourceB(123);
        Destinations.Destination4 dst = new Destinations.Destination4();
        FieldMapper.map(Destinations.Destination4.class, src, dst);
        assertNull(dst.getDestValue(), "Destination4 should remain null if no candidate matches");
    }

    @Test
    void testMultipleAnnotatedFields() {
        // Local destination class with two annotated fields.
        class MultiDest {
            @FieldMapping(types = {Sources.SourceA.class}, fieldName = "value")
            private String destValueA;

            @FieldMapping(types = {Sources.SourceB.class}, fieldName = "value")
            private int destValueB;

            public String getDestValueA() { return destValueA; }
            public int getDestValueB() { return destValueB; }
        }

        Sources.SourceA srcA = new Sources.SourceA("MultiTest");
        Sources.SourceB srcB = new Sources.SourceB(7);
        MultiDest dst = new MultiDest();
        FieldMapper.map(MultiDest.class, srcA, dst);
        FieldMapper.map(MultiDest.class, srcB, dst);
        assertEquals("MultiTest", dst.getDestValueA(), "destValueA should be set from SourceA");
        assertEquals(7, dst.getDestValueB(), "destValueB should be set from SourceB");
    }

    @Test
    void testCopyFromSourceWithInnerToDestinationWithInner() {
        // Test the inner container mapping.
        Sources.InnerSourceA inner = new Sources.InnerSourceA("InnerHello");
        Sources.SourceAWithInner src = new Sources.SourceAWithInner(inner);
        Destinations.DestinationWithInner dst = new Destinations.DestinationWithInner();
        FieldMapper.map(Destinations.DestinationWithInner.class, src, dst);
        assertEquals("InnerHello", dst.getDestValue(),
                "DestinationWithInner should receive the value from the inner container");
    }
}