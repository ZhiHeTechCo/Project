package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.List;

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
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamCollectionService;
import zh.co.item.bank.web.exam.service.ExamService;

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

    private List<ExamModel> questions;

    private TbQuestionClassifyBean classifyBean;

    private String title;

    private ExamModel question;

    private Integer questionId;

    private String subject;

    private UserModel userInfo;

    private boolean isResume;

    private String examFlag;

    private List<ExamReportModel> reportModels;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_003;
    }

    /**
     * [结果一览]画面初始化
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
     * [试题详细]返回结果一览画面
     * 
     * @return
     */
    public String goBackToResult() {
        // 前画面为考试结果一览
        if (StringUtils.isNotEmpty(examFlag)) {
            return examReport();
        }
        // 做题结果一览
        return init();
    }

    /**
     * [试题详细]画面初始化
     * 
     * @return
     */
    public String showDetail() {
        try {
            pushPathHistory("examResultBean");
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            if (StringUtils.isEmpty(subject)) {
                subject = "";
            }
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
                question = examService.selectReportDetailByQuestionId(questionId);
            }
            if(question != null) {
            	prepareData();
            }

        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_004;
    }
    
    /**
     * 画面序号,折行
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
     * 询问解析（暂时不用）
     * 
     * @return
     */
    public String askQuestion() {
        try {
            if (questionId != null) {
                userInfo = (UserModel) WebUtils.getLoginUserInfo();
                if (!checkuser()) {
                    return SystemConstants.PAGE_ITBK_EXAM_004;
                }

            }
        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_004;
    }

    /**
     * [考试结果一览]画面初始化
     * 
     * @return
     */
    public String examReport() {
        try {
            // 显示本次考试结果
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            String source = request.getParameter("source");

            reportModels = new ArrayList<ExamReportModel>();
            // 本次考试出现的试题种别
            List<String> examTypes = examCollectionService.getReportTypes(source);
            // 对应种别正确率
            ExamReportModel param = new ExamReportModel();
            param.setSource(source);
            for (String type : examTypes) {
                param.setExamType(type);
                ExamReportModel record = examCollectionService.getPercentage(param);
                reportModels.add(record);
            }

            questions = examCollectionService.getExamReport(source);
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

    private boolean checkuser() {
        if (userInfo == null) {
            logger.log(MessageId.COMMON_E_0009);
            CmnBizException ex = new CmnBizException(MessageId.COMMON_E_0009);
            processForException(logger, ex);
            return false;
        }
        return true;
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
}
