package zh.co.item.bank.model.entity;

import java.util.List;

import zh.co.item.bank.db.entity.TbQuestionStructureBean;

public class QuestionStructure extends TbQuestionStructureBean {

    private List<ExamModel> questions;

    private List<FirstLevelModel> firstLevels;
    
    private String examType;

    public List<ExamModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamModel> questions) {
        this.questions = questions;
    }

    public List<FirstLevelModel> getFirstLevels() {
        return firstLevels;
    }

    public void setFirstLevels(List<FirstLevelModel> firstLevels) {
        this.firstLevels = firstLevels;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

}
