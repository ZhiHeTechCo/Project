package zh.co.item.bank.web.exam.controller;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
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

    private List<ExamModel> questions;

    private TbQuestionClassifyBean classifyBean;

    private String title;

    private ExamModel question;

    private Integer questionId;

    private UserModel userInfo;

    private boolean isResume;

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
     * 返回考试/错题画面
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
            questionId = Integer.valueOf(request.getParameter("questionId"));
            if (questionId == null) {
                logger.log(MessageId.COMMON_E_0001);
                processForException(this.logger, new Exception(MessageId.COMMON_E_0001));
                // 留在当前画面
                return SystemConstants.PAGE_ITBK_EXAM_003;
            }
            question = examService.selectQuestionById(questionId);

        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_004;
    }

    /**
     * 询问解析
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

}
