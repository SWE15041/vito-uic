package com.jay.vito.storage.service;

import com.vito.storage.domain.MybatisDao;
import com.vito.storage.model.Condition;
import com.vito.storage.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Describe:
 * CreateUser: zhaixm
 * CreateTime: 2016/3/7 16:52
 */
@Service
public class QueryServiceMybatis implements QueryService {

    @Autowired
    private MybatisDao mybatisDao;

    @Override
    public List query(String queryStatement, Condition cond, Order order) {
        Map<String, Object> params = cond.toMap();
        setOrderParam(order, params);
        return mybatisDao.query(queryStatement, params);
    }

    private void setOrderParam(Order order, Map<String, Object> params) {
        if (order != null) {
            String orderSql = order.orderSql();
            params.put("orderBy", orderSql);
        }
    }

    @Override
    public long count(String countStatement, Condition cond) {
        Map<String, Object> params = cond.toMap();
        return mybatisDao.count(countStatement, params);
    }

    @Override
    public List queryForPagination(String queryStatement, Condition cond, Order order, int beginIndex, int endIndex) {
        Map<String, Object> params = cond.toMap();
        setOrderParam(order, params);
        return mybatisDao.queryForPagination(queryStatement, params, beginIndex, endIndex);
    }
}
