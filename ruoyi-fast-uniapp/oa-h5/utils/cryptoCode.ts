import cryptoJs from "crypto-js";
import type { PropType } from "@/utils/typeHelper";

/**
 * 加密
 */
export const encryptCode = (plaintText: string): string => {
  const sKey = "abc123456789";
  const key = cryptoJs.enc.Utf8.parse(sKey);
  const ciphertext = cryptoJs.AES.encrypt(plaintText, key, {
    iv: key,
    mode: cryptoJs.mode.CBC, // 使用CBC算法
    padding: cryptoJs.pad.ZeroPadding, // 使用pkcs7进行padding
  }).toString();
  return encodeURIComponent(ciphertext);
};

/**
 * 解密
 */
export const decryptCode = (ciphertext: string) => {
  const sKey = "abc123456789";
  const key = cryptoJs.enc.Utf8.parse(sKey);
  const bytes = cryptoJs.AES.decrypt(decodeURIComponent(ciphertext), key, {
    iv: key,
    mode: cryptoJs.mode.CBC, // 使用CBC算法
    padding: cryptoJs.pad.ZeroPadding, // 使用pkcs7进行padding
  });
  return bytes.toString(cryptoJs.enc.Utf8);
};

/**
 * 对是url方式的参数进行解密
 */
export const decryptQuery = (ciphertext: string): PropType => {
  const obj: PropType = {};
  const queryString: string = decryptCode(ciphertext);
  const pairs: string[] = queryString.split("&");
  for (let i = 0; i < pairs.length; i++) {
    const pair: string[] = pairs[i].split("=");
    obj[pair[0]] = pair[1];
  }
  return obj;
};
