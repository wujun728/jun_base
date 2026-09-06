package com.aidex.common.utils;

public class Hex {
    private static final char[] hexes = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Hex() {
    }

    public static byte[] decodeHex(char[] data) {
        int i;
        byte[] buf = new byte[(i = data.length) >> 1];
        int j = 0;

        for(int n = 0; n < i; ++j) {
            int b = Character.digit(data[n], 16) << 4;
            ++n;
            b |= Character.digit(data[n], 16);
            ++n;
            buf[j] = (byte)b;
        }

        return buf;
    }

    public static String encodeHexString(byte[] data) {
        int len;
        char[] buf = new char[(len = data.length) << 1];
        int i = 0;

        for(int j = 0; i < len; ++i) {
            buf[j++] = hexes[(240 & data[i]) >>> 4];
            buf[j++] = hexes[15 & data[i]];
        }

        return new String(buf);
    }
}
