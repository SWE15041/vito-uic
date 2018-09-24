/**
 * Created by lenovo on 2016/2/14.
 */
package com.jay.vito.uic.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/2/14 0:04
 */

@Entity
@Table(name = "sys_dict")
//暂时将userId映射到id字段 解决无法忽略该字段的问题
//@AttributeOverride(name = "userId", column = @Column(name = "id", insertable = false, updatable = false))
public class SysDict extends BaseBusinessEntity<Long> {

    public static final Long ADMIN_USER_ID = 1L;

    /**
     * 字典编码
     */
    private String code;
    /**
     * 存储值
     */
    private String value;
    /**
     * 显示值
     */
    private String label;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
