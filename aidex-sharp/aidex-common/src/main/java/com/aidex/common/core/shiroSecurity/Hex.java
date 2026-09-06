package com.aidex.common.core.shiroSecurity;

public final class Hex
{

    public Hex()
    {
    }

    public static char[] encode(byte bytes[])
    {
        int nBytes = bytes.length;
        char result[] = new char[2 * nBytes];
        int j = 0;
        for(int i = 0; i < nBytes; i++)
        {
            result[j++] = HEX[(240 & bytes[i]) >>> 4];
            result[j++] = HEX[15 & bytes[i]];
        }

        return result;
    }

    private static final char HEX[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f'
    };

}
