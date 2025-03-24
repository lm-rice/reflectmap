package poc;


/**
 * A source class with 25 fields.
 */
public class FieldMapperBenchmarkVerbose {
    private static final Sources.Source25 warmupSrc = new Sources.Source25("Warmup1", "Warmup2", "Warmup3", "Warmup4", "Warmup5",
            "Warmup6", "Warmup7", "Warmup8", "Warmup9", "Warmup10",
            "Warmup11", "Warmup12", "Warmup13", "Warmup14", "Warmup15",
            "Warmup16", "Warmup17", "Warmup18", "Warmup19", "Warmup20",
            "Warmup21", "Warmup22", "Warmup23", "Warmup24", "Warmup25");

    private static final Sources.Source25 primarySrc = new Sources.Source25("Hello1", "Hello2", "Hello3", "Hello4", "Hello5",
            "Hello6", "Hello7", "Hello8", "Hello9", "Hello10",
            "Hello11", "Hello12", "Hello13", "Hello14", "Hello15",
            "Hello16", "Hello17", "Hello18", "Hello19", "Hello20",
            "Hello21", "Hello22", "Hello23", "Hello24", "Hello25");

    public static void main(String[] args) {
        // Warm-up: initialize the copy plan for Destination25 so that the reflective analysis is done only once.
        Destinations.Destination25 warmupDst = new Destinations.Destination25();

        FieldMapper.map(Destinations.Destination25.class, warmupSrc, warmupDst);

        int iterations = 10_000_000;
        int repeats = 5;

        for (int i = 0; i < repeats; i++) {
            runBenchmarkForIterations(iterations);
        }

//        for (int i = 0; i < iterations; i++) {
//            Destinations.Destination25 dst = new Destinations.Destination25();
//            FieldMapper.map(Destinations.Destination25.class, primarySrc, dst);
//        }
    }

    private static void runBenchmarkForIterations(int iterations) {
        Runtime runtime = Runtime.getRuntime();
        // Force garbage collection to get a baseline for memory usage.
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            Destinations.Destination25 dst = new Destinations.Destination25();
            FieldMapper.map(Destinations.Destination25.class, primarySrc, dst);
            // Validate on the last iteration: check a couple of fields.
            if (i == iterations - 1) {
                if (!"Hello1".equals(dst.getDestValue1()) ||
                        !"Hello25".equals(dst.getDestValue25())) {
                    throw new RuntimeException("Benchmark failed: Destination25 did not receive correct values.");
                }
            }
        }

        long endTime = System.nanoTime();
        runtime.gc();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();

        double timeTakenMs = (endTime - startTime) / 1_000_000.0;
        long memoryUsedBytes = memoryAfter - memoryBefore;

        System.out.println("Performed " + iterations + " allocations in " + timeTakenMs + " ms");
        System.out.println("Memory used: " + memoryUsedBytes + " bytes");
        System.out.println("-----------------------------------------");
    }
}