package poc;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Sources {
    /**
     * A source class with 25 fields.
     */
    @AllArgsConstructor
    @Getter
    public static class Source25 {
        private final String value1;
        private final String value2;
        private final String value3;
        private final String value4;
        private final String value5;
        private final String value6;
        private final String value7;
        private final String value8;
        private final String value9;
        private final String value10;
        private final String value11;
        private final String value12;
        private final String value13;
        private final String value14;
        private final String value15;
        private final String value16;
        private final String value17;
        private final String value18;
        private final String value19;
        private final String value20;
        private final String value21;
        private final String value22;
        private final String value23;
        private final String value24;
        private final String value25;
    }

    @AllArgsConstructor
    @Getter
    static
    class InnerSourceA {
        // Field is non-final to allow reflective access.
        Object value;
    }

    @AllArgsConstructor
    @Getter
    static
    class SourceA {
        private final String value;
    }

    // ----- Source and Destination classes for inner container mapping -----
    @AllArgsConstructor
    @Getter
    static
    class SourceAWithInner {
        private final InnerSourceA innerA;
    }

    @AllArgsConstructor
    @Getter
    static
    class SourceB {
        private final int value;
    }
}
