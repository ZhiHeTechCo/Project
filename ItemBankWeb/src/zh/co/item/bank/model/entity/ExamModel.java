package zh.co.item.bank.model.entity;

import java.util.Date;

public class ExamModel extends QuestionBean {

    // 用户ID
    private int userId;

    // 最后更新时间
    private Date updateTime;

    // 用户选择答案
    private String myAnswer;

    // 一级目录中的大题目
    private String subject;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
