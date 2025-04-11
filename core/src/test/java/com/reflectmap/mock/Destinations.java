package com.reflectmap.mock;

import com.reflectmap.annotation.FieldMapping;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.reflectmap.mock.Sources.*;

public class Destinations {
    /**
     * A destination class with 25 fields.
     * Each field is annotated to map from the corresponding field in Source25.
     */
    @Setter
    @Getter
    public static class Destination3Fields {
        @FieldMapping(srcType = Source3Fields.class, srcFieldName = "value1")
        private String destValue1;
        @FieldMapping(srcType = Source3Fields.class, srcFieldName = "value2")
        private String destValue2;
        @FieldMapping(srcType = Source3Fields.class, srcFieldName = "value3")
        private String destValue3;
    }

    @Getter
    public static class Destination1 {
        @FieldMapping(srcType = SourceA.class, srcFieldName = "value")
        private String destValue;
    }

    @Getter
    public static class Destination2 {
        @FieldMapping(srcType = SourceB.class, srcFieldName = "value")
        private Object destValue;
    }

    // This destination intentionally causes a type incompatibility: SourceA.value is a String.
    @Getter
    public static class Destination3 {
        @FieldMapping(srcType = SourceA.class, srcFieldName = "value")
        private int destValue;
    }

    // Destination4 expects a SourceA candidate, but we will supply SourceB.
    @Getter
    public static class Destination4 {
        @FieldMapping(srcType = SourceA.class, srcFieldName = "value")
        private String destValue;
    }

    @Getter
    @Setter
    public static class DestinationWithInner {
        @FieldMapping(srcType = SourceAWithInner.class, srcFieldName = "innerA.value")
        private Object destValue;
    }

    @Getter
    @Setter
    public static class DestinationWithMultipleSources {
        @FieldMapping(srcType = SourceA.class, srcFieldName = "value")
        private String destValueA;

        @FieldMapping(srcType = SourceB.class, srcFieldName = "value")
        private int destValueB;

        public String getDestValueA() { return destValueA; }
        public int getDestValueB() { return destValueB; }
    }
}
