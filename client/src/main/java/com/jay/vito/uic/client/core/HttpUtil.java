/**
 *
 */
package com.jay.vito.uic.client.core;

import com.alibaba.fastjson.JSONObject;
import com.jay.vito.common.util.json.JsonParser;
import com.jay.vito.common.util.string.EncodeUtil;
import com.jay.vito.common.util.validate.Validator;
import com.jay.vito.uic.client.vo.AuthResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

    public static String postJson(String url, String param) {
        HttpClient client = HttpClientBuilder.create().build();
        ;
        HttpPost post = new HttpPost(url);
//        //使用系统提供的默认的恢复策略
//        post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//        //设置超时的时间
//        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 3000);
        if (Validator.isNotNull(param)) {
            StringEntity se = new StringEntity(param, EncodeUtil.UTF_8);
            post.setEntity(se); //post方法中，加入json数据
            post.setHeader("Content-Type", "application/json");
        }

        String result = null;
        try {
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, EncodeUtil.UTF_8);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        } finally {
            post.releaseConnection();
            return result;
        }
    }

    public static void main(String[] args) {
        JSONObject param = new JSONObject();
        param.put("serviceTicket", "cea8b7111a58494092ebce3707aeaec4");
        String resp = HttpUtil.postJson("http://localhost:8080/auth", param.toJSONString());
        System.out.println(resp);
        AuthResponse authResponse = JsonParser.parseJson2Obj(resp, AuthResponse.class);
        System.out.println(authResponse);
    }
}
