package com.reflectmap.benchmark.direct;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SampleTimeDirectMapperBenchmark extends AbstractDirectMapperBenchmark {

    @Benchmark
    public void map() {
        DirectMapper.map(src, dst);
    }

}