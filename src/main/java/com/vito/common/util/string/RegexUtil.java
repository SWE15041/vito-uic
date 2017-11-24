/**  
 * @Title RegexUtil.java  
 * @Package com.meiah.core.util  
 * @Description  
 * @author "zhangshaofeng"  
 * @time Sep 13, 2011 11:45:16 AM  
 */

package com.vito.common.util.string;

import com.vito.common.util.validate.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * 
 * @author "zhangshaofeng"
 * 
 */
public class RegexUtil {

	/**
	 * 判断是否是IP地址
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIPAdress(String str) {
		Pattern pattern = Pattern
				.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 判断是否为URL
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url){
		 return url.matches("[a-zA-z]+://[^\\s]*");  
	}

	/**
	 * 判断是否为手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){
//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
//		String str = "^((13)|(15)|(18))\\d{9}$";
		Matcher m = p.matcher(mobiles);
		return m.matches();
	} 
	
	/**
	 * 判断是否为电话号码
	 * @param telephone
	 * @return
	 */
	public static boolean isTelephone(String telephone){
//		Pattern p = Pattern.compile("\\d{7,8}");
		Pattern p = Pattern.compile("\\d{5,8}");	//改成5位开始
		Matcher m = p.matcher(telephone);
		return m.matches();
	} 
	
	/**
	 * 过滤网站名称的特殊字符，-前面的名称
	 * 
	 * @param name
	 * @return
	 */
	public static String filterWebName(String name) {
		String temp=name.replaceAll("\"", "").replaceAll("\'", "");
		return temp.split("-|--|_|—| |―")[0];
	}
	
	/**
	 * 预处理事件关键词，
	 * 将所有的AND、OR特殊字符转成空格
	 * 最后全部转成OR关系（同一组关键词，只包含OR的关系）
	 * @param word
	 * @return
	 */
	public static String replaceEventWord(String word){
		if (Validator.isNotNull(word)) {
			word = word.replaceAll("(?i) AND "," ").replaceAll("(?i) OR "," ");//输入的关键词先替换成空格
			String replaceResult=" ";
			word=RegexUtil.replaceBlank(word, replaceResult).replaceAll(" ", " OR ");
			
			if(word.indexOf(" OR ")>-1){
				word="("+word+")";
			}
			return word;
		}
		return "";
	}
	
	/**
	 * 组装关键词表达式
	 * @param exp
	 * @param word
	 * @return
	 */
	public static String uniteExp(String exp,String word){
		if (Validator.isNotNull(word)) {
			if (Validator.isNotNull(exp)) {
				exp+=" AND ";
			}
			exp+=word;
		}
		return exp;
	}
	
	/**
	 * 将字符串中多余的空格转换为特定的字符
	 * @param word 字符串
	 * @param result 特定的字符
	 * @return
	 * @author "zhangshaofeng"  
	 * @time Sep 5, 2012 4:13:43 PM
	 */
	public static String replaceBlankTo(String word,String result){
		if (Validator.isNotNull(word)) {
			String replaceResult=" ";
			word=RegexUtil.replaceBlank(word, replaceResult).replace(" ", result);
			return word;
		}
		return "";
	}
	
	/**
	 * 替换表达式中多余的空格
	 * @param exp
	 * @return
	 */
	public static String replaceBlank(String exp,String result){
		if (Validator.isNotNull(exp)) {
			exp=exp.trim().replaceAll("\\s{2,}", result).replaceAll("　", result).replaceAll(" ", result).replaceAll("　{2,}",result).replaceAll(" {2,}",result);
			return exp;
		}
		return "";
		
	}
	/**
	 * 过滤查询的表达式中的 AND  OR
	 * @param exp
	 * @return
	 */
	public static String replaceExp(String exp){
		if (Validator.isNotNull(exp)) {
			//先将多余的空格去掉
			exp = exp.replaceAll("\\s{2,}", " ");
			
			//替换关键字
			exp = exp.replaceAll("(?i) and ", " AND ").replaceAll("(?i) or ", " OR ").replaceAll("[+]", " AND ").replaceAll("[|]", " OR ").replaceAll("（", "(").replaceAll("）", ")");
			
			//替换后再将多余空格去掉
			exp = exp.replaceAll("\\s{2,}", " ");
			
			return exp;
		}
		return "";
	}
	
	/**
	 * 过滤聚焦关键词特殊字符以符合网聚关键词
	 * @param keyword
	 * @return
	 */
	public static String replaceYqKeywordToMige(String keyword){
		if (Validator.isNotNull(keyword)) {
			return keyword.replaceAll("(?i)and", " ").replaceAll("\\+", " ").replaceAll("(?i)or", "|").replaceAll("\\|", " ").replaceAll("\\(", " ").replaceAll("\\)", " ").replaceAll("\\（", " ").replaceAll("\\）", " ");
		}
		return "";
	}
	
