package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbNoteBean;

public class NoteModel extends TbNoteBean {

    private String nickName;

    private String headimgurl;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

}
