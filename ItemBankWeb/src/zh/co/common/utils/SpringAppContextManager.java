package zh.co.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import zh.co.common.log.CmnLogger;

/**
 * <p>[概要] Facility class to easily load spring application context file in classpath.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class SpringAppContextManager {

    private static CmnLogger logger = CmnLogger
            .getLogger(SpringAppContextManager.class);

    /**
     * default path of the spring configuration file in class path
     */
    public static String DEFAULT_CONFIG_PATH = "file:"
            + "WEB-INF/spring/applicationContext_dataSource.xml";

    /**
     * spring bean factory
     */
    private static ApplicationContext applicationContext;

    /**
     * default constructor
     */
    private SpringAppContextManager() {
    }

    /**
     * Get bean object from the default application context.
     * 
     * @param beanName
     *            bean name
     * @return a bean
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static void initialize() {
    	initialize(DEFAULT_CONFIG_PATH);
    }
    
    /**
     * 
     * @param configPath file system xml config file path
     */
    public static void initialize(String configPath) {
        if (applicationContext == null) {
            synchronized (SpringAppContextManager.class) {
                if (applicationContext == null) {
                    doInit(configPath);
                }
            }
        }
    }

    /**
     * Initialize the bean Factory, use the spring config file in class path
     * 
     * @param ConfigPath
     *            the spring application context file path
     */
    private static void doInit(String ConfigPath) {
        try {
            applicationContext = new FileSystemXmlApplicationContext(ConfigPath);
            logger.debug("Application Context loaded successfully.");
        } catch (Throwable t) {
            throw new RuntimeException(
                    "Can not load the spring application context successfully.",
                    t);
        }
    }
    
	public static void initialize(ApplicationContext applicationContext) {
		if (SpringAppContextManager.applicationContext == null) {
			synchronized (SpringAppContextManager.class) {
				if (SpringAppContextManager.applicationContext == null) {
					SpringAppContextManager.applicationContext = applicationContext;
				}
			}
		}
	}
}
