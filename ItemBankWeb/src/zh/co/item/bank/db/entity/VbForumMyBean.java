package zh.co.item.bank.db.entity;

public class VbForumMyBean {
    private Integer asker;

    private Integer questionid;

    private String context;

    private String contextkey;

    private String contextafter;

    private String status;

    private String statusname;

    public Integer getAsker() {
        return asker;
    }

    public void setAsker(Integer asker) {
        this.asker = asker;
    }

    public Integer getQuestionid() {
        return questionid;
    }

    public void setQuestionid(Integer questionid) {
        this.questionid = questionid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getContextkey() {
        return contextkey;
    }

    public void setContextkey(String contextkey) {
        this.contextkey = contextkey == null ? null : contextkey.trim();
    }

    public String getContextafter() {
        return contextafter;
    }

    public void setContextafter(String contextafter) {
        this.contextafter = contextafter == null ? null : contextafter.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname == null ? null : statusname.trim();
    }
}