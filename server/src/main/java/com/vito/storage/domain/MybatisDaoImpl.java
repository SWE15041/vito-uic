package com.vito.storage.domain;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作者: zhaixm
 * 日期: 2017/11/24 12:57
 * 描述:
 */
@Component
public class MybatisDaoImpl implements MybatisDao {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SqlSession sqlSession;

    public MybatisDaoImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private SqlSession getSqlSession() {
        return this.sqlSession;
    }

    @Override
    public List query(String statement, Object param) {
        return getSqlSession().selectList(statement, param);
    }

    @Override
    public long count(String countStatement, Object param) {
        logger.debug("查询条件：{}", param);
        long beginTime = System.currentTimeMillis();
        long count = getSqlSession().selectOne(countStatement, param);
        long endTime = System.currentTimeMillis();
        logger.debug("count花费时间：{}", (endTime - beginTime));
        return count;
    }

    @Override
    public List queryForPagination(String statement, Object param, int beginIndex, int endIndex) {
        logger.debug("查询条件：" + param);
        long beginTime = System.currentTimeMillis();
        RowBounds rowBounds = new RowBounds(beginIndex, endIndex - beginIndex);
        List results = getSqlSession().selectList(statement, param, rowBounds);
        long endTime = System.currentTimeMillis();
        logger.debug("pagination query花费时间：{}", (endTime - beginTime));
        return results;
    }
}
