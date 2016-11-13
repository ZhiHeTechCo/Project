package zh.co.common.ibatis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import zh.co.common.constant.SystemConstants;
import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.prop.PropertiesUtils;
import zh.co.common.utils.FileUtils;
import zh.co.common.utils.WebUtils;
import freemarker.template.Template;

/**
 * With the support of this class, sql mappers could be reloaded.
 * 
 */
public class XmlMapperUtils {

	private static CmnLogger logger = CmnLogger.getLogger(XmlMapperUtils.class);

	private static Pattern namespacePattern = Pattern.compile(
			"<mapper\\s*namespace\\s*=\\s*\"(.*?)\"\\s*>", Pattern.DOTALL);

	private static Pattern originalSqlPattern = Pattern
			.compile("<select\\s*id\\s*=\\s*\"(.*?)\".*?>(.*?)</select>",
					Pattern.DOTALL);
	
	private static Pattern selectSqlPattern = Pattern
			.compile("<select\\s*id\\s*=\\s*\"(.*?)\".*?>(.*?)</select>",
					Pattern.DOTALL);

	private static Pattern insertSqlPattern = Pattern
			.compile("<insert\\s*id\\s*=\\s*\"(.*?)\".*?>(.*?)</insert>",
					Pattern.DOTALL);

	private static Pattern updateSqlPattern = Pattern
			.compile("<update\\s*id\\s*=\\s*\"(.*?)\".*?>(.*?)</update>",
					Pattern.DOTALL);

	private static Pattern deleteSqlPattern = Pattern
			.compile("<delete\\s*id\\s*=\\s*\"(.*?)\".*?>(.*?)</delete>",
					Pattern.DOTALL);

	public static Template countSqlTemplate;

	public static String[] DYNAMIC_SQL_PATHS = new String[] {
			"/im/inventory/sql/",
			"/im/inventory/sql/",
			"/common/freemarker/sql/" };
	
	static {
		freemarker.template.Configuration cfg = new freemarker.template.Configuration();
		cfg.setClassForTemplateLoading(XmlMapperUtils.class, "");
		cfg.setDefaultEncoding("UTF-8");
		String templateName = "count_sql.ftl";
		try {
			countSqlTemplate = cfg.getTemplate(templateName);
		} catch (IOException e) {
			logger.log(MessageId.COMMON_E_0002,
					new String[] { templateName }, e);
			throw new CmnSysException(MessageId.COMMON_E_0002,
					new String[] { templateName }, e);
		}
	}

	public static void addMapper(Configuration configuration,
			Resource mapperResource) {
		// parse mapper xml
		XMLMapperBuilder mapperBuilder = null;
		String url = null;
		try {
			if (!(mapperResource instanceof ByteArrayResource)) {
				url = mapperResource.getURL().toString();
			} else {
				url = ((ByteArrayResource) mapperResource).getDescription();
			}
			logger.debug("loading sql mapper : " + url);
			mapperBuilder = new XMLMapperBuilder(new InputStreamReader(
					new BomRemoverInputStream(mapperResource.getInputStream()),
					"UTF-8"), configuration, url, new HashMap<String, XNode>());
			mapperBuilder.parse();
		} catch (Exception e) {
			logger
					.log(MessageId.COMMON_E_0002, new String[] { url },
							e);
			throw new CmnSysException(MessageId.COMMON_E_0002,
					new String[] { url }, e);
		}
	}

	public static void addMapper(Configuration configuration, File mapperFile) {
	    // get file content
        String fileContent = fileContent(mapperFile);
        String encryptionPass = PropertiesUtils.getInstance().getSgValue(SystemConstants.DB_ENCRYPTION_PASSWORD);
        if(encryptionPass == null){
            encryptionPass = "";
        }
        fileContent = fileContent.replaceAll("\\$\\{DB_ENCODE_KEY\\}", encryptionPass);
        
        Integer maxDisplayCount= WebUtils.getMaxSearchDisplayCount();
        fileContent = fileContent.replaceAll("\\$\\{MAX_SEARCH_DISPLAY\\}", String.valueOf(maxDisplayCount));
        
        // get namespace
        String namespace = namespace(fileContent);
        //logger.debug("count_sql = \n" + fileContent);

        // add to configuration
        try {
            addMapper(configuration, new ByteArrayResource(fileContent
                    .getBytes("UTF-8"), namespace + ".xml"));
        } catch (UnsupportedEncodingException e) {
            // do nothing since it never happens
        }
	}

