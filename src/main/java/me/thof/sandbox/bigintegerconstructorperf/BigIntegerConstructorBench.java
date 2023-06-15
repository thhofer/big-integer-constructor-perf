package me.thof.sandbox.bigintegerconstructorperf;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(value = Mode.AverageTime)
@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Measurement(iterations = 3)
@Warmup(iterations = 0)
public class BigIntegerConstructorBench {
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"2048", "3072"})
        public int bitLength;

        public String hexString;
        public String base64;

        @Setup(Level.Trial)
        public void setup() {
            BigInteger integer = new BigInteger(bitLength, new SecureRandom());
            hexString = integer.toString(16);
            base64 = Base64.getEncoder().encodeToString(integer.toByteArray());
        }
    }

    @Benchmark
    public void fromHexString(BenchmarkState state, Blackhole bh) {
        bh.consume(BigIntegerInstantiator.fromHexString(state.hexString));
    }

    @Benchmark
    public void throughHexFormat(BenchmarkState state, Blackhole bh) {
        bh.consume(BigIntegerInstantiator.throughHexFormat(state.hexString));
    }

    @Benchmark
    public void fromBase64(BenchmarkState state, Blackhole bh) {
        bh.consume(BigIntegerInstantiator.fromBase64(state.base64));
    }
}
