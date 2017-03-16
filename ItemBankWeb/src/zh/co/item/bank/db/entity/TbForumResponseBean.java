package zh.co.item.bank.db.entity;

import java.util.Date;

public class TbForumResponseBean extends TbForumResponseBeanKey {
    private Integer questionId;

    private String userChoose;

    private String systemChoose;

    private Integer responser;

    private String response;

    private Short count;

    private String status;

    private Date createTime;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getUserChoose() {
        return userChoose;
    }

    public void setUserChoose(String userChoose) {
        this.userChoose = userChoose == null ? null : userChoose.trim();
    }

    public String getSystemChoose() {
        return systemChoose;
    }

    public void setSystemChoose(String systemChoose) {
        this.systemChoose = systemChoose == null ? null : systemChoose.trim();
    }

    public Integer getResponser() {
        return responser;
    }

    public void setResponser(Integer responser) {
        this.responser = responser;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response == null ? null : response.trim();
    }

    public Short getCount() {
        return count;
    }

    public void setCount(Short count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}