	/**
	 * 将聚焦关键词的表达式中的 AND  OR 转为|+
	 * @param exp
	 * @return
	 */
	public static String convertYqCustomKeywWord(String exp){
		if (Validator.isNotNull(exp)) {
			return RegexUtil.replaceBlank(exp, "").replaceAll("(?i)and", "+").replaceAll("(?i)or", "|").replaceAll("\\s{2,}", "").replaceAll("（", "(").replaceAll("）", ")");
		}
		return "";
	}
	
	
	/**
	 * 将聚焦关键词的表达式中的 AND  OR 转为 |
	 * @param exp
	 * @return
	 */
	public static String convertMigeKeywWord(String exp){
		if (Validator.isNotNull(exp)) {
			return exp.replaceAll("(?i)and", " ").replaceAll("\\+", " ").replaceAll("(?i)or", "|").replaceAll("\\s{2,}", "").replaceAll("（", "(").replaceAll("）", ")");
		}
		return "";
	}
	
   /**
    *  替换换行符
    * @param exp
    * @return
    * @author "zhangshaofeng"  
    * @time Mar 21, 2013 11:36:55 AM
    */
	public static String replaceLine(String exp) {      
        if (Validator.isNotNull(exp)) {
        	return exp.replaceAll("\t|\r|\n", "");
		}
        return "";
    }     
	
	/**
	 * 用OR分隔关键词（包含英文词组）
	 */
	public static String getKeyWords(String keyWordStr) {
		if(null == keyWordStr || keyWordStr.trim().isEmpty())
			return null;
		List<String> keyWordList = new ArrayList<String>();
		String str = keyWordStr;
		Integer qLeftIndex = 0;
		Integer qRightIndex = 0;
		String wordOrg = null;
		while((qLeftIndex = str.indexOf("\"")) != -1) {
			qRightIndex = str.indexOf("\"", qLeftIndex + 1);
			if(qRightIndex == -1) {
				str = str.replaceAll("\"", "");
				break;
			}
			wordOrg = str.substring(qLeftIndex + 1, qRightIndex);
			keyWordList.add(wordOrg);
			str = str.substring(0, qLeftIndex).trim() + " " + str.substring(qRightIndex + 1, str.length()).trim();
			str = str.trim();
		}
		if(keyWordList.isEmpty())
			return str.replaceAll(" ", " OR ");
		else if(Validator.isNotNull(str))
			keyWordList.addAll(Arrays.asList(str.split(" ")));
		String result = "";
		for (String keyword : keyWordList) {
			result = result.concat(" OR ").concat(keyword);
		}
		return result.replaceFirst(" OR ", "");
	}
	
	/**
	 * 关键词过滤
	 * @param keyword
	 * and前后只要一个是字母就不转换;
	 * or前后只要一个是字母就不转换;
	 * 中文括号转换成英文括号;
	 * 替换换页符;
	 * 替换换行符;
	 * 替换回车符;
	 * 替换制表符;
	 * 替换垂直制表符;
	 * 替换空格;
	 * @return
	 */
	public static String formatKeyword(String keyword){
		String result = keyword;//(?i)忽略大小写
		result = result
				.replaceAll("(?i)d([^A-Za-z])(?i)a", "d $1 a")//正则的一个问题，需先这样处理
				.replaceAll("(?i)r([^A-Za-z])(?i)o", "r $1 o")
				.replaceAll("([^A-Za-z])(?i)or([^A-Za-z])", "$1|$2")//or前后只要一个是字母就不转换
				.replaceAll("([^A-Za-z])(?i)and([^A-Za-z])", "$1+$2")//and前后只要一个是字母就不转换
				.replaceAll("（", "(").replaceAll("）", ")")//中文括号转换成英文括号
				.replaceAll("\\f+", "")//替换换页符
				.replaceAll("\\n+", "")//替换换行符
				.replaceAll("\\r+", "")//替换回车符
				.replaceAll("\\t+", "")//替换制表符
				.replaceAll("\\v+", "")//替换垂直制表符
				.replaceAll("\\s+", "")//替换空格
				.replaceAll("\\|+", "|")//连续出现|，替换成一次
				.replaceAll("\\++", "+");//连续出现+，替换成一次
		return result;
	}
	
