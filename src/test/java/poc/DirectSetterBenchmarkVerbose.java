package poc;

public class DirectSetterBenchmarkVerbose {
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
        // Warm-up
        Destinations.Destination25 warmupDst = new Destinations.Destination25();
        manuallySetFields(warmupSrc, warmupDst);

        int iterations = 10_000_000;
        int repeats = 5;

        for (int i = 0; i < repeats; i++) {
            runBenchmarkForIterations(iterations);
        }
    }

    private static void runBenchmarkForIterations(int iterations) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i++) {
            Destinations.Destination25 dst = new Destinations.Destination25();
            manuallySetFields(primarySrc, dst);
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

    private static void manuallySetFields(Sources.Source25 src, Destinations.Destination25 dst) {
        dst.setDestValue1(src.getValue1());
        dst.setDestValue2(src.getValue2());
        dst.setDestValue3(src.getValue3());
        dst.setDestValue4(src.getValue4());
        dst.setDestValue5(src.getValue5());
        dst.setDestValue6(src.getValue6());
        dst.setDestValue7(src.getValue7());
        dst.setDestValue8(src.getValue8());
        dst.setDestValue9(src.getValue9());
        dst.setDestValue10(src.getValue10());
        dst.setDestValue11(src.getValue11());
        dst.setDestValue12(src.getValue12());
        dst.setDestValue13(src.getValue13());
        dst.setDestValue14(src.getValue14());
        dst.setDestValue15(src.getValue15());
        dst.setDestValue16(src.getValue16());
        dst.setDestValue17(src.getValue17());
        dst.setDestValue18(src.getValue18());
        dst.setDestValue19(src.getValue19());
        dst.setDestValue20(src.getValue20());
        dst.setDestValue21(src.getValue21());
        dst.setDestValue22(src.getValue22());
        dst.setDestValue23(src.getValue23());
        dst.setDestValue24(src.getValue24());
        dst.setDestValue25(src.getValue25());
    }
}