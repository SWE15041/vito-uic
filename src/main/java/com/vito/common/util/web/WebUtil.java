package com.vito.common.util.web;

import com.vito.common.util.json.JsonParser;
import com.vito.common.util.validate.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class WebUtil {

    private static Logger logger = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 将request中的访问参数转换为Map存储
     *
     * @param request
     * @return
     */
    public static Map<String, Object> parseParams(ServletRequest request) {
        try {
            //使用form方式提交的数据解析
            Map<String, Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameterValues(paramName)[0];
                paramMap.put(paramName, paramValue);
            }

            //使用request payload方式提交的数据解析
            ServletInputStream inputStream = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder sb = new StringBuilder("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            inputStream.close();
            String paramJsonStr = sb.toString();

            //以上是之前的处理方式   jdk8可用下面这句，一句搞定
//            BufferedReader reader = request.getReader();
//            String paramJsonStr = reader.lines().collect(Collectors.joining(System.lineSeparator()));
//            reader.close();
            if (Validator.isNotNull(paramJsonStr)) {
                try {
                    Map<String, String> payloadParamMap = JsonParser.parseJson2Map(paramJsonStr);
                    paramMap.putAll(payloadParamMap);
                } catch (Exception e) {
                    logger.error("从requestBody中解析参数出错", e);
                }
            }
            return paramMap;
        } catch (Exception e) {
            throw new RuntimeException("解析request请求参数出错", e);
        }

    }

    /**
     * 获取完整的访问地址
     *
     * @param request
     * @return
     */
    public static String getWholeUrl(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(getWholeContextPath(request));
        sb.append(request.getServletPath());
        if (Validator.isNotNull(request.getQueryString())) {
            sb.append("?").append(request.getQueryString());
        }
        return sb.toString();
    }

    /**
     * 获取系统完整的上下文路径，如http://www.baidu.com:8080/baike
     *
     * @param request
     * @return
     */
    public static String getWholeContextPath(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = request.getScheme();
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if (("http".equals(scheme) && (port != 80)) || ("https".equals(scheme) && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getContextPath());
        return url.toString();
    }

    /**
     * 从一个URL字符串中解析出其中的参数并转换为Map存储
     * URL格式为：http://www.baidu.com/baike?name=zhaixm
     * 解析后的参数为：Map<key:name,value:zhaixm>
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParams(String url) {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (Validator.isNotNull(url)) {
            if (url.indexOf('?') < 0) {
                return paramMap;
            }
            url = url.substring(url.indexOf('?') + 1);
            String paramaters[] = url.split("&");
            for (String param : paramaters) {
                String values[] = param.split("=");
                switch (values.length) {
                    case 1:
                        paramMap.put(values[0], "");
                        break;
                    case 2:
                        paramMap.put(values[0], values[1]);
                        break;
                    default:
                        break;
                }
            }
        }
        return paramMap;
    }

    /**
     * 获取请求来源IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 判断ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")
                                                                                                .toString())) ||
                (request.getParameter("ajax") != null && "true".equals(request.getParameter("ajax")));
    }

    /**
     * 输出json字符串
     *
     * @param response
     * @param obj
     */
    public static void renderJson(HttpServletResponse response, Object obj) {
        try {
            String responseContent = JsonParser.convertObjectToJson(obj);
            response.setCharacterEncoding("UTF-8");
//            response.setContentType("text/plain;charset=UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(responseContent);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 作者: huangqj
     * 版本: Apr 8, 2012 5:23:05 PM v1.0
     * 日期: Apr 8, 2012
     * 参数: @param cookieName cookie名称
     * 参数: @param cookieContent cookie内容
     * 参数: @param time 有效时间
     * 参数: @param response 请求
     * 描述: 根据指定的名称和value添加客户端cookie
     */
    public static void addCookie(String cookieName, String cookieContent, int time, HttpServletResponse response) {
        if (Validator.isNull(cookieName))
            throw new RuntimeException("Cookie名称不能为空");
        Cookie cookie = new Cookie(cookieName, cookieContent);
        cookie.setMaxAge(time);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 作者: huangqj
     * 参数: @param cookieName cookie名称
     * 参数: @param request
     * 参数: @return
     * 描述: 根据cookie名称获取对应的cookie信息
     */
    public static Cookie getCookie(String cookieName, HttpServletRequest request) {
        if (Validator.isNull(cookieName))
            throw new RuntimeException("Cookie名称不能为空");
        Cookie[] cookies = request.getCookies();
        Cookie result = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    result = cookie;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 作者: huangqj
     * 参数: @param cookieName cookie的名称
     * 参数: @param request
     * 参数: @param response
     * 描述: 删除指定cookie名称的cookie
     */
    public static void delCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
        if (Validator.isNull(cookieName))
            throw new RuntimeException("Cookie名称不能为空");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    Cookie deledCookie = new Cookie(cookieName, null);
                    deledCookie.setPath("/");
                    deledCookie.setMaxAge(0);
                    response.addCookie(deledCookie);
                }
            }
        }
    }

}
