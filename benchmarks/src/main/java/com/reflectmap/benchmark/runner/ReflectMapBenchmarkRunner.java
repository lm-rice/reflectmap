package com.reflectmap.benchmark.runner;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class ReflectMapBenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(".*ReflectMapBenchmark.*")
                .forks(1)
                .threads(1)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result("benchmarks/results/reflectmap/all-metrics.json")
                .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+DebugNonSafepoints")
                .build();

        new Runner(opt).run();
    }
}