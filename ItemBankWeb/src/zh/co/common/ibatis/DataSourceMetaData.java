package zh.co.common.ibatis;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DataSourceMetaData {
	public static final String SUFFIX_DATASOURCE_DRIVER = "datasource.driverClassName";
	public static final String SUFFIX_DATASOURCE_URL = "datasource.url";
	public static final String SUFFIX_DATASOURCE_USERNAME = "datasource.username";
	public static final String SUFFIX_DATASOURCE_PASSWORD = "datasource.password";
	public static final String SUFFIX_DATASOURCE_TESTONBORROW = "datasource.testOnBorrow";
	public static final String SUFFIX_DATASOURCE_VALIDATIONQUERY = "datasource.validationQuery";

	private String dataSourceName;
	private String driverClassName;
	private String url;
	private String username;
	private String password;
	private boolean testOnBorrow;
	private String validationQuery;
	private boolean isDefault = false;

	public DataSourceMetaData(String dataSourceName) {
		this.dataSourceName = dataSourceName;
		if ("".equals(dataSourceName)) {
			isDefault = true;
		}
	}

	public void setProperty(String propertyName, String propertyValue) {
		if (SUFFIX_DATASOURCE_DRIVER.equals(propertyName)) {
			this.driverClassName = propertyValue;
		} else if (SUFFIX_DATASOURCE_URL.equals(propertyName)) {
			this.url = propertyValue;
		} else if (SUFFIX_DATASOURCE_USERNAME.equals(propertyName)) {
			this.username = propertyValue;
		} else if (SUFFIX_DATASOURCE_PASSWORD.equals(propertyName)) {
			this.password = propertyValue;
		} else if (SUFFIX_DATASOURCE_TESTONBORROW.equals(propertyName)) {
			this.testOnBorrow = "true".equalsIgnoreCase(propertyValue);
		} else if (SUFFIX_DATASOURCE_VALIDATIONQUERY.equals(propertyName)) {
			this.validationQuery = propertyValue;
		}
	}

	public boolean isDefault() {
		return isDefault;
	}

	public DataSource createDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		if (testOnBorrow && !StringUtils.isEmpty(validationQuery)) {
			ds.setTestOnBorrow(true);
			ds.setValidationQuery(validationQuery);
		}
		return ds;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static Map<String, DataSourceMetaData> dataSourceMetaDataMap(
			Properties props) {
		Map<String, DataSourceMetaData> dataSourceMetaDataMap = new HashMap<String, DataSourceMetaData>();

		if (props == null) {
			return dataSourceMetaDataMap;
		}

		for (String propName : Collections.list((Enumeration<String>) props
				.propertyNames())) {
			String dataSourceName = null;
			DataSourceMetaData dataSourceMetaData = null;
			if (propName.contains(SUFFIX_DATASOURCE_DRIVER)) {
				dataSourceName = dataSourceName(propName,
						SUFFIX_DATASOURCE_DRIVER);
				dataSourceMetaData = dataSourceMetaData(dataSourceName,
						dataSourceMetaDataMap);
				dataSourceMetaData.setProperty(SUFFIX_DATASOURCE_DRIVER,
						props.getProperty(propName));
			} else if (propName.contains(SUFFIX_DATASOURCE_URL)) {
				dataSourceName = dataSourceName(propName, SUFFIX_DATASOURCE_URL);
				dataSourceMetaData = dataSourceMetaData(dataSourceName,
						dataSourceMetaDataMap);
				dataSourceMetaData.setProperty(SUFFIX_DATASOURCE_URL,
						props.getProperty(propName));
			} else if (propName.contains(SUFFIX_DATASOURCE_USERNAME)) {
				dataSourceName = dataSourceName(propName,
						SUFFIX_DATASOURCE_USERNAME);
				dataSourceMetaData = dataSourceMetaData(dataSourceName,
						dataSourceMetaDataMap);
				dataSourceMetaData.setProperty(SUFFIX_DATASOURCE_USERNAME,
						props.getProperty(propName));
			} else if (propName.contains(SUFFIX_DATASOURCE_PASSWORD)) {
				dataSourceName = dataSourceName(propName,
						SUFFIX_DATASOURCE_PASSWORD);
				dataSourceMetaData = dataSourceMetaData(dataSourceName,
						dataSourceMetaDataMap);
				dataSourceMetaData.setProperty(SUFFIX_DATASOURCE_PASSWORD,
						props.getProperty(propName));
			} else if (propName.contains(SUFFIX_DATASOURCE_TESTONBORROW)) {
				dataSourceName = dataSourceName(propName,
						SUFFIX_DATASOURCE_TESTONBORROW);
				dataSourceMetaData = dataSourceMetaData(dataSourceName,
						dataSourceMetaDataMap);
				dataSourceMetaData.setProperty(SUFFIX_DATASOURCE_TESTONBORROW,
						props.getProperty(propName));
			} else if (propName.contains(SUFFIX_DATASOURCE_VALIDATIONQUERY)) {
				dataSourceName = dataSourceName(propName,
						SUFFIX_DATASOURCE_VALIDATIONQUERY);
				dataSourceMetaData = dataSourceMetaData(dataSourceName,
						dataSourceMetaDataMap);
				dataSourceMetaData.setProperty(SUFFIX_DATASOURCE_VALIDATIONQUERY,
						props.getProperty(propName));
			}

		}
		return dataSourceMetaDataMap;
	}

	private static String dataSourceName(String propKey, String dataSourceSuffix) {
		String dsName = null;
		if (propKey.length() > dataSourceSuffix.length()) {
			dsName = propKey
					.substring(0, propKey.indexOf(dataSourceSuffix) - 1);
		} else {
			dsName = "";
		}
		return dsName;
	}

	private static DataSourceMetaData dataSourceMetaData(String dataSourceName,
			Map<String, DataSourceMetaData> dataSourceMetaDataMap) {
		DataSourceMetaData dataSourceMetaData = null;
		if (dataSourceMetaDataMap.containsKey(dataSourceName)) {
			dataSourceMetaData = dataSourceMetaDataMap.get(dataSourceName);
		} else {
			dataSourceMetaData = new DataSourceMetaData(dataSourceName);
			dataSourceMetaDataMap.put(dataSourceName, dataSourceMetaData);
		}
		return dataSourceMetaData;
	}
}
