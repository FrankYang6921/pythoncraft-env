package top.frankyang.pre.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Digests {
    private static final MessageDigest md5Digest, sha1Digest;

    static {
        try {
            md5Digest = MessageDigest.getInstance("md5");
            sha1Digest = MessageDigest.getInstance("sha1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private Digests() {
    }

    public static String digestMD5(String string) {
        return new BigInteger(1, md5Digest.digest(string.getBytes())).toString(16);
    }

    public static String digestSHA1(String string) {
        return new BigInteger(1, sha1Digest.digest(string.getBytes())).toString(16);
    }
}