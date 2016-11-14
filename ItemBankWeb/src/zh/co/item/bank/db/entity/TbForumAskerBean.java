package zh.co.item.bank.db.entity;

public class TbForumAskerBean extends TbForumAskerBeanKey {
    private Integer questionid;

    private Integer asker;

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public Integer getAsker() {
        return asker;
    }

    public void setAsker(Integer asker) {
        this.asker = asker;
    }
}