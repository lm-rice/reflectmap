package com.reflectmap.benchmark;

import com.reflectmap.ReflectMap;
import com.reflectmap.mock.Sources.Source25;

import static com.reflectmap.mock.Destinations.*;

public class ReflectMapperBenchmark {
    private static final Source25 warmupSrc = new Source25("Warmup1", "Warmup2", "Warmup3", "Warmup4", "Warmup5",
            "Warmup6", "Warmup7", "Warmup8", "Warmup9", "Warmup10",
            "Warmup11", "Warmup12", "Warmup13", "Warmup14", "Warmup15",
            "Warmup16", "Warmup17", "Warmup18", "Warmup19", "Warmup20",
            "Warmup21", "Warmup22", "Warmup23", "Warmup24", "Warmup25");

    private static final Source25 primarySrc = new Source25("Hello1", "Hello2", "Hello3", "Hello4", "Hello5",
            "Hello6", "Hello7", "Hello8", "Hello9", "Hello10",
            "Hello11", "Hello12", "Hello13", "Hello14", "Hello15",
            "Hello16", "Hello17", "Hello18", "Hello19", "Hello20",
            "Hello21", "Hello22", "Hello23", "Hello24", "Hello25");

    private static final Destination25 dst = new Destination25();

    public static void main(String[] args) {
        // Warm-up: initialize the copy plan for Destination25 so that the reflective analysis is done only once.
        Destination25 warmupDst = new Destination25();
        ReflectMap.map(warmupSrc, Source25.class, warmupDst, Destination25.class);

        if (!"Warmup1".equals(warmupDst.getDestValue1()) || !"Warmup25".equals(warmupDst.getDestValue25())) {
            throw new RuntimeException("Benchmark failed: Destination25 did not receive correct values.");
        }

        int iterations = 50_000_000;
        iterate(iterations);

        for (int i = 0; i < 500; i++) {
            iterate(50);
        }
    }

    private static void iterate(int iterations) {
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            ReflectMap.map(primarySrc, Source25.class, dst, Destination25.class);
        }
        long endTime = System.nanoTime();
        long timeTakenNanos = (endTime - startTime);
        System.out.println("Performed " + iterations + " allocations in " + timeTakenNanos + " ns");
    }
}
