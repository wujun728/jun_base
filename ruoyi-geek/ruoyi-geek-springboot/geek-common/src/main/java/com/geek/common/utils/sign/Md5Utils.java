package com.geek.common.utils.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.geek.common.core.text.CharsetKit;

/**
 * Md5加密方法
 *
 * @author geek
 */
public class Md5Utils {
    private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

    private static final ThreadLocal<MessageDigest> md5DigestThreadLocal = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    });

    private static byte[] md5(String s) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes(StandardCharsets.UTF_8));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        } catch (Exception e) {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static final String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s) {
        try {
            return new String(toHex(md5(s)).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("not supported charset...{}", e);
            return s;
        }
    }

    public static String encryptMd5(String string) throws UnsupportedEncodingException {
        return encryptMd5(string, CharsetKit.UTF_8);
    }

    public static String encryptMd5(String string, String charSet) throws UnsupportedEncodingException {
        return DigestUtils.md5Hex(string.getBytes(charSet));
    }

    /**
     * 计算文件的md5
     * 
     * @param file 文件，可以是 MultipartFile 或 File
     * @return
     */
    public static String getMd5(Object file) {
        try (InputStream inputStream = getInputStream(file)) {
            long fileSize = getFileSize(file);
            // 10MB作为分界点
            if (fileSize < 10 * 1024 * 1024) {
                return getMd5ForSmallFile(inputStream);
            } else {
                return getMd5ForLargeFile(inputStream);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static InputStream getInputStream(Object file) throws IOException {
        if (file instanceof MultipartFile) {
            return ((MultipartFile) file).getInputStream();
        } else if (file instanceof File) {
            return new FileInputStream((File) file);
        }
        throw new IllegalArgumentException("Unsupported file type");
    }

    private static long getFileSize(Object file) throws IOException {
        if (file instanceof MultipartFile) {
            return ((MultipartFile) file).getSize();
        } else if (file instanceof File) {
            return ((File) file).length();
        }
        throw new IllegalArgumentException("Unsupported file type");
    }

    /**
     * 计算小文件的md5
     *
     * @param inputStream 文件输入流
     * @return
     */
    private static String getMd5ForSmallFile(InputStream inputStream) {
        try {
            byte[] uploadBytes = inputStream.readAllBytes();
            MessageDigest md5 = md5DigestThreadLocal.get();
            byte[] digest = md5.digest(uploadBytes);
            String md5Hex = new BigInteger(1, digest).toString(16);
            while (md5Hex.length() < 32) {
                md5Hex = "0" + md5Hex;
            }
            return md5Hex;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 计算大文件的md5
     *
     * @param inputStream 文件输入流
     * @return
     */
    private static String getMd5ForLargeFile(InputStream inputStream) {
        try (InputStream is = inputStream) {
            MessageDigest md = md5DigestThreadLocal.get();
            byte[] buffer = new byte[81920];
            int read;
            while ((read = is.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Md5Utils() {
    }
}
