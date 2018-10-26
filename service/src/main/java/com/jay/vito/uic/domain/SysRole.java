/**
 * Created by lenovo on 2016/2/14.
 */
package com.jay.vito.uic.domain;


import com.jay.vito.common.model.enums.YesNoEnum;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/2/14 0:04
 */
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseBusinessEntity<Long> {

    /**
     * 角色编码
     */
    @NotEmpty(message = "角色编码不能为空")
    private String code;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum isDefault = YesNoEnum.NO;

    private Set<Long> resourceIds = new HashSet<>();

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 每个用户对应有多条站内消息
     */
//    private Set<SysResource> sysResources = null;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YesNoEnum getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(YesNoEnum isDefault) {
        this.isDefault = isDefault;
    }

    @Transient
    public Set<Long> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