	/**
	 * 正则匹配
	 * 
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static String[] match(String s, String pattern) {
		Matcher m = Pattern.compile(pattern).matcher(s);

		while (m.find()) {
			int n = m.groupCount();
			String[] ss = new String[n + 1];
			for (int i = 0; i <= n; i++) {
				ss[i] = m.group(i);
			}
			return ss;
		}
		return null;
	}
	
	/**
	 * 正则匹配
	 * 
	 * @param s
	 * @param pattern
	 * @return
	 */
	public static List<String[]> matchAll(String s, String pattern) {
		Matcher m = Pattern.compile(pattern).matcher(s);
		List<String[]> result = new ArrayList<String[]>();

		while (m.find()) {
			int n = m.groupCount();
			String[] ss = new String[n + 1];
			for (int i = 0; i <= n; i++) {
				ss[i] = m.group(i);
			}
			result.add(ss);
		}
		return result;
	}
	
	/**
	 * 判断and or是单词还是字符（判断前后的字符，如果是不是字母就在前后加空格）
	 * @param keywordStr
	 * @return
	 */
	public static String formatKeywords(String keywordStr){
		if(null == keywordStr || keywordStr.trim().isEmpty()){
			return null;
		}
		//keywordStr = keywordStr.replaceAll("\\s+", "");//先把空格去掉
		System.out.println(keywordStr);
		int ai = keywordStr.indexOf("and");
		System.out.println(ai);
		List<String[]> andList = matchAll(keywordStr, "(.and.)");
		if(Validator.isNotNull(andList)){
			for(String[] as: andList){
				System.out.println("++++++++++"+as[1]);
				/*for(String a:as){
					System.out.println("++++++++++"+a);
				}*/
			}
		}
		List<String[]> orList = matchAll(keywordStr, "(.or.)");
		if(Validator.isNotNull(orList)){
			for(String[] os: orList){
				System.out.println("-----------"+os[1]);
				/*for(String o:os){
					System.out.println("---------"+o);
				}*/
			}
		}
		String result = "";
		return result;
	}
	
	/** 
     * 查询字符串searchValue在字符串str中的位置索引(最多查询3位长度字符) 
     * @param str 一组字符串 
     * @param searchValue 要查找的字符串 
     * @return List<Integer> 存储查找的字符串索引 
     */  
    public static List<Integer> searchStrIndex(String str,String searchValue){  
        //定义存储查找的字符串索引集合  
        List<Integer> searchIndexList = new ArrayList<Integer>();  
          
        for(int i=0;i<str.length();i++){  
            if(searchValue.length()==1){  
                String ch1 = String.valueOf(str.charAt(i));  
                if(ch1.equalsIgnoreCase(searchValue)){  
                    searchIndexList.add(i);  
                }  
            }else if(searchValue.length()==2){  
                String ch1 = String.valueOf(str.charAt(i));  
                String ch2;  
                if(i<str.length()-1){  
                    ch2 = String.valueOf(str.charAt(i+1));  
                    String value = ch1+ch2;  
                    if(value.equalsIgnoreCase(searchValue)){  
                        searchIndexList.add(i);  
                    }  
                }  
            }else if(searchValue.length()==3){  
                String ch1 = String.valueOf(str.charAt(i));  
                String ch2;  
                String ch3;  
                if(i<str.length()-1){  
                    ch2 = String.valueOf(str.charAt(i+1));  
                    if(i<str.length()-2){  
                        ch3 = String.valueOf(str.charAt(i+2));  
                        String value = ch1+ch2+ch3;  
                        //System.out.println(value);  
                        if(value.equalsIgnoreCase(searchValue)){  
                            searchIndexList.add(i);  
                        }  
                    }  
                }  
            }  
        }  
        return searchIndexList;  
    }  
      
      
	
