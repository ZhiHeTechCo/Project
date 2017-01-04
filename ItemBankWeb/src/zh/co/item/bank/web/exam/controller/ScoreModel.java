package zh.co.item.bank.web.exam.controller;

import zh.co.item.bank.db.entity.VbScoreBean;

public class ScoreModel extends VbScoreBean {

    // 得点
    private int myScore;

    // 配点
    private int totalScore;

    // 得点率
    private String percentage;

    // 评价
    private String level;

    public int getMyScore() {
        return myScore;
    }

    public void setMyScore(int myScore) {
        this.myScore = myScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
