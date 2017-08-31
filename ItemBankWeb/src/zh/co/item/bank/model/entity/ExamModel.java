package zh.co.item.bank.model.entity;

import java.util.Date;
import java.util.List;

import zh.co.common.utils.CmnStringUtils;

public class ExamModel extends QuestionBean {

    // 用户ID
    private int userId;

    // 最后更新时间
    private Date updateTime;

    // 用户选择答案
    private String myAnswer;

    // 用户选择答案（解析题用）
    private String myAnswer1;

    // 用户选择答案（解析题用）
    private String myAnswer2;

    // 用户答案（解析题用）
    private String answer1;

    // 用户答案（解析题用）
    private String answer2;

    // 题干
    private String title;

    // 一级目录中的大题目
    private String subject;

    // 一级目录中的图片
    private byte[] img;

    // 考试类别[详细画面]
    private String examName;

    // 考题种别[详细画面]
    private String examTypeName;

    // 考试级别[详细画面]
    private String levelName;

    // 序号[试题库，错题库]
    private int rowNo;

    // 问题添加人
    private int asker;

    // 问题回答人
    private int responser;

    // 问题描述
    private String description;

    /** radio显示格式 */
    private String layoutStyle;

    /** radio样式 */
    private String radioClass;

    // 题干
    private List<String> contextList;

    // 笔记
    private List<NoteModel> noteInfo;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }

    public String getMyAnswer1() {
        return myAnswer1;
    }

    public void setMyAnswer1(String myAnswer1) {
        this.myAnswer1 = myAnswer1;
    }

    public String getMyAnswer2() {
        return myAnswer2;
    }

    public void setMyAnswer2(String myAnswer2) {
        this.myAnswer2 = myAnswer2;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamTypeName() {
        return examTypeName;
    }

    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAsker() {
        return asker;
    }

    public void setAsker(int asker) {
        this.asker = asker;
    }

    public int getResponser() {
        return responser;
    }

    public void setResponser(int responser) {
        this.responser = responser;
    }

    public String getLayoutStyle() {
        return layoutStyle;
    }

    public void setLayoutStyle(String layoutStyle) {
        this.layoutStyle = layoutStyle;
    }

    public String getRadioClass() {
        return radioClass;
    }

    public void setRadioClass(String radioClass) {
        this.radioClass = radioClass;
    }

    public int getContextListSize() {
        return getContextList().size();
    }

    public List<String> getContextList() {
        return CmnStringUtils.getContextList(this.getContext());
    }

    public void setContextList(List<String> contextList) {
        this.contextList = contextList;
    }

    public List<NoteModel> getNoteInfo() {
        return noteInfo;
    }

    public void setNoteInfo(List<NoteModel> noteInfo) {
        this.noteInfo = noteInfo;
    }

}
