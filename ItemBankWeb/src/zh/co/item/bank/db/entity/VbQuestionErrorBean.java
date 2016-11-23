package zh.co.item.bank.db.entity;

import java.util.Date;

public class VbQuestionErrorBean {
    private Integer no;

    private Integer userId;

    private String context;

    private String contextKey;

    private String contextAfter;

    private String a;

    private String b;

    private String c;

    private String d;

    private String answer;

    private String analysis;

    private Integer structureId;

    private Integer fatherId;

    private String finish;

    private Date updateTime;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getContextKey() {
        return contextKey;
    }

    public void setContextKey(String contextKey) {
        this.contextKey = contextKey == null ? null : contextKey.trim();
    }

    public String getContextAfter() {
        return contextAfter;
    }

    public void setContextAfter(String contextAfter) {
        this.contextAfter = contextAfter == null ? null : contextAfter.trim();
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a == null ? null : a.trim();
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b == null ? null : b.trim();
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c == null ? null : c.trim();
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d == null ? null : d.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis == null ? null : analysis.trim();
    }

    public Integer getStructureId() {
        return structureId;
    }

    public void setStructureId(Integer structureId) {
        this.structureId = structureId;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish == null ? null : finish.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}