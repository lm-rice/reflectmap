package com.reflectmap.benchmark.modelmapper;

import com.reflectmap.mock.Destination25;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SampleTimeModelMapperBenchmark extends AbstractModelMapperBenchmark {

    @Benchmark
    public Destination25 map() {
        return modelMapper.map(src, Destination25.class);
    }

}