package zh.co.item.bank.db.entity;

public class TbMediaQuestionBean extends TbMediaQuestionBeanKey {
    private String context;

    private String a;

    private String b;

    private String c;

    private String d;

    private byte[] aImg;

    private byte[] bImg;

    private byte[] cImg;

    private byte[] dImg;

    private String answer;

    private String analysis;

    private Integer structureId;

    private Integer mediaId;

    private String deleteFlag;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
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

    public byte[] getaImg() {
        return aImg;
    }

    public void setaImg(byte[] aImg) {
        this.aImg = aImg;
    }

    public byte[] getbImg() {
        return bImg;
    }

    public void setbImg(byte[] bImg) {
        this.bImg = bImg;
    }

    public byte[] getcImg() {
        return cImg;
    }

    public void setcImg(byte[] cImg) {
        this.cImg = cImg;
    }

    public byte[] getdImg() {
        return dImg;
    }

    public void setdImg(byte[] dImg) {
        this.dImg = dImg;
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

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }
}