	public static String formatKeywordsNotDone(String[] args){
		/*String input="t and 星星 | 哈哈 and andorid or 太阳operator or t";
		formatKeywords(input);*/
		String input="t and 星星 | 哈哈 And aNdorid or 太阳operator Or tand风";
		//input = input.replaceAll("\\s+", "");
		List<Integer> searchIndexList = searchStrIndex(input,"and");  
        for(Integer index : searchIndexList){  
            System.out.println("index   1:   "+index.intValue());
        } 
        StringBuilder andsb = new StringBuilder();
        for(int i = 0; i < searchIndexList.size(); i++){
        	System.out.println("++++++++++++"+input.substring(searchIndexList.get(i)-1, searchIndexList.get(i)+4));
        	System.out.println("+++"+input.charAt(searchIndexList.get(i)-1)+"+++"+input.charAt(searchIndexList.get(i)+3));
        	int a = (int)input.charAt(searchIndexList.get(i)-1);
        	int b = (int)input.charAt(searchIndexList.get(i)+3);
        	System.out.println(a+"   "+b);
        	//大写字母ask2码值65——90，小写字母ask2码值97——122
        	if( !((a>=65 && a<=90) || (a>=97 && a<=122) || (b>=65 && b<=90) || (b>=97 && b<=122)) ){
        		System.out.println("转换@@@@@@@@@@@@@@@@@@@@");
        	}
        	if(i==0) andsb.append(input.substring(0, searchIndexList.get(i)));
        	if(i==searchIndexList.size()-1){ 
        		if( !((a>=65 && a<=90) || (a>=97 && a<=122) || (b>=65 && b<=90) || (b>=97 && b<=122)) ){
            		andsb.append("+");
            		System.out.println("^^^^^^^^^^^^^^"+input.substring(searchIndexList.get(i)+3));
            		andsb.append(input.substring(searchIndexList.get(i)+3));
            	}else{
            		andsb.append(input.substring(searchIndexList.get(i)));
            		System.out.println("^^^^^^^^^^^^"+input.substring(searchIndexList.get(i)));
            	}
        		
        	}else{
        		if( !((a>=65 && a<=90) || (a>=97 && a<=122) || (b>=65 && b<=90) || (b>=97 && b<=122)) ){
            		andsb.append("+");
            		System.out.println(input.substring(searchIndexList.get(i)+3,searchIndexList.get(i+1)));
            		andsb.append(input.substring(searchIndexList.get(i)+3,searchIndexList.get(i+1)));
            	}else{
            		System.out.println("********"+input.substring(searchIndexList.get(i),searchIndexList.get(i+1)));
            		andsb.append(input.substring(searchIndexList.get(i),searchIndexList.get(i+1)));
            	}
        	}
        	System.out.println("                        "+andsb);
        }
        
        input = andsb.toString();
       List<Integer> searchIndexList2 = searchStrIndex(input,"or");  
        for(Integer index : searchIndexList2){  
            System.out.println("index   2:   "+index.intValue());  
        } 
        StringBuilder orsb = new StringBuilder();
        for(int j = 0; j < searchIndexList2.size(); j++){
        	System.out.println("------------"+input.substring(searchIndexList2.get(j)-1, searchIndexList2.get(j)+3));
        	System.out.println("---"+input.charAt(searchIndexList2.get(j)-1)+"---"+input.charAt(searchIndexList2.get(j)+2));
        	int a = (int)input.charAt(searchIndexList2.get(j)-1);
        	int b = (int)input.charAt(searchIndexList2.get(j)+2);
        	System.out.println(a+"   "+b);
        	//大写字母ask2码值65——90，小写字母ask2码值97——122
        	if(!((a>=65 && a<=90) || (a>=97 && a<=122) || (b>=65 && b<=90) || (b>=97 && b<=122))){
        		System.out.println("转换@@@@@@@@@@@@@@@@@@@@");
        	}
        	if(j==0) orsb.append(input.substring(0, searchIndexList2.get(j)));
        	if(j==searchIndexList2.size()-1){ 
        		if( !((a>=65 && a<=90) || (a>=97 && a<=122) || (b>=65 && b<=90) || (b>=97 && b<=122)) ){
        			orsb.append("|");
            		System.out.println("^^^^^^^^^^^^^^"+input.substring(searchIndexList2.get(j)+2));
            		orsb.append(input.substring(searchIndexList2.get(j)+2));
            	}else{
            		orsb.append(input.substring(searchIndexList2.get(j)));
            		System.out.println("^^^^^^^^^^^^"+input.substring(searchIndexList2.get(j)));
            	}
        		
        	}else{
        		if( !((a>=65 && a<=90) || (a>=97 && a<=122) || (b>=65 && b<=90) || (b>=97 && b<=122)) ){
        			orsb.append("|");
            		System.out.println(input.substring(searchIndexList2.get(j)+2,searchIndexList2.get(j+1)));
            		orsb.append(input.substring(searchIndexList2.get(j)+2,searchIndexList2.get(j+1)));
            	}else{
            		System.out.println("********"+input.substring(searchIndexList2.get(j),searchIndexList2.get(j+1)));
            		orsb.append(input.substring(searchIndexList2.get(j),searchIndexList2.get(j+1)));
            	}
        	}
        	System.out.println("                        "+orsb);
        	
        }
        return orsb.toString();
	}
	
	public static void main(String[] args){
		/*String s = "aNdorid+t and 星星 | 哈哈 And aNdorid oR 太阳operator Or t and （风 Or 雨)" +
				"and andorid";*/
		String s = "苹果工程师and 赃and玩 and则and手机and在沪or android and填or他or风or啧啧or orther and ander";
		System.out.println(s);
		s = formatKeyword(s);
		System.out.println(s);
		/*List<String[]> ss = matchAll(s, "([^A-Za-z])(?i)and([^A-Za-z])");
		for(String[] o:ss){
			for(String e : o)
				System.out.println(e);
		}*/
	}
}
