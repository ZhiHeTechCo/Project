package zh.co.item.bank.db.entity;

public class TbQuestionStructureBean extends TbQuestionStructureBeanKey {
    private String title;

    private Integer classifyId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(Integer classifyId) {
        this.classifyId = classifyId;
    }
}