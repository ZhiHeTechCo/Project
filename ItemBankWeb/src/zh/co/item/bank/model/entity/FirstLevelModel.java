package zh.co.item.bank.model.entity;

import java.util.List;

import zh.co.item.bank.db.entity.TbFirstLevelDirectoryBean;

public class FirstLevelModel extends TbFirstLevelDirectoryBean {

    private List<ExamModel> questions;

    private List<String> subjectList;

    public List<ExamModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamModel> questions) {
        this.questions = questions;
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

}
