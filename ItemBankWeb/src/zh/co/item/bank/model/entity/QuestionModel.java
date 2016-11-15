package zh.co.item.bank.model.entity;

public class QuestionModel extends QuestionBean {

    private String examTypeShow;

    // 问题添加人
    private int asker;

    // 问题回答人
    private int responser;

    // 问题描述
    private String description;

    public String getExamTypeShow() {
        return examTypeShow;
    }

    public void setExamTypeShow(String examTypeShow) {
        this.examTypeShow = examTypeShow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAsker() {
        return asker;
    }

    public void setAsker(int asker) {
        this.asker = asker;
    }

    public int getResponser() {
        return responser;
    }

    public void setResponser(int responser) {
        this.responser = responser;
    }

}
