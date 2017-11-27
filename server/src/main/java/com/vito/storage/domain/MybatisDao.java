package com.vito.storage.domain;

import java.util.List;

public interface MybatisDao {
    /**
     * 查询出对象集合
     *
     * @param statement
     * @param param
     * @return
     */
    List query(String statement, Object param);

    /**
     * 执行计数语句
     *
     * @param countStatement
     * @param param
     * @return
     */
    long count(String countStatement, Object param);

    /**
     * 分页查询
     *
     * @param statement
     * @param param
     * @param beginIndex
     * @param endIndex
     * @return
     */
    List queryForPagination(String statement, Object param, int beginIndex, int endIndex);
}