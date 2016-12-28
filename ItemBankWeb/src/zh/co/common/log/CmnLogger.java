package zh.co.common.log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.xml.DOMConfigurator;

import zh.co.common.constant.SystemConstants;
import zh.co.common.prop.CmnPropertiesResourceBundle;
import zh.co.common.prop.PropertiesUtils;
import zh.co.common.utils.WebUtils;

/**
 * <p>[概要] CmnLogger类</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class CmnLogger {

    private static final String LOGGER_FQCN = CmnLogger.class.getName() + ".";

    /**
     * The enclosed log4j Logger.
     */
    private Logger logger;

    /**
     * log messages
     */
    private static ResourceBundle logMessages;

	static {
		initResource(PropertiesUtils.baseDir() + "/message.properties");
	}

	public static void initLog4jConfig() {
		initConfig(PropertiesUtils.baseDir() + "/log4j.xml");
	}
    
	public static void initScopedConfig() {
		CmnRepositorySelector.init();
	}
    
    private CmnLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    /**
     * initialize the logger with log4j configuration path
     * 
     * @param configPath
     *            configuration path
     */
    private static void initConfig(String configPath) {
        DOMConfigurator.configure(configPath);
    }

    /**
     * Initialize application resources.
     * 
     * @param resourcePath
     *            application resource path
     */
    private static void initResource(String resourcePath) {
        try {
            logMessages = new CmnPropertiesResourceBundle(
                    new InputStreamReader(new FileInputStream(resourcePath),
                            SystemConstants.FILE_ENCODING));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static CmnLogger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    public static CmnLogger getLogger(String name) {
        return new CmnLogger(name);
    }

    /**
     * output log message, with log level defined in log level file.
     * 
     * @param messageCode
     *            message id
     * @param arguments
     *            placeholders
     * @param t
     *            throwable
     * @return message content with placeholder processed
     */
    public String log(String messageCode, Object[] arguments, Throwable t) {
        return doLogWithMessageCode(messageCode, arguments, t, true);
    }

    /**
     * output log message, with log level defined in log level file.
     * 
     * @param messageCode
     *            message id
     * @param arguments
     *            placeholders
     * @return message content with placeholder processed
     */
    public String log(String messageCode, Object[] arguments) {
        return log(messageCode, arguments, (Throwable) null);
    }

    /**
     * output log message, with log level defined in log level file.
     * 
     * @param messageCode
     *            message id
     * @return message content with placeholder processed
     */
    public String log(String messageCode) {
        return log(messageCode, (Object[]) null);
    }

    /**
     * output business log.
     * 
     * @param messageCode
     *            message id
     * @param arguments
     *            placeholders
     * @param userId
     *            user id
     * @param t
     *            throwable
     * @return message content with placeholder processed
     */
    private String bizLog(String messageCode, Object[] arguments,
            String userId, Throwable t) {
        return doLogWithMessageCode(messageCode, arguments, t, false);
    }

    /**
     * output business log.
     * 
     * @param messageCode
     *            message id
     * @param arguments
     *            placeholders
     * @return message content with placeholder processed
     */
    public String bizLog(String messageCode, Object[] arguments) {
        return bizLog(messageCode, arguments, null, null);
    }

    /**
     * Output Business log.
     * 
     * @param messageCode
     *            message id
     * @return message content with placeholder processed
     */
    public String bizLog(String messageCode) {
        return bizLog(messageCode, null, null, null);
    }

    private String doLogWithMessageCode(String messageCode, Object[] arguments,
            Throwable t, boolean outputMsgCode) {
        StringBuffer message = new StringBuffer();
        int messageLevel = CmnLogLevelManager.getLogLevel(messageCode);
        Level log4jLevel;
        switch (messageLevel) {
        case 0:
            log4jLevel = Level.DEBUG;
            break;
        case 1:
            log4jLevel = Level.INFO;
            break;
        case 3:
            log4jLevel = Level.WARN;
            break;
        case 5:
            log4jLevel = Level.ERROR;
            break;
        default:
            messageLevel = 5;
            log4jLevel = Level.ERROR;
        }
        message.append(constructMessageContent(messageCode, messageLevel,
                arguments, outputMsgCode));
        doLog(LOGGER_FQCN, log4jLevel, message.toString(), t);

        return message.toString();
    }

    private String constructMessageContent(String msgCode, int intLevel,
            Object[] args, boolean outputMsgCode) {
        StringBuffer message = new StringBuffer();
        if (outputMsgCode) {
            message.append(msgCode);
            message.append(" ");
        }
        message.append(WebUtils.getCurrentPageId()).append(" ");
        message.append(WebUtils.getLoginUserId()).append(" ");
        message.append(getRawMessageContent(msgCode, args));
        return message.toString();
    }

    /**
     * Get message content
     * 
     * @param messageCode
     *            message id
     * @param arguments
     *            place holders
     * @return message content
     **/
    private String getRawMessageContent(String messageCode, Object[] arguments) {
        String message;
        StringBuffer messageBuf = new StringBuffer();

        try {
            message = logMessages.getString(messageCode);
        } catch (MissingResourceException e) {
            return messageBuf.toString();
        }

        try {
            message = MessageFormat.format(message, arguments);
        } catch (IllegalArgumentException e) {
            return messageBuf.toString();
        }

        messageBuf.append(message);
        return messageBuf.toString();
    }

    public void debug(Object message) {
        doLog(LOGGER_FQCN, Level.DEBUG, message, null);
    }

    public void debug(Object message, Throwable t) {
        doLog(LOGGER_FQCN, Level.DEBUG, message, t);
    }

    /**
     * This is the most generic printing method.
     * 
     * @param callerFQCN
     *            The wrapper class' fully qualified class name.
     * @param level
     *            The level of the logging request.
     * @param message
     *            The message of the logging request.
     * @param t
     *            The Throwable of the logging request, may be null.
     */
    protected void doLog(String callerFQCN, Priority level, Object message,
            Throwable t) {
        if (this.logger.getLoggerRepository().isDisabled(level.toInt())) {
            return;
        }
        if (level.isGreaterOrEqual(this.logger.getEffectiveLevel())) {
            try {
                InetAddress ia = InetAddress.getLocalHost();
                String host = ia.getHostName();
                String ip= ia.getHostAddress();
                MDC.put("host", host); 
                MDC.put("ip", ip); 
            } catch (UnknownHostException e) {
            }
            processMessageBlock(callerFQCN, level, message, t);
        }
    }

    /**
     * the max message size is 0.1M, otherwise the message will be divided into
     * pieces.
     * 
     * @param fqcn
     *            logger name
     * @param level
     *            log level
     * @param message
     *            message content
     * @param t
     *            exceptions
     */
    protected void processMessageBlock(String fqcn, Priority level,
            Object message, Throwable t) {
        String msg = (message != null) ? message.toString() : "";
        int size = msg.length();
        int messageBlockSize = 100 * 1024;
        int count = size / messageBlockSize;
        int i;
        String messageBlock;
        for (i = 0; i < count; i++) {
            messageBlock = msg.substring(i * messageBlockSize, (i + 1)
                    * messageBlockSize);
            this.logger.callAppenders(new LoggingEvent(fqcn, this.logger,
                    level, messageBlock, t));
        }

        if (size % messageBlockSize != 0) {
            messageBlock = msg.substring(i * messageBlockSize);
            this.logger.callAppenders(new LoggingEvent(fqcn, this.logger,
                    level, messageBlock, t));
        }
    }

}
