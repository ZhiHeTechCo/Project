package zh.co.item.bank.model.entity;

import java.util.List;

import zh.co.item.bank.db.entity.TbQuestionStructureBean;

public class QuestionStructure extends TbQuestionStructureBean {

    private List<ExamModel> questions;

    private List<FirstLevelModle> firstLevels;

    public List<ExamModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamModel> questions) {
        this.questions = questions;
    }

    public List<FirstLevelModle> getFirstLevels() {
        return firstLevels;
    }

    public void setFirstLevels(List<FirstLevelModle> firstLevels) {
        this.firstLevels = firstLevels;
    }

}
