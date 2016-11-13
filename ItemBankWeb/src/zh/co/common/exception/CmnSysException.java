package zh.co.common.exception;


/**
 * <p>[概要] System exception is of <code>RuntimeException</code>.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class CmnSysException extends RuntimeException {

    private static final long serialVersionUID = -2880729943392142083L;

    protected Message message;

    public CmnSysException(Message message) {
        this(message, null);
    }

    public CmnSysException(Message message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    public CmnSysException(String messageKey) {
        this(messageKey, null, null);
    }

    public CmnSysException(String messageKey, Object[] args) {
        this(messageKey, args, null);
    }

    public CmnSysException(String messageKey, Object[] args, Throwable cause) {
        super(cause);
        message = new Message(messageKey, args);
        this.msgId = messageKey;
        this.msgArgs = args;
    }

    public CmnSysException(String messageKey, boolean resource) {
        this(messageKey, resource, null);
    }

    public CmnSysException(String messageKey, boolean resource, Throwable cause) {
        super(cause);
        message = new Message(messageKey, resource);
    }
    
    public Message getMessages(){
    	return message;
    }
    
    @Override
    public String getMessage() {
		return getMessages().toString();
    	
    }
    
    /**
     * message id
     */
    private String msgId;

    /**
     * message arguments
     */
    private Object[] msgArgs;

    /**
     * <p>[概要]Constructor</p>
     * <p>[备考]</p>
     */
    public CmnSysException() {
    	this("", false, null);
    }

    /**
     * <p>[概要]Constructor</p>
     * <p>[备考]</p>
     * 
     * @param cause 例外
     */
    public CmnSysException(Throwable cause) {
    	this("", false, cause);
    }


    /**
     * <p>[概要]Constructor</p>
     * <p>[备考]</p>
     * @param amsgId message id
     * @param cause cause
     */
    public CmnSysException(String amsgId, Throwable cause) {
        this(amsgId, null, cause);
    }

    /**
     * <p>[概要]get message id</p>
     * <p>[备考]</p>
     * @return message id
     */
    public String getMsgId() {
        return this.msgId;
    }

    /**
     * <p>[概要]get message arguments</p>
     * <p>[备考]</p>
     * @return message arguments
     */
    public Object[] getMsgArgs() {
        return this.msgArgs;
    }
}
