package com.jay.vito.uic.server.domain;

import com.jay.vito.uic.client.domain.BaseBusinessEntity;

/**
 * 作者: zhaixm
 * 日期: 2018/9/18 23:19
 * 描述:
 */
//@Entity
//@Table(name = "sys_group")
public class SysGroup extends BaseBusinessEntity<Long> {

    private String name;
    private Integer userLimit;

}
