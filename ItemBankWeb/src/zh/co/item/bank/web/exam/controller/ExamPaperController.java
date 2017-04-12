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
import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.FirstLevelModel;
import zh.co.item.bank.model.entity.NoteModel;
import zh.co.item.bank.model.entity.PaginatorLogger;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.RepeatPaginator;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamCollectionService;
import zh.co.item.bank.web.exam.service.ExamResultService;

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

    @Inject
    private ExamResultService examResultService;

    /** 画面初始化变量 */
    private List<QuestionStructure> examPaper;

    private UserModel userInfo;

    private String source;

    private String examType;

    private String beforePage;

    private TbNoteBean noteBean;

    // DB中取到的全部笔记
    private List<NoteModel> notes;

    // 分页
    private RepeatPaginator paginator;

    private PaginatorLogger paginatorLogger = new PaginatorLogger(logger, SystemConstants.PAGE_ITBK_USER_007, "向前翻页",
            "向后翻页", "指定页");

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
            noteBean = new TbNoteBean();

            // a.前画面为考试结果一览
            if (source != null) {
                examPaper = examCollectionService.selectReportStructure(userInfo.getId(), source, examType);
            }

            if (examPaper != null && examPaper.size() > 0) {
                for (QuestionStructure questionStructure : examPaper) {
                    List<FirstLevelModel> firstLevelModles = questionStructure.getFirstLevels();
                    for (FirstLevelModel firstLevelModle : firstLevelModles) {
                        List<String> subjectList = CmnStringUtils.getSubjectList((firstLevelModle.getSubject()));
                        firstLevelModle.setSubjectList(subjectList);
                        String graphicImage = CmnStringUtils.getGraphicImage(firstLevelModle.getImg());
                        firstLevelModle.setGraphicImage(graphicImage);
                        CmnStringUtils.answerLayoutSet(firstLevelModle.getQuestions());

                        // b.检索笔记（不区分用户）
                        notes = examResultService.selectNotesByQuestionId(firstLevelModle.getQuestions(), null);

                        // c.关联并显示当前用户的笔记
                        connectWithQuestions(userInfo.getId(), firstLevelModle.getQuestions());
                    }
                }
            }

            // 分页
            paginator = new RepeatPaginator(examPaper, paginatorLogger, "1");

        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_012;
    }

    /**
     * 记笔记
     * 
     * @return
     */
    public String note() {
        System.out.println("note");
        return getPageId();
    }

    /**
     * 3.返回前画面
     * 
     * @return
     */
    public String goBack() {
        if (SystemConstants.PAGE_ITBK_EXAM_013.equals(beforePage)) {
            ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
            return resumeBean.init();
        } else {
            return beforePage;
        }
    }

    /**
     * 4.记笔记
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
            refresh();

        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 5.更新笔记
     * 
     * @return
     */
    public void updateNote() {
        try {
            // a.更新笔记
            noteBean.setUserId(userInfo.getId());
            examResultService.updateNote(noteBean);
            // b.画面显示
            refresh();

        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 笔记关联试题
     */
    private void connectWithQuestions(Integer userId, List<ExamModel> questions) {
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
     * 刷新画面
     * 
     * @param questions
     */
    private void refresh() {
        for (QuestionStructure questionStructure : examPaper) {
            for (FirstLevelModel firstLevelModel : questionStructure.getFirstLevels()) {
                for (ExamModel question : firstLevelModel.getQuestions()) {
                    if (question.getNo() == noteBean.getQuestionId()) {
                        List<ExamModel> list = new ArrayList<ExamModel>();
                        list.add(question);
                        List<NoteModel> noteModel = examResultService.selectNotesByQuestionId(list, userInfo.getId());
                        question.setNoteInfo(noteModel);
                        break;
                    }
                }
            }
        }
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

    public String getBeforePage() {
        return beforePage;
    }

    public void setBeforePage(String beforePage) {
        this.beforePage = beforePage;
    }

    public TbNoteBean getNoteBean() {
        return noteBean;
    }

    public void setNoteBean(TbNoteBean noteBean) {
        this.noteBean = noteBean;
    }

    public RepeatPaginator getPaginator() {
        return paginator;
    }

    public void setPaginator(RepeatPaginator paginator) {
        this.paginator = paginator;
    }
}
