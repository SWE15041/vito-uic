package com.jay.vito.uic.client.core;


import com.jay.vito.uic.client.vo.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2017/12/3 23:30
 * 描述: 用户上下文
 */
public class UserContext extends TokenData {

	private User user;

	private Map<String, Object> datas = new HashMap<>();

	public UserContext() {
	}

	public UserContext(Long userId, Long groupId, Boolean manager) {
		super(userId, groupId, manager);
	}

	public User getUser() {
		if (user == null) {
			//todo 从认证服务器获取该用户相关信息
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Object> getDatas() {
		return datas;
	}

	/**
	 * 获取用户上下文中的缓存数据
	 *
	 * @param key
	 * @return
	 */
	public Object getData(String key) {
		return this.datas.get(key);
	}

	/**
	 * 在用户上下文添加缓存
	 *
	 * @param key
	 * @param data
	 */
	public void addData(String key, Object data) {
		this.datas.put(key, data);
	}

}
