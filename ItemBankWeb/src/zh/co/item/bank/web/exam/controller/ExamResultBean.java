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
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbErrorReportBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.model.entity.ForumModel;
import zh.co.item.bank.model.entity.MediaModel;
import zh.co.item.bank.model.entity.TbQuestionStructure;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamCollectionService;
import zh.co.item.bank.web.exam.service.ExamService;
import zh.co.item.bank.web.forum.service.ForumService;

/**
 * 结果一览画面
 * 
 * @author gaoya
 *
 */
@Named("examResultBean")
@Scope("session")
public class ExamResultBean extends BaseController {
    private final CmnLogger logger = CmnLogger.getLogger(getClass());
    @Inject
    private ExamService examService;

    @Inject
    private ExamCollectionService examCollectionService;

    @Inject
    private ForumService forumService;

    private List<ExamModel> questions;

    private TbQuestionClassifyBean classifyBean;

    private String title;

    private ExamModel question;

    private Integer questionId;

    private String subject;

    /** 大题干List */
    private List<String> subjectList;

    private String graphicImage;

    private UserModel userInfo;

    private boolean isResume;

    private String examFlag;

    private List<ExamReportModel> reportModels;

    /** 听力 */
    // 听力试题
    private List<TbQuestionStructure> mediaQuestions;
    // 音频
    private MediaModel mediaModel;

