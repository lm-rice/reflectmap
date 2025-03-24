package com.reflectmap.mock;

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
    public static class InnerSourceA {
        private final Object value;
    }

    @AllArgsConstructor
    @Getter
    public static class SourceA {
        private final String value;
    }

    @AllArgsConstructor
    @Getter
    public static class SourceAWithInner {
        private final InnerSourceA innerA;
    }

    @AllArgsConstructor
    @Getter
    public static class SourceB {
        private final int value;
    }
}
