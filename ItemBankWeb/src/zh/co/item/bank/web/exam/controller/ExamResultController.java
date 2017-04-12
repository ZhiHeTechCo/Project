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
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.NoteModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamResultService;

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

    @Inject
    private ExamResultService examResultService;

    /** 画面初始化变量 */
    private List<ExamModel> questions;

    private String title;

    private String subject;

    private String graphicImage;

    // 大题干List
    private List<String> subjectList;

    private boolean isResume;

    private UserModel userInfo;

    private TbNoteBean noteBean;

    // DB中取到的全部笔记
    private List<NoteModel> notes;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_003;
    }

    /**
     * 1.[做題模式结果一览]画面初始化
     */
    public String init() {
        try {
            pushPathHistory("examResultController");

            // a.初始化
            if (questions == null) {
                logger.log(MessageId.COMMON_E_0001);
            }
            userInfo = WebUtils.getLoginUserInfo();
            noteBean = new TbNoteBean();

            // b.检索笔记（不区分用户）
            notes = examResultService.selectNotesByQuestionId(questions, null);

            // c.关联并显示当前用户的笔记
            connectWithQuestions(userInfo.getId());

        } catch (Exception e) {
            processForException(this.logger, e);
        }
        return getPageId();
    }

    /**
     * 笔记关联试题
     */
    private void connectWithQuestions(Integer userId) {
        for (ExamModel question : questions) {
            List<NoteModel> tmp = new ArrayList<NoteModel>();
            for (NoteModel bean : notes) {
                // 试题相等
                // 指定显示当前用户笔记（参数userId是否为空）
                if (question.getNo() == bean.getQuestionId() && (userId == null || bean.getUserId() == userId)) {
                    tmp.add(bean);
                }
            }
            // 笔记计入试题
            question.setNoteInfo(tmp);
        }
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
     * 5.查询关联试题
     * 
     * @return
     */
    public String searchCorrelation() {
        try {
            List<QuestionStructure> correlationList = examResultService.searchCorrelationQuestions(questions.get(0));
            if (correlationList == null || correlationList.size() == 0) {
                logger.log(MessageId.ITBK_I_0016);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0016);
                throw ex;
            }
            // 关联试题画面
            CorrelationQuestionsController correlationQuestionsController = (CorrelationQuestionsController) SpringAppContextManager
                    .getBean("correlationQuestionsController");
            correlationQuestionsController.setQuestions(correlationList);
            return correlationQuestionsController.init();
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 6.记笔记
     * 
     * @return
     */
    public void doNote() {
        try {
            // a.登录笔记（画面check笔记不为空）
            if (StringUtils.isNotEmpty(noteBean.getNote())) {
                noteBean.setUserId(userInfo.getId());
                examResultService.doNote(noteBean);
            }
            // b.画面显示
            refresh(questions);

        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 7.更新笔记
     * 
     * @return
     */
    public void updateNote() {
        try {
            // a.更新笔记
            noteBean.setUserId(userInfo.getId());
            examResultService.updateNote(noteBean);
            // b.画面显示
            refresh(questions);

        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 刷新画面
     * @param questions
     */
    private void refresh(List<ExamModel> questions) {
        for (ExamModel question : questions) {
            if (question.getNo() == noteBean.getQuestionId()) {
                List<ExamModel> list = new ArrayList<ExamModel>();
                list.add(question);
                List<NoteModel> noteModel = examResultService.selectNotesByQuestionId(list, userInfo.getId());
                question.setNoteInfo(noteModel);
//                // 新增笔记信息
//                NoteModel noteModel = (NoteModel) noteBean;
//                // 添加当前用户信息
//                noteModel.setNickName(userInfo.getNickName());
//                noteModel.setHeadimgurl(userInfo.getHeadimgurl());
//                question.getNoteInfo().add(noteModel);
                break;
            }
        }
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

    public TbNoteBean getNoteBean() {
        return noteBean;
    }

    public void setNoteBean(TbNoteBean noteBean) {
        this.noteBean = noteBean;
    }

}
