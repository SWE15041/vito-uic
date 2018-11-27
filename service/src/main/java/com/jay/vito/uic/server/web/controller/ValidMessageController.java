package com.jay.vito.uic.server.web.controller;

import com.jay.vito.uic.client.interceptor.IgnoreUserAuth;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(value = "/api")
public class ValidMessageController {


    private String apikey = "bc7a912879e2bb5dfba641017c9c664b";
    private String textConstant = "【云片网】您的验证码是";
    private Random randomNum = new Random();

    /**
     * 生成短信验证码
     */
    @IgnoreUserAuth
    @RequestMapping(value = "/mobile/validMessage", method = RequestMethod.POST)
    public boolean buildMessage(@RequestBody Map<String, String> map, HttpSession session) {
        //初始化client,apikey作为所有请求的默认值(可以为空)
        String mobile = map.get("mobile");
        YunpianClient client = new YunpianClient().init();
        Map<String, String> message = client.newParam(2);
        message.put(YunpianClient.APIKEY, apikey);
        message.put(YunpianClient.MOBILE, mobile);
        int[] random = randomNum.ints(4, 0, 9).toArray();//生成6个i0-9范围内的整数；
        String text = textConstant;
        String randomNum = "";
        for (int i : random) {
            randomNum += i;
        }
        text += randomNum;
        message.put(YunpianClient.TEXT, text);
        Result<SmsSingleSend> sendResult = client.sms().single_send(message);
        SmsSingleSend data = sendResult.getData();
        System.out.println("返回结果：" + data);
        client.close();
// todo       session.setAttribute("MessageCode", randomNum);
        session.setAttribute("MessageCode", "1234");
        session.setMaxInactiveInterval(60);//单位：秒

        return true;
    }


    public static void main(String[] args) {
        ValidMessageController test = new ValidMessageController();
        HttpSession session = null;
//        boolean b = test.buildMessage("18960168738", session);

    }
}
//    public static void testSendSms(String apikey, String mobile, String text) {
//        //初始化client,apikey作为所有请求的默认值(可以为空)
//        YunpianClient clnt = new YunpianClient("apikey").init();
//
//        Map<String, String> param = clnt.newParam(2);
//        param.put(YunpianClient.MOBILE, mobile);
//        param.put(YunpianClient.TEXT, "【云片网】您的验证码是1234");
//        Result<SmsSingleSend> r = clnt.sms().single_send(param);
//        //获取返回结果，返回码:r.getCode(),返回码描述:r.getMsg(),API结果:r.getData(),其他说明:r.getDetail(),调用异常:r.getThrowable()
//        r.getData();
//        //账户:clnt.user().* 签名:clnt.sign().* 模版:clnt.tpl().* 短信:clnt.sms().* 语音:clnt.voice().* 流量:clnt.flow().* 隐私通话:clnt.call().*
//
//        //最后释放client
//        clnt.close()
//    }