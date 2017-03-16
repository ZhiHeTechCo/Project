package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbForumResponseBean;

public class ForumResponseModel extends TbForumResponseBean {

    private String responseName;

    public String getResponseName() {
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

}
