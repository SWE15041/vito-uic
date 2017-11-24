package com.vito.common.util.collection;

import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 *<pre><b><font color="blue">ListUtils</font></b></pre>
 *
 *<pre><b>&nbsp;--List工具类--</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   ListUtils obj = new ListUtils();
 *   obj.method();
 * </pre>
 * JDK版本：JDK1.4
 * @author  <b>zhaixm</b> 
 */
public class CollectionUtil {
	
	private static Logger logger = Logger.getLogger(CollectionUtil.class);
	
	/**
	 * 判断集合中存储的对象列表中是否存在一个某属性与指定值相匹配的元素
	 * 
	 * @param objs
	 *            对象列表
	 * @param propertyName
	 *            指定的属性
	 * @param destObj
	 *            要匹配的值
	 * @return boolean
	 */
	public static <T> boolean contains(Collection<T> objs, String propertyName, Object destObj) {
		boolean exist = false;
		if (getMatchedBean(objs, propertyName, destObj)!=null) {
			exist = true;
		}
		return exist;
	}

	/**
	 * 判断集合中存储的对象列表中是否存在一个某组属性与指定一组值相匹配的元素
	 * 
	 * @param objs
	 *            对象列表
	 * @param propertyNames
	 *            (该属性不可以用hibernate model中其他对象的名称，因为是代理的所以用equals无法比对) 指定的属性数组
	 * @param destObjs
	 *            要匹配的值数组
	 * @return boolean
	 */
	public static <T> boolean contains(Collection<T> objs, String[] propertyNames, Object[] destObjs) {
		boolean exist = false;
		if (getMatchedBean(objs, propertyNames, destObjs)!=null) {
			exist = true;
		}
		return exist;
	}
	
	/**
	 * 获取list存储的对象列表中某属性与指定值相匹配的元素
	 * 
	 * @param objs
	 *            对象列表
	 * @param propertyName
	 *            (该属性不可以用hibernate model中其他对象的名称，因为是代理的所以用equals无法比对) 指定的属性
	 * @param destObj
	 *            要匹配的值
	 * @return Object
	 */
	public static <T> T getMatchedBean(Collection<T> objs, String propertyName, Object destObj) {
		return getMatchedBean(objs, new String[]{propertyName}, new Object[]{destObj});
	}
	
	/**
	 * 获取集合存储的对象列表中与某组属性与指定一组值相匹配的元素
	 * 
	 * @param objs
	 *            对象列表
	 * @param propertyNames
	 *            (该属性不可以用hibernate model中其他对象的名称，因为是代理的所以用equals无法比对) 指定的属性数组
	 * @param destObjs
	 *            要匹配的值数组
	 * @return Object
	 */
	public static <T> T getMatchedBean(Collection<T> objs, String[] propertyNames, Object[] destObjs) {
		T matchBean = null;
		if (isNotEmpty(objs)) {
			Iterator<T> it = objs.iterator();
			while (it.hasNext()) {
				T bean = it.next();
				int i = 0;
				for (i = 0; i < propertyNames.length; i++) {
					Object propertyValue = getProperty(bean, propertyNames[i]);
					if (!destObjs[i].equals(propertyValue)) {
						break;
					}
				}
				if (i == propertyNames.length) {
					matchBean = bean;
					break;
				}
			}
		}
		return matchBean;
	}
	
	/**
	 * 获取集合存储的对象列表中某属性与指定值相匹配的元素
	 * 
	 * @param objs
	 *            对象列表
	 * @param propertyName
	 *            (该属性不可以用hibernate model中其他对象的名称，因为是代理的所以用equals无法比对) 指定的属性
	 * @param destObj
	 *            要匹配的值
	 * @return List
	 */
	public static <T> List<T> getMatchedBeanList(Collection<T> objs, String propertyName, Object destObj) {
		return getMatchedBeanList(objs, new String[]{propertyName}, new Object[]{destObj});
	}
	
	/**
	 * 获取集合存储的对象列表中与某组属性与指定一组值相匹配的一组元素
	 * 
	 * @param objs
	 *            对象列表
	 * @param propertyNames
	 *            (该属性不可以用hibernate model中其他对象的名称，因为是代理的所以用equals无法比对) 指定的属性数组
	 * @param destObjs
	 *            要匹配的值数组
	 * @return List
	 */
	public static <T> List<T> getMatchedBeanList(Collection<T> objs, String[] propertyNames, Object[] destObjs) {
		List<T> matchBeanList = new ArrayList<T>(0);
		if (isNotEmpty(objs)) {
			Iterator<T> it = objs.iterator();
			while (it.hasNext()) {
				T bean = it.next();
				int i = 0;
				for (i = 0; i < propertyNames.length; i++) {
					Object propertyValue = getProperty(bean, propertyNames[i]);
					if (!destObjs[i].equals(propertyValue)) {
						break;
					}
				}
				if (i == propertyNames.length) {
					matchBeanList.add(bean);
				}
			}
		}
		return matchBeanList;
	}
	

