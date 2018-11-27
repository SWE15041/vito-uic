/**
 * Created by lenovo on 2016/2/14.
 */
package com.jay.vito.uic.server.domain;

import com.jay.vito.storage.domain.BaseEntity;

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
public class SysDict extends BaseEntity<Long> {

    public static final Long ADMIN_USER_ID = 1L;

    /**
     * 字典编码
     */
    private String name;
    /**
     * 存储值
     */
    private String code;
    /**
     * 显示值
     */
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
