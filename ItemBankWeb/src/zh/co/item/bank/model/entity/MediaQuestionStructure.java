package zh.co.item.bank.model.entity;

import java.util.List;

import zh.co.item.bank.db.entity.TbQuestionStructureBean;

public class MediaQuestionStructure extends TbQuestionStructureBean {

    private List<MediaModel> question;

    public List<MediaModel> getQuestion() {
        return question;
    }

    public void setQuestion(List<MediaModel> question) {
        this.question = question;
    }

}
