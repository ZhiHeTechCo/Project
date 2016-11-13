package zh.co.common.utils;

import zh.co.common.exception.Message;

/**
 * <p>[概要] 取message内容的utils</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class MessageUtils {
    
    /**
     * 
     * <p>[概 要]根据messageId取得内容</p>
     * <p>[備 考]</p>
     * 
     * @param msgId messageId
     * @return result 内容
     */
    public static String getMessage(String msgId) {
        return getMessage(msgId, null);
    }

    /**
     * 
     * <p>[概 要]根据messageId取得内容 有参数</p>
     * <p>[備 考]</p>
     * 
     * @param msgId messageId
     * @param arg 参数
     * @return result 内容
     */
    public static String getMessage(String msgId, Object[] arg) {
        Message message = new Message(msgId, arg);
        return message.getMessageContent();
    }
}