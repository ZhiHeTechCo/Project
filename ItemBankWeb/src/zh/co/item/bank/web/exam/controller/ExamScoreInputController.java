package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.MediaQuestionStructure;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.CollectionService;
import zh.co.item.bank.web.exam.service.ExamScoreInputService;
import zh.co.item.bank.web.exam.service.MediaService;

/**
 * 答案录入画面
 * 
 * @author gyang
 */
@Named("examScoreInputController")
@Scope("session")
public class ExamScoreInputController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamScoreInputService examScoreService;

    @Inject
    private CollectionService collectionService;

    @Inject
    private MediaService mediaService;

    private List<QuestionStructure> questions;

    private List<MediaQuestionStructure> mediaQuestions;

    private String source;

    // 用户信息
    private UserModel userInfo;

    private List<ExamModel> models;

    @Override
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_011;
    }

    public String init() {
        try {
            pushPathHistory("examScoreInputController");
            // a.对象初始化
            userInfo = WebUtils.getLoginUserInfo();

            // 画面初始化
            questions = new ArrayList<QuestionStructure>();
            mediaQuestions = new ArrayList<MediaQuestionStructure>();

            // Source取得
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            String requestSource = request.getParameter("source");
            source = requestSource == null ? source : requestSource;
            if (source == null) {
                logger.log(MessageId.COMMON_E_0001);
                processForException(this.logger, new Exception(MessageId.COMMON_E_0001));
                return getPageId();
            }

            // 1.是否已经做过
            boolean isExist = examScoreService.selectExamCollectionForCount(userInfo.getId(), source);
            if (isExist) {
                // 已经做过，跳转去成绩一览
                return gotoScoreList();
            } else {
                // 取试题
                questions = examScoreService.selectQuestionsStructure(source);
            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 登录非听力成绩并显示听力
     */
    public String toMedia() {
        try {
            // 非听力部分录入保存（暂不登陆）
            models = new ArrayList<ExamModel>();
            for (QuestionStructure question : questions) {
                models.addAll(question.getQuestions());
            }

            // 画面显示制御
            questions.clear();

            // 显示听力
            mediaQuestions = examScoreService.selectMediaStructure(source);

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 登录答案
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String doSubmit() {
        try {
            // 非听力部分录入保存
            collectionService.insertCollections(models, userInfo, "exist");
            // 听力部分录入保存
            mediaService.doInsertCollections(mediaQuestions, userInfo, source, SystemConstants.TRUE);
            mediaQuestions.clear();

            // 跳转至成绩单
            return gotoScoreList();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 跳转至成绩单
     * 
     * @param flag
     * 
     * @return
     */
    private String gotoScoreList() {
        ExamScoreController examScoreController = (ExamScoreController) SpringAppContextManager
                .getBean("examScoreController");
        examScoreController.setGobackFlag(SystemConstants.FALSE);
        return examScoreController.init(source);
    }

    public List<QuestionStructure> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionStructure> questions) {
        this.questions = questions;
    }

    public List<MediaQuestionStructure> getMediaQuestions() {
        return mediaQuestions;
    }

    public void setMediaQuestions(List<MediaQuestionStructure> mediaQuestions) {
        this.mediaQuestions = mediaQuestions;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
