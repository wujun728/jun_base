
package com.aidex.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * IO工具类
 *
 * @author ruoyi
 */
public final class IOUtils {
    public static final Charset UTF8_CHARSET = Charset.forName("utf-8");

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private IOUtils() {

    }
    
    /**
     * Use this function instead of new String(byte[]) to avoid surprises from non-standard default encodings.
     * @param bytes
     * @return
     */
    public static String newStringFromBytes(byte[] bytes) {
        try {
            return new String(bytes, UTF8_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw 
                new RuntimeException("Impossible failure: Charset.forName(\"utf-8\") returns invalid name.");

        }
    }

    /**
     * Use this function instead of new String(byte[], int, int) 
     * to avoid surprises from non-standard default encodings.
     * @param bytes
     * @param start
     * @param length
     * @return
     */
    public static String newStringFromBytes(byte[] bytes, int start, int length) {
        try {
            return new String(bytes, start, length, UTF8_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            throw 
                new RuntimeException("Impossible failure: Charset.forName(\"utf-8\") returns invalid name.");

        }
    }

    public static int copy(final InputStream input, final OutputStream output)
        throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }
    
    public static int copyAndCloseInputNoBuffer(final InputStream input, final OutputStream output) throws IOException {
		try {
			int n = input.read();
			int total = 0;
			while (-1 != n) {
				output.write(n);
				total++;
				n = input.read();
			}
			return total;
		} finally {
			input.close();
		}
	}

    public static int copyAndCloseInput(final InputStream input,
            final OutputStream output) throws IOException {
        try {
            return copy(input, output, DEFAULT_BUFFER_SIZE);
        } finally {
            input.close();
        }
    }

    public static int copyAndCloseInput(final InputStream input, final OutputStream output, int bufferSize) throws IOException {
        try {
            return copy(input, output, bufferSize);
        } finally {
            input.close();
        }
    }

    public static int copy(final InputStream input, final OutputStream output,int bufferSize) throws IOException {
        int avail = input.available();
        if (avail > 262144) {
            avail = 262144;
        }
        if (avail > bufferSize) {
            bufferSize = avail;
        }
        final byte[] buffer = new byte[bufferSize];
        int n = 0;
        n = input.read(buffer);
        int total = 0;
        while (-1 != n) {
            output.write(buffer, 0, n);
            total += n;
            n = input.read(buffer);
        }
        return total;
    }

    public static void copy(final Reader input, final Writer output,
            final int bufferSize) throws IOException {
        final char[] buffer = new char[bufferSize];
        int n = 0;
        n = input.read(buffer);
        while (-1 != n) {
            output.write(buffer, 0, n);
            n = input.read(buffer);
        }
    }

    public static String toString(final InputStream input) throws IOException {
        return toString(input, DEFAULT_BUFFER_SIZE);
    }

    public static String toString(final InputStream input, int bufferSize)
        throws IOException {

        int avail = input.available();
        if (avail > bufferSize) {
            bufferSize = avail;
        }

        StringBuilder buf = new StringBuilder();
        final byte[] buffer = new byte[bufferSize];
        int n = 0;
        n = input.read(buffer);
        while (-1 != n) {
            buf.append(newStringFromBytes(buffer, 0, n));
            n = input.read(buffer);
        }
        input.close();
        return buf.toString();
    }

    public static String toString(final Reader input) throws IOException {

        StringBuilder buf = new StringBuilder();
        final char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        int n = 0;
        n = input.read(buffer);
        while (-1 != n) {
            buf.append(new String(buffer, 0, n));
            n = input.read(buffer);
        }
        input.close();
        return buf.toString();
    }

    public static String readStringFromStream(InputStream in)
        throws IOException {

        StringBuilder sb = new StringBuilder(1024);

        for (int i = in.read(); i != -1; i = in.read()) {
            sb.append((char) i);
        }

        in.close();

        return sb.toString();
    }

    public static byte[] readBytesFromStream(InputStream in) throws IOException {
        int i = in.available();
        if (i < DEFAULT_BUFFER_SIZE) {
            i = DEFAULT_BUFFER_SIZE;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(i);
        copy(in, bos);
        in.close();
        return bos.toByteArray();
    }
    
    public static InputStream skipFully(InputStream in,long howMany)throws Exception{         
    	long remainning = howMany;         
    	long len = 0;         
    	while(remainning>0){             
    		len = in.skip(len);             
    		remainning -= len;         
    	}         
    	return in;     
    }
}
