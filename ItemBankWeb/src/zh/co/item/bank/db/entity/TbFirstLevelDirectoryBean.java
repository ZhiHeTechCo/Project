package zh.co.item.bank.db.entity;

public class TbFirstLevelDirectoryBean extends TbFirstLevelDirectoryBeanKey {
    private String subject;

    private byte[] img;

    private String deleteFlag;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }
}