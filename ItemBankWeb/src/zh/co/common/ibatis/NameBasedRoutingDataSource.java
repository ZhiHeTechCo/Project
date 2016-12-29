package zh.co.common.ibatis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;




import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import zh.co.common.log.CmnLogger;
import zh.co.common.prop.PropertiesUtils;

public class NameBasedRoutingDataSource extends AbstractRoutingDataSource {
	private static CmnLogger logger = CmnLogger
			.getLogger(NameBasedRoutingDataSource.class);

	private String dataSourceConfigLocation;

	public void setDataSourceConfigLocation(String dataSourceConfigLocation) {
		this.dataSourceConfigLocation = dataSourceConfigLocation;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceSelector.getDataSourceName();
	}

	@Override
	public void afterPropertiesSet() {
		// read datasource.conf to generate properties
		Properties properties = new Properties();
	    FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(PropertiesUtils.configDir() + dataSourceConfigLocation);
			properties.load(inputStream);
			
		} catch (FileNotFoundException e) {
			logger.debug("The datasource.conf file does not exist.", e);
		} catch (IOException e) {
			logger.debug("Fail to read the datasource.conf file.", e);
		} finally{
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					
					logger.debug("Fail to read the datasource.conf file.", e);
				}
			}
		}

		// prepare targetDataSources and defaultTargetDataSource
		Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
		for (Map.Entry<String, DataSourceMetaData> entry : DataSourceMetaData
				.dataSourceMetaDataMap(properties).entrySet()) {
			String dataSourceName = entry.getKey();
			DataSourceMetaData dataSourceMetaData = entry.getValue();
			DataSource dataSourceInstance = dataSourceMetaData
					.createDataSource();
			targetDataSources.put(dataSourceName, dataSourceInstance);
			if (dataSourceMetaData.isDefault()) {
				this.setDefaultTargetDataSource(dataSourceInstance);
			}
		}
		this.setTargetDataSources(targetDataSources);

		// invoke parent method
		super.afterPropertiesSet();
	}
}
