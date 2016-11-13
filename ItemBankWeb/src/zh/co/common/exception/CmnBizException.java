package zh.co.common.exception;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>[概要] Business exceptions implement so-called composite design pattern.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class CmnBizException extends Exception {

    private static final long serialVersionUID = -6647986260550183888L;

    /**
     * message that may be from resource bundles.
     */
    protected Message message;
    
    protected CmnBizException() {
        super();
    }
    
    public CmnBizException(Message message) {
        this(message, null);
    }
    /**
     * 
     * 构造函数
     * @param cause 异常原因
     */
    public CmnBizException(Throwable cause) {
        this(null, cause);
    }
    
    public CmnBizException(Message message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public CmnBizException(String messageKey) {
        this(messageKey, null);
    }

    public CmnBizException(String messageKey, Object[] args) {
        this(messageKey, args, null);
    }

    public CmnBizException(String messageKey, Object[] args,
            Throwable cause) {
        super(cause);
        message = new Message(messageKey, args);
    }

    public CmnBizException(String messageKey, boolean resource) {
        this(messageKey, resource, null);
    }

    public CmnBizException(String messageKey, boolean resource,
            Throwable cause) {
        super(cause);
        message = new Message(messageKey, resource);
    }

    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<Message>();
        messages.add(message);
        return messages;
    }
    
    @Override
    public String getMessage() {
        if(message != null){
            return message.getMessageContent();
        }
        return super.getMessage();
    }
    
    public String getMessageId() {
        return this.message.getKey();
    }
}
