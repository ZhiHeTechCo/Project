package zh.co.item.bank.model.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zh.co.item.bank.db.entity.VbForumAskerBean;

public class ForumModel extends VbForumAskerBean {

    private Map<Integer, String> userName = new HashMap<Integer, String>();

    /** 试题详细 */
    private ExamModel question;

    private List<String> subjectList;

    private String graphicImage;

    /** 问题回答 */
    List<ForumResponseModel> responses;

    /** 提问者昵称 */
    private String askers;

    /** 行号 */
    private int rowNo;

    public ExamModel getQuestion() {
        return question;
    }

    public void setQuestion(ExamModel question) {
        this.question = question;
    }

    public List<ForumResponseModel> getResponses() {
        return responses;
    }

    public void setResponses(List<ForumResponseModel> responses) {
        this.responses = responses;
    }

    public String getAskers() {
        return askers;
    }

    public void setAskers(String askers) {
        this.askers = askers;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public Map<Integer, String> getUserName() {
        return userName;
    }

    public void setUserName(Map<Integer, String> userName) {
        this.userName = userName;
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public String getGraphicImage() {
        return graphicImage;
    }

    public void setGraphicImage(String graphicImage) {
        this.graphicImage = graphicImage;
    }

}
