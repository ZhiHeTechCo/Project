package zh.co.item.bank.model.entity;

import java.util.List;

import zh.co.common.utils.CmnStringUtils;
import zh.co.item.bank.db.entity.TbMediaQuestionBean;

public class MediaModel extends TbMediaQuestionBean {

    // 用户ID
    private int userId;

    // 听力音频ID[检索用（登录不用）]
    private Integer id;

    // 图片Base64
    private String contextImgCode;
    
    // 听力音频路径
    private String mediaPath;

    // 用户选择答案
    private String myAnswer;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getContextImgCode() {
		return CmnStringUtils.getGraphicImage(this.getContextImg());
	}

	public void setContextImgCode(String contextImgCode) {
		this.contextImgCode = contextImgCode;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
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

    public List<String> getContextList() {
        return CmnStringUtils.getSubjectList(this.getContext());
    }

}
