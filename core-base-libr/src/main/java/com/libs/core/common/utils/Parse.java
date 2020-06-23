package com.libs.core.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parse {

    public static Parse parse;
    /**
     * 最多保留2位小数
     */
    public static DecimalFormat df = new DecimalFormat("#0.00");
    public static DecimalFormat ddf = new DecimalFormat("#0.##");

    /**
     * 私有构造函数，保证单例模式
     */
    private Parse() {

    }

    public static Parse getInstance() {
        if (parse == null) {
            parse = new Parse();
        }
        return parse;
    }

    /**
     * 判断Map中的值是否为空
     *
     * @param obj
     * @return String
     */
    public String isNull(Object obj) {
        if (obj == null) {
            obj = "";
        }
        return obj.toString();
    }

    /**
     * 判断Map中的值是否为空
     *
     * @param obj
     * @param def
     * @return String
     */
    public String isNull(Object obj, String def) {
        if (obj == null) {
            obj = def;
        }
        return obj.toString();
    }

    /**
     * 将Object对象转换成Map
     *
     * @param obj
     * @return ArrayList
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> parseMap(Object obj) {
        Map<String, Object> map = null;
        if (obj == null) {
            map = new HashMap<String, Object>();
            return map;
        }
        try {
            map = (Map<String, Object>) obj;
        } catch (Exception e) {
            map = new HashMap<String, Object>();
        }
        return map;
    }

    /**
     * 将Object对象转换成ArrayList
     *
     * @param obj
     * @return ArrayList
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> parseList(Object obj) {
        ArrayList<Map<String, Object>> list = null;
        if (obj == null) {
            list = new ArrayList<Map<String, Object>>();
            return list;
        }
        try {
            list = (ArrayList<Map<String, Object>>) obj;
        } catch (Exception e) {
            list = new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    /**
     * 转成float类型
     *
     * @param obj
     * @return float
     */
    public float parseFloat(Object obj) {
        float fl;
        String str = isNull(obj);
        if ("".equals(str) || "null".equals(str)) {
            return 0.0f;
        } else {
            try {
                fl = Float.parseFloat(str);
            } catch (Exception e) {
                // TODO: handle exception
                return 0.0f;
            }
        }
        return fl;
    }

    /**
     * 转成double类型
     *
     * @param obj
     * @return double
     */
    public double parseDouble(Object obj) {
        double db;
        String str = isNull(obj);
        if ("".equals(str) || "null".equals(str)) {
            return 0;
        } else {
            try {
                db = Double.parseDouble(str);
                return db;
            } catch (Exception e) {
                return 0;
            }
        }
    }

    /**
     * 转成double类型,保留小数点后多少位（四舍五入）
     *
     * @param obj
     * @param num 例：保留后两位"#.##";保留后三位"#.###"……
     * @return double
     */
    public double parseDouble(Object obj, String num) {
        double db;
        String str = isNull(obj);
        if ("".equals(str) || "null".equals(str)) {
            return 0;
        } else {
            try {
                String numLength = num.replace("#.", "");
                db = Double.parseDouble(str)
                        + Math.pow(0.1, numLength.length() + 1);
                db = Double.parseDouble(new DecimalFormat(num).format(db));
                return db;
            } catch (Exception e) {
                return 0;
            }
        }
    }

    /**
     * 将数字转为四舍五入保留2位小数的字符串，末尾补0
     *
     * @param obj
     * @return string
     */
    public String parse2String(Object obj) {
        String str = isNull(obj);
        if ("-".equals(str)) {
            return "-";
        }
        double d = parseDouble(str);
        // 超过2000的价格就是最多显示两位小数
        if (d > 2000)
            return ddf.format(d);
        // 2000以内价格，必须显示2位
        return df.format(d);
    }

    /**
     * 将数字4舍5入到任意小数位
     *
     * @param obj
     * @param num
     * @return
     */
    public String parse2String(Object obj, int num) {
        double d = parseDouble(obj);
        return String.format("%." + num + "f", d);
    }

    public String parseDouble2String(double value, int num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(num);//设置最小保留2位小数，不足则补0
        nf.setMinimumFractionDigits(num);//设置最大保留2位小数
        nf.setGroupingUsed(false);//不进行分组
        return nf.format(value);
    }

    /**
     * 将数字4舍5入到任意小数位
     *
     * @param obj
     * @param num
     * @return
     */
    public String parse2String(Object obj, int num, boolean isJiaHao) {
        double d = parseDouble(obj);
        if (isJiaHao && d > 0) return "+" + String.format("%." + num + "f", d);
        else return String.format("%." + num + "f", d);
    }

    /**
     * 数字转化为保留num位小数的字符串，自动解析亿，万，四舍五入
     *
     * @param num    小数的位数
     * @param div100 主要针对成交量需要除以100，转换为手；true为去掉最后两位
     * @return 例如：10000000 -》 1.0亿
     */
    public String parse2CNString(Object obj, int num, boolean div100) {

        double dou = parseDouble(obj);
        String str = String.format("%." + num + "f", dou);
        String oldstr = null;
        if (str.contains("."))
            oldstr = str.split("\\.")[0];
        else
            oldstr = str;
        if (div100) {
            // 两位数字以下，返回0手
            if (oldstr.length() <= 2) {
                oldstr = "0";
            } else {
                oldstr = oldstr.substring(0, oldstr.length() - 2);
            }
        }
        String newstr = "";

        if (oldstr.length() > 8) {

            newstr += String.format("%." + num + "f",
                    parseDouble(oldstr) / 100000000) + "亿";
            return newstr;
        }

        if (oldstr.length() > 4) {
            newstr += String.format("%." + num + "f",
                    parseDouble(oldstr) / 10000) + "万";
            return newstr;
        }
        return oldstr;
    }

    /**
     * 数字转化为保留num位小数的字符串，自动解析亿，万，四舍五入,不满足万级别数将保留num位小数返回
     *
     * @param num 小数的位数
     * @return 例如：10000000.555 -》 1.00亿,例如：1111.555 -》 1111.56
     */
    public String parse2Money(Object obj, int num) {
        double dou = parseDouble(obj);
        if (num <= 0) {
            return isNull(dou);
        }
        String str = String.format("%." + num + "f", dou);
        String[] strs = str.split("\\.");
        String oldstr = strs[0];
        String newstr = "";
        if (oldstr.length() > 8) {
            newstr += String.format("%." + num + "f",
                    parseDouble(oldstr) / 100000000) + "亿";
            return newstr;
        }
        if (oldstr.length() > 4) {
            newstr += String.format("%." + num + "f",
                    parseDouble(oldstr) / 10000) + "万";
            return newstr;
        }
        if (oldstr.length() <= 4) {
            return str;
        }
        return oldstr;
    }

    /**
     * 数字转化为保留num位小数的字符串，自动解析 万，四舍五入
     *
     * @param num    小数的位数
     * @param div100 主要针对成交量需要除以100，转换为手；true为去掉最后两位
     * @return 例如：10000000 -》 1.0
     */
    public String parse2CNStringWan(Object obj, int num, boolean div100) {
        double dou = parseDouble(obj);
        String str = String.format("%.2f", dou);
        String oldstr = null;
        if (str.contains("."))
            oldstr = str.split("\\.")[0];
        else
            oldstr = str;
        if (div100) {
            // 两位数字以下，返回0手
            if (oldstr.length() <= 2) {
                oldstr = "0";
            } else {
                oldstr = oldstr.substring(0, oldstr.length() - 2);
            }
        }
        String newstr = "";

        if (oldstr.length() > 4) {
            newstr += String.format("%." + num + "f",
                    parseDouble(oldstr) / 10000) + "万";
            return newstr;
        }

        return oldstr;
    }

    /**
     * 数字转化为保留num位小数的字符串，自动解析 万,亿，四舍五入
     *
     * @param num    小数的位数
     * @param div100 主要针对成交量需要除以100，转换为手；true为去掉最后两位
     * @return 例如：10000000 -》 1.0
     */
    public String parse2CNStringWanYi(Object obj, int num, boolean div100) {
        double dou = parseDouble(obj);
        String str = String.format("%.2f", dou);
        String oldstr = null;
        if (str.contains("."))
            oldstr = str.split("\\.")[0];
        else
            oldstr = str;
        if (div100) {
            // 两位数字以下，返回0手
            if (oldstr.length() <= 2) {
                oldstr = "0";
            } else {
                oldstr = oldstr.substring(0, oldstr.length() - 2);
            }
        }
        String newstr = "";

        if (oldstr.length() > 4) {
            newstr += String.format("%." + num + "f",
                    parseDouble(oldstr) / 10000) + "万";
            if(parseDouble(oldstr) / 10000 > 1000){
                newstr = String.format("%." + num + "f",
                        parseDouble(oldstr) / 10000000) + "亿";
            }
            return newstr;
        }

        return oldstr;
    }

    /**
     * 数字转化为保留num位小数的字符串，自动解析亿，万，四舍五入
     *
     * @param obj
     * @param num 小数的位数
     * @return 例如：10000000 -》 1.0亿
     */
    public String parse2CNString(Object obj, int num) {
        return parse2CNString(obj, num, false);
    }

    /**
     * 数字转化为保留1位小数的字符串，自动解析亿，万，不四舍五入
     *
     * @param obj
     * @return
     */
    public String parse2CNString(Object obj) {
        return parse2CNString(obj, 1);
    }

    /**
     * 转成int类型
     *
     * @param obj
     * @return int
     */
    public int parseInt(Object obj) {
        int in;
        String str = isNull(obj);
        if ("".equals(str) || "null".equals(str)) {
            return 0;
        } else {
            try {
                in = Integer.parseInt(str);
                return in;
            } catch (Exception e) {
                // TODO: handle exception
                return 0;
            }
        }
    }

    /**
     * 四舍五入到万、亿级别，没有则返回实数
     *
     * @param obj
     * @return
     */
    public String parseToCHString(Object obj) {
        String str = isNull(obj);
        if ("".equals(str) || "-".equals(str) || "--".equals(str)) {
            return "--";
        }
        if (str.length() < 5) {
            return str;
        } else if (str.length() < 9) {
            int in = parseInt(str.substring(str.length() - 4, str.length() - 3));
            if (in < 5) {
                str = str.substring(0, str.length() - 4);
            } else {
                in = parseInt(str.subSequence(0, str.length() - 4)) + 1;
                str = isNull(in);
            }
            return str + "万";
        } else {
            int in = parseInt(str.substring(str.length() - 8, str.length() - 7));
            if (in < 5) {
                str = str.substring(0, str.length() - 8);
            } else {
                in = parseInt(str.subSequence(0, str.length() - 8)) + 1;
                str = isNull(in);
            }
            return str + "亿";
        }
    }

    /**
     * 转成long类型
     *
     * @param obj
     * @return int
     */
    public long parseLong(Object obj) {
        long lo;
        String str = isNull(obj);
        if ("".equals(str) || "null".equals(str)) {
            return 0;
        } else {
            try {
                lo = Long.parseLong(str);
                return lo;
            } catch (Exception e) {
                return 0;
            }
        }
    }

    /**
     * 转成boolean类型
     *
     * @param obj
     * @return boolean
     */
    public boolean parseBool(Object obj) {
        boolean bool;
        String str = isNull(obj);
        if ("".equals(str) || "null".equals(str)) {
            bool = false;
        } else {
            try {
                bool = Boolean.parseBoolean(str);
            } catch (Exception e) {
                bool = false;
            }
        }
        return bool;
    }
}

