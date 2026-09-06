package com.aidex.common.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * <p>aidex</p>
 * <p>作者：aidex</p>
 * <p>邮箱：aidex@qq.com</p>
 * <p>创建时间： 2020-9-15 下午9:13:53 </p>
 * <p>类说明：解决多音字的工具类</p>
 * <p>修改记录： </p>
 */
public class PinYin4JCn {

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失
     * 支持多音字，生成方式如（重当参:cdc,zds,cds,zdc）
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            // 取首字母
                            pinyinName.append(strs[j].charAt(0));
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                    // else {
                    // pinyinName.append(nameChar[i]);
                    // }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
    }

    /**
     * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
     * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen,chongdangshen,zhongdangshen,chongdangcan）
     *
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines) {
        StringBuffer pinyinName = new StringBuffer();
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    // 取得当前汉字的所有全拼
                    String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if (strs != null) {
                        for (int j = 0; j < strs.length; j++) {
                            pinyinName.append(strs[j]);
                            if (j != strs.length - 1) {
                                pinyinName.append(",");
                            }
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName.append(nameChar[i]);
            }
            pinyinName.append(" ");
        }
        // return pinyinName.toString();
        return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
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
                    //onlyOne.put(s, new Integer(1));
                    onlyOne.put(s, Integer.valueOf(1));
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
        for (int i = 0; i < list.size(); i++) {
            // 每一组集合与上一次组合的Map
            Map<String, Integer> temp = new Hashtable<String, Integer>();
            // 第一次循环，first为空
            if (first != null) {
                // 取出上次组合与此次集合的字符，并保存
                for (String s : first.keySet()) {
                    for (String s1 : list.get(i).keySet()) {
                        String str = s + s1;
                        temp.put(str, 1);
                    }
                }
                // 清理上一次组合数据
                if (temp != null && temp.size() > 0) {
                    first.clear();
                }
            } else {
                for (String s : list.get(i).keySet()) {
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
     * 解析并组合拼音，循环读取方案（不灵活，不推荐使用）
     * <p>
     * 现在有如下几个数组: {1,2,3} {4,5} {7,8,9} {5,2,8}
     * 要求写出算法对以上数组进行数据组合,如:1475,1472,1478,1485,1482....如此类推，得到的组合刚好是以上数组的最隹组合（不多不少）.
     * 注：要求有序组合（并非象“全排列算法”那般得到的组合是无序的）：组合过程中，第一组数组排第一位、第二组排第二位、第三组排第三位....
     *
     * @param list
     * @return
     */
    private static String parseTheChineseByFor(List<Map<String, Integer>> list) {
        StringBuffer sbf = new StringBuffer();
        int size = list.size();
        switch (size) {
            case 1:
                for (String s : list.get(0).keySet()) {
                    String str = s;
                    sbf.append(str + ",");
                }
                break;
            case 2:
                for (String s : list.get(0).keySet()) {
                    for (String s1 : list.get(1).keySet()) {
                        String str = s + s1;
                        sbf.append(str + ",");
                    }
                }
                break;
            case 3:
                for (String s : list.get(0).keySet()) {
                    for (String s1 : list.get(1).keySet()) {
                        for (String s2 : list.get(2).keySet()) {
                            String str = s + s1 + s2;
                            sbf.append(str + ",");
                        }
                    }
                }
                break;
            // 此处省略了数据组装过程，组装后的数据结构如下。
            // 注:List<Map<String, Integer>> list：List存的就是有多少组数据上面的是4组
            // Map就是具体的某一个数组（此处用Map主要是方便对其中数组中重复元素作处理）
            // StringBuffer sbf = new StringBuffer();--用于记录组合字符的缓冲器
            case 4:
                for (String s : list.get(0).keySet()) {
                    for (String s1 : list.get(1).keySet()) {
                        for (String s2 : list.get(2).keySet()) {
                            for (String s3 : list.get(3).keySet()) {
                                String str = s + s1 + s2 + s3;
                                // 此处的sbf为StringBuffer
                                sbf.append(str + ",");
                            }
                        }
                    }
                }
                break;
            case 5:
                for (String s : list.get(0).keySet()) {
                    for (String s1 : list.get(1).keySet()) {
                        for (String s2 : list.get(2).keySet()) {
                            for (String s3 : list.get(3).keySet()) {
                                for (String s4 : list.get(4).keySet()) {
                                    String str = s + s1 + s2 + s3 + s4;
                                    sbf.append(str + ",");
                                }
                            }
                        }
                    }
                }
                break;
            case 6:
                for (String s : list.get(0).keySet()) {
                    for (String s1 : list.get(1).keySet()) {
                        for (String s2 : list.get(2).keySet()) {
                            for (String s3 : list.get(3).keySet()) {
                                for (String s4 : list.get(4).keySet()) {
                                    for (String s5 : list.get(5).keySet()) {
                                        String str = s + s1 + s2 + s3 + s4 + s5;
                                        sbf.append(str + ",");
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case 7:
                for (String s : list.get(0).keySet()) {
                    for (String s1 : list.get(1).keySet()) {
                        for (String s2 : list.get(2).keySet()) {
                            for (String s3 : list.get(3).keySet()) {
                                for (String s4 : list.get(4).keySet()) {
                                    for (String s5 : list.get(5).keySet()) {
                                        for (String s6 : list.get(6).keySet()) {
                                            String str = s + s1 + s2 + s3 + s4 + s5 + s6;
                                            sbf.append(str + ",");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }

        //语句
        String str = sbf.toString();
        return str.substring(0, str.length() - 1);
    }

    /**
     * 根据返回长度处理获取中文对应的全拼和简拼
     *
     * @param cnStr
     * @param length
     */
    public static String getFullAndSimplePinYin(String cnStr, int length) {
        String fullPinYin = PinYin4JCn.converterToSpell(cnStr);
        String simplePinYin = PinYin4JCn.converterToFirstSpell(cnStr);
        String pinYin = fullPinYin + "," + simplePinYin;
        if (fullPinYin.length() > length) {
            //全拼长度超过可返回长度时返回全拼截取
            pinYin = fullPinYin.substring(0, length);
            return pinYin;
        }
        if (simplePinYin.length() > length) {
            //简拼长度超过可返回长度时返回简拼截取
            pinYin = fullPinYin + "," + simplePinYin.substring(0, (length - fullPinYin.length() - 1));
            return pinYin;
        }
        if (pinYin.length() > length) {
            pinYin = pinYin.substring(0, length);
            return pinYin;
        }
        return pinYin;
    }

    public static void main(String[] args) throws Exception {
        String ddd = PinYin4JCn.getFullAndSimplePinYin("手动阀是的发送到", 20);
        System.out.println(ddd);
    }
}
