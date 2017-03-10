package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.item.bank.model.entity.MediaQuestionStructure;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.web.exam.service.MediaService;
import zh.co.item.bank.web.exam.service.QuestionService;

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
    private QuestionService questionService;

    @Inject
    private MediaService mediaService;

    private List<QuestionStructure> questions;

    private List<MediaQuestionStructure> mediaQuestions;

    private String source;

    @Override
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_011;
    }

    public String init() {
        try {
            pushPathHistory("examScoreInputController");

            questions = new ArrayList<QuestionStructure>();
            mediaQuestions = new ArrayList<MediaQuestionStructure>();

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            source = request.getParameter("source");

            if (source == null) {
                logger.log(MessageId.COMMON_E_0001);
                processForException(this.logger, new Exception(MessageId.COMMON_E_0001));
                return getPageId();
            }

            // 画面初始化
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("source", source);
            map.put("deleteFlag", "1");
            questions = questionService.selectQuestionsStructure(map);

        } catch (Exception e) {
            processForException(logger, e);
        }
        // 画面初始化

        return getPageId();
    }

    /**
     * 登录非听力成绩并显示听力
     */
    public String doSubmit() {
        try {
            // 非听力部分录入保存TODO
            questions.clear();

            // 显示听力
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("source", source);
            map.put("deleteFlag", "1");
            mediaQuestions = mediaService.selectMediaStructure(map);

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 登录听力成绩
     */
    public String doSubmitMedia() {
        try {
            // 听力部分录入保存TODO
            mediaQuestions.clear();

            // 跳转至成绩单
            ExamScoreController examScoreController = (ExamScoreController) SpringAppContextManager
                    .getBean("examScoreController");
            examScoreController.init(source);

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
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