	/**
	 * 移除集合对象列表中某属性与指定值相匹配的元素
	 * 
	 * @param objs
	 *            对象列表
	 * @param propertyName
	 *            (该属性不可以用hibernate model中其他对象的名称，因为是代理的所以用equals无法比对) 指定的属性
	 * @param destObj
	 *            要匹配的值
	 */
	public static <T> void removeMatchedBean(Collection<T> objs, String propertyName,	Object destObj) {
		removeMatchedBean(objs, new String[]{propertyName}, new Object[]{destObj});
	}
	
	/**
	 * 移除集合对象列表中某组属性与指定一组值相匹配的元素
	 * @param objs 对象列表
	 * @param propertyNames (该属性不可以用hibernate model中其他对象的名称，因为是代理的所以用equals无法比对) 指定的属性数组
	 * @param destObjs 要匹配的值数组
	 */
	public static <T> void removeMatchedBean(Collection<T> objs, String[] propertyNames, Object[] destObjs) {
		if (isNotEmpty(objs)) {
			Iterator<T> it = objs.iterator();
			while (it.hasNext()) {
				T bean = it.next();
				int i = 0;
				for (i = 0; i < propertyNames.length; i++) {
					Object propertyValue = getProperty(bean, propertyNames[i]);
					if (!destObjs[i].equals(propertyValue)) {
						break;
					}
				}
				if (i == propertyNames.length) {
					it.remove();
				}
			}
		}
	}
	
	/**
	 * 取得bean中的指定属性的value
	 * 
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Object getProperty(Object bean, String propertyName) {
		Object propertyValue = null;
		try {
			if(bean instanceof Map) {
				propertyValue = ((Map)bean).get(propertyName);
			} else {
				PropertyDescriptor pd = new PropertyDescriptor(propertyName, bean.getClass());
				Method readMethod = pd.getReadMethod();
				propertyValue = readMethod.invoke(bean);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} 
		return propertyValue;
	}
	
	/**
	 * 判断list是否不为空
	 * @param list 要判断的目标list
	 * @return boolean
	 * @author:zhaixm
	 */
	public static <T> boolean isNotEmpty(Collection<T> collection) {
		boolean isNotBlank = false;
		if (collection != null && collection.size() > 0) {
			isNotBlank = true;
		}
		return isNotBlank;
	}
	
	/**
	 * 判断list是否为空
	 * @param list 要判断的目标list
	 * @return boolean
	 * @author:zhaixm
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return !isNotEmpty(collection);
	}
	
	/**
	 * 将bean转换成map
	 * @param t
	 * @return
	 * @author:luosy
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> changeBeanToMap(Object bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		Class cls = bean.getClass();
		Field[] fieldList = cls.getDeclaredFields();
		for (int i = 0; i < fieldList.length; i++) {
			Field f = fieldList[i];
			String name = f.getName();
			try {
				PropertyDescriptor pd = new PropertyDescriptor(name, bean.getClass());
				Method meth = pd.getReadMethod();
				Object rtcs = meth.invoke(bean);
				map.put(name, rtcs);
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public static Map<String, String> convertMapValue(Map<String,? extends Object> map){
		Map<String, String> convertMap = new HashMap<String, String>();
		for(Map.Entry<String, ? extends Object> entry : map.entrySet()){
			convertMap.put(entry.getKey(), (String)entry.getValue());
		}
		return convertMap;
	}
	       
	public static void main(String[] args){
		CollectionUtil cu = new CollectionUtil();
		List<Object> list = new ArrayList<Object>();
		User user1 = cu.new User();
		user1.setName("jack");
		user1.setEmail("jack@g.com");
		list.add(user1);
		User user2 = cu.new User();
		user2.setName("mary");
		user2.setEmail("mary@g.com");
		list.add(user2);
		//CollectionUtils.removeMatchedBean(list, new String[]{"name","email"}, new Object[]{"jack", "jack@g.com"});
		System.out.println(list);
		User u = (User)CollectionUtil.getMatchedBean(list, new String[]{"name","email"}, new Object[]{"jack", "jack@g.com"});
		System.out.println(u);
	}
	
	class User {
		
		String name;
		String email;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
		public String toString() {
			return this.name;
		}
	}
}
