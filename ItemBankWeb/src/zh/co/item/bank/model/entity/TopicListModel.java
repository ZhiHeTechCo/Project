package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbTopicListBean;

public class TopicListModel extends TbTopicListBean {

    private String ownerName;

    private String createTimeShow;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCreateTimeShow() {
        return createTimeShow;
    }

    public void setCreateTimeShow(String createTimeShow) {
        this.createTimeShow = createTimeShow;
    }

}
