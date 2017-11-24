package com.vito.common.util.string;

import com.vito.common.util.validate.Validator;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String replaceLink2CamelCase(String str, String linkStr) {
        return replaceLink2CamelCase(str, linkStr, false);
    }

    public static String replaceLink2CamelCase(String str, String linkStr, boolean isUpperFirstChar) {
        if (Validator.isNull(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        String[] partStrs = str.toLowerCase().split(linkStr);
        int i = 0;
        for (String partStr : partStrs) {
            boolean isUpper;
            if (isUpperFirstChar) {
                isUpper = true;
            } else {
                isUpper = i++ > 0;
            }
            if (isUpper) {
                sb.append(Character.toUpperCase(partStr.charAt(0)));
                sb.append(partStr.substring(1, partStr.length()));
            } else {
                sb.append(partStr);
            }
        }
        return sb.toString();
    }

    /**
     * 将驼峰转成下划线（或其他连接符）
     *
     * @param str
     * @return 用连接符连接，全大写字符串
     */
    public static String replaceCamelCase2Link(String str, String linkStr) {
        List record = new ArrayList();
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
            if ((tmp <= 'Z') && (tmp >= 'A') && i != 0) {
                record.add(i);//记录每个大写字母的位置
            }

        }

        str = str.toLowerCase();
        char[] charofstr = str.toCharArray();
        String[] t = new String[record.size()];
        for (int i = 0; i < record.size(); i++) {
            t[i] = linkStr + charofstr[(int) record.get(i)];//加“_”
        }
        String result = "";
        int flag = 0;
        for (int i = 0; i < str.length(); i++) {
            if ((flag < record.size()) && (i == (int) record.get(flag))) {
                result += t[flag];
                flag++;
            } else {
                result += charofstr[i];
            }
        }

        return result.toUpperCase();
    }

    public static String removeHtmlMark(String htmlStr) {
        if (StringUtils.isBlank(htmlStr)) {
            return null;
        }
        return htmlStr.replaceAll("&nbsp;?", " ").replaceAll("&gt;?", ">").replaceAll("&lt;?", "<").replaceAll("&#149;", "").replaceAll("&quot;", "\"")
                .replaceAll("(?i)(?s)<style.*?</style>", "").replaceAll("(?i)(?s)<script.*?</script>", "").replaceAll("(?i)(?s)<[^>]*>", " ")
                .replaceAll("[\t ]+", " ").replaceAll(" +", " ").replaceAll("( *\r?\n *)+", "\r\n")
                .replaceAll("\r\n([^\r\n]{1,15})\r\n([^\r\n]{1,15})\r\n", "\r\n$1\t$2\r\n").replaceAll("(\r?\n){2,}", "\r\n").replaceAll(" +", " ");
    }

    /**
     * 作者: zhaixm
     * 版本: 2014-5-28 下午12:49:48
     * 日期: 2014-5-28
     * 参数: @param htmlStr
     * 参数: @param beginIndex
     * 参数: @param endIndex
     * 参数: @return
     * 返回: String
     * 描述: 截取html字符串
     */
    public static String subHtmlString(String htmlStr, int beginIndex, int endIndex) {
        String subStr = htmlStr;
        subStr = htmlStr.substring(beginIndex, endIndex);
        subStr = removeUncompleteTag(subStr);
        subStr = fillUncloseTag(subStr);
        return subStr;
    }

    /**
     * 作者: zhaixm
     * 版本: 2014-5-28 下午12:52:31
     * 日期: 2014-5-28
     * 参数: @param htmlStr
     * 参数: @return
     * 返回: List<String>
     * 描述: 补足未关闭的html标签
     */
    public static String fillUncloseTag(String htmlStr) {
        List<String> uncloseTags = findUncloseTags(htmlStr);
        for (String uncloseTag : uncloseTags) {
            htmlStr += uncloseTag;
        }
        return htmlStr;
    }

    /**
     * 作者: zhaixm
     * 版本: 2014-5-28 下午12:52:31
     * 日期: 2014-5-28
     * 参数: @param htmlStr
     * 参数: @return
     * 返回: List<String>
     * 描述: 查找未关闭的html标签：如<font color='red'>人才</font>被截断为<font color='red'>人，该方法会返回</font>
     */
    public static List<String> findUncloseTags(String htmlStr) {
        List<String> uncloseTags = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<span.*?>");
        Matcher matcher = pattern.matcher(htmlStr);
        int startTagNum = 0;
        while (matcher.find()) {
            startTagNum++;
        }
        pattern = Pattern.compile("</span>");
        matcher = pattern.matcher(htmlStr);
        int endTagNum = 0;
        while (matcher.find()) {
            endTagNum++;
        }
        int missTagNum = startTagNum - endTagNum;
        if (missTagNum > 0) {
            for (int i = 0; i < missTagNum; i++) {
                uncloseTags.add("</span>");
            }
        }
        return uncloseTags;
    }

    /**
     * 作者: zhaixm
     * 版本: 2014-5-28 下午12:50:21
     * 日期: 2014-5-28
     * 参数: @param htmlStr
     * 参数: @return
     * 返回: String
     * 描述: 去除被截断的html标签：如 <font color='red'>被截断为<font col，该方法可将<font col去除掉
     */
    private static String removeUncompleteTag(String htmlStr) {
        int rightBraceIdx = htmlStr.lastIndexOf(">");
        int leftBraceIdx = htmlStr.lastIndexOf("<");
        if (leftBraceIdx > rightBraceIdx) {
            return htmlStr.substring(0, leftBraceIdx);
        } else {
            return htmlStr;
        }
    }

    public static String reverse(String s) {
        if (s == null) {
            return null;
        }

        char[] c = s.toCharArray();
        char[] reverse = new char[c.length];

        for (int i = 0; i < c.length; i++) {
            reverse[i] = c[c.length - i - 1];
        }

        return new String(reverse);
    }

    public static String extractDigits(String s) {
        if (s == null) {
            return StringPool.BLANK;
        }

        StringBuffer sm = new StringBuffer();

        char[] c = s.toCharArray();

        for (int i = 0; i < c.length; i++) {
            if (Validator.isDigit(c[i])) {
                sm.append(c[i]);
            }
        }
        return sm.toString();
    }

    public String getStringID() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        int tmp = 10 + new Random().nextInt(90);
        return sdf.format(now) + tmp;
    }


