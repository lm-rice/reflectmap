package com.reflectmap.benchmark.runner;

import com.reflectmap.ReflectMap;
import com.reflectmap.mock.Destination25;
import com.reflectmap.mock.Source25;

public class ReflectMapFlameGraphRunner {
    public static void main(String[] args) {
        var src = new Source25("Hello1", "Hello2", "Hello3", "Hello4", "Hello5",
                "Hello6", "Hello7", "Hello8", "Hello9", "Hello10",
                "Hello11", "Hello12", "Hello13", "Hello14", "Hello15",
                "Hello16", "Hello17", "Hello18", "Hello19", "Hello20",
                "Hello21", "Hello22", "Hello23", "Hello24", "Hello25");
        var dst = new Destination25();

        // Warm up JVM a bit
        for (int i = 0; i < 10_000; i++) {
            ReflectMap.map(src, Source25.class, dst, Destination25.class);
        }

        // 🔥 Profile starts here
        for (int i = 0; i < 1_000_000; i++) {
            ReflectMap.map(src, Source25.class, dst, Destination25.class);
        }
    }
}