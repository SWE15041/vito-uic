package com.jay.vito.storage.domain;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseEntity<ID extends Serializable> {

    protected ID id;

    private Date createTime;

    private Date updateTime;

    private String updateKey;

    /**
     * 主键字段
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public ID getId() {
        return id;
    }

    /**
     * 创建时间 设置JsonField format是为了防止前台把Date类型的format设置为其他格式来输出json，导致保存的时候将saveTime覆盖为不精确的实际
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss:SSS")
    @Column(name = "create_time")
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss:SSS")
    @Column(name = "update_time")
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 更新密钥
     */
    @Column(name = "update_key")
    public String getUpdateKey() {
        return this.updateKey;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setUpdateKey(String updateKey) {
        this.updateKey = updateKey;
    }
}