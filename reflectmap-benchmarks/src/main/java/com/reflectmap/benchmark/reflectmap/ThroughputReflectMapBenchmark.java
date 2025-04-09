package com.reflectmap.benchmark.reflectmap;

import com.reflectmap.ReflectMap;
import com.reflectmap.mock.Destination25;
import com.reflectmap.mock.Source25;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class ThroughputReflectMapBenchmark {

    protected Source25 src;
    protected Destination25 dst;

    @Setup(Level.Trial)
    public void setup() {
        src = new Source25("Hello1", "Hello2", "Hello3", "Hello4", "Hello5",
                "Hello6", "Hello7", "Hello8", "Hello9", "Hello10",
                "Hello11", "Hello12", "Hello13", "Hello14", "Hello15",
                "Hello16", "Hello17", "Hello18", "Hello19", "Hello20",
                "Hello21", "Hello22", "Hello23", "Hello24", "Hello25");

        dst = new Destination25();
    }

    @Benchmark
    public void map() {
        ReflectMap.map(src, Source25.class, dst, Destination25.class);
    }

}