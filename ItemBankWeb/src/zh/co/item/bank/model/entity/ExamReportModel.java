package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbExamCollectionBean;

public class ExamReportModel extends TbExamCollectionBean {

    // 正确率
    private String percentage;

    // 分数
    private String myScore;

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getMyScore() {
        return myScore;
    }

    public void setMyScore(String myScore) {
        this.myScore = myScore;
    }

}
