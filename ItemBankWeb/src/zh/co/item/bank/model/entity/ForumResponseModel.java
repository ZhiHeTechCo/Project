package zh.co.item.bank.model.entity;

import java.util.ArrayList;
import java.util.List;

import zh.co.item.bank.db.entity.TbForumResponseBean;

public class ForumResponseModel extends TbForumResponseBean {

    private String responserName;

    private String status;

    private List<String> responseList;

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

    public List<String> getResponseList() {
        return responseList == null ? new ArrayList<String>() : responseList;
    }

    public void setResponseList(List<String> responseList) {
        this.responseList = responseList;
    }

}
