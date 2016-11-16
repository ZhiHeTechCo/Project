package zh.co.item.bank.web.exam.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.web.exam.service.ExamService;

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

    private TuUserBean userInfo;

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
            exams.remove(exams.size() - 1);
            examTypes = examService.getExamTypes();
            jlptLevels = examService.getJlptLevels();
            jlptLevels.remove(jlptLevels.size() - 1);
            jtestLevels = examService.getJtestLevels();
            jtestLevels.remove(jtestLevels.size() - 1);

            classifyBean = new TbQuestionClassifyBean();

            userInfo = (TuUserBean) WebUtils.getSessionAttribute(WebUtils.SESSION_USER_INFO);

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
            if (!checkuser()) {
                // 跳转至登录画面
                return SystemConstants.PAGE_ITBK_USER_002;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userInfo.getId());

            if (classifyBean.getExam().isEmpty() && classifyBean.getExamType().isEmpty()
                    && classifyBean.getJlptLevel().isEmpty() && classifyBean.getJtestLevel().isEmpty()) {
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

            if (!checkuser()) {
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

        } catch (CmnBizException ex) {
            processForException(logger, ex);
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

        // TODO
        return SystemConstants.PAGE_ITBK_EXAM_001;
    }

    /**
     * 跳转至[试题库]画面
     * 
     * @return
     */
    private String toExam() {
        ExamBean examBean = (ExamBean) SpringAppContextManager.getBean("examBean");
        examBean.setUserInfo(userInfo);
        examBean.setClassifyBean(classifyBean);

        return examBean.init();
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

    public CmnLogger getLogger() {
        return logger;
    }

}
