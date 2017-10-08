package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.CollectionService;
import zh.co.item.bank.web.exam.service.ExamService;

/**
 * 试题类型选择画面
 * 
 * @author gaoya
 *
 */
@Named("oneQuestionController")
@Scope("session")
public class OneQuestionController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private CollectionService collectionService;

    @Inject
    private ExamService examService;

    private ExamModel question;

    private UserModel userInfo;

    /** 大题干List */
    private List<String> subjectList;

    private String graphicImage;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_016;
    }

    /**
     * 1.初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("oneQuestionController");

            // a.对象初始化
            userInfo = (UserModel) WebUtils.getLoginUserInfo();
            subjectList = new ArrayList<String>();
            graphicImage = "";

            // b.检索当日试题
            Integer userId = userInfo.getId();
            question = examService.selectOneQuestion(userId);

            // b.检索到试题
            if (question != null) {
                // 画面排版
                question = CmnStringUtils.selectionLayoutSet(question);
                question.setRowNo(1);
                // 题目
                subjectList = CmnStringUtils.getSubjectList(question.getSubject());
                graphicImage = CmnStringUtils.getGraphicImage(question.getImg());

                // 用户未做跳转至做题页面，已做则跳转至结果画面
                return StringUtils.isEmpty(question.getMyAnswer()) ? getPageId() : goToResult();
            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 2.[确认]按钮点下
     * 
     * @return
     */
    public String doSubmit() {
        try {

            // a.取当前用户的错题记录
            question.setUserId(userInfo.getId());

            // b.更新做题记录
            collectionService.updateCollection(question);

            // c.去结果一览画面
            return goToResult();

        } catch (Exception e) {
            processForException(logger, e);
        }
        // 留在本画面
        return getPageId();
    }

    /**
     * 3.[去做题]按钮按下
     * 
     * @return
     */
    public String toClassify() {
        ExamClassifyController examClassifyController = (ExamClassifyController) SpringAppContextManager
                .getBean("examClassifyController");
        return examClassifyController.init();
    }

    /**
     * 4.[退出]按钮按下
     * 
     * @return
     */
    public String goBackToIndex() {
        return SystemConstants.PAGE_ITBK_EXAM_000;
    }

    private String goToResult() {
        // c.去结果一览画面
        ExamResultController examResultController = (ExamResultController) SpringAppContextManager
                .getBean("examResultController");
        List<ExamModel> questions = new ArrayList<>();
        questions.add(question);
        examResultController.setQuestions(questions);
        examResultController.setSubject(question.getSubject());
        examResultController.setSubjectList(subjectList);
        examResultController.setTitle(question.getTitle());
        examResultController.setGraphicImage(graphicImage);
        examResultController.setBeforePageId(getPageId());
        return examResultController.init();
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public String getGraphicImage() {
        return graphicImage;
    }

    public void setGraphicImage(String graphicImage) {
        this.graphicImage = graphicImage;
    }

    public ExamModel getQuestion() {
        return question;
    }

    public void setQuestion(ExamModel question) {
        this.question = question;
    }

}
