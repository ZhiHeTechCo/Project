package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.CmnContants;
import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbErrorReportBean;
import zh.co.item.bank.db.entity.TbForumAskerBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamService;
import zh.co.item.bank.web.forum.service.ForumService;

/**
 * 试题详细画面
 * 
 * @author gaoya
 *
 */
@Named("questionDetail")
@Scope("session")
public class ExamDetailController extends BaseController {
    private final CmnLogger logger = CmnLogger.getLogger(getClass());
    @Inject
    private ExamService examService;

    @Inject
    private ForumService forumService;

    /** 画面初始化变量 */
    // TODO
    private String title;

    private ExamModel question;

    private Integer questionId;

    private String subject;

    /** 大题干List */
    private List<String> subjectList;

    private String graphicImage;

    private UserModel userInfo;

    private String examFlag;

    /** 试题报错 */
    // 所有报错类型
    private List<TsCodeBean> reasons;

    // 用户选择
    private List<String> reasonList;

    // 用户备注
    private String comment;

    /** 画面间传值变量 */
    private String beforePageId;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_004;
    }

    /**
     * 1.画面初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("questionDetail");

            // 获取用户信息
            userInfo = (UserModel) WebUtils.getLoginUserInfo();

            // 试题报错初始化
            if (reasons == null) {
                reasons = examService.getReasons();
            }
            // 未检索到数据，画面不显示报错内容
            if (reasons.size() == 0) {
                reasons = null;
            }
            reasonList = new ArrayList<String>();
            comment = null;
            subject = null;
            graphicImage = null;

            if (StringUtils.isEmpty(subject)) {
                subject = "";
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            questionId = Integer.valueOf(request.getParameter("questionId"));
            examFlag = request.getParameter("examFlag");
            if (questionId == null) {
                logger.log(MessageId.COMMON_E_0001);
                processForException(this.logger, new Exception(MessageId.COMMON_E_0001));
                // 留在当前画面
                return getPageId();
            }
            if (StringUtils.isEmpty(examFlag)) {
                // 做题模式
                question = examService.selectQuestionById(questionId);
            } else {
                // 考试模式
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("questionId", questionId);
                map.put("userId", userInfo.getId());
                question = examService.selectReportDetailByQuestionId(map);
            }
            if (question != null) {
                // 取大题干
                Integer fatherId = question.getFatherId();
                if (fatherId != null) {
                    // 特殊试题检索
                    List<ExamModel> list = examService.selectQuestionByFatherId(fatherId);
                    // 取大题
                    subject = list.get(0).getSubject();
                    graphicImage = CmnStringUtils.getGraphicImage(list.get(0).getImg());

                }
                prepareData(subject);

                // 记叙题时显示答案控制
                if (StringUtils.isNotEmpty(question.getAnswer()) && question.getAnswer().contains(";")) {
                    question.setAnswer(question.getAnswer().replace(";", "  ").trim());
                    question.setMyAnswer(question.getMyAnswer().replace(";", "  ").trim());
                }

            }

            // 权限判断
            if (Integer.parseInt(userInfo.getRole()) >= 90) {
                // 试题管理
                QuestionUpdateController questionUpdateController = (QuestionUpdateController) SpringAppContextManager
                        .getBean("questionUpdateController");
                // question的answer和myAnswer可能已改变 TODO
                questionUpdateController.setQuestion(question);
                if (StringUtils.isNotEmpty(examFlag)) {
                    questionUpdateController.setExamFlag("true");
                } else {
                    questionUpdateController.setExamFlag(null);
                }
                // 试题变更画面
                return questionUpdateController.init();
            }
        } catch (Exception e) {
            processForException(this.logger, e);
        }

        return getPageId();
    }

    /**
     * 2.询问解析（询问解析按钮）
     * 
     * @return
     */
    public String askQuestion() {
        try {
            if (questionId != null) {
                TbForumAskerBean bean = new TbForumAskerBean();
                bean.setAsker(userInfo.getId());
                bean.setQuestionId(questionId);

                // 记录数据
                forumService.insertForumAsker(bean);
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0017), BaseController.MESSAGE_LEVEL_INFO);

            } else {
                setMessage(MessageUtils.getMessage(MessageId.ITBK_E_0008), MESSAGE_LEVEL_ERROR);
            }
        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return getPageId();
    }

    /**
     * 3.试题报错（试题报错按钮）
     * 
     * @return
     */
    public String reportError() {
        try {
            Integer questionId = question.getNo();
            if (questionId != 0) {
                if (!checkuser(userInfo)) {
                    return SystemConstants.PAGE_ITBK_USER_002;
                }
                // 登录报错表
                TbErrorReportBean bean = new TbErrorReportBean();
                StringBuffer buffer = new StringBuffer();
                for (String reason : reasonList) {
                    buffer.append(reason);
                    buffer.append(CmnContants.KOMA);
                }
                if (buffer.length() > 0) {
                    bean.setReason(buffer.substring(0, buffer.length() - 1));
                }
                if (StringUtils.isNotEmpty(comment)) {
                    bean.setComment(comment);
                }
                bean.setUserId(userInfo.getId());
                bean.setQuestionId(questionId);
                examService.insertErrorReport(bean);
                logger.log(MessageId.ITBK_I_0014);
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0014), MESSAGE_LEVEL_INFO);

            } else {
                logger.log(MessageId.ITBK_E_0010);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0010);
                throw ex;
            }
        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return getPageId();
    }

    /**
     * 4.返回结果一览画面（返回前页按钮）
     * 
     * @return
     */
    public String goBackToResult() {
        // 考试模式返回【考试结果一览】，单项练习模式返回【做题结果一览】
        return StringUtils.isNotEmpty(examFlag) ? SystemConstants.PAGE_ITBK_EXAM_006
                : SystemConstants.PAGE_ITBK_EXAM_003;
    }

    /**
     * 5.返回[试题库]/[错题库]画面（继续做题按钮）
     * 
     * @return
     */
    public String goBackToExam() {
        if (SystemConstants.PAGE_ITBK_EXAM_005.equals(beforePageId)) {
            ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
            return resumeBean.init();

        } else if (SystemConstants.PAGE_ITBK_EXAM_002.equals(beforePageId)) {
            ExamController examController = (ExamController) SpringAppContextManager.getBean("examController");
            return examController.init();

        } else if (SystemConstants.PAGE_ITBK_EXAM_016.equals(beforePageId)) {
            OneQuestionController oneQuestionController = (OneQuestionController) SpringAppContextManager
                    .getBean("oneQuestionController");
            return oneQuestionController.init();
        }
        return SystemConstants.PAGE_ITBK_EXAM_000;
    }

    /**
     * [共通]画面序号,折行
     * 
     * @param subject 题干
     */
    private void prepareData(String subject) {
        // 画面序号和显示设置
        if (WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT) != null && SystemConstants.AGENT_FLAG
                .equals((String) WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT))) {
            question.setLayoutStyle("pageDirection");
        } else if (StringUtils.isNotEmpty(question.getA()) && question.getA().length() > CmnContants.FOLDING_LINE) {
            question.setLayoutStyle("pageDirection");
        } else if (StringUtils.isNotEmpty(question.getB()) && question.getB().length() > CmnContants.FOLDING_LINE) {
            question.setLayoutStyle("pageDirection");
        } else if (StringUtils.isNotEmpty(question.getC()) && question.getC().length() > CmnContants.FOLDING_LINE) {
            question.setLayoutStyle("pageDirection");
        } else if (StringUtils.isNotEmpty(question.getD()) && question.getD().length() > CmnContants.FOLDING_LINE) {
            question.setLayoutStyle("pageDirection");
        } else {
            question.setLayoutStyle("lineDirection");
        }

        if ("lineDirection".equals(question.getLayoutStyle())) {
            question.setRadioClass("radioTable1");
        } else {
            question.setRadioClass("radioTable2");
        }
        // 折行
        subjectList = CmnStringUtils.getSubjectList(subject);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExamModel getQuestion() {
        return question;
    }

    public void setQuestion(ExamModel question) {
        this.question = question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getExamFlag() {
        return examFlag;
    }

    public void setExamFlag(String examFlag) {
        this.examFlag = examFlag;
    }

    public List<TsCodeBean> getReasons() {
        return reasons;
    }

    public void setReasons(List<TsCodeBean> reasons) {
        this.reasons = reasons;
    }

    public List<String> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<String> reasonList) {
        this.reasonList = reasonList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBeforePageId() {
        return beforePageId;
    }

    public void setBeforePageId(String beforePageId) {
        this.beforePageId = beforePageId;
    }

}
