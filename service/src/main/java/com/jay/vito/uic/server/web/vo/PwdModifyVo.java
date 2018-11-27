package com.jay.vito.uic.server.web.vo;

/**
 * 作者: zhaixm
 * 日期: 2018/1/8 0:24
 * 描述: 密码修改vo
 */
public class PwdModifyVo {

    private String origPwd;
    private String newPwd;

    public String getOrigPwd() {
        return origPwd;
    }

    public void setOrigPwd(String origPwd) {
        this.origPwd = origPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
