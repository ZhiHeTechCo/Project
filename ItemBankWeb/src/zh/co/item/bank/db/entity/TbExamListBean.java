package zh.co.item.bank.db.entity;

public class TbExamListBean extends TbExamListBeanKey {
    private String language;

    private String exam;

    private String level;

    private String year;

    private String count;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam == null ? null : exam.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count == null ? null : count.trim();
    }
}