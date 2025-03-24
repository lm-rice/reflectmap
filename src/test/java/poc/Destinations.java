package poc;

import lombok.Getter;
import lombok.Setter;

public class Destinations {
    /**
     * A destination class with 25 fields.
     * Each field is annotated to map from the corresponding field in Source25.
     */
    @Setter
    @Getter
    public static class Destination25 {
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value1")
        private String destValue1;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value2")
        private String destValue2;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value3")
        private String destValue3;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value4")
        private String destValue4;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value5")
        private String destValue5;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value6")
        private String destValue6;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value7")
        private String destValue7;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value8")
        private String destValue8;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value9")
        private String destValue9;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value10")
        private String destValue10;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value11")
        private String destValue11;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value12")
        private String destValue12;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value13")
        private String destValue13;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value14")
        private String destValue14;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value15")
        private String destValue15;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value16")
        private String destValue16;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value17")
        private String destValue17;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value18")
        private String destValue18;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value19")
        private String destValue19;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value20")
        private String destValue20;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value21")
        private String destValue21;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value22")
        private String destValue22;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value23")
        private String destValue23;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value24")
        private String destValue24;
        @FieldMapping(types = {Sources.Source25.class}, fieldName = "value25")
        private String destValue25;
    }

    @Getter
    static
    class Destination1 {
        @FieldMapping(types = {Sources.SourceA.class}, fieldName = "value")
        private String destValue;
    }

    @Getter
    static
    class Destination2 {
        @FieldMapping(types = {Sources.SourceA.class, Sources.SourceB.class}, fieldName = "value")
        private Object destValue;
    }

    // This destination intentionally causes a type incompatibility: SourceA.value is a String.
    @Getter
    static
    class Destination3 {
        @FieldMapping(types = {Sources.SourceA.class}, fieldName = "value")
        private int destValue;
    }

    // Destination4 expects a SourceA candidate, but we will supply SourceB.
    @Getter
    static
    class Destination4 {
        @FieldMapping(types = {Sources.SourceA.class}, fieldName = "value")
        private String destValue;
    }

    @Getter
    @Setter
    static
    class DestinationWithInner {
        // Specify containerField to indicate that the actual value is held within innerA.
        @FieldMapping(types = {Sources.SourceAWithInner.class}, fieldName = "value", containerField = "innerA")
        private Object destValue;
    }
}
