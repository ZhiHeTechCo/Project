package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbForumResponseBean;

public class ForumResponseModel extends TbForumResponseBean {

    private String responserName;

    private String status;

    public String getResponserName() {
        return responserName;
    }

    public void setResponserName(String responserName) {
        this.responserName = responserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
