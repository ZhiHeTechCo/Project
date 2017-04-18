package zh.co.item.bank.db.entity;

import java.util.Date;

public class TbTopicCommentBean extends TbTopicCommentBeanKey {
    private Integer topicId;

    private Integer responser;

    private String comment;

    private Short count;

    private Date createTime;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getResponser() {
        return responser;
    }

    public void setResponser(Integer responser) {
        this.responser = responser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Short getCount() {
        return count;
    }

    public void setCount(Short count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}