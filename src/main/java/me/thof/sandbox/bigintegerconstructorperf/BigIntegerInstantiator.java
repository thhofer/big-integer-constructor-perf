package me.thof.sandbox.bigintegerconstructorperf;

import java.math.BigInteger;
import java.util.Base64;
import java.util.HexFormat;

public class BigIntegerInstantiator {

    private static final HexFormat FORMAT = HexFormat.of();

    private BigIntegerInstantiator() {
        // No instantiation needed here
    }

    public static BigInteger fromHexString(String hexString) {
        return new BigInteger(hexString, 16);
    }

    public static BigInteger throughHexFormat(String hexString) {
        if (hexString.startsWith("-")) {
            return new BigInteger(-1, FORMAT.parseHex(hexString.substring(1)));
        } else {
            return new BigInteger(1, FORMAT.parseHex(hexString));
        }
    }

    public static BigInteger fromBase64(String base64) {
        return new BigInteger(Base64.getDecoder().decode(base64));
    }
}