    /** 试题报错 */
    // 所有报错类型
    private List<TsCodeBean> reasons;
    // 用户选择
    private List<String> reasonList;
    // 用户备注
    private String comment;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_003;
    }

    /**
     * [做題模式结果一览]画面初始化
     */
    public String init() {
        try {
            pushPathHistory("examResultBean");
        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_003;
    }

    /**
     * [考试结果一览]画面初始化
     * 
     * @return
     */
    public String examReport(String source) {
        try {
            // 显示本次考试结果
            userInfo = (UserModel) WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                return SystemConstants.PAGE_ITBK_EXAM_004;
            }

            reportModels = new ArrayList<ExamReportModel>();
            // 本次考试出现的试题种别
            List<String> examTypes = examCollectionService.getReportTypes(source);
            // 对应种别正确率
            ExamReportModel param = new ExamReportModel();
            param.setSource(source);
            param.setUserId(userInfo.getId());
            for (String type : examTypes) {
                param.setExamType(type);
                ExamReportModel record = examCollectionService.getPercentage(param);
                reportModels.add(record);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("source", source);
            map.put("userId", userInfo.getId());
            questions = examCollectionService.getExamReport(map);

            // 没有查询到当前考题的成绩
            if (questions == null || questions.size() == 0) {
                // 去试题选择
                ExamClassifyBean classifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
                return classifyBean.init();
            }

        } catch (Exception e) {
            processForException(this.logger, e);
        }
        // 去[考试结果一览]画面
        return SystemConstants.PAGE_ITBK_EXAM_006;

    }

    /**
     * [考试结果一览]转至听力画面
     * 
     * @return
     */
    public String toMediaExam() {
        ExamBean examBean = (ExamBean) SpringAppContextManager.getBean("examBean");
        // 设置为听力
        classifyBean.setExamType("6");
        examBean.setClassifyBean(classifyBean);
        return examBean.mediaOfExam(questions.get(0).getSource());
    }

    /**
     * [试题详细]画面初始化
     * 
     * @return
     */
    public String showDetail() {
        try {
            pushPathHistory("examResultBean");
            // 试题报错初始化
            if (reasons == null) {
                reasons = examService.getReasons();
            }
            reasonList = new ArrayList<String>();
            comment = null;

            // 获取用户信息
            userInfo = (UserModel) WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                return SystemConstants.PAGE_ITBK_EXAM_004;
            }
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
                return SystemConstants.PAGE_ITBK_EXAM_003;
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
                prepareData();
            }

        } catch (Exception e) {
            processForException(this.logger, e);
        }

        // 权限判断
        if (Integer.parseInt(userInfo.getRole()) >= 90) {
            // 试题管理
            QuestionUpdateController questionUpdateController = (QuestionUpdateController) SpringAppContextManager
                    .getBean("questionUpdateController");
            questionUpdateController.setQuestion(question);
            if (StringUtils.isNotEmpty(examFlag)) {
                questionUpdateController.setExamFlag("true");
            }
            return questionUpdateController.init();
        }
        return SystemConstants.PAGE_ITBK_EXAM_004;
    }

    /**
     * [试题详细]返回结果一览画面
     * 
     * @return
     */
    public String goBackToResult() {
        // 前画面为考试结果一览
        if (StringUtils.isNotEmpty(examFlag)) {
            return examReport(question.getSource());
        }
        // 做题结果一览
        return init();
    }

    /**
     * [试题详细]询问解析
     * 
     * @return
     */
    public String askQuestion() {
        try {
            if (questionId != null) {
                userInfo = (UserModel) WebUtils.getLoginUserInfo();
                if (!checkuser(userInfo)) {
                    return SystemConstants.PAGE_ITBK_EXAM_004;
                }
                ForumModel model = new ForumModel();
                model.setAsker(userInfo.getId());
                model.setQuestionId(questionId);

                if (forumService.selectForumAskerForOne(model) == 1) {
                    // 当前用户已询问过此道问题
                    setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0016), MESSAGE_LEVEL_INFO);

                } else if (forumService.checkQuestionIsExist(questionId) == 1) {
                    // 其他用户已询问过此道问题
                    forumService.insertForumAsker(model, true);
                    setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0017), MESSAGE_LEVEL_INFO);

                } else {
                    // 首次有用户询问此道问题
                    forumService.insertForumAsker(model, false);
                    setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0017), MESSAGE_LEVEL_INFO);
                }
            } else {
                setMessage(MessageUtils.getMessage(MessageId.ITBK_E_0008), MESSAGE_LEVEL_ERROR);
            }
        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_004;
    }

    /**
     * [试题详细]试题报错
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
        return SystemConstants.PAGE_ITBK_EXAM_004;
    }

    /**
     * 返回[试题库]/[错题库]画面
     * 
     * @return
     */
    public String goBackToExam() {
        if (isResume) {
            ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
            return resumeBean.init();

        } else {
            ExamBean examBean = (ExamBean) SpringAppContextManager.getBean("examBean");
            examBean.setClassifyBean(classifyBean);
            examBean.setQuestions(null);
            return examBean.init();
        }
    }

    /**
     * 返回试题选择
     * 
     * @return
     */
    public String goBackToClassify() {
        ExamClassifyBean examClassifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
        return examClassifyBean.init();
    }

    /**
     * 共通：画面序号,折行
     * 
     * @param subject 题干
     */
    private void prepareData() {

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
    }

    /**
     * [听力]结果一览（init）
     * 
     * @return
     */
    public String mediaReport() {
        return SystemConstants.PAGE_ITBK_EXAM_008;
    }

    /**
     * [听力][考试模式]返回考试结果一览
     * 
     * @return
     */
    public String goBackToExamResult() {
        return examReport(mediaModel.getSource());
    }

    public ExamService getExamService() {
        return this.examService;
    }

    public void setExamService(ExamService examService) {
        this.examService = examService;
    }

    public List<ExamModel> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<ExamModel> questions) {
        this.questions = questions;
    }

    public CmnLogger getLogger() {
        return this.logger;
    }

    public String getTitle() {
        return this.title;
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

    public TbQuestionClassifyBean getClassifyBean() {
        return this.classifyBean;
    }

    public void setClassifyBean(TbQuestionClassifyBean classifyBean) {
        this.classifyBean = classifyBean;
    }

    public ExamModel getQuestion() {
        return question;
    }

    public void setQuestion(ExamModel question) {
        this.question = question;
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean isResume) {
        this.isResume = isResume;
    }

    public String getExamFlag() {
        return examFlag;
    }

    public void setExamFlag(String examFlag) {
        this.examFlag = examFlag;
    }

    public List<ExamReportModel> getReportModels() {
        return reportModels;
    }

    public void setReportModels(List<ExamReportModel> reportModels) {
        this.reportModels = reportModels;
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

    public List<TbQuestionStructure> getMediaQuestions() {
        return mediaQuestions;
    }

    public void setMediaQuestions(List<TbQuestionStructure> mediaQuestions) {
        this.mediaQuestions = mediaQuestions;
    }

    public MediaModel getMediaModel() {
        return mediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        this.mediaModel = mediaModel;
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

}
