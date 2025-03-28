package com.reflectmap.mock;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Sources {
    /**
     * A source class with 25 fields.
     */
    @AllArgsConstructor
    @Getter
    public static class Source3Fields {
        private final String value1;
        private final String value2;
        private final String value3;
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
