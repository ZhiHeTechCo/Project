package zh.co.common.log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.prop.PropertiesUtils;

/**
 * <p>[概要] This class manages the log level of all log messages</p>
 * <p>[详细] 没有</p>
 * <p>[备考] 没有</p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public final class CmnLogLevelManager {

    private static CmnLogger logger = CmnLogger
            .getLogger(CmnLogLevelManager.class);

    /**
     * log levels of all log messages
     */
    private static Properties messageLogLevels = new Properties();

    static {
        CmnLogLevelManager.init();
    }

    /**
     * constructor
     */
    private CmnLogLevelManager() {
    }

    /**
     * Get the log level according to the message code.
     * 
     * @param messageKey
     *            message code
     * @return log level of this message code
     */
    public static int getLogLevel(String messageKey) {
        if (messageLogLevels.isEmpty()) {
            init();
        }

        String level = messageLogLevels.getProperty(messageKey);
        if (level == null) {
            logger.log(MessageId.COMMON_E_0003,
                    new Object[] { messageKey });
            return 5;
        }

        try {
            return Integer.parseInt(level);
        } catch (Exception e) {
            logger.log(MessageId.COMMON_E_0003,
                    new Object[] { messageKey }, e);
            return 5;
        }
    }

    /**
     * Initialization for loading log level config file.
     */
    private static void init() {
        InputStream is = null;

        String pathname = PropertiesUtils.baseDir() + "config/LogLevelProperties.conf";
        try {
            is = new FileInputStream(pathname);
            messageLogLevels.load(is);
        } catch (Exception e) {
            logger.log(MessageId.COMMON_E_0002,
                    new Object[] { pathname }, e);
            throw new CmnSysException(MessageId.COMMON_E_0002,
                    new Object[] { pathname }, e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                logger.debug("Failed to close the stream : " + e);
            }
        }
    }
}
