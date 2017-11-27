package com.vito.common.util.bean;

import com.vito.common.util.string.StringUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: zhaixm
 * 日期: 2016/10/22 11:45
 * 描述: Map工具类
 */
public class MapUtil {

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param map  包含属性值的 map
     * @param type 要转化的类型
     * @return 转化出来的 JavaBean 对象
     */
    public static  <T> T toObject(Map map, Class<T> type) {
        BeanInfo beanInfo = null; // 获取类属性
        T obj = null;
        try {
            beanInfo = Introspector.getBeanInfo(type);// 获取类属性
            obj = type.newInstance(); // 创建 JavaBean 对象
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            String key = propertyName;
            Object value = map.get(key);
            // 取不到值时，自动进行属性名转换 将驼峰转成link方式再去取值
            if (!map.containsKey(key) || null == value) {
                key = StringUtil.replaceCamelCase2Link(propertyName, "_");
                value = map.get(key);
                if (value == null) {
                    key = key.toLowerCase();
                    value = map.get(key);
                }
            }
            if (null != value) {
                Object[] args = new Object[]{value};
                try {
                    descriptor.getWriteMethod().invoke(obj, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    public static class User {
        private String name;
        private String sex;
        private String schoolName;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        @Override
        public String toString() {
            return "name:" + name + " sex:" + sex + " schoolName:" + schoolName;
        }
    }

    public static void main(String[] args) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("NAME", "zhaixm");
        userMap.put("SEX", "男");
        userMap.put("school_name", "第一中学");

        User user = toObject(userMap, User.class);
        System.out.println(user);
    }

}
