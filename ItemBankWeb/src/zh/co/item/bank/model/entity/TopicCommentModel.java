package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbTopicCommentBean;

public class TopicCommentModel extends TbTopicCommentBean {

    private String responserName;

    private String createTimeShow;

    public String getResponserName() {
        return responserName;
    }

    public void setResponserName(String responserName) {
        this.responserName = responserName;
    }

    public String getCreateTimeShow() {
        return createTimeShow;
    }

    public void setCreateTimeShow(String createTimeShow) {
        this.createTimeShow = createTimeShow;
    }

}
