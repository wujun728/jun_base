package com.ruoyi.tools.utils.pinyin;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;


import com.ruoyi.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ClassUtils;

/**
 * 汉字转拼音工具类<br>
 *  1、常用词典在本模块：resources/config目录下，其中：
 *      duoyinci_dict为多音词点，如果找不到，可自行添加完善；
 *      xingshi_dict为姓氏的多音字
 *  2、其他场景可自己扩展
 * @author wocurr.com
 */
@Slf4j
public final class Pinyin4jUtil {

    private static Map<String, List<String>> pinyinMap = new HashMap<String, List<String>>();

    /**
     * 姓氏多音字
     */
    private static Map<String, List<String>> xsMap = new HashMap<String, List<String>>();

    static {
        try {
            log.info("## 开始加载多音词库。。。");
            initPinyin("config/duoyinci_dict.txt");
            initXs("config/xingshi_dict.txt");
            log.info("## 加载多音词库完成！");
        } catch (Exception e) {
            log.error("## 初始化拼音词库失败！");
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取姓名首字母（适用获取姓名首字母的场景）<br>
     *  1、取第一个字符，如果是非汉字则直接返回此字符；
     *
     * @param chines
     * @param isUpperCase 是否大写
     * @return 例如：华佗->H或h
     */
    public static String getNameFisrtSpell(String chines, boolean isUpperCase) {
        if (StringUtils.isBlank(chines)) {
            return "";
        }
        char character = chines.toCharArray()[0];
        if (!isChinese(character)) {
            return String.valueOf(character);
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String result = "";
        try {
            // 取得当前汉字的所有全拼
            String[] strs = PinyinHelper.toHanyuPinyinStringArray(character, format);
            if (strs == null) {
                return "";
            }
            for (String s : strs) {
                String py = filterStr(s);
                List<String> keyList = pinyinMap.get(py);
                result = String.valueOf(py.charAt(0));
                if (keyList != null && keyList.contains(s)) {
                    break;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            log.error(e.getMessage());
        }
        return isUpperCase ? result.toUpperCase() : result;
    }

    /**
     * 获取姓名字母简拼（适用获取姓名首字母的场景）<br>
     *  1、取第一个字符，如果是非汉字则直接返回此字符；
     *
     * @param chines
     * @param isUpperCase 是否大写
     * @return 例如：华佗->HT或ht
     */
    public static String getNameSimpleSpell(String chines, boolean isUpperCase) {
        if (StringUtils.isBlank(chines)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] chars = chines.toCharArray();
        for (char character : chars) {
            if (!isChinese(character)) {
                result.append(character);
                continue;
            }
            String py = toPinyinAndmatch(character, format);
            result.append(py.charAt(0));
        }
        return isUpperCase ? result.toString().toUpperCase() : result.toString();
    }

    /**
     * 获取姓名全拼
     *
     * @param chines
     * @param isFirstUpperCase 首字母是否大写
     * @return 例如：华佗->huatuo 或者 HuaTuo
     */
    public static String getNameFullSpell(String chines, boolean isFirstUpperCase) {
        if (StringUtils.isBlank(chines)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] chars = chines.toCharArray();
        for (char character : chars) {
            if (!isChinese(character)) {
                result.append(character);
                continue;
            }
            String py = toPinyinAndmatch(character, format);
            result.append(isFirstUpperCase ? convertInitialToUpperCase(py) : py);
        }
        return result.toString();
    }

    /**
     * 获取汉字首字母<br>
     *
     * @param chines
     * @param isUpperCase 是否大写
     * @return 汉字的第一个拼音字母，如：中国->Z或z
     */
    public static String getFirstSpell(String chines, boolean isUpperCase) {
        String chart = toFullSpell(chines).substring(0, 1);
        return isUpperCase ? chart.toUpperCase() : chart;
    }

    /**
     * 转简拼<br>
     * 1、英文字符不变，特殊字符丢失；
     * 2、多音字返回多个，如果只想返回更准确的一个，请使用toPinyin方法
     *
     * @param chines
     * @return 多音字返回多个拼音，如：中国银行->zgyx,zgyh
     */
    public static String toSimpleSpell(String chines) {
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        for (char character : nameChar) {
            if (isChinese(character)) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(character, format);
                    if (strs != null) {
                        for (int i = 0; i < strs.length; i++) {
                            // 取首字母
                            pinyinName.append(strs[i].charAt(0));
                            if (i != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    log.error(e.getMessage());
                }
            } else {
                pinyinName.append(character);
            }
            pinyinName.append(" ");
        }
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 转全拼<br>
     * 1、英文字符不变，特殊字符丢失；
     * 2、多音字返回多个，如果只想返回更准确的一个，请使用toPinyin方法
     *
     * @param chines
     * @return 多音字返回多个，如：中国银行->zhongguoyinhang,zhongguoyinxing
     */
    public static String toFullSpell(String chines) {
        StringBuilder pinyinName = new StringBuilder();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        for (char character : nameChar) {
            if (isChinese(character)) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(character, format);
                    if (strs != null) {
                        for (int i = 0; i < strs.length; i++) {
                            pinyinName.append(filterStr(strs[i]));
                            if (i != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    log.error(e.getMessage());
                }
            } else {
                pinyinName.append(character);
            }
            pinyinName.append(" ");
        }
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 汉字转拼音（优先使用此方法）<br>
     * 使用词库优先匹配
     *
     * @param chinese
     * @param shortTerm 是否取每个汉字的第一个拼音字母
     * @return 首字母大写，如：中国银行-><br>
     * 当shortTerm为false时，返回：ZhongGuoYinHang；
     * 当shortTerm为true时，返回zgyh
     */
    public static String toPinyin(String chinese, boolean shortTerm) {
        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat formart = new HanyuPinyinOutputFormat();
        formart.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        formart.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        formart.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arr = chinese.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];
            // 非汉字，返回原字符
            if (!isChinese(ch)) {
                pinyin.append(arr[i]);
                continue;
            }
            try {
                // 取得当前汉字的所有全拼
                String[] results = PinyinHelper.toHanyuPinyinStringArray(ch, formart);
                if (results == null) {
                    continue;
                }
                String result = "";
                int len = results.length;
                // 不是多音字或非多音字有多个音，取第一个
                if (len == 1 || results[0].equals(results[1])) {
                    result = filterStr(results[0]);
                } else {
                    // 多音字
                    int length = chinese.length();
                    boolean flag = false;
                    String s = null;
                    List<String> keyList = null;
                    for (int x = 0; x < len; x++) {
                        String py = filterStr(results[x]);
                        keyList = pinyinMap.get(py);
                        if (i + 3 <= length) {   // 后向匹配2个汉字
                            s = chinese.substring(i, i + 3);
                            if (keyList != null && keyList.contains(s)) {
                                result = py;
                                flag = true;
                                break;
                            }
                        }
                        if (i + 2 <= length) {   // 后向匹配 1个汉字
                            s = chinese.substring(i, i + 2);
                            if (keyList != null && keyList.contains(s)) {
                                result = py;
                                flag = true;
                                break;
                            }
                        }
                        if ((i - 2 >= 0) && (i + 1 <= length)) {  // 前向匹配2个汉字 龙固大
                            s = chinese.substring(i - 2, i + 1);
                            if (keyList != null && keyList.contains(s)) {
                                result = py;
                                flag = true;
                                break;
                            }
                        }
                        if ((i - 1 >= 0) && (i + 1 <= length)) {  // 前向匹配1个汉字   固大
                            s = chinese.substring(i - 1, i + 1);
                            if (keyList != null && keyList.contains(s)) {
                                result = py;
                                flag = true;
                                break;
                            }
                        }
                        if ((i - 1 >= 0) && (i + 2 <= length)) {  // 前向1个，后向1个
                            s = chinese.substring(i - 1, i + 2);
                            if (keyList != null && keyList.contains(s)) {
                                result = py;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (!flag) {  // 都没有找到，匹配默认的读音
                        s = String.valueOf(ch);
                        boolean found = false;
                        for (int x = 0; x < len; x++) {
                            String py = filterStr(results[x]);
                            keyList = pinyinMap.get(py);
                            if (keyList != null && keyList.contains(s)) {
                                result = py;
                                found = true;
                                break;
                            }
                        }
                        if (!found && results.length > 0) {
                            result = filterStr(results[0]);
                        }
                    }
                }
                pinyin.append(shortTerm ? getInitialChar(result) : convertInitialToUpperCase(result));
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                log.error(e.getMessage());
            }
        }
        return pinyin.toString();
    }

    /**
     * 转换拼音并匹配姓氏字典
     * @param character
     * @param format
     * @return
     */
    private static String toPinyinAndmatch(char character, HanyuPinyinOutputFormat format) {
        String namePy = "";
        try {
            // 取得当前汉字的所有全拼
            String[] strs = PinyinHelper.toHanyuPinyinStringArray(character, format);
            if (strs == null) {
                return "";
            }
            for (String s : strs) {
                String py = filterStr(s);
                List<String> keyList = pinyinMap.get(py);
                namePy = py;
                if (keyList != null && keyList.contains(s)) {
                    break;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            log.error(e.getMessage());
        }
        return namePy;
    }

    /**
     * 校验是否汉字
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        return String.valueOf(c).matches("[\\u4e00-\\u9fa5]+");
    }

    /**
     * 去除多音字重复数据
     *
     * @param theStr
     * @return
     */
    private static List<Map<String, Integer>> discountTheChinese(String theStr) {
        // 去除重复拼音后的拼音列表
        List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
        // 用于处理每个字的多音字，去掉重复
        Map<String, Integer> onlyOne = null;
        String[] firsts = theStr.split(" ");
        // 读出每个汉字的拼音
        for (String str : firsts) {
            onlyOne = new Hashtable<String, Integer>();
            String[] china = str.split(",");
            // 多音字处理
            for (String s : china) {
                Integer count = onlyOne.get(s);
                if (count == null) {
                    onlyOne.put(s, new Integer(1));
                } else {
                    onlyOne.remove(s);
                    count++;
                    onlyOne.put(s, count);
                }
            }
            mapList.add(onlyOne);
        }
        return mapList;
    }

    /**
     * 解析并组合拼音，对象合并方案(推荐使用)
     *
     * @return
     */
    private static String parseTheChineseByObject(List<Map<String, Integer>> list) {
        Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
        // 遍历每一组集合
        for (Map<String, Integer> map : list) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new LinkedHashMap<>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : map.keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp != null && temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : map.keySet()) {
                    String str = s;
                    temp.put(str, 1);
                }
            }
            // 保存组合数据以便下次循环使用
            if (temp != null && temp.size() > 0) {
                first = temp;
            }
        }
        String returnStr = "";
        if (first != null) {
            // 遍历取出组合字符串
            for (String str : first.keySet()) {
                returnStr += (str + ",");
            }
        }
        if (returnStr.length() > 0) {
            returnStr = returnStr.substring(0, returnStr.length() - 1);
        }
        return returnStr;
    }

    /**
     * 将某个字符串的首字母转为大写
     *
     * @param str
     * @return
     */
    private static String convertInitialToUpperCase(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 获取字符串的首字母
     *
     * @param str
     * @return
     */
    private static String getInitialChar(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return str.substring(0, 1);
    }

    /**
     * 过滤 u:
     *
     * @param str
     * @return
     */
    private static String filterStr(String str) {
        if (str.contains("u:")) {  //过滤 u:
            str = str.replace("u:", "v");
        }
        return str;
    }

    /**
     * 初始化
     * 所有的多音字词组
     *
     * @param fileName
     */
    private static void initPinyin(String fileName) {
        String filePath = ClassUtils.getDefaultClassLoader().getResource(fileName).getPath();
        BufferedReader reader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(URLDecoder.decode(filePath, "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split("#");
                String pinyin = arr[0];
                String chinese = arr[1];
                if (chinese != null) {
                    String[] strs = chinese.split(" ");
                    List<String> list = Arrays.asList(strs);
                    pinyinMap.put(pinyin, list);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * 初始化姓氏字典
     *
     * @param path
     */
    private static void initXs(String path) {
        String filePath = ClassUtils.getDefaultClassLoader().getResource(path).getPath();
        BufferedReader reader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(URLDecoder.decode(filePath, "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arr = line.split("#");
                String pinyin = arr[0];
                String chinese = arr[1];
                if (chinese != null) {
                    String[] strs = chinese.split(" ");
                    List<String> list = Arrays.asList(strs);
                    xsMap.put(pinyin, list);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

}