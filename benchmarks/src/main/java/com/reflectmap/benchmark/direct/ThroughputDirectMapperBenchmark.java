package com.reflectmap.benchmark.direct;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ThroughputDirectMapperBenchmark extends AbstractDirectMapperBenchmark {

    @Benchmark
    public void map() {
        DirectMapper.map(src, dst);
    }

}