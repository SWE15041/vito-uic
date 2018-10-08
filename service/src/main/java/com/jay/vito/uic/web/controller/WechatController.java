package com.jay.vito.uic.web.controller;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weixin.popular.api.BaseAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.client.LocalHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping(value = "/api/wechat")
public class WechatController extends BaseAPI {
    private static Logger logger = LoggerFactory.getLogger(WechatController.class);
//    private String OPEN_URI = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    private String APPID = "";
    private String REDIRECT_URI = "http://shipweb.aixsj.com/";
    private String response_type = "code";
    private String SCOPE = "snsapi_userinfo";//snsapi_base snsapi_userinfo
    private String STATE = "123";

//    //授权,获取code
//    private String webUrl = URL +
//            "appid="+APPID+"&" +
//            "redirect_uri=" + REDIRECT_URI + "&" +
//            "response_type=" + response_type + "&" +
//            "scope=" + SCOPE + "&" +
//            "state=" + STATE +
//            "#wechat_redirect";



    /**
     * 生成网页授权 URL
     *
     * @param appid           appid
     * @param redirect_uri    自动URLEncoder
     * @param snsapi_userinfo snsapi_userinfo
     * @param state           可以为空
     * @return url
     */
    public static String connectOauth2Authorize(String appid, String redirect_uri, boolean snsapi_userinfo, String state, String component_appid) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(OPEN_URI + "/connect/oauth2/authorize?")
                    .append("appid=").append(appid)
                    .append("&redirect_uri=").append(URLEncoder.encode(redirect_uri, "utf-8"))
                    .append("&response_type=code")
                    .append("&scope=").append(snsapi_userinfo ? "snsapi_userinfo" : "snsapi_base")
                    .append("&state=").append(state == null ? "" : state);
            if (component_appid != null) {
                sb.append("&component_appid=").append(component_appid);
            }
            sb.append("#wechat_redirect");
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return null;
    }


}
