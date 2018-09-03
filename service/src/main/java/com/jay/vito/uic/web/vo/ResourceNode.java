package com.jay.vito.uic.web.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/12/8 23:39
 * 描述: 资源节点
 */
public class ResourceNode {

    private Long id;
    private Long pid;
    private String name;
    private String icon;
    private Integer sortNo;
    private List<ResourceNode> children = new ArrayList<>();

    public void addChild(ResourceNode childNode) {
        this.children.add(childNode);
        Collections.sort(this.children, Comparator.comparing(ResourceNode::getSortNo));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<ResourceNode> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceNode> children) {
        this.children = children;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
}
