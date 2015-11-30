package com.okar.icz.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 判断是否是手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 获取字符串后面的数字
     * s1s123=123  s1=1
     */
    public static String rearNumber(String str) {
        if (!rearHasNumber(str)) return "";
        return str.replaceAll(".*[^\\d](?=(\\d+))", "");
    }

    /**
     * 判断字符串后面是否有数字
     *
     *  a = false b1 = true b1b = false
     */
    public static boolean rearHasNumber(String str) {
        String regex = ".*[^\\d](?=(\\d+))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 获取字符串 数字前面的前缀
     * <p/>
     * s1s123=s1s  s1=s
     */
    public static String numberFrontStr(String str) {
        return str.replaceAll("\\d+$", "");
    }


    /**
     * 带0的字符串累加
     */
    public static String numberStringMod(String str, int mod) {

        String result = "";

        if (str == null || "".equals(str)) {
            return str;
        }

        Integer num = Integer.parseInt(str);
        int l = str.length() - num.toString().length();

        num = num + mod;
        if (num < 0) {
            num = 0;
        }
        result = num.toString();
        for (int i = 0; i < l; i++) {
            result = "0" + result;
        }
        return result;
    }


    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean isBlank(String s) {
        return s == null || (s.trim()).equals("") || (s.trim()).equals("null");
    }

    public static void main(String[] args) {
        System.out.println("a"+numberFrontStr("123456"));
    }

}
