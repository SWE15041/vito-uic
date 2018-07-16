/**
 * Created by lenovo on 2016/2/14.
 */
package com.jay.vito.uic.domain;


import com.alibaba.fastjson.annotation.JSONField;
import com.jay.vito.storage.domain.BaseBusinessEntity;
import com.jay.vito.uic.constant.UserSex;
import com.jay.vito.common.model.enums.YesNoEnum;

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
public class User extends BaseBusinessEntity<Long> {

    public static final Long ADMIN_USER_ID = 1L;

    private String loginName;
    @JSONField(serialize = false)
    private String password;
    private String name;
    private String nickName;
    private String weixinName;
    @JSONField(serialize = false)
    private String weixinOpenId;
    private String email;
    private String mobile;
    private String headImg;

    @Enumerated(EnumType.ORDINAL)
    private UserSex sex;

    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum enable;

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

    public String getWeixinName() {
        return weixinName;
    }

    public void setWeixinName(String weixinName) {
        this.weixinName = weixinName;
    }

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId;
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
