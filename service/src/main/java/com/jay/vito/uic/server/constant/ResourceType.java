package com.jay.vito.uic.server.constant;

/**
 * 资源类型
 * 日期: 2017/12/6 16:30
 *
 * @author zhaixm
 */
public enum ResourceType {

	/**
	 * 模块
	 */
	Module,

	/**
	 * 目录（前端组件对应的上级目录）
	 */
	Catalog,

	/**
	 * 菜单
	 */
	Menu,

	/**
	 * 功能
	 */
	Func;

	public static void main(String[] args) {
		System.out.println(ResourceType.Module.ordinal());
	}

	public int getValue() {
		return ordinal() + 1;
	}

}
