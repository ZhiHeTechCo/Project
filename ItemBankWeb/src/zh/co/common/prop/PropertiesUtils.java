
package zh.co.common.prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import zh.co.common.exception.CmnSysException;

/**
 * <p>[概要] Properties file utilities</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public final class PropertiesUtils {
    /**
     * Instance of PropertiesUtils
     */
    private static PropertiesUtils instance;

    /**
     * Put the PropertiesFile.
     */
    private Map<String, PropertiesFile> properties;

    /**
     * Put the absoluteFiles path.
     */
    private Map<String, PropertiesFile> absoluteFiles;
    

    
    /**
     * PropertiesUtils Constructor.
     */
    private PropertiesUtils() {
        properties = new HashMap<String, PropertiesFile>();
        absoluteFiles = new HashMap<String, PropertiesFile>();
    }

    /**
     * New a PropertiesUtils,when get Instance for the first time.
     * @return PropertiesUtils PropertiesUtils
     */
    public static PropertiesUtils getInstance() {
        if (instance == null) {
            synchronized (PropertiesUtils.class) {
                if (instance == null) {
                    instance = new PropertiesUtils();
                }
            }
        }
        return instance;
    }
    
    public static String baseDir() {
        return  System.getProperty("web.root");
    }
    
    /**
     * 
     * @param fileClasspath fullPath
     * @param key key
     * @return String String
     */
    public String getValue(String fileClasspath, String key) {
        String value = null;
        if (properties.containsKey(fileClasspath)) {
            PropertiesFile propertiesFile = properties.get(fileClasspath);
            value = propertiesFile.getProp().getProperty(key);
            value = convertPropertyValue(value);
        } else {
            Properties prop = new Properties();
            InputStream is = this.getClass().getResourceAsStream(fileClasspath);
            if (is == null) { // resource not found
                throw new CmnSysException();
            }
            try {
                prop.load(is);
                PropertiesFile pf = new PropertiesFile(prop, 0);
                properties.put(fileClasspath, pf);
                value = prop.getProperty(key);
                value = convertPropertyValue(value);
                is.close();
            } catch (IOException e) {
                throw new CmnSysException();
            }
        }
        return value;
    }
    
    
    public String getSgValue(String key){
        //return this.getValue("application.properties", key);
        return this.getValueFromAbsoluteFile(baseDir() + "config/application.properties", key);
    }
    
    private String convertPropertyValue(String value) {
        if (value == null) {
            return null;
        }
        try {
            byte[] bytes = value.getBytes("ISO-8859-1");
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            return value;
        }
    }

    /**
     * 
     * @param fullPath full path
     * @param key key
     * @return String
     */
    public String getValueFromAbsoluteFile(String fullPath, String key) {
        String value = null;
        File file = new File(fullPath);
        long lastModified = file.lastModified();
        if (absoluteFiles.containsKey(fullPath)) {

            PropertiesFile propertiesFile = absoluteFiles.get(fullPath);

            if (propertiesFile.lastModified != lastModified) {
                // reload
                Properties prop = new Properties();
                FileInputStream fileInputStream;
                try {
                    fileInputStream = new FileInputStream(file);
                    prop.load(fileInputStream);
                    propertiesFile.setProp(prop);
                    propertiesFile.setLastModified(lastModified);
                    absoluteFiles.put(fullPath, propertiesFile);
                    value = prop.getProperty(key);
                    value = convertPropertyValue(value);
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    throw new CmnSysException();
                } catch (IOException e) {
                    throw new CmnSysException();
                }
            } else {
                value = propertiesFile.getProp().getProperty(key);
                value = convertPropertyValue(value);
            }
        } else {
            Properties prop = new Properties();
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
                prop.load(fileInputStream);
                PropertiesFile pf = new PropertiesFile(prop, lastModified);
                absoluteFiles.put(fullPath, pf);
                value = prop.getProperty(key);
                value = convertPropertyValue(value);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                throw new CmnSysException();
            } catch (IOException e) {
                throw new CmnSysException();
            }
        }
        return value;
    }
    
    /**
     * <p>[概 要]</p>
     * <p>[備 考]</p>
     * 
     * @param pageId
     * @return
     */
    public String getPageTitle(String pageId){
        return this.getValueFromAbsoluteFile(baseDir() + "config/page_title.conf", pageId);
    }
    
    /**
     * private class for properties file
     * @author 
     *  
     */
    private class PropertiesFile {
        /**
         * lastModified time.
         */
        private long lastModified;
        /**
         * Properties.
         */
        private Properties prop;
        /**
         * Constructor.
         */
        public PropertiesFile() {

        }
        /**
         * PropertiesFile
         * @param prop prop
         * @param lastModified lastModified
         */
        public PropertiesFile(Properties prop, long lastModified) {
            this.prop = prop;
            this.lastModified = lastModified;
        }
        /**
         * getLastModified
         * @return long long
         */
        public long getLastModified() {
            return lastModified;
        }
        /**
         * getProp
         * @return Properties Properties
         */
        public Properties getProp() {
            return prop;
        }
        /**
         * setLastModified
         * @param lastModified lastModified
         */
        public void setLastModified(long lastModified) {
            this.lastModified = lastModified;
        }
        /**
         * setProp
         * @param prop prop
         */
        public void setProp(Properties prop) {
            this.prop = prop;
        }

    }
}
