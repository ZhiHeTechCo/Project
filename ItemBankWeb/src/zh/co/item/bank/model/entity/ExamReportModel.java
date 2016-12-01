package zh.co.item.bank.model.entity;

import zh.co.item.bank.db.entity.TbExamCollectionBean;

public class ExamReportModel extends TbExamCollectionBean {

    private String percentage;

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

}
