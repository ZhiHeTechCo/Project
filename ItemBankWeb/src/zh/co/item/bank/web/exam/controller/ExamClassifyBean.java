package zh.co.item.bank.web.exam.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamService;
import zh.co.item.bank.web.user.controller.SignInBean;

/**
 * 试题类型选择画面
 * 
 * @author gaoya
 *
 */
@Named("examClassifyBean")
@Scope("session")
public class ExamClassifyBean extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamService examService;

    // 所有考试类别
    private List<TsCodeBean> exams;

    // 所有考题种别
    private List<TsCodeBean> examTypes;

    // 所有JLPT考试级别
    private List<TsCodeBean> jlptLevels;

    // 所有J.TEST考试级别
    private List<TsCodeBean> jtestLevels;

    private TbQuestionClassifyBean classifyBean;

    private UserModel userInfo;

    private String showExamFlag;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_001;
    }

    /**
     * initial
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("examClassifyBean");
            exams = examService.getExams();
            examTypes = examService.getExamTypes();
            jlptLevels = examService.getJlptLevels();
            jtestLevels = examService.getJtestLevels();

            classifyBean = new TbQuestionClassifyBean();

            userInfo = WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                // 跳转至登录画面
            	SignInBean signInBean = (SignInBean) SpringAppContextManager.getBean("signInBean");
                return signInBean.init();
            }
            // 语言信息为空，则智能推题不显示。
            showExamFlag = StringUtils.isEmpty(userInfo.getJlptLevel()) && StringUtils.isEmpty(userInfo.getJtestLevel())
                    ? "" : "true";

        } catch (Exception e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_EXAM_001;
    }

    /**
     * 考题种别变更，刷新考试级别
     */
    public void changExamType() {
        logger.debug("题种别变更，刷新考试级别");
    }

    /**
     * Classify选题
     * 
     * @return
     */
    public String classifySearch() {

        try {
            if (!checkuser(userInfo)) {
                // 跳转至登录画面
                return SystemConstants.PAGE_ITBK_USER_002;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userInfo.getId());
            if (StringUtils.isEmpty(classifyBean.getExam()) && StringUtils.isEmpty(classifyBean.getExamType())
                    && StringUtils.isEmpty(classifyBean.getJlptLevel())
                    && StringUtils.isEmpty(classifyBean.getJtestLevel())) {
                logger.log(MessageId.ITBK_E_0005);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0005);
                throw ex;
            }

            return toExam();

        } catch (CmnBizException ex) {
            processForException(logger, ex);
        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_001;
    }

    /**
     * 智能推题
     * 
     * @return
     */
    public String smartSearch() {
        try {

            if (!checkuser(userInfo)) {
                // 跳转至登录画面
                return SystemConstants.PAGE_ITBK_USER_002;
            }

            // 用户信息中不包含日语等级
            if (StringUtils.isEmpty(userInfo.getJlptLevel()) && StringUtils.isEmpty(userInfo.getJtestLevel())) {
                logger.log(MessageId.ITBK_I_0009);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0009);
                throw ex;
            }

            classifyBean = null;
            return toExam();

        } catch (Exception e) {
            processForException(logger, e);
        }
        // 留在当前画面
        return SystemConstants.PAGE_ITBK_EXAM_001;
    }

    /**
     * 考试模式
     * 
     * @return
     */
    public String examSearch() {
        try {
            if (StringUtils.isEmpty(classifyBean.getExam()) || (StringUtils.isEmpty(classifyBean.getJlptLevel())
                    && StringUtils.isEmpty(classifyBean.getJtestLevel()))) {
                logger.log(MessageId.ITBK_E_0006);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0006);
                throw ex;
            }
        } catch (Exception e) {
            processForException(logger, e);
            return SystemConstants.PAGE_ITBK_EXAM_001;
        }
        ExamBean examBean = (ExamBean) SpringAppContextManager.getBean("examBean");
        examBean.setClassifyBean(classifyBean);
        examBean.setYear(null);
        examBean.setSafeList(new CopyOnWriteArrayList<ExamModel>());
        return examBean.examSearch();
    }

    /**
     * 跳转至[试题库]画面
     * 
     * @return
     */
    private String toExam() {
        ExamBean examBean = (ExamBean) SpringAppContextManager.getBean("examBean");
        examBean.setClassifyBean(classifyBean);

        return examBean.init();
    }

    public List<TsCodeBean> getExams() {
        return exams;
    }

    public void setExams(List<TsCodeBean> exams) {
        this.exams = exams;
    }

    public List<TsCodeBean> getExamTypes() {
        return examTypes;
    }

    public void setExamTypes(List<TsCodeBean> examTypes) {
        this.examTypes = examTypes;
    }

    public List<TsCodeBean> getJlptLevels() {
        return jlptLevels;
    }

    public void setJlptLevels(List<TsCodeBean> jlptLevels) {
        this.jlptLevels = jlptLevels;
    }

    public List<TsCodeBean> getJtestLevels() {
        return jtestLevels;
    }

    public void setJtestLevels(List<TsCodeBean> jtestLevels) {
        this.jtestLevels = jtestLevels;
    }

    public TbQuestionClassifyBean getClassifyBean() {
        return classifyBean;
    }

    public void setClassifyBean(TbQuestionClassifyBean classifyBean) {
        this.classifyBean = classifyBean;
    }

    public String getShowExamFlag() {
        return showExamFlag;
    }

    public void setShowExamFlag(String showExamFlag) {
        this.showExamFlag = showExamFlag;
    }

}
