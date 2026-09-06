package com.aidex.common.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SerializeUtil {
    /**
     * 序列化 for redis
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

    public static byte[] serialize(String[] strs) {
        ArrayList<Byte> byteList = new ArrayList<Byte>();
        for (String str: strs) {
            int len = str.getBytes().length;
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(len);
            byte[] lenArray = bb.array();
            for (byte b: lenArray) {
                byteList.add(b);
            }
            byte[] strArray = str.getBytes();
            for (byte b: strArray) {
                byteList.add(b);
            }
        }
        byte[] result = new byte[byteList.size()];
        for (int i=0; i<byteList.size(); i++) {
            result[i] = byteList.get(i);
        }
        return result;
    }


    /**
     * 反序列化
     * 
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

    public static byte[][] encodeMany(String... strs) {
        byte[][] many = new byte[strs.length][];
        for(int i = 0; i < strs.length; ++i) {
            many[i] = encode(strs[i]);
        }

        return many;
    }

    public static byte[] encode(String str) {
        try {
            if (str == null || str.trim().length() == 0) {
                return new byte[0];
            } else {
                return str.getBytes("UTF-8");
            }
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
        }
        return new byte[0];
    }

    public static String encode(byte[] data) {
        try {
            if(data == null || data.length == 0){
                return null;
            } else {
                return new String(data, "UTF-8");
            }
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
        }
        return null;
    }

    public static String encode(Object data){
        try{
            return encode((byte[])data);
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }


}