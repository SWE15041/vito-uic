package com.vito.website.core.cache;

import com.vito.common.util.string.EncodeUtil;
import com.vito.common.util.string.StringUtil;
import com.vito.website.core.Application;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class SystemDataHolder {
	
	/** 日志记录器 */
	private static Logger LOG = LoggerFactory.getLogger(SystemDataHolder.class);

	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * web目录的绝对地址
	 */
	public static final String WEBCONTEXT_REAL_PATH = "webContextRealPath";

	/**
	 * web上下文
	 */
	public static final String WEBCONTEXT_PATH = "webContextPath";
	
	private static SystemDataHolder instance = null;

	private Properties props = null;
	
	private CacheDataModel systemCache = new CacheDataModel();
	
	private static SystemDataHolder getInstance() {
		if(instance == null) {
			init();
		}
		return instance;
	}
	
	public SystemDataHolder(Properties props) {
		initParameters(props);
	}
	
	private void initParameters(Properties props) {
		this.props = props;
	}
	
	/**
	 * 系统数据缓存初始化
	 */
	public static void init() {
		if(instance == null) {
			synchronized(SystemDataHolder.class) {
				LOG.info("系统数据持有类初始化...");
				Properties props = getSystemConfProps();
				instance = new SystemDataHolder(props);
				instance.initParameters(props);
				LOG.info("系统数据持有类初始化[OK]");
			}
		}
	}
	
	/**
	 * 其他模块系统数据持有类初始化
	 * 
	 */
	public void initModuleDataHolder(){
		Map<String, CustomCahceProvider> customCahceProviders = Application.getBeanContext().getBeansByType(CustomCahceProvider.class);
		for (CustomCahceProvider customCahceProvider : customCahceProviders.values()) {
			customCahceProvider.addCacheData(systemCache);
		}
	}

	public static synchronized void refresh(){
		LOG.info("刷新系统缓存数据开始...");
		if (instance == null) {
			init();
			instance.initModuleDataHolder();
		} else {
			Properties props = getSystemConfProps();
			instance.initParameters(props);
			instance.initModuleDataHolder();
		}
		LOG.info("刷新系统缓存数据OK.");
	}
	
	private static Properties getSystemConfProps() {
		LOG.info("systemConf.properties系统配置读取开始...");
		InputStream inputStream = null;
		Properties sysConf = new Properties();
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties");
			if (inputStream != null) {
				sysConf.load(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
				}
			}
		}
		LOG.info("systemConf.properties系统配置读取[OK]");
		return sysConf;
	}
	
	public Object getSystemParam(String paramKey) {
		Object paramValue = this.systemCache.get(paramKey);
		if (paramValue == null) {
			String isoParamValue = props.getProperty(paramKey);
			if (StringUtils.isNotBlank(isoParamValue)) {
				paramValue = EncodeUtil.convertStrEncode(isoParamValue, EncodeUtil.ISO_8859_1, EncodeUtil.UTF_8);
			}

		}
		return paramValue;
	}
	
	public <T> T getSystemParam(String paramKey, Class<T> requiredType) {
		Object paramValue = getSystemParam(paramKey);
		if (paramValue != null) {
			if (paramValue instanceof String) {
				return StringUtil.cast((String) paramValue, requiredType);
			} else {
				return requiredType.cast(paramValue);
			}
		} else {
			return null;
		}
	}

	public void setSystemParam(String paramKey, Object paramValue) {
		this.systemCache.add(paramKey, paramValue);
	}

	public static Object getParam(String paramKey) {
		return getInstance().getSystemParam(paramKey);
	}

	public static Object getParam(String paramKey, Object defaultVal) {
		Object paramValue = getInstance().getSystemParam(paramKey);
		if (paramValue == null) {
			paramValue = defaultVal;
		}
		return paramValue;
	}

	public static <T> T getParam(String paramKey, Class<T> clazz) {
		return  getInstance().getSystemParam(paramKey, clazz);
	}

	public static <T> T getParam(String paramKey, Class<T> clazz, T defaultVal) {
		T paramValue = getInstance().getSystemParam(paramKey, clazz);
		if (paramValue == null) {
			paramValue = defaultVal;
		}
		return paramValue;
	}

	public static void setParam(String paramKey, Object paramValue) {
		getInstance().setSystemParam(paramKey, paramValue);
	}
	
}
