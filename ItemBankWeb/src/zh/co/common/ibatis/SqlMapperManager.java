package zh.co.common.ibatis;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;









import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CollectionUtils;

/**
 * This class managers inventory SQL mapper configuration files. Modified files
 * will be reloaded immediately.
 * 
 * @see zh.co.common.ibatis.DynaConfiguration
 */
public class SqlMapperManager {
    private static CmnLogger logger = CmnLogger
            .getLogger(SqlMapperManager.class);

    private static Map<String, SqlMapperInfo> sqlMapperCache = new ConcurrentHashMap<String, SqlMapperInfo>();

    /**
     * last modified information of sql mapper files.
     */
    private static class SqlMapperInfo {
        private String filePath;
        private long lastModified;

        public SqlMapperInfo(String filePath, long lastModified) {
            this.filePath = filePath;
            this.lastModified = lastModified;
        }
    }

    /**
     * Record last modified information for the specified sql mapper file.
     * 
     * @param filePath
     * @param lastModified
     */
    public static void addSqlMapperInfo(String filePath, long lastModified) {
        sqlMapperCache.put(filePath, new SqlMapperInfo(filePath,
                lastModified));
    }

    /**
     * Reload the specified sqlId. Reload the corresponding SQL mapper file if
     * it has been modified, or do nothing.
     */
    public static void reload(String sqlId, SqlSessionFactory sqlSessionFactory) {
		if (StringUtils.isEmpty(sqlId)) {
			logger.log(MessageId.COMMON_E_0004, new String[] { sqlId });
			throw new CmnSysException(MessageId.COMMON_E_0004,
					new String[] { sqlId });
		}
    	
        logger.debug("sqlId = " + sqlId);
        String sqlNamespace = sqlId.split("\\.")[0];
        String filePath = sqlMapperFilePath(sqlId);
        File file = new File(filePath);
		if (!file.exists()) {
			logger.log(MessageId.COMMON_E_0004, new String[] { sqlId });
			throw new CmnSysException(MessageId.COMMON_E_0004,
					new String[] { sqlId });
		}
        SqlMapperInfo mapperInfo = sqlMapperCache.get(filePath);
        if (mapperInfo == null
                || file.lastModified() != mapperInfo.lastModified) {
        	
//            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) SpringAppContextManager
//                    .getBean("sqlSessionFactory");

            DynaConfiguration sqlConfig = (DynaConfiguration) sqlSessionFactory
                    .getConfiguration();
            sqlConfig.removeSqlMapper(sqlNamespace);
            XmlMapperUtils.addMapper(sqlConfig, file);
            XmlMapperUtils.addCountSql(sqlConfig, file);
            sqlMapperCache.put(filePath, new SqlMapperInfo(filePath, file
                    .lastModified()));
        }
        
        if (!sqlIdExists(sqlId, sqlSessionFactory)) {
        	logger.log(MessageId.COMMON_E_0004, new String[] { sqlId });
        	throw new CmnSysException(MessageId.COMMON_E_0004,
        			new String[] { sqlId });
        }
    }

    private static String sqlMapperFilePath(String sqlId) {
        String sqlNamespace = sqlId.split("\\.")[0];
        
        String fileName = sqlNamespace + ".xml";
        SqlMapperInfo sqlMapperInfo = (SqlMapperInfo) CollectionUtils
                .getWithSuffix(sqlMapperCache, fileName);
        if (sqlMapperInfo != null && !XmlMapperUtils.isDynamicSqlFile(sqlMapperInfo.filePath)) {
            return sqlMapperInfo.filePath;
        } else {
            // considered as a SQL of a dynamic SQL file
            return XmlMapperUtils.getDynamicSqlFilePath(sqlNamespace);
        }
    }
    
	private static boolean sqlIdExists(String sqlId, SqlSessionFactory sqlSessionFactory) {
		DynaConfiguration sqlConfig = (DynaConfiguration) sqlSessionFactory
				.getConfiguration();
		return sqlConfig.getMappedStatementNames().contains(sqlId);
	}
	
}