	public static String getDynamicSqlFilePath(String fileNamePrefix) {
		List<File> sqlFiles = listDynamicSqlFiles();
		int index = lastIndexWithSuffix(sqlFiles, fileNamePrefix + ".xml");
		if (index != -1) {
			return sqlFiles.get(index).getPath();
		} else {
			return "/im/inventory/sql/" + fileNamePrefix
					+ ".xml";
		}
	}
	
	private static int lastIndexWithSuffix(List<File> list, String suffix) {
    	int max = list.size() - 1;
    	for (int i = max; i >= 0; i--) {
    		String path = list.get(i).getPath();
    		int index = path.lastIndexOf(FileUtils.fileSeparator());
    		String fileNm = path.substring(index + 1);
    		if (fileNm.equals(suffix)) {
    			return i;
    		}
    	}
    	return -1;
    }

	public static void addCountSql(Configuration configuration, File mapperFile) {
		// get file content
		String fileContent = fileContent(mapperFile);
		String encryptionPass = PropertiesUtils.getInstance().getSgValue(SystemConstants.DB_ENCRYPTION_PASSWORD);
        if(encryptionPass == null){
            encryptionPass = "";
        }
		fileContent = fileContent.replaceAll("\\$\\{DB_ENCODE_KEY\\}", encryptionPass);
		
		Integer maxDisplayCount= WebUtils.getMaxSearchDisplayCount();
        fileContent = fileContent.replaceAll("\\$\\{MAX_SEARCH_DISPLAY\\}", String.valueOf(maxDisplayCount));
        
		// get namespace
		String namespace = namespace(fileContent);

		// get originalSqls
		List<Sql> originalSqls = sqls(fileContent);

		// generate count sql
		StringWriter writer = new StringWriter();
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("namespace", namespace);
		rootMap.put("originalSqls", originalSqls);
		rootMap.put("MAX_COUNT", String.valueOf(WebUtils.getMaxSearchCount()+1));
		try {
			countSqlTemplate.process(rootMap, writer);
		} catch (Exception e) {
			logger.log(MessageId.COMMON_E_0005, null, e);
			throw new CmnSysException(MessageId.COMMON_E_0005, null, e);
		}
		//logger.debug("count_sql = \n" + writer.toString());

		// add to configuration
		try {
			addMapper(configuration, new ByteArrayResource(writer.toString()
					.getBytes("UTF-8"), namespace + ".xml"));
		} catch (UnsupportedEncodingException e) {
			// do nothing since it never happens
		}
	}

