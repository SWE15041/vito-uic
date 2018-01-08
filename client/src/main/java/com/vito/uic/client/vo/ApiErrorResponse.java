/**
 * 文件名: JsonResult.java
 * 作者: zhaixm
 * 版本: 2014-6-29 下午10:44:08
 * 日期: 2014-6-29
 * 描述:
 */
package com.vito.uic.client.vo;

import com.vito.common.util.json.JsonParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: zhaixm 
 * 日期: 2014-6-29 下午10:44:08 
 * 描述: 执行结果json
 */
public class ApiErrorResponse {
    private String errCode;
    private String msg;
    private Map<String, Object> datas = new HashMap<String, Object>();

    /**
     * 创建一个新的实例 JsonResult.
     *
     */
    public ApiErrorResponse() {
    }

    /**
     * 创建一个新的实例 JsonResult.
     *
     */
    public ApiErrorResponse(String msg) {
        this.msg = msg;
    }

    public ApiErrorResponse(String errCode, String msg) {
        this.errCode = errCode;
        this.msg = msg;
    }

    /**
     * 创建一个新的实例 JsonResult.
     *
     */
    public ApiErrorResponse(Map<String, Object> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return JsonParser.convertObjectToJson(this);
    }

    public void addData(String name, Object value) {
        datas.put(name, value);
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }
}
