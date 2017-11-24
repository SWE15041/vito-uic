package com.vito.common.util.cmd;

public enum StreamGobblerType {

	ERROR("ERROR", "错误"), OUTPUT("OUTPUT", "输出");

	private String code;
	private String name;

	private StreamGobblerType(String code, String name) {
		this.code = code;
		this.name = name;
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

}
