/**
 * Created by lenovo on 2016/2/14.
 */
package com.jay.vito.uic.domain;


import com.alibaba.fastjson.annotation.JSONField;
import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.storage.domain.BaseBusinessEntity;
import com.jay.vito.uic.constant.UserSex;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/2/14 0:04
 */
@Entity
@Table(name = "sys_user")
//暂时将userId映射到id字段 解决无法忽略该字段的问题
//@AttributeOverride(name = "userId", column = @Column(name = "id", insertable = false, updatable = false))
public class SysUser extends BaseBusinessEntity<Long> {

    public static final Long ADMIN_USER_ID = 1L;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    @JSONField(serialize = false)
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 微信名称
     */
    private String wechatName;
    /**
     * 微信号
     */
    @JSONField(serialize = false)
    private String wechatOpenId;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 性别
     */
    @Enumerated(EnumType.ORDINAL)
    private UserSex sex;

    /**
     * 是否启用  0-未启用 1-启用
     */
    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum enable;

    /**
     * 是否可登录 0-不可登录 1-可登录
     */
    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum loginable;

    /**
     * 是否管理员
     */
    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum manager;

    private Set<String> roleCodes = new HashSet<>();
    private Set<Long> roleIds = new HashSet<>();
    private Set<String> resourceCodes = new HashSet<>();

    public boolean authorizeResource(String resourceCode) {
        if (getResourceCodes() == null) {
            return false;
        } else {
            return getResourceCodes().contains(resourceCode);
        }
    }

    @Transient
    public Set<String> getResourceCodes() {
        return resourceCodes;
    }

    public void addResourceCode(String... resourceCodes) {
        for (String resourceCode : resourceCodes) {
            this.resourceCodes.add(resourceCode);
        }
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UserSex getSex() {
        return sex;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public String getWechatOpenId() {
        return wechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    public YesNoEnum getEnable() {
        return enable;
    }

    public void setEnable(YesNoEnum enable) {
        this.enable = enable;
    }

    public boolean enable() {
        return enable != null && enable == YesNoEnum.YES;
    }

    public YesNoEnum getLoginable() {
        return loginable;
    }

    public void setLoginable(YesNoEnum loginable) {
        this.loginable = loginable;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public YesNoEnum getManager() {
        return manager;
    }

    public void setManager(YesNoEnum manager) {
        this.manager = manager;
    }

    public boolean manager() {
        return manager != null && manager == YesNoEnum.YES;
    }

    public boolean loginable() {
        return loginable != null && loginable == YesNoEnum.YES;
    }

    @Transient
    public Set<String> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(Set<String> roleCodes) {
        this.roleCodes = roleCodes;
    }

    public void addRoleCode(String... roleCodes) {
        for (String roleCode : roleCodes) {
            this.roleCodes.add(roleCode);
        }
    }

    public boolean containRole(String roleCode) {
        return this.roleCodes.contains(roleCode);
    }

    @Transient
    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public void addRoleId(Long... roleIds) {
        for (Long roleId : roleIds) {
            this.roleIds.add(roleId);
        }
    }
}