//    public static String getRandomStr(int length) {
//        String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
//        Random random = new Random();
//        StringBuffer sf = new StringBuffer();
//        for (int i = 0; i < length; i++) {
//            int number = random.nextInt(62);//0~61
//            sf.append(str.charAt(number));
//        }
//        return sf.toString();
//    }

    public static <T> T cast(String value, Class<T> requiredType) {
        if (Validator.isNull(value)) {
            return null;
        }
        if (Integer.class.isAssignableFrom(requiredType)) {
            return (T) Integer.valueOf(value);
        } else if (Long.class.isAssignableFrom(requiredType)) {
            return (T) Long.valueOf(value);
        } else if (Short.class.isAssignableFrom(requiredType)) {
            return (T) Short.valueOf(value);
        } else if (Double.class.isAssignableFrom(requiredType)) {
            return (T) Double.valueOf(value);
        } else if (Boolean.class.isAssignableFrom(requiredType)) {
            return (T) Boolean.valueOf(value);
        } else if (Float.class.isAssignableFrom(requiredType)) {
            return (T) Float.valueOf(value);
        } else if (BigDecimal.class.isAssignableFrom(requiredType)) {
            return (T) BigDecimal.valueOf(cast(value, Double.class));
        }
        return requiredType.cast(value);
    }

    public static byte[] getBytes(String str) {
        byte[] bytes = null;
        try {
            if (Validator.isNotNull(str)) {
                bytes = str.getBytes(EncodeUtil.UTF_8);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的字符串编码", e);
        }
        return bytes;
    }

    public static String getStr(byte[] bytes) {
        String str = null;
        try {
            if (Validator.isNotNull(bytes)) {
                str = new String(bytes, EncodeUtil.UTF_8);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的字符串编码", e);
        }
        return str;
    }

    public static String formatDouble(Double d) {
        if (d == null) {
            return "";
        }
        return new BigDecimal(d).stripTrailingZeros().toString();
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.replaceLink2CamelCase("primary-col", "-"));
        System.out.println(new BigDecimal(4800.0d).toString());
        System.out.println(formatDouble(4800.0d));

        System.out.println(StringUtil.replaceCamelCase2Link("UserName", "_"));
    }

}
