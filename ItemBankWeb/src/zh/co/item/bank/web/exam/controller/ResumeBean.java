package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.CollectionService;
import zh.co.item.bank.web.exam.service.ExamService;
import zh.co.item.bank.web.exam.service.ResumeService;

/**
 * 试题类型选择画面
 * 
 * @author gaoya
 *
 */
@Named("resumeBean")
@Scope("session")
public class ResumeBean extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ResumeService resumeService;

    @Inject
    private CollectionService collectionService;

    @Inject
    private ExamService examService;

    private List<ExamModel> questions;

    private UserModel userInfo;

    private String tableShow;

    private String title;

    private String subject;

    /** 大题干List */
    private List<String> subjectList;

    private String graphicImage;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_005;
    }

    /**
     * 1.初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("resumeBean");

            // a.对象初始化
            userInfo = (UserModel) WebUtils.getLoginUserInfo();
            title = "";
            subjectList = new ArrayList<String>();
            subject = "";
            graphicImage = "";

            // b.检索错题
            // b-1.检索符合记忆曲线的错题，取第一件
            Integer userId = userInfo.getId();
            questions = resumeService.selectQuestionForError(userId);

            // b-2.未检索到符合记忆曲线的题，则检索该用户所有错题，取第一件
            if (questions.size() == 0) {
                questions = resumeService.selectQuestionForErrorAll(userId);
                // 当前用户没有错题，则报错
                if (questions.size() == 0) {
                    tableShow = null;
                    logger.log(MessageId.ITBK_I_0006);

                } else {
                    tableShow = MessageUtils.getMessage(MessageId.ITBK_I_0007);
                    checkFatherId(questions.get(0));
                }

            } else {
                checkFatherId(questions.get(0));
                tableShow = MessageUtils.getMessage(MessageId.ITBK_I_0008);
            }

        } catch (Exception e) {
            tableShow = null;
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 1-b-2.判断fatherId生成试题
     * 
     * @param examModel
     */
    private void checkFatherId(ExamModel question) {
        // 判断是否为文字，阅读类试题
        Integer fatherId = question.getFatherId();
        if (fatherId != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("fatherId", fatherId);
            param.put("userId", userInfo.getId());
            questions = resumeService.selectErrorByFatherId(param);
            question = questions.get(0);
        }
        // 画面序号
        questions = CmnStringUtils.answerLayoutSet(questions);
        // 题目
        title = examService.getTitle(question.getStructureId());
        // 大题干
        subject = StringUtils.isEmpty(question.getSubject()) ? "" : question.getSubject();
        subjectList = CmnStringUtils.getSubjectList(subject);
        graphicImage = CmnStringUtils.getGraphicImage(question.getImg());
    }

    /**
     * 2.[确认]按钮点下
     * 
     * @return
     */
    public String doResumeSubmit() {
        try {

            for (int i = 0; i < questions.size(); i++) {
                // a.取当前用户的错题记录
                ExamModel examModel = (ExamModel) questions.get(i);
                examModel.setUserId(userInfo.getId());

                // b.更新做题记录
                collectionService.updateCollection(examModel);
            }

            // c.去结果一览画面
            ExamResultController examResultController = (ExamResultController) SpringAppContextManager
                    .getBean("examResultController");
            examResultController.setQuestions(questions);
            examResultController.setSubject(subject);
            examResultController.setSubjectList(subjectList);
            examResultController.setTitle(title);
            examResultController.setGraphicImage(graphicImage);
            examResultController.setResume(true);
            return examResultController.init();

        } catch (Exception e) {
            processForException(logger, e);
            tableShow = null;
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

    public List<ExamModel> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExamModel> questions) {
        this.questions = questions;
    }

    public String getTableShow() {
        return tableShow;
    }

    public void setTableShow(String tableShow) {
        this.tableShow = tableShow;
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

}