	public static String fileContent(File file) {
		StringBuilder sb = new StringBuilder();
		String url = null;
		BufferedReader reader = null;
		InputStreamReader inputStreamReader = null;
		FileInputStream fileInputStream = null;
		
		try {
			url = file.getCanonicalPath();
			
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
		    reader = new BufferedReader(inputStreamReader);
			
			String line = null;
			boolean firstLine = true;
			while ((line = reader.readLine()) != null) {
				if (firstLine) {
					firstLine = false;
				} else {
					sb.append("\n");
				}
				sb.append(line);
			}
		} catch (IOException e) {
			logger
					.log(MessageId.COMMON_E_0002, new String[] { url },
							e);
			throw new CmnSysException(MessageId.COMMON_E_0002,
					new String[] { url }, e);
		} finally{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.log(MessageId.COMMON_E_0002, new String[] { url }, e);
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					logger.log(MessageId.COMMON_E_0002, new String[] { url }, e);
				}
			}
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					logger.log(MessageId.COMMON_E_0002, new String[] { url }, e);
				}
			}
		}
		return sb.toString();
	}

	public static String namespace(String fileContent) {
		Matcher matcher = namespacePattern.matcher(fileContent);
		matcher.find();
		String namespace = matcher.group(1);
		logger.debug("mapper namespace = " + namespace);
		return namespace;
	}

	private static List<Sql> sqls(String fileContent) {
		List<Sql> sqls = new ArrayList<Sql>();
		Matcher matcher = originalSqlPattern.matcher(fileContent);
		while (matcher.find()) {
			String sqlId = matcher.group(1);
			//logger.debug("sqlId = " + sqlId);
			String sqlContent = matcher.group(2);
			//logger.debug("sqlContent = " + sqlContent);
			sqlContent = sqlContent.replaceAll("(?i:limit)([^\\)]*)$", "");
			sqlContent = sqlContent.replaceAll("(?i:order by)([^\\)]*)$", "");
			//logger.debug("sqlContent(remove [limit] and [order by]) = " + sqlContent);
			if (countSqlAvailable(sqlId, sqlContent)) {
				sqls.add(new Sql(sqlId, sqlContent));
			}
		}
		return sqls;
	}
	
	private static boolean countSqlAvailable(String sqlId, String sqlContent) {
		return !sqlId.endsWith("Count")
				&& !includesSqlFragment(sqlId, sqlContent);
	}
	
	private static boolean includesSqlFragment(String sqlId, String sqlContent) {
		return sqlContent.contains("<include");
	}

	public static class Sql {
		private String sqlId;
		private String sqlContent;

		public String getSqlId() {
			return sqlId;
		}

		public void setSqlId(String sqlId) {
			this.sqlId = sqlId;
		}

		public String getSqlContent() {
			return sqlContent;
		}

		public void setSqlContent(String sqlContent) {
			this.sqlContent = sqlContent;
		}

		Sql(String sqlId, String sqlContent) {
			this.sqlId = sqlId;
			this.sqlContent = sqlContent;
		}
	}

	public static List<File> listDynamicSqlFiles() {
		List<File> fileList = new ArrayList<File>();
		for (String config : DYNAMIC_SQL_PATHS) {
			fileList.addAll(FileUtils.listFile(new File(config), "xml", true));
		}
		return fileList;
	}
	
	public static boolean isDynamicSqlFile(String file) {
		String path = new File(file).getPath().replace("\\", "/");
		for (String dynamicSqlPath : DYNAMIC_SQL_PATHS) {
			if (path.contains(dynamicSqlPath)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> sqlIds(String fileContent) {
		return sqlIds(fileContent, true);
	}

	public static List<String> sqlIds(String fileContent, boolean withCountSql) {
		List<String> sqlIds = new ArrayList<String>();
		Matcher matcher = null;

		// select
		matcher = selectSqlPattern.matcher(fileContent);
		while (matcher.find()) {
			String sqlId = matcher.group(1);
			logger.debug("sqlId = " + sqlId);
			String sqlContent = matcher.group(2);
			logger.debug("sqlContent = " + sqlContent);
			sqlIds.add(sqlId);
			if (withCountSql) {
				sqlIds.add(sqlId + "_count");
			}
		}

		// insert
		matcher = insertSqlPattern.matcher(fileContent);
		while (matcher.find()) {
			String sqlId = matcher.group(1);
			logger.debug("sqlId = " + sqlId);
			String sqlContent = matcher.group(2);
			logger.debug("sqlContent = " + sqlContent);
			sqlIds.add(sqlId);
		}

		// update
		matcher = updateSqlPattern.matcher(fileContent);
		while (matcher.find()) {
			String sqlId = matcher.group(1);
			logger.debug("sqlId = " + sqlId);
			String sqlContent = matcher.group(2);
			logger.debug("sqlContent = " + sqlContent);
			sqlIds.add(sqlId);
		}

		// delete
		matcher = deleteSqlPattern.matcher(fileContent);
		while (matcher.find()) {
			String sqlId = matcher.group(1);
			logger.debug("sqlId = " + sqlId);
			String sqlContent = matcher.group(2);
			logger.debug("sqlContent = " + sqlContent);
			sqlIds.add(sqlId);
		}

		return sqlIds;
	}

}
