package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.FirstLevelModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.web.exam.service.CollectionService;

/**
 * 试卷画面
 * 
 * @author gaoya
 *
 */
@Named("correlationQuestionsController")
@Scope("session")
public class CorrelationQuestionsController extends BaseController {
    private final CmnLogger logger = CmnLogger.getLogger(getClass());

    @Inject
    private CollectionService collectionService;

    /** 画面初始化变量 */
    private List<QuestionStructure> questions;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_013;
    }

    /**
     * 1.浏览试卷（非听力）
     * 
     * @return
     */
    public String init() {

        pushPathHistory("correlationQuestionsController");
        if (questions != null && questions.size() > 0) {
            for (QuestionStructure questionStructure : questions) {
                List<FirstLevelModel> firstLevelModles = questionStructure.getFirstLevels();
                for (FirstLevelModel firstLevelModle : firstLevelModles) {
                    List<String> subjectList = CmnStringUtils.getSubjectList((firstLevelModle.getSubject()));
                    firstLevelModle.setSubjectList(subjectList);
                    String graphicImage = CmnStringUtils.getGraphicImage(firstLevelModle.getImg());
                    firstLevelModle.setGraphicImage(graphicImage);
                    CmnStringUtils.answerLayoutSet(firstLevelModle.getQuestions());
                }
            }
        }
        return getPageId();
    }

    /**
     * 提交
     * 
     * @return
     */
    public String doSubmit() {
        try {
            List<ExamModel> models = new ArrayList<ExamModel>();
            for (QuestionStructure questionStructure : questions) {
                for (FirstLevelModel firstLevelModel : questionStructure.getFirstLevels()) {
                    models.addAll(firstLevelModel.getQuestions());
                }
            }
            collectionService.insertCollections(models, WebUtils.getLoginUserInfo(), null);
            ExamPaperController examPaperController = (ExamPaperController) SpringAppContextManager
                    .getBean("examPaperController");
            examPaperController.setBeforePage(SystemConstants.PAGE_ITBK_EXAM_013);
            examPaperController.setExamPaper(questions);
            return examPaperController.init();
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 3.返回错题画面
     * 
     * @return
     */
    public String goBackToResume() {
        ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
        return resumeBean.init();
    }

    public List<QuestionStructure> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionStructure> questions) {
        this.questions = questions;
    }

}
