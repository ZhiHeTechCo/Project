package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbExamListBean;

public class ExamListModel extends TbExamListBean {

    /** 用户试题完成度 */
    private String rate;

    /** 听力试题情况 */
    private String mediaStatus;

    /** 备注 */
    private String comment;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMediaStatus() {
        return mediaStatus;
    }

    public void setMediaStatus(String mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
