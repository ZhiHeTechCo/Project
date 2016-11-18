package zh.co.item.bank.db.entity;

public class TbForumAskerBean extends TbForumAskerBeanKey {
    private Integer questionId;

    private Integer asker;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getAsker() {
        return asker;
    }

    public void setAsker(Integer asker) {
        this.asker = asker;
    }
}