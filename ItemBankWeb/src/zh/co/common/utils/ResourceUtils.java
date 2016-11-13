package zh.co.common.utils;

import java.text.MessageFormat;

import zh.co.common.prop.PropertiesManager;
import zh.co.common.prop.PropertiesUtils;

/**
 * <p>[概要] Convenience methods for access message in application resources.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class ResourceUtils {
    /**
     * Get message content with the specified message id in application
     * resources.
     * 
     * @param messageId
     *            message id
     * @return message content in application resources
     */
    public static String getMessage(String messageId) {
        String path = PropertiesUtils.baseDir() + "config/message.properties";
        return PropertiesManager.getInstance().getConfValue(path, messageId);
    }

    /**
     * Get message content with the specified message id and place holder
     * arguments in application resources.
     * 
     * @param messageId
     *            message id
     * @param args
     *            place holder arguments
     * @return message content in application resources with place holders
     *         processed
     */
    public static String getMessage(String messageId, Object[] args) {
        String message = getMessage(messageId);
        return MessageFormat.format(message, args);
    }
}
