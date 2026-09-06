package com.aidex.common.core.shiroSecurity;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
public final class Utf8
{

    public Utf8()
    {
    }

    public static byte[] encode(CharSequence string)
    {
        try
        {
            ByteBuffer bytes = CHARSET.newEncoder().encode(CharBuffer.wrap(string));
            byte copy[] = new byte[bytes.limit()];
            System.arraycopy(bytes.array(), 0, copy, 0, bytes.limit());
            return copy;
        }
        catch(CharacterCodingException e)
        {
            throw new IllegalArgumentException("Encoding failed", e);
        }
    }

    public static String decode(byte bytes[])
    {
        try
        {
            return CHARSET.newDecoder().decode(ByteBuffer.wrap(bytes)).toString();
        }
        catch(CharacterCodingException e)
        {
            throw new IllegalArgumentException("Decoding failed", e);
        }
    }

    private static final Charset CHARSET = Charset.forName("UTF-8");

}
