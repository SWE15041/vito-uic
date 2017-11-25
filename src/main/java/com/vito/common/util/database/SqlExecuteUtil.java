/**
 * 
 * 类描述：SQL执行工具
 * 
 */
package com.vito.common.util.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *<pre><b><font color="blue">SqlExecuteUtil</font></b></pre>
 *
 *<pre><b>&nbsp;--SQL执行工具--</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   SqlExecuteUtil.executeQuery(conn,sql);
 * </pre>
 * JDK版本：JDK1.4
 * @author  <b>hongzy</b>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SqlExecuteUtil {
	/** 写日志器 */
	private static Logger logger = LoggerFactory.getLogger(SqlExecuteUtil.class);

	/**
	 * 执行insert ,update ,delete等SQL操作
	 * @param sql SQL
	 * @param conn 连接
	 * @return 更新记录数
	 */
	public static int executeUpdate(String sql, Connection conn) {
		int state = 0;
		Statement stm = null;
		try {
			stm = conn.createStatement();
			state = stm.executeUpdate(sql);
			return state;
		} catch (SQLException e) {
			throw new RuntimeException("执行SQL出错,Sql=" + sql, e);
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException("关闭stm出错", e);
			}
		}
	}

	/**
	 * 查询单条记录
	 * @param sqlStr SQL
	 * @param conn 连接
	 * @return 一条记录，key是字段名(大写),value是值
	 */
	public static Map queryForMap(String sqlStr, Connection conn){
		List results = executeQuery(sqlStr, conn, Integer.MAX_VALUE);
		int size = results == null ? 0 : results.size();
		if(size == 0){
			return null;
		}	
		else{
			return (Map)results.iterator().next();
		}  
	}
	
	/**
	 * 执行数据查询，并将结果集返回到QueryDataSet中，不限定返回的行数。如果需要限制返回的行数 请用{@link
	 * executeQueryDX(String,int)}方法执行查询。
	 * 
	 * @param sqlStr String 查询的SQL语句
	 * @param conn 连接
	 * @return QueryDataSet 返回的结果集
	 */
	public static List executeQuery(String sqlStr, Connection conn){
		return executeQuery(sqlStr, conn, 500);
	}

	/**
	 * 执行数据查询，并将结果集返回到QueryDataSet中，限定返回的行数。如果不需要限制返回的行数 请用{@link
	 * executeQueryDX(String)}方法执行查询。
	 * 
	 * @param sqlStr  String 查询的SQL语句
	 * @param conn 连接
	 * @param maxRows  限制返回的行数
	 * @return QueryDataSet 返回的结果集
	 * @throws SQLException
	 */
	public static List executeQuery(String sqlStr, Connection conn, int maxRows){
		if (conn == null) {
			throw new IllegalStateException("数据连接是空的，请保证调用此方法前设置了为该类设置了数据连接");
		} else {
			Statement stm = null;
			try {
				stm = conn.createStatement();
				stm.setMaxRows(maxRows);
				logger.debug("执行查询的语句为：" + sqlStr + "，返回的最大行数为：" + maxRows);
				ResultSet rs = stm.executeQuery(sqlStr);
				return cast(rs);
			} catch (SQLException e) {
				throw new RuntimeException("执行SQL出错,Sql=" + sqlStr, e);
			} finally {
				try {
					if (stm != null) {
						stm.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException("关闭stm出错", e);
				}
			}
		}
	}

	/**
	 * 执行create ,alter 等DDL SQL操作
	 * 
	 * @param sqlStr String sql语句
	 * @param conn 连接
	 * @return  
	 * @throws SQLException
	 */
	public static void execute(String sqlStr, Connection conn){
		if (conn == null) {
			throw new IllegalStateException("数据连接是空的，请保证调用此方法前设置了为该类设置了数据连接");
		} else {
			Statement stm = null;
			try {
				stm = conn.createStatement();
				logger.debug("执行查询的语句为：" + sqlStr);
				stm.execute(sqlStr);
			} catch (SQLException e) {
				throw new RuntimeException("执行SQL出错,Sql=" + sqlStr, e);
			} finally {
				try {
					if (stm != null) {
						stm.close();
					}
				} catch (SQLException e) {
					throw new RuntimeException("关闭stm出错", e);
				}
			}
		}
	}

	/**
	 * 结果集转换成List
	 * @param rs ResultSet
	 * @return  每条记录是一个Map, 组成一个List
	 * @throws SQLException 抛出SQL异常
	 * @author: hongzy
	 */
	public static List cast(ResultSet rs) throws SQLException {
		List result = new ArrayList();
		while (rs.next()) {
			Map row = new HashMap();
			result.add(row);
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				row.put(rs.getMetaData().getColumnName(i + 1), rs
						.getObject(i + 1));
			}
		}
		return result;
	}
}
