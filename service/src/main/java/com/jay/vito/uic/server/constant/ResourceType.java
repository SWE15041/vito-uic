package com.jay.vito.uic.server.constant;

/**
 * 作者: zhaixm
 * 日期: 2017/12/6 16:30
 * 描述:
 */
public enum ResourceType {

    MODULE,
    MENU,
    FUNC;

    public static void main(String[] args) {
        System.out.println(ResourceType.MODULE.ordinal());
    }

    public int getValue() {
        return ordinal() + 1;
    }

}
