package zh.co.item.bank.db.entity;

import java.util.Date;

public class TbForumResponseBean {
    private Integer questionId;

    private int count;

    private String userChoose;

    private String systemChoose;

    private String response;

    private Integer responser;

    private String status;

    private Date createTime;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserChoose() {
        return userChoose;
    }

    public void setUserChoose(String userChoose) {
        this.userChoose = userChoose;
    }

    public String getSystemChoose() {
        return systemChoose;
    }

    public void setSystemChoose(String systemChoose) {
        this.systemChoose = systemChoose;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getResponser() {
        return responser;
    }

    public void setResponser(Integer responser) {
        this.responser = responser;
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