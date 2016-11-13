package zh.co.item.bank.db.entity;

public class VbQuestionDetailBean {
    private Integer no;

    private String context;

    private String contextkey;

    private String contextafter;

    private String a;

    private String b;

    private String c;

    private String d;

    private String answer;

    private String analysis;

    private String source;

    private Integer classifyid;

    private Integer structureid;

    private String deleteflag;

    private String exam;

    private String jlptlevel;

    private String jtestlevel;

    private String examtype;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public Integer getClassifyid() {
        return classifyid;
    }

    public void setClassifyid(Integer classifyid) {
        this.classifyid = classifyid;
    }

    public Integer getStructureid() {
        return structureid;
    }

    public void setStructureid(Integer structureid) {
        this.structureid = structureid;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag == null ? null : deleteflag.trim();
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam == null ? null : exam.trim();
    }

    public String getJlptlevel() {
        return jlptlevel;
    }

    public void setJlptlevel(String jlptlevel) {
        this.jlptlevel = jlptlevel == null ? null : jlptlevel.trim();
    }

    public String getJtestlevel() {
        return jtestlevel;
    }

    public void setJtestlevel(String jtestlevel) {
        this.jtestlevel = jtestlevel == null ? null : jtestlevel.trim();
    }

    public String getExamtype() {
        return examtype;
    }

    public void setExamtype(String examtype) {
        this.examtype = examtype == null ? null : examtype.trim();
    }
}