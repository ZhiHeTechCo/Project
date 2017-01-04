package zh.co.item.bank.db.entity;

public class TsScoreBean extends TsScoreBeanKey {
    private String classifyId;

    private String structureId;

    private Short score;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId == null ? null : classifyId.trim();
    }

    public String getStructureId() {
        return structureId;
    }

    public void setStructureId(String structureId) {
        this.structureId = structureId == null ? null : structureId.trim();
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }
}