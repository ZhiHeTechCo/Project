package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
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


    public String getPageId() {
        return SystemConstants.PAGE_ITBK_QUE_002;
    }

    /**
     * initial
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("questionInsertController");
            exams = examService.getExams();
            examTypes = examService.getExamTypes();
            jlptLevels = examService.getJlptLevels();
            jtestLevels = examService.getJtestLevels();

            structures = questionService.getStructures();

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * [提交]按钮按下
     * 
     * @return
     */
    public String doUpdate() {
        try {
            TbQuestionClassifyBean classifyBean = new TbQuestionClassifyBean();
            if (!question.getExam().isEmpty()) {
                classifyBean.setExam(question.getExam());
            }
            if (!question.getExamType().isEmpty()) {
                classifyBean.setExamType(question.getExamType());
            }
            if (!question.getJlptLevel().isEmpty()) {
                classifyBean.setJlptLevel(question.getJlptLevel());
            }
            if (!question.getJtestLevel().isEmpty()) {
                classifyBean.setJtestLevel(question.getJtestLevel());
            }
            // 取ClassifyId
            Integer classifyId = examService.getClassifyId(classifyBean);
            if (classifyId == null) {
                logger.log(MessageId.COMMON_Q_0001);
                CmnBizException ex = new CmnBizException(MessageId.COMMON_Q_0001);
                throw ex;
            }
            question.setClassifyId(classifyId);
            // 登录试题表
            questionService.updateQuestion(question);

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

}
