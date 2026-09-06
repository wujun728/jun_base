package com.danny.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketUtils.class);
    private static Socket socket = null;
    private static String archivesCenterAPIIP = StringUtils
            .defaultString("", "127.0.0.1");
    private static String archivesCenterAPIPort = StringUtils
            .defaultString("", "24992");

    public static boolean connection() {
        if (socket != null) {
            return true;
        }

        try {
            socket = new Socket(archivesCenterAPIIP, NumberUtils.toInt(archivesCenterAPIPort));
            return true;
        } catch (Exception e) {
            LOGGER.error("connection error", e);
            return false;
        }
    }

    public static void stop() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (Exception e) {
            LOGGER.error("connection error", e);
        }
    }

    /**
     * 发送数据
     *
     * @param cmd 需要发送的数据(十六进制的字符串形式)
     * @return 接受到的数据(十六进制的字符串形式)
     */
    public static String sendCmd(String cmd) {
        if (!connection() || socket == null) {
            return "error";
        }

        try {
            OutputStream out = socket.getOutputStream();
            byte[] hexStrToByteArrs = hexStrToByteArrs(cmd);
            if (hexStrToByteArrs == null) {
                return "error";
            }
            out.write(hexStrToByteArrs);

            InputStream in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = in.read(buf);

            stop();

            return bytesToHexString(buf);
        } catch (IOException e) {
            LOGGER.error("sendCmd error", e);
            return "error";
        }
    }

    /**
     * 将十六进制的字符串转换成字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStrToByteArrs(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }

        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        int index = 0;

        byte[] bytes = new byte[len / 2];

        while (index < len) {
            String sub = hexString.substring(index, index + 2);
            bytes[index / 2] = (byte) Integer.parseInt(sub, 16);
            index += 2;
        }

        return bytes;
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
            // 在这里故意追加一个逗号便于最后的区分
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(sendCmd("0f 0f"));
        System.out.println(sendCmd("0f 0f"));
    }
}