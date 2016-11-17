package zh.co.item.bank.db.entity;

import java.util.Date;

public class TcMessageBean extends TcMessageBeanKey {

    private Integer userId;

    private String message;

    private String status;

    private Date createTime;

    public String getMessage() {
        return message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}