package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
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
import zh.co.item.bank.db.entity.TbExamListBean;
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
@Named("examClassifyController")
@Scope("session")
public class ExamClassifyController extends BaseController {

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

    // 考卷（年）
    private List<String> years;

    // 考卷（月）
    private List<String> counts;

    private String chooseYear;

    private String chooseCount;

    private TbQuestionClassifyBean classifyBean;

    private UserModel userInfo;

    private String showExamFlag;

    private String mode;

    List<TbExamListBean> examListBeans;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_001;
    }

    /**
     * 1.初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("examClassifyController");

            // a.初始化
            userInfo = WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                // 跳转至登录画面
                SignInBean signInBean = (SignInBean) SpringAppContextManager.getBean("signInBean");
                return signInBean.init();
            }
            years = new ArrayList<String>();
            counts = new ArrayList<String>();

            // 跳转至
            if (StringUtils.isEmpty(mode)) {
                return SystemConstants.PAGE_ITBK_EXAM_000;
            }

            // b.画面显示
            // 获取考试类别
            exams = examService.getExams();
            // 获取试题种别
            examTypes = examService.getExamTypes();
            // 1:考试模式时获取试卷
            if ("1".equals(mode)) {
                examListBeans = examService.getExamListAll();
            }

            if (SystemConstants.ROLE_NORMAL.equals(WebUtils.getLoginUserInfo().getRole())) {
                for (TsCodeBean bean : examTypes) {
                    if (SystemConstants.EXAM_TYPE_LISTION.equals(bean.getKey())) {
                        examTypes.remove(bean);
                        break;
                    }
                }
            }

            // 获取JLPT等级
            jlptLevels = examService.getJlptLevels();
            // 获取JTEST等级
            jtestLevels = examService.getJtestLevels();

            // c.初始化Bean
            classifyBean = new TbQuestionClassifyBean();

            // d.智能推题
            // 语言信息为空，则智能推题不显示。
            showExamFlag = StringUtils.isEmpty(userInfo.getJlptLevel()) && StringUtils.isEmpty(userInfo.getJtestLevel())
                    ? "" : "true";

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 2.Classify选题
     * 
     * @return
     */
    public String classifySearch() {

        try {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userInfo.getId());
            // a.用户选择check
            if (StringUtils.isEmpty(classifyBean.getExam()) && StringUtils.isEmpty(classifyBean.getExamType())
                    && StringUtils.isEmpty(classifyBean.getJlptLevel())
                    && StringUtils.isEmpty(classifyBean.getJtestLevel())) {
                logger.log(MessageId.ITBK_E_0005);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0005);
                throw ex;
            }

            // b.画面跳转
            if ("6".equals(classifyBean.getExamType())) {
                // b-1.选择听力
                // 跳转至听力画面
                MediaExamController mediaExamController = (MediaExamController) SpringAppContextManager
                        .getBean("mediaExamController");
                mediaExamController.setClassifyBean(classifyBean);
                return mediaExamController.init();

            } else {
                // b-2.选择听力以外
                // 跳转至【试题库】画面
                return toExam();
            }

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 3.智能推题
     * 
     * @return
     */
    public String smartSearch() {
        try {

            // 用户信息中不包含日语等级
            if (StringUtils.isEmpty(userInfo.getJlptLevel()) && StringUtils.isEmpty(userInfo.getJtestLevel())) {
                logger.log(MessageId.ITBK_I_0009);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0009);
                throw ex;
            }

            // a.跳转至【试题库】画面
            // 用户选择设为空
            classifyBean = null;
            return toExam();

        } catch (Exception e) {
            processForException(logger, e);
        }
        // 留在当前画面
        return getPageId();
    }

    /**
     * 3.考试模式
     * 
     * @return
     */
    public String examSearch() {
        try {
            // a.用户选择check
            if (StringUtils.isEmpty(classifyBean.getExam()) || (StringUtils.isEmpty(classifyBean.getJlptLevel())
                    && StringUtils.isEmpty(classifyBean.getJtestLevel()))
                    && (counts.size() > 0 && StringUtils.isEmpty(chooseCount))) {
                logger.log(MessageId.ITBK_E_0006);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0006);
                throw ex;
            }

            // b.跳转至【考试题库】画面
            String source = null;
            for (TbExamListBean bean : examListBeans) {
                // Exam相同 and (chooseYear为空 or Year相同) and (chooseCount为空 or
                // Count相同) and
                // (level等于jlptLevel或者jtestLevel)
                if (classifyBean.getExam().equals(bean.getExam())
                        && (StringUtils.isEmpty(chooseYear) || chooseYear.equals(bean.getYear()))
                        && (StringUtils.isEmpty(chooseCount) || chooseCount.equals(bean.getCount()))
                        && bean.getLevel().equals(classifyBean.getJlptLevel())
                        || bean.getLevel().equals(classifyBean.getJtestLevel())) {
                    source = bean.getSource();
                }
            }
            ExamController examController = (ExamController) SpringAppContextManager.getBean("examController");
            examController.setClassifyBean(classifyBean);
            examController.setSource(source);
            examController.setSafeList(new CopyOnWriteArrayList<ExamModel>());
            return examController.examSearch();

        } catch (Exception e) {
            processForException(logger, e);
        }
        // 留在当前画面
        return getPageId();
    }


    /**
     * 5.模式选择跳转
     * 
     * @param currentMode
     * @return
     */
    public String goBackToIndex() {
        mode = null;
        return SystemConstants.PAGE_ITBK_EXAM_000;
    }

    /**
     * [共通]跳转至[试题库]画面
     * 
     * @return
     */
    private String toExam() {
        ExamController examController = (ExamController) SpringAppContextManager.getBean("examController");
        examController.setClassifyBean(classifyBean);

        return examController.init();
    }

    /**
     * [Ajax]考题种别变更,试卷年变更
     */
    public void changExamType() {
        logger.debug("题种别变更，刷新考试级别");

        if ("1".equals(mode)) {
            years.clear();
            counts.clear();

            chooseYear = "";
            chooseCount = "";
            // JLPT的场合
            if (SystemConstants.EXAM_1.equals(classifyBean.getExam())) {
                classifyBean.setJtestLevel(null);
            }
            // J.TEST的场合
            if (SystemConstants.EXAM_2.equals(classifyBean.getExam())) {
                classifyBean.setJlptLevel(null);
            }
        }
    }

    /**
     * [Ajax]试卷年变更
     */
    public String changeYear() {
        if (!"1".equals(mode)) {
            return getPageId();
        }
        try {
            logger.debug("题种别变更，刷新考卷年。");
            years.clear();
            // 获取考卷年(JLPT)
            if (SystemConstants.EXAM_1.equals(classifyBean.getExam())
                    && StringUtils.isNotEmpty(classifyBean.getJlptLevel())) {
                for (TbExamListBean examListBean : examListBeans) {
                    // 取JLPT对应等级的年份
                    if (classifyBean.getExam().equals(examListBean.getExam())
                            && classifyBean.getJlptLevel().equals(examListBean.getLevel())
                            && !years.contains(examListBean.getYear())) {
                        years.add(examListBean.getYear());
                    }
                }
                if (years.size() == 0) {
                    logger.log(MessageId.ITBK_I_0020);
                    CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0020);
                    throw ex;
                }
            }
            // 获取考次（JTEST）
            counts.clear();
            if (SystemConstants.EXAM_2.equals(classifyBean.getExam())
                    && StringUtils.isNotEmpty(classifyBean.getJtestLevel())) {
                for (TbExamListBean examListBean : examListBeans) {
                    if (classifyBean.getExam().equals(examListBean.getExam())
                            && classifyBean.getJtestLevel().equals(examListBean.getLevel())
                            && !counts.contains(chooseCount)) {
                        counts.add(examListBean.getCount());
                    }
                }
                if (counts.size() == 0) {
                    logger.log(MessageId.ITBK_I_0020);
                    CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0020);
                    throw ex;
                }
            }
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * [Ajax]试卷月变更
     */
    public String changeMonth() {
        if (!"1".equals(mode)) {
            return getPageId();
        }
        try {
            logger.debug("题种别变更，刷新考卷月。");
            counts.clear();
            // 获取考卷月
            for (TbExamListBean examListBean : examListBeans) {
                // 考试级别相等+考试年相等+考试种别相等
                if (classifyBean.getJlptLevel().equals(examListBean.getLevel())
                        && examListBean.getYear().equals(chooseYear)
                        && examListBean.getExam().equals(classifyBean.getExam())) {
                    if (!counts.contains(examListBean.getCount()) && StringUtils.isNotEmpty(examListBean.getCount())) {
                        counts.add(examListBean.getCount());
                    }
                }
            }
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public List<String> getCounts() {
        return counts;
    }

    public void setCounts(List<String> counts) {
        this.counts = counts;
    }

    public String getChooseYear() {
        return chooseYear;
    }

    public void setChooseYear(String chooseYear) {
        this.chooseYear = chooseYear;
    }

    public String getChooseCount() {
        return chooseCount;
    }

    public void setChooseCount(String chooseCount) {
        this.chooseCount = chooseCount;
    }

}
