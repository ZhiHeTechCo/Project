package zh.co.common.log;

import java.util.HashMap;
import java.util.Map;


import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootLogger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * <p>[概要] This RepositorySelector is for use with web applications</p>
 * <p>[详细] 没有</p>
 * <p>[备考] 没有</p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class CmnRepositorySelector implements RepositorySelector {

	private static boolean initialized = false;
	private static Object guard = LogManager.getRootLogger();

	private static Map<ClassLoader, LoggerRepository> repositories = new HashMap<ClassLoader, LoggerRepository>();
	private static LoggerRepository defaultRepository;

	private CmnRepositorySelector() {
	}

	/**
	 * Register your web-app with this repository selector.
	 */
	public static synchronized void init() {
		if (!initialized) { // set the global RepositorySelector
			defaultRepository = LogManager.getLoggerRepository();
			RepositorySelector theSelector = new CmnRepositorySelector();
			LogManager.setRepositorySelector(theSelector, guard);
			initialized = true;
		}

		Hierarchy hierarchy = new Hierarchy(new RootLogger(Level.DEBUG));
		initRepositoryConfig(hierarchy);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		repositories.put(loader, hierarchy);
	}

	private static void initRepositoryConfig(LoggerRepository repository) {
		new DOMConfigurator().doConfigure("log4j.xml", repository);
	}
	
	public static synchronized void removeFromRepository() {
		repositories.remove(Thread.currentThread().getContextClassLoader());
	}

	public LoggerRepository getLoggerRepository() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		LoggerRepository repository = (LoggerRepository) repositories
				.get(loader);

		if (repository == null) {
			return defaultRepository;
		} else {
			return repository;
		}
	}

}
