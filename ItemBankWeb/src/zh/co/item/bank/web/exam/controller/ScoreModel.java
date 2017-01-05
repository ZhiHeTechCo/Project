package zh.co.item.bank.web.exam.controller;

import zh.co.item.bank.db.entity.VbScoreBean;

public class ScoreModel extends VbScoreBean {

    // 得点
    private int myTotalScore;

    // 配点
    private int totalScore;

    // 得点率
    private String percentage;

    // 评价
    private String level;

    public int getMyTotalScore() {
        return myTotalScore;
    }

    public void setMyTotalScore(int myTotalScore) {
        this.myTotalScore = myTotalScore;
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
