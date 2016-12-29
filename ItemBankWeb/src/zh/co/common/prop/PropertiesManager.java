package zh.co.common.prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import zh.co.common.constant.SystemConstants;
import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;

/**
 * <p>[概要] Properties file reader. This class provide convenience methods for read
 *           properties files in both classpath and file system. The properties files are
 *           assumed to be encoded in UTF-8.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class PropertiesManager {

    private static CmnLogger logger = CmnLogger
            .getLogger(PropertiesManager.class);

    /**
     * Properties file info, especially last modified time.
     * 
     */
    private class PropertiesFileInfo {

        private Properties properties;

        private long lastModified;

        public PropertiesFileInfo(Properties properties, long lastModified) {
            this.properties = properties;
            this.lastModified = lastModified;
        }

        public Properties getProperties() {
            return properties;
        }

        public long getLastModified() {
            return lastModified;
        }
    }

    /**
     * Instance of CmnPropertiesReader
     */
    private static PropertiesManager instance;

    /**
     * classpath properties files cache
     */
    private Map<String, PropertiesFileInfo> classpathPropertiesCache = new HashMap<String, PropertiesFileInfo>();

    /**
     * file system conf files cache
     */
    private Map<String, PropertiesFileInfo> fileSystemPropertiesCache = new HashMap<String, PropertiesFileInfo>();

    /**
     * default constructor
     */
    private PropertiesManager() {

    }

    /**
     * Exploit the double check locking code idiom.
     * 
     * @return CmnPropertiesReader
     */
    public static PropertiesManager getInstance() {
        if (instance == null) {
            synchronized (PropertiesManager.class) {
                if (instance == null) {
                    instance = new PropertiesManager();
                }
            }
        }
        return instance;
    }

    /**
     * classpath properties files
     */
    public String getPropValue(String pathName, String key) {
        return getClassPathProperties(pathName).getProperty(key);
    }
    
    public Properties getClassPathProperties(String pathName) {
    	if (!classpathPropertiesCache.containsKey(pathName)) {
    		logger.debug(pathName);
            PropertiesFileInfo propsFile = loadPropertiesFile(pathName);
            classpathPropertiesCache.put(pathName, propsFile);
        }
    	
        return classpathPropertiesCache.get(pathName).getProperties();
    }

    /**
     * file system conf files (actually properties files)
     */
    public String getConfValue(String pathName, String key) {
        return getFileSystemProperties(pathName).getProperty(key);
    }
    
    public Properties getFileSystemProperties(String pathName) {
    	if (!fileSystemPropertiesCache.containsKey(pathName)
                || fileSystemPropertiesCache.get(pathName).getLastModified() != new File(
                		pathName).lastModified()) {
            PropertiesFileInfo propsFile = loadConfFile(pathName);
            fileSystemPropertiesCache.put(pathName, propsFile);
        }
    	
    	return fileSystemPropertiesCache.get(pathName).getProperties();
    }

    /**
     * classpath properties files
     */
    private PropertiesFileInfo loadPropertiesFile(String pathname) {
        PropertiesFileInfo propsFile = null;
        Reader reader = null;
        try {
            reader = new InputStreamReader(getClass().getResourceAsStream(
                    pathname), SystemConstants.FILE_ENCODING);
            Properties props = new Properties();
            PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
            propertiesPersister.load(props, reader);
            propsFile = new PropertiesFileInfo(props, 0);
        } catch (IOException e) {
            logger.log(MessageId.COMMON_E_0002,
                    new Object[] { pathname }, e);
            throw new CmnSysException(MessageId.COMMON_E_0002,
                    new Object[] { pathname }, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.debug("Failed to close the reader : ", e);
            }
        }
        return propsFile;
    }

    /**
     * file system conf files
     */
    private PropertiesFileInfo loadConfFile(String pathname) {
        PropertiesFileInfo propsFile = null;
        Reader reader = null;
        try {
            File file = new File(pathname);
            reader = new InputStreamReader(new FileInputStream(file),
            		SystemConstants.FILE_ENCODING);
            Properties props = new Properties();
            PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
            propertiesPersister.load(props, reader);
            propsFile = new PropertiesFileInfo(props, file.lastModified());
        } catch (IOException e) {
            logger.log(MessageId.COMMON_E_0002,
                    new Object[] { pathname }, e);
            throw new CmnSysException(MessageId.COMMON_E_0002,
                    new Object[] { pathname }, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.debug("Failed to close the reader : ", e);
            }
        }
        return propsFile;
    }

}
