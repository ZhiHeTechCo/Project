package zh.co.item.bank.web.exam.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.FirstLevelModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamCollectionService;

/**
 * 试卷画面
 * 
 * @author gaoya
 *
 */
@Named("examPaperController")
@Scope("session")
public class ExamPaperController extends BaseController {
    private final CmnLogger logger = CmnLogger.getLogger(getClass());

    @Inject
    private ExamCollectionService examCollectionService;

    /** 画面初始化变量 */
    private List<QuestionStructure> examPaper;

    private UserModel userInfo;
    
    private String source;

    private String examType;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_012;
    }

    /**
     * 1.浏览试卷（非听力）
     * 
     * @return
     */
    public String init() {
        try {

            pushPathHistory("examPaperController");
            userInfo = (UserModel) WebUtils.getLoginUserInfo();

            examPaper = examCollectionService.selectReportStructure(userInfo.getId(), source, examType);

            if (examPaper != null && examPaper.size() > 0) {
                for (QuestionStructure questionStructure : examPaper) {
                    List<FirstLevelModel> firstLevelModles = questionStructure.getFirstLevels();
                    for (FirstLevelModel firstLevelModle : firstLevelModles) {
                        source = firstLevelModle.getQuestions().get(0).getSource();
                        List<String> subjectList = CmnStringUtils.getSubjectList((firstLevelModle.getSubject()));
                        firstLevelModle.setSubjectList(subjectList);
                        String graphicImage = CmnStringUtils.getGraphicImage(firstLevelModle.getImg());
                        firstLevelModle.setGraphicImage(graphicImage);
                        CmnStringUtils.answerLayoutSet(firstLevelModle.getQuestions());
                    }
                }
            }

        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_012;
    }

    /**
     * 2.返回考试结果一览画面
     * 
     * @return
     */
    public String goBackToReport() {
        return SystemConstants.PAGE_ITBK_EXAM_006;
    }

    public List<QuestionStructure> getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(List<QuestionStructure> examPaper) {
        this.examPaper = examPaper;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

}
