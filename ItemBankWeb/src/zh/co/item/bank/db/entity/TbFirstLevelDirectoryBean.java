package zh.co.item.bank.db.entity;

public class TbFirstLevelDirectoryBean extends TbFirstLevelDirectoryBeanKey {
    private String subject;

    private String deleteFlag;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }
}