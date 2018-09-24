/**
 * Created by lenovo on 2016/2/14.
 */
package com.jay.vito.uic.domain;


import com.alibaba.fastjson.JSONObject;
import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.uic.constant.ResourceType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/2/14 0:04
 */
@Entity
@Table(name = "sys_resource")
public class SysResource extends BaseBusinessEntity<Long> {

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * 唯一性编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * URL地址
     */
    private String url;

    /**
     * 菜单图标名称
     */
    private String icoName;

    /**
     * 资源类型 0-模块 1-菜单 2-功能
     */
    @Enumerated(EnumType.ORDINAL)
    private ResourceType resourceType;

    /**
     * 排序号
     */
    private Integer sortNo;

    /**
     * 是否启用 1-启用 0-禁用
     */
    @Enumerated(EnumType.ORDINAL)
    private YesNoEnum enable = YesNoEnum.YES;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 父菜单
     */
    private SysResource parent;

    /**
     * 子菜单
     */
    private List<SysResource> children = null;

    public SysResource() {
    }

    public SysResource(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public void addChild(SysResource sysResource) {
        getChildren().add(sysResource);
    }

    public void removeChild(SysResource sysResource) {
        getChildren().remove(sysResource);
    }

    //    @OneToMany(mappedBy = "parent", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
//    @OrderBy("sortNo asc")
//    @Fetch(FetchMode.SELECT)
    @Transient
    public List<SysResource> getChildren() {
        if (children == null) {
            children = new ArrayList<SysResource>();
        }
        return children;
    }

    public void setChildren(List<SysResource> children) {
        this.children = children;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public YesNoEnum getEnable() {
        return enable;
    }

    public void setEnable(YesNoEnum enable) {
        this.enable = enable;
    }

    @Transient
    public boolean enable() {
        if (enable == null) {
            return false;
        }
        return YesNoEnum.YES == enable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SysResource other = (SysResource) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", getId());
        jsonObject.put("pId", getParentId());
        jsonObject.put("name", getName());
        jsonObject.put("code", getCode());
        jsonObject.put("resUrl", getUrl());
        jsonObject.put("sortNo", getSortNo());
        jsonObject.put("enable", getEnable());
        jsonObject.put("resourceType", getResourceType());
        return jsonObject;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcoName() {
        return icoName;
    }

    public void setIcoName(String icoName) {
        this.icoName = icoName;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", insertable = false, updatable = false)
    public SysResource getParent() {
        return parent;
    }

    public void setParent(SysResource parent) {
        this.parent = parent;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
