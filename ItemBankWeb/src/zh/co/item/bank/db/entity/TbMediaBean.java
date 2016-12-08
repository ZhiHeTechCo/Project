package zh.co.item.bank.db.entity;

public class TbMediaBean extends TbMediaBeanKey {
    private String language;

    private byte[] media;
    
    private Integer classifyId;

    private String deleteFlag;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public byte[] getMedia() {
        return media;
    }

    public void setMedia(byte[] media) {
        this.media = media;
    }

    public Integer getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Integer classifyId) {
        this.classifyId = classifyId;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }
}