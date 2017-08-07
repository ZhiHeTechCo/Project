package zh.co.item.bank.model.entity;

import java.util.List;

import zh.co.item.bank.db.entity.TbTopicCommentBean;

public class TopicCommentModel extends TbTopicCommentBean {

    private String responserName;

    private String createTimeShow;

    /** 折行显示回答 */
    private List<String> commentList;

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

    public List<String> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<String> commentList) {
        this.commentList = commentList;
    }

}
