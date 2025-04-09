package com.reflectmap.benchmark.runner;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class ModelMapperBenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(".*ModelMapperBenchmark.*")
                .forks(1)
                .threads(1)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result("reflectmap-benchmarks/results/modelmapper/all-metrics.json")
                .jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+DebugNonSafepoints")
                .build();

        new Runner(opt).run();
    }
}