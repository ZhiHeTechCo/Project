package zh.co.item.bank.db.entity;

public class TbExamDropoutBean {

    private Integer userId;

    private String year;

    private String exam;

    private String jlptLevel;

    private String jtestLevel;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getJlptLevel() {
        return jlptLevel;
    }

    public void setJlptLevel(String jlptLevel) {
        this.jlptLevel = jlptLevel;
    }

    public String getJtestLevel() {
        return jtestLevel;
    }

    public void setJtestLevel(String jtestLevel) {
        this.jtestLevel = jtestLevel;
    }

}