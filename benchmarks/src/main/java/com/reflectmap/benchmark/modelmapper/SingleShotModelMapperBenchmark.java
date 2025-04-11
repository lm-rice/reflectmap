package com.reflectmap.benchmark.modelmapper;

import com.reflectmap.mock.Destination25;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class SingleShotModelMapperBenchmark extends AbstractModelMapperBenchmark {

    @Benchmark
    public Destination25 map() {
        return modelMapper.map(src, Destination25.class);
    }

}