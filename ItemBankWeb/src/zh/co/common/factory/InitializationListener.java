package zh.co.common.factory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



import org.springframework.web.context.support.WebApplicationContextUtils;

import zh.co.common.utils.SpringAppContextManager;

/**
 * Servlet listener class for NGOSS framework initialization.
 * 
 */
public class InitializationListener implements ServletContextListener {

	/**
	 * Initialize the log4j logger.
	 * 
	 * @param event
	 *            ServletContextEvent
	 */
	public void contextInitialized(ServletContextEvent event) {
		// Lets WebAplicationContext initialized by ContextLoaderListener to be
		// available via SpringAppContextManager
		SpringAppContextManager.initialize(WebApplicationContextUtils
				.getWebApplicationContext(event.getServletContext()));
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
