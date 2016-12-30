package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.List;

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
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.web.exam.service.ExamService;
import zh.co.item.bank.web.exam.service.QuestionService;

/**
 * 试题更新画面
 * 
 * @author gaoya
 *
 */
@Named("questionUpdateController")
@Scope("session")
public class QuestionUpdateController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamService examService;

    @Inject
    private QuestionService questionService;

    // 所有考试类别
    private List<TsCodeBean> exams;

    // 所有考题种别
    private List<TsCodeBean> examTypes;

    // 所有JLPT考试级别
    private List<TsCodeBean> jlptLevels;

    // 所有J.TEST考试级别
    private List<TsCodeBean> jtestLevels;

    // 题目
    private List<TbQuestionStructureBean> structures = new ArrayList<TbQuestionStructureBean>();

    private ExamModel question = new ExamModel();

    private String examFlag;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_QUE_002;
    }

    /**
     * 1.画面初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("questionInsertController");
            // a.画面显示项目取得
            exams = examService.getExams();
            examTypes = examService.getExamTypes();
            jlptLevels = examService.getJlptLevels();
            jtestLevels = examService.getJtestLevels();
            // 大题目
            structures = questionService.getStructures();

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 2.[提交]按钮按下
     * 
     * @return
     */
    public String doUpdate() {
        try {
            // a.取ClassifyId
            TbQuestionClassifyBean classifyBean = new TbQuestionClassifyBean();
            if (StringUtils.isNotEmpty(question.getExam())) {
                classifyBean.setExam(question.getExam());
            }
            if (StringUtils.isNotEmpty(question.getExamType())) {
                classifyBean.setExamType(question.getExamType());
            }
            if (StringUtils.isNotEmpty(question.getJlptLevel())) {
                classifyBean.setJlptLevel(question.getJlptLevel());
            }
            if (StringUtils.isNotEmpty(question.getJtestLevel())) {
                classifyBean.setJtestLevel(question.getJtestLevel());
            }
            Integer classifyId = examService.getClassifyId(classifyBean);
            if (classifyId == null) {
                logger.log(MessageId.COMMON_Q_0001);
                CmnBizException ex = new CmnBizException(MessageId.COMMON_Q_0001);
                throw ex;
            }
            question.setClassifyId(classifyId);
            // b.登录试题表
            questionService.updateQuestion(question);

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 3.[试题更正]返回结果一览画面
     * 
     * @return
     */
    public String goBackToResult() {
        // 前画面为考试结果一览
        if (StringUtils.isNotEmpty(examFlag)) {
            ExamReportController examReportController = (ExamReportController) SpringAppContextManager
                    .getBean("examReportController");
            return examReportController.init(question.getSource());
        }
        // 做题结果一览
        ExamResultController examResultController = (ExamResultController) SpringAppContextManager.getBean("examResultController");
        return examResultController.init();
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

    public ExamModel getQuestion() {
        return question;
    }

    public void setQuestion(ExamModel question) {
        this.question = question;
    }

    public List<TbQuestionStructureBean> getStructures() {
        return structures;
    }

    public void setStructures(List<TbQuestionStructureBean> structures) {
        this.structures = structures;
    }

    public String getExamFlag() {
        return examFlag;
    }

    public void setExamFlag(String examFlag) {
        this.examFlag = examFlag;
    }

}
