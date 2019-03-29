/**
 * Created by lenovo on 2016/2/14.
 */
package com.jay.vito.uic.client.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/2/14 0:04
 */
@Data
public class User {

    protected Long id;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
    private String updateKey;
    private Long userId;
    private Long groupId;
    private String loginName;
    private String password;
    private String name;
    private String nickName;
    private String wechatName;
    private String wechatOpenId;
    private String email;
    private String mobile;

    private String headImg;

    private Integer sex;
    private Integer enable;
    private Integer loginable;

    /**
     * 是否管理员
     */
    private Integer manager;


    private Set<String> roleCodes = new HashSet<>();

    public void addRoleCode(String... roleCodes) {
        for (String roleCode : roleCodes) {
            this.roleCodes.add(roleCode);
        }
    }
}
