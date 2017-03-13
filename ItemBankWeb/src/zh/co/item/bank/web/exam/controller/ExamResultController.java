package zh.co.item.bank.web.exam.controller;

import java.util.List;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.FirstLevelModle;
import zh.co.item.bank.model.entity.QuestionStructure;

/**
 * 结果一览画面
 * 
 * @author gaoya
 *
 */
@Named("examResultController")
@Scope("session")
public class ExamResultController extends BaseController {
    private final CmnLogger logger = CmnLogger.getLogger(getClass());

    /** 画面初始化变量 */
    private List<ExamModel> questions;

    private String title;

    private String subject;

    private String graphicImage;

    // 大题干List
    private List<String> subjectList;

    private boolean isResume;

    // 试卷
    private List<QuestionStructure> examPaper;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_003;
    }

    /**
     * 1.[做題模式结果一览]画面初始化
     */
    public String init() {
        try {
            pushPathHistory("examResultController");
            if (questions == null) {
                logger.log(MessageId.COMMON_E_0001);
            }
        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_003;
    }

    /**
     * 2.跳转至[试题详细]画面
     * 
     * @return
     */
    public String showDetail() {
        try {
            ExamDetailController examDetailController = (ExamDetailController) SpringAppContextManager
                    .getBean("questionDetail");
            examDetailController.setResume(isResume);
            return examDetailController.init();

        } catch (Exception e) {
            processForException(this.logger, e);
        }

        return getPageId();
    }

    /**
     * 3.返回【试题选择】画面
     * 
     * @return
     */
    public String goBackToClassify() {
        ExamClassifyController examClassifyController = (ExamClassifyController) SpringAppContextManager
                .getBean("examClassifyController");
        return examClassifyController.init();
    }

    /**
     * 4.返回[试题库]/[错题库]画面（继续做题按钮）
     * 
     * @return
     */
    public String goBackToExam() {
        if (isResume) {
            ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
            return resumeBean.init();

        } else {
            ExamController examController = (ExamController) SpringAppContextManager.getBean("examController");
            return examController.init();
        }
    }

    /**
     * 5.浏览试卷
     * 
     * @return
     */
    public String showPaper() {
        pushPathHistory("examResultController");
        if (examPaper != null && examPaper.size() > 0) {
            for (QuestionStructure questionStructure : examPaper) {
                List<FirstLevelModle> firstLevelModles = questionStructure.getFirstLevels();
                for(FirstLevelModle firstLevelModle:firstLevelModles){
                   List<String> subjectList= CmnStringUtils.getSubjectList((firstLevelModle.getSubject()));
                   firstLevelModle.setSubjectList(subjectList);
                }
            }
        }
        return SystemConstants.PAGE_ITBK_EXAM_012;
    }

    /**
     * 画面序号,折行
     * 
     * @param subject
     *            题干
     */
    private void prepareData(String subject) {
        // 画面序号和显示设置
        questions = CmnStringUtils.answerLayoutSet(questions);
        // 折行
        subjectList = CmnStringUtils.getSubjectList(subject);
    }

    public List<ExamModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamModel> questions) {
        this.questions = questions;
    }

    public String getTitle() {
        return title;
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

    public String getGraphicImage() {
        return graphicImage;
    }

    public void setGraphicImage(String graphicImage) {
        this.graphicImage = graphicImage;
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean isResume) {
        this.isResume = isResume;
    }

    public List<QuestionStructure> getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(List<QuestionStructure> examPaper) {
        this.examPaper = examPaper;
    }

}
