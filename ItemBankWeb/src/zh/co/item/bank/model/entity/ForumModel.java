package zh.co.item.bank.model.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zh.co.item.bank.db.entity.TbForumResponseBean;

public class ForumModel {

    private Map<Integer, String> userName = new HashMap<Integer, String>();

    /** 试题详细 */
    ExamModel question;

    List<TbForumResponseBean> responses;

    /** 提问者昵称 */
    private String askers;

    /** 提问者 */
    private int asker;

    /** 问题状态 */
    private String statusName;

    /** 行号 */
    private int rowNo;

    private String responserName;

    public ExamModel getQuestion() {
        return question;
    }

    public void setQuestion(ExamModel question) {
        this.question = question;
    }

    public List<TbForumResponseBean> getResponses() {
        return responses;
    }

    public void setResponses(List<TbForumResponseBean> responses) {
        this.responses = responses;
    }

    public String getAskers() {
        return askers;
    }

    public void setAskers(String askers) {
        this.askers = askers;
    }

    public int getAsker() {
        return asker;
    }

    public void setAsker(int asker) {
        this.asker = asker;
    }

    public String getStatusName() {
        return statusName;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Map<Integer, String> getUserName() {
        return userName;
    }

    public void setUserName(Map<Integer, String> userName) {
        this.userName = userName;
    }

    public String getResponserName() {
        return responserName;
    }

    public void setResponserName(String responserName) {
        this.responserName = responserName;
    }

}
