package zh.co.common.exception;

import java.io.Serializable;

import zh.co.common.utils.ResourceUtils;

/**
 * <p>[概要] Base class for messages.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class Message implements Serializable {

    private static final long serialVersionUID = -148701967801792356L;

    private long messageId;
	
	private long timestamp;
	
	private String stepName;
	
	private String message;
	
    /**
     * message key.
     */
    protected String key;

    /**
     * Indicates whether the key is taken to be as a bundle key or literal
     * value[false].
     */
    protected boolean resource = true;

    /**
     * replacement arguments.
     */
    protected Object[] args;
    
    public String getKey(){
        return this.key;
    }
    
    public Object[] getArgs(){
        return this.args;
    }
    
    public Message(String key) {
        this(key, null);
    }

    public Message(String key, Object[] args) {
        this.key = key;
        this.args = args;
    }

    public Message(String key, boolean resource) {
        this.key = key;
        this.resource = resource;
    }

    public String getMessageContent() {
        if (resource) {
            return ResourceUtils.getMessage(key, args);
        } else {
            return key;
        }
    }

    public String toString() {
        return getMessageContent();
    }
    
    public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
