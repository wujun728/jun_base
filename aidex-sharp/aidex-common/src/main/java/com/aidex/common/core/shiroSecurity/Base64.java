package com.aidex.common.core.shiroSecurity;

public final class Base64 {

    public Base64() {
    }

    public static byte[] decode(byte bytes[]) {
        return decode(bytes, 0, bytes.length, 0);
    }

    public static byte[] encode(byte bytes[]) {
        return encodeBytesToBytes(bytes, 0, bytes.length, 0);
    }

    public static boolean isBase64(byte bytes[]) {
        try {
            decode(bytes);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static final byte[] getAlphabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_ALPHABET;
        }
        if ((options & 32) == 32) {
            return _ORDERED_ALPHABET;
        } else {
            return _STANDARD_ALPHABET;
        }
    }

    private static final byte[] getDecodabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_DECODABET;
        }
        if ((options & 32) == 32) {
            return _ORDERED_DECODABET;
        } else {
            return _STANDARD_DECODABET;
        }
    }

    private static byte[] encode3to4(byte source[], int srcOffset, int numSigBytes, byte destination[], int destOffset, int options) {
        byte ALPHABET[] = getAlphabet(options);
        int inBuff = (numSigBytes <= 0 ? 0 : (source[srcOffset] << 24) >>> 8) | (numSigBytes <= 1 ? 0 : (source[srcOffset + 1] << 24) >>> 16) | (numSigBytes <= 2 ? 0 : (source[srcOffset + 2] << 24) >>> 24);
        switch (numSigBytes) {
            case 3: // '\003'
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                return destination;

            case 2: // '\002'
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                destination[destOffset + 3] = 61;
                return destination;

            case 1: // '\001'
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = 61;
                destination[destOffset + 3] = 61;
                return destination;
        }
        return destination;
    }

    private static byte[] encodeBytesToBytes(byte source[], int off, int len, int options) {
        if (source == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }
        if (off < 0) {
            throw new IllegalArgumentException((new StringBuilder()).append("Cannot have negative offset: ").append(off).toString());
        }
        if (len < 0) {
            throw new IllegalArgumentException((new StringBuilder()).append("Cannot have length offset: ").append(len).toString());
        }
        if (off + len > source.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", new Object[]{
                    Integer.valueOf(off), Integer.valueOf(len), Integer.valueOf(source.length)
            }));
        }
        boolean breakLines = (options & 8) > 0;
        int encLen = (len / 3) * 4 + (len % 3 <= 0 ? 0 : 4);
        if (breakLines) {
            encLen += encLen / 76;
        }
        byte outBuff[] = new byte[encLen];
        int d = 0;
        int e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        while (d < len2) {
            encode3to4(source, d + off, 3, outBuff, e, options);
            lineLength += 4;
            if (breakLines && lineLength >= 76) {
                outBuff[e + 4] = 10;
                e++;
                lineLength = 0;
            }
            d += 3;
            e += 4;
        }
        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e, options);
            e += 4;
        }
        if (e <= outBuff.length - 1) {
            byte finalOut[] = new byte[e];
            System.arraycopy(outBuff, 0, finalOut, 0, e);
            return finalOut;
        } else {
            return outBuff;
        }
    }

    private static int decode4to3(byte source[], int srcOffset, byte destination[], int destOffset, int options) {
        if (source == null){
            throw new NullPointerException("Source array was null.");}
        if (destination == null){
            throw new NullPointerException("Destination array was null.");}
        if (srcOffset < 0 || srcOffset + 3 >= source.length){
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", new Object[]{
                    Integer.valueOf(source.length), Integer.valueOf(srcOffset)
            }));}
        if (destOffset < 0 || destOffset + 2 >= destination.length){
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[]{
                    Integer.valueOf(destination.length), Integer.valueOf(destOffset)
            }));}
        byte DECODABET[] = getDecodabet(options);
        if (source[srcOffset + 2] == 61) {
            int outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12;
            destination[destOffset] = (byte) (outBuff >>> 16);
            return 1;
        }
        if (source[srcOffset + 3] == 61) {
            int outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12 | (DECODABET[source[srcOffset + 2]] & 255) << 6;
            destination[destOffset] = (byte) (outBuff >>> 16);
            destination[destOffset + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            int outBuff = (DECODABET[source[srcOffset]] & 255) << 18 | (DECODABET[source[srcOffset + 1]] & 255) << 12 | (DECODABET[source[srcOffset + 2]] & 255) << 6 | DECODABET[source[srcOffset + 3]] & 255;
            destination[destOffset] = (byte) (outBuff >> 16);
            destination[destOffset + 1] = (byte) (outBuff >> 8);
            destination[destOffset + 2] = (byte) outBuff;
            return 3;
        }
    }

    private static byte[] decode(byte source[], int off, int len, int options) {
        if (source == null){
            throw new NullPointerException("Cannot decode null source array.");}
        if (off < 0 || off + len > source.length){
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", new Object[]{
                    Integer.valueOf(source.length), Integer.valueOf(off), Integer.valueOf(len)
            }));}
        if (len == 0){
            return new byte[0];}
        if (len < 4){
            throw new IllegalArgumentException((new StringBuilder()).append("Base64-encoded string must have at least four characters, but length specified was ").append(len).toString());}
        byte DECODABET[] = getDecodabet(options);
        int len34 = (len * 3) / 4;
        byte outBuff[] = new byte[len34];
        int outBuffPosn = 0;
        byte b4[] = new byte[4];
        int b4Posn = 0;
        int i = 0;
        byte sbiDecode = 0;
        for (i = off; i < off + len; i++) {
            sbiDecode = DECODABET[source[i] & 255];
            if (sbiDecode >= -5) {
                if (sbiDecode < -1){
                    continue;}
                b4[b4Posn++] = source[i];
                if (b4Posn <= 3){
                    continue;}
                outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
                b4Posn = 0;
                if (source[i] == 61){
                    break;}
            } else {
            }
        }

        byte out[] = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    public static final int NO_OPTIONS = 0;
    public static final int ENCODE = 1;
    public static final int DECODE = 0;
    public static final int DO_BREAK_LINES = 8;
    public static final int URL_SAFE = 16;
    public static final int ORDERED = 32;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte EQUALS_SIGN = 61;
    private static final byte NEW_LINE = 10;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte _STANDARD_ALPHABET[] = {
            65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
            75, 76, 77, 78, 79, 80, 81, 82, 83, 84,
            85, 86, 87, 88, 89, 90, 97, 98, 99, 100,
            101, 102, 103, 104, 105, 106, 107, 108, 109, 110,
            111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
            121, 122, 48, 49, 50, 51, 52, 53, 54, 55,
            56, 57, 43, 47
    };
    private static final byte _STANDARD_DECODABET[] = {
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -5,
            -5, -9, -9, -5, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -5, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, 62, -9, -9, -9, 63, 52, 53,
            54, 55, 56, 57, 58, 59, 60, 61, -9, -9,
            -9, -1, -9, -9, -9, 0, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, -9, -9, -9, -9, -9, -9, 26, 27, 28,
            29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
            39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
            49, 50, 51, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9
    };
    private static final byte _URL_SAFE_ALPHABET[] = {
            65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
            75, 76, 77, 78, 79, 80, 81, 82, 83, 84,
            85, 86, 87, 88, 89, 90, 97, 98, 99, 100,
            101, 102, 103, 104, 105, 106, 107, 108, 109, 110,
            111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
            121, 122, 48, 49, 50, 51, 52, 53, 54, 55,
            56, 57, 45, 95
    };
    private static final byte _URL_SAFE_DECODABET[] = {
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -5,
            -5, -9, -9, -5, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -5, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, 62, -9, -9, 52, 53,
            54, 55, 56, 57, 58, 59, 60, 61, -9, -9,
            -9, -1, -9, -9, -9, 0, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, -9, -9, -9, -9, 63, -9, 26, 27, 28,
            29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
            39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
            49, 50, 51, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9
    };
    private static final byte _ORDERED_ALPHABET[] = {
            45, 48, 49, 50, 51, 52, 53, 54, 55, 56,
            57, 65, 66, 67, 68, 69, 70, 71, 72, 73,
            74, 75, 76, 77, 78, 79, 80, 81, 82, 83,
            84, 85, 86, 87, 88, 89, 90, 95, 97, 98,
            99, 100, 101, 102, 103, 104, 105, 106, 107, 108,
            109, 110, 111, 112, 113, 114, 115, 116, 117, 118,
            119, 120, 121, 122
    };
    private static final byte _ORDERED_DECODABET[] = {
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -5,
            -5, -9, -9, -5, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -5, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, 0, -9, -9, 1, 2,
            3, 4, 5, 6, 7, 8, 9, 10, -9, -9,
            -9, -1, -9, -9, -9, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
            26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, -9, -9, -9, -9, 37, -9, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
            61, 62, 63, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9,
            -9, -9, -9, -9, -9, -9, -9
    };

}
