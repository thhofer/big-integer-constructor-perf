# `BigInteger` constructor performance

## TL;DR

If you're handling hex-encoded `BigInteger`s feed them through `HexFormat` before instantiating them.
Better yet, if you can change the format, encode them in Base64.

## Motivation

While working on some performance on a Java project for a client, I identified that deserialization of JSON objects was
slower than I had expected. Some profiling later, it became apparent that a good chunk of the time was spent inside the
`BigInteger` constructor.

Within the JSON files, the `BigInteger` values were serialized as hexadecimal strings.
After some playing around with the possibility of encoding them in Base64, we were surprised at the magnitude of the
speed-up.
I decided to dig a little deeper and came up with this benchmark.

## Build

`mvn clean verify`

## Run benchmark

`java -jar target/benchmarks.jar`

## Benchmark results

### Implementations

All implementations centralized
within [BigIntegerInstantiator](src/main/java/me/thof/sandbox/bigintegerconstructorperf/BigIntegerInstantiator.java).

- `fromHex`: `new BigInteger(hexString, 16)`
- `throughHexFormat`: `new BigInteger(HexFormat.of().parseHex(hexString))`
- `fromByase64`: `new BigInteger(Base64.getDecoder().decode(base64))`

### Table of results

| Benchmark                                   | (bitLength) | Mode | Cnt |     Score |      Error | Units |
|---------------------------------------------|------------:|------|-----|----------:|-----------:|-------|
| BigIntegerConstructorBench.fromBase64       |        2048 | avgt | 3   |   460.397 | ±  215.369 | ns/op |
| BigIntegerConstructorBench.fromBase64       |        3072 | avgt | 3   |   610.859 | ±  563.657 | ns/op |
| BigIntegerConstructorBench.fromHexString    |        2048 | avgt | 3   |  5549.389 | ±  100.973 | ns/op |
| BigIntegerConstructorBench.fromHexString    |        3072 | avgt | 3   | 12161.285 | ± 1488.165 | ns/op |
| BigIntegerConstructorBench.throughHexFormat |        2048 | avgt | 3   |   455.874 | ±   70.699 | ns/op |
| BigIntegerConstructorBench.throughHexFormat |        3072 | avgt | 3   |   698.157 | ±  108.850 | ns/op |

