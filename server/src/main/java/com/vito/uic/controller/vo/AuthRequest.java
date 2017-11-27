package com.vito.uic.controller.vo;

/**
 * 作者: zhaixm
 * 日期: 2017/11/26 0:00
 * 描述:
 */
public class AuthRequest {

    private String serviceTicket;
    private String tickGrantCookie;

    public String getServiceTicket() {
        return serviceTicket;
    }

    public void setServiceTicket(String serviceTicket) {
        this.serviceTicket = serviceTicket;
    }

    public String getTickGrantCookie() {
        return tickGrantCookie;
    }

    public void setTickGrantCookie(String tickGrantCookie) {
        this.tickGrantCookie = tickGrantCookie;
    }
}
