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
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbNoteBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.FirstLevelModel;
import zh.co.item.bank.model.entity.NoteModel;
import zh.co.item.bank.model.entity.PaginatorLogger;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.RepeatPaginator;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.ExamService;
import zh.co.item.bank.web.exam.service.NoteService;
import zh.co.item.bank.web.exam.service.ResumeService;

/**
 * 试卷画面
 * 
 * @author gaoya
 *
 */
@Named("resumeListController")
@Scope("session")
public class ResumeListController extends BaseController {
    private final CmnLogger logger = CmnLogger.getLogger(getClass());

    @Inject
    private ExamService examService;

    @Inject
    private ResumeService resumeService;

    @Inject
    private NoteService noteService;

    /** 画面初始化变量 */
    private List<QuestionStructure> examPaper;

    private UserModel userInfo;

    // 所有考题种别
    private List<TsCodeBean> examTypes;

    private String examType;

    private TbNoteBean noteBean;

    // DB中取到的全部笔记
    private List<NoteModel> notes;

    // 用户要移除的试题
    private Integer removeQuestionId;

    // 当前选择的类型
    private String currentExamType;

    // 分页
    private RepeatPaginator paginator;

    private PaginatorLogger paginatorLogger = new PaginatorLogger(logger, SystemConstants.PAGE_ITBK_USER_007, "向前翻页",
            "向后翻页", "指定页");

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_014;
    }

    /**
     * 1.浏览试卷（非听力）
     * 
     * @return
     */
    public String init() {
        try {

            // a.画面初始化
            pushPathHistory("resumeListController");
            userInfo = (UserModel) WebUtils.getLoginUserInfo();
            noteBean = new TbNoteBean();
            examPaper = new ArrayList<QuestionStructure>();
            paginator = null;
            removeQuestionId = null;
            currentExamType = null;

            // 试题种别
            examTypes = examService.getExamTypes();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 2.根据考题种别检索试题
     */
    public void searchQuestionByExamType() {
        try {

            currentExamType = StringUtils.isEmpty(WebUtils.getRequestParam("examType")) ? currentExamType
                    : WebUtils.getRequestParam("examType");
            examPaper.clear();
            if (StringUtils.isEmpty(currentExamType)) {
                return;
            }
            // 取错题
            examPaper = resumeService.searchCorrelationErrorQuestions(currentExamType, userInfo.getId());
            if (examPaper != null && examPaper.size() > 0) {
                for (QuestionStructure questionStructure : examPaper) {
                    List<FirstLevelModel> firstLevelModles = questionStructure.getFirstLevels();
                    for (FirstLevelModel firstLevelModle : firstLevelModles) {
                        // subject显示格式设置
                        List<String> subjectList = CmnStringUtils.getSubjectList((firstLevelModle.getSubject()));
                        firstLevelModle.setSubjectList(subjectList);
                        // 图片显示设置
                        String graphicImage = CmnStringUtils.getGraphicImage(firstLevelModle.getImg());
                        firstLevelModle.setGraphicImage(graphicImage);
                        CmnStringUtils.answerLayoutSet(firstLevelModle.getQuestions());

                        // b.检索笔记（不区分用户）
                        notes = noteService.selectNotesByQuestionId(firstLevelModle.getQuestions(), null);

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
    }

    /**
     * 3.返回前画面
     * 
     * @return
     */
    public String goBack() {
        return SystemConstants.PAGE_ITBK_EXAM_000;
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
                noteService.doNote(noteBean);
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
            noteService.updateNote(noteBean);
            // b.画面显示
            refresh();

        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 6.从错题库中移除试题
     * 
     * @return
     */
    public void removerErrorQuestion() {
        try {
            // a.错题集中删除用户指定试题
            // 解决方法：用户从错题库中删除试题时，视为用户做了一次该题且做对
            resumeService.removeErrorQuestion(userInfo.getId(), removeQuestionId);

            // b.刷新画面
            searchQuestionByExamType();

        } catch (Exception e) {
            processForException(logger, e);
        }
        removeQuestionId = null;
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
                        List<NoteModel> noteModel = noteService.selectNotesByQuestionId(list, userInfo.getId());
                        question.setNoteInfo(noteModel);
                        break;
                    }
                }
            }
        }
    }

    public List<TsCodeBean> getExamTypes() {
        return examTypes;
    }

    public void setExamTypes(List<TsCodeBean> examTypes) {
        this.examTypes = examTypes;
    }

    public List<QuestionStructure> getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(List<QuestionStructure> examPaper) {
        this.examPaper = examPaper;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
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

    public Integer getRemoveQuestionId() {
        return removeQuestionId;
    }

    public void setRemoveQuestionId(Integer removeQuestionId) {
        this.removeQuestionId = removeQuestionId;
    }

}
