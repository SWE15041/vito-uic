package com.jay.vito.uic.server.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jay.vito.uic.server.constant.ResourceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 描述: 资源节点
 * 日期: 2017/12/8 23:39
 *
 * @author zhaixm
 */
public class ResourceNode {

	private Long id;
	private Long pid;
	private String name;

	/**
	 * 菜单图标
	 */
	private String icon;
	private Integer sortNo;
	private String code;

	/**
	 * 资源url或组件路径
	 */
	private String url;

	/**
	 * 资源类型
	 */
	private ResourceType type;

	/**
	 * 父节点
	 */
	@JsonIgnore
	private ResourceNode parent;

	private List<ResourceNode> children = new ArrayList<>();

	public void addChild(ResourceNode childNode) {
		this.children.add(childNode);
		childNode.setParent(this);
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public ResourceNode getParent() {
		return parent;
	}

	public void setParent(ResourceNode parent) {
		this.parent = parent;
	}
}
