package com.jay.vito.storage.service;
/**
 * 等级：A
 * Created by zhaixm on 2016/3/7.
 */

import com.vito.storage.model.Condition;
import com.vito.storage.model.Order;

import java.util.List;

/**
 * Describe: 查询服务（专用于分页）
 * CreateUser: zhaixm
 * CreateTime: 2016/3/7 16:27
 */
public interface QueryService {

    List query(String queryStatement, Condition cond, Order order);

    /**
     * (mybatis)执行计数语句
     * @param countStatement
     * @param cond
     * @return
     */
    long count(String countStatement, Condition cond);

    /**
     * (mybatis)分页查询
     * @param queryStatement
     * @param cond
     * @param beginIndex
     * @param endIndex
     * @return
     */
    List queryForPagination(String queryStatement, Condition cond, Order order, int beginIndex, int endIndex);

}
