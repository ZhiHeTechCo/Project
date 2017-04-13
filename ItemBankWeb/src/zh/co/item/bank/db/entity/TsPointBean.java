package zh.co.item.bank.db.entity;

public class TsPointBean extends TsPointBeanKey {
    private Short point;

    private String description;

    private String deleteFlag;

    public Short getPoint() {
        return point;
    }

    public void setPoint(Short point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }
}