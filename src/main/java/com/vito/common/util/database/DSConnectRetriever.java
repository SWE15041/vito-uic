/**
 * 类描述：通过T_MD_SJY获得某个数据源的连接
 */
package com.vito.common.util.database;

import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

public class DSConnectRetriever {
	/** 日志记录器 */
	private static Logger logger = Logger.getLogger(DSConnectRetriever.class);

	/** 驱动 */
	public static final String ORACLE = "oracle.jdbc.driver.OracleDriver";

	/** 用于存储数据源的Map */
	private static HashMap<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

	/**
	 * 通过JNDI名获取Connection，需要注意的是需要在连接配置文件中指定应用服务器的类型
	 * 
	 * @param jndiName
	 *            String
	 * @return Connection
	 * @throws SQLException
	 *             SQLException
	 */
	public static Connection getConnectionByJndi(String jndiName) throws SQLException {

		// 从数据源中获取数据连接，如果Map中没有对应的数据源创建这个数据源并放入Map中
		// 否则从Map直接获取数据源
		try {
			DataSource ds = null;
			if (dataSourceMap.containsKey(jndiName)) {
				ds = (DataSource) dataSourceMap.get(jndiName);
			} else {
				InitialContext context = new InitialContext();
				ds = (DataSource) context.lookup(jndiName);
				dataSourceMap.put(jndiName, ds);
			}
			return ds.getConnection();
		} catch (NamingException ex) {
			logger.error("以" + jndiName + "从数据源中获取数据连接失败，异常信息为：" + ex.getMessage());
			throw new RuntimeException("没有名为" + jndiName + "的JNDI，有两种可能，"
					+ "1:JNDI名字有误，2:Web服务器未配置这个JNDI数据源");
		} catch (Exception ex) {
			throw new RuntimeException(jndiName + "取Jndi连接失败!");
		}
	}

	/**
	 * 通过指定的JDBC连接串获取数据连接
	 * 
	 * @param jdbcUrl
	 *            String JDBC连接串
	 * @param userName
	 *            String 用户名
	 * @param password
	 *            String 密码
	 * @param driver
	 *            数据库驱动程序
	 * @return Connection 数据连接
	 * @throws SQLException
	 *             获取数据连接时失败
	 */
	public static Connection getConnectionByUrl(String jdbcUrl, String userName, String password, String driver)
			throws SQLException {
		String driverName = driver;
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException ex) {
			logger.fatal("没有找到指定的数据库驱动器类：" + ex.getMessage());
		}
		Properties sysProps = new Properties();
		sysProps.put("user", userName);
		sysProps.put("password", password);
		return DriverManager.getConnection(jdbcUrl, sysProps);
	}
	
}
