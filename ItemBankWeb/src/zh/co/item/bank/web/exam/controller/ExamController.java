package zh.co.item.bank.web.exam.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbExamDropoutBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.CollectionService;
import zh.co.item.bank.web.exam.service.ExamDropoutService;
import zh.co.item.bank.web.exam.service.ExamService;

/**
 * 考试画面
 * 
 * @author gaoya
 *
 */
@Named("examController")
@Scope("session")
public class ExamController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamService examService;

    @Inject
    private CollectionService collectionService;

    @Inject
    private ExamDropoutService examDropoutService;

    /** 初始化变量 */
    // 试题
    private List<ExamModel> questions;

    private TbQuestionClassifyBean classifyBean;

    private String title;

    private String subject;

    private List<String> subjectList;

    private String graphicImage;

    /** --共通变量-- */
    // 用户信息
    private UserModel userInfo;

    // 当前试题
    private String source;

    /** --考试模式用变量-- */
    // 试题结构
    CopyOnWriteArrayList<ExamModel> safeList;

    // 考试进行状态
    private String status;

    // 考卷年
    String year = null;

    // 考卷月
    String count = null;

    // 开始做题时间
    Date startTime = null;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * 1.做题画面初始化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("examController");
            // a.对象初始化
            userInfo = WebUtils.getLoginUserInfo();

            title = "";
            subjectList = new ArrayList<String>();
            subject = "";
            graphicImage = "";
            status = null;
            questions = new ArrayList<ExamModel>();

            // b.检索试题
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userInfo.getId());
            // b-1.智能选题
            if (classifyBean == null) {
                if (!StringUtils.isEmpty(userInfo.getJlptLevel())) {
                    map.put("jlptLevel", userInfo.getJlptLevel());
                }
                if (!StringUtils.isEmpty(userInfo.getJtestLevel())) {
                    map.put("jtestLevel", userInfo.getJtestLevel());
                }
                questions = examService.smartSearch(map);

            } else {
                // b-2.题型选题
                if (StringUtils.isNotEmpty(classifyBean.getExam())) {
                    map.put("exam", classifyBean.getExam());
                }
                if (StringUtils.isNotEmpty(classifyBean.getExamType())) {
                    map.put("examType", classifyBean.getExamType());
                }
                if (StringUtils.isNotEmpty(classifyBean.getJlptLevel())) {
                    map.put("jlptLevel", classifyBean.getJlptLevel());
                }
                if (StringUtils.isNotEmpty(classifyBean.getJtestLevel())) {
                    map.put("jtestLevel", classifyBean.getJtestLevel());
                }

                // ----日语特殊设置----
                // 选择J.TEST时，examType选择词汇或者文法，都设为文法
                if ("2".equals(classifyBean.getExam()) && "2".equals(classifyBean.getExamType())) {
                    map.put("examType", "3");

                    // 只选择了词汇
                } else if (StringUtils.isEmpty(classifyBean.getExam()) && "2".equals(classifyBean.getExamType())) {
                    // 先检索JLPT的词汇，若没有，则检索J.TEST的文法
                    map.put("exam", "1");
                    questions = examService.classifySearch(classifyBean, map);
                    if (questions.size() == 0) {
                        map.put("exam", "2");
                        map.put("examType", "3");
                        questions = examService.classifySearch(classifyBean, map);
                    }

                    // 正常条件检索
                } else {
                    questions = examService.classifySearch(classifyBean, map);
                }
            }

            // 题库已空
            if (questions == null || questions.size() == 0) {
                logger.log(MessageId.ITBK_I_0010);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0010);
                throw ex;
            }

            Integer fatherId = questions.get(0).getFatherId();
            if (fatherId != null) {
                // 特殊试题检索
                questions.clear();
                questions = examService.selectQuestionByFatherId(fatherId);
                // 取大题
                subject = questions.get(0).getSubject();
                graphicImage = CmnStringUtils.getGraphicImage(questions.get(0).getImg());

            } else if (questions.get(questions.size() - 1).getFatherId() != null) {
                // 特殊试题不显示[下次显示]
                List<ExamModel> list = new CopyOnWriteArrayList<ExamModel>();
                list.addAll(questions);
                for (ExamModel examModel : questions) {
                    if (examModel.getFatherId() == null) {
                        list.remove(examModel);
                    }
                }
                questions.clear();
                questions.addAll(list);
            }

            // 取题目
            title = examService.getTitle(questions.get(0).getStructureId());
            // 画面序号
            prepareData(subject);

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();

    }

    /**
     * 2.考试模式（开始考试按钮按下）
     * 
     * @return
     */
    public String examSearch() {
        try {
            pushPathHistory("examController");
            // a.初始化变量
            title = "";
            subjectList = new ArrayList<String>();
            subject = "";
            graphicImage = "";

            if (!"ing".equals(status)) {
                status = "";
            }
            // 记录考试开始时间
            if (startTime == null) {
                startTime = new Date();
            }
            if (userInfo == null) {
                userInfo = WebUtils.getLoginUserInfo();
            }

            // b.检索试题
            // 检索用Map
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userInfo.getId());
            // J.TEST，JLPT区分，order By控制
            map.put("exam", classifyBean.getExam());
            map.put("source", source);

//            // b-1.检测是否有中途退出的试题存在
//            if (year == null) {
//                year = examDropoutService.getYear(getExamDropoutBean());
//            }
            // b-2.获得试题结构(一次考试只获取一次)(听力大题目不检索)
            if (safeList == null || safeList.size() == 0) {
                List<ExamModel> examStructure = examService.getStructure(classifyBean);
                safeList.addAll(examStructure);
            }
            for (ExamModel model : safeList) {
                map.put("structureId", model.getStructureId());

//                // 添加年限选择后废弃
//                if (year == null) {
//                    // 获取题库中最新一年的试题year
//                    year = examService.getYear(map);
//                    if (year == null) {
//                        safeList.remove(model);
//                        continue;
//                    }
//                }
//                // 添加年限选择后废弃
//                map.put("year", year);
                // b-3.检索指定年限试题
                questions = examService.getTestQuestionBySource(map);

                // 当前大题已做完
                if (questions == null || questions.size() == 0) {
                    safeList.remove(model);
                    continue;
                } else if (source == null || !source.equals(questions.get(0).getSource())) {
                    source = questions.get(0).getSource();
                }

                Integer fatherId = questions.get(0).getFatherId();
                if (fatherId != null) {
                    // 特殊试题检索
                    questions.clear();
                    questions = examService.selectQuestionByFatherId(fatherId);
                    subject = questions.get(0).getSubject();
                    graphicImage = CmnStringUtils.getGraphicImage(questions.get(0).getImg());
                }
                // 画面序号、折行
                prepareData(subject);
                title = model.getTitle();

                return getPageId();
            }
            // c.考试结束
            if ("ing".equals(status)) {

                // c-1.删除中途退出表
                examDropoutService.deleteExamDropout(getExamDropoutBean());
                doclear();
                // c-2.显示成绩
                ExamReportController examReportController = (ExamReportController) SpringAppContextManager
                        .getBean("examReportController");
                examReportController.setMediaFlag("true");
                examReportController.setJtestFlag("true");
                examReportController.setClassifyBean(classifyBean);
                return examReportController.init(source);
            } else {
//                // 未检索到指定试题
//                logger.log(MessageId.ITBK_I_0015);
//                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0015);
//                throw ex;
                // 显示上次成绩
                ExamReportController examReportController = (ExamReportController) SpringAppContextManager
                        .getBean("examReportController");
                examReportController.setMediaFlag("false");
                examReportController.setClassifyBean(classifyBean);
                return examReportController.init(source);
            }

//        } catch (CmnBizException ex) {
//            processForException(logger, ex);
//            // 返回题型选择
//            ExamClassifyController classifyBean = (ExamClassifyController) SpringAppContextManager
//                    .getBean("examClassifyController");
//            return classifyBean.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 3.[确认]按钮点下
     * 
     * @return
     */
    public String doSubmit() {
        try {

            // a.做题结果登录
            collectionService.insertCollections(questions, userInfo, status);

            // b.考试继续
            if ("ing".equals(status) || "exist".equals(status)) {
                return null;
            }
            // c.考试结束
            // 跳转至[结果一览]画面
            ExamResultController examResultController = (ExamResultController) SpringAppContextManager
                    .getBean("examResultController");
            examResultController.setQuestions(questions);
            examResultController.setTitle(title);
            examResultController.setSubject(subject);
            examResultController.setSubjectList(subjectList);
            examResultController.setGraphicImage(graphicImage);
            examResultController.setResume(false);
            return examResultController.init();
        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * 4.[我的错题]按下
     * 
     * @return
     */
    public String doResume() {
        // 跳转至错题库
        ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
        return resumeBean.init();
    }

    /**
     * 5.[下一题]按下
     * 
     * @return
     */
    public String doNext() {
        try {
            // 考试进行中
            status = "ing";
            // a.保存当前做题记录
            doSubmit();

            // b.继续下一道题
            return examSearch();
        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * 6.[直接退出]按下
     * 
     * @return
     */
    public String doExist() {
        // a.清空本次做题记录
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", questions.get(0).getSource());
        map.put("startTime", startTime);
        examService.deleteExamCollectionBySource(map);
        doclear();
        // b.跳转至试题选择
        ExamClassifyController examClassifyController = (ExamClassifyController) SpringAppContextManager
                .getBean("examClassifyController");
        return examClassifyController.init();
    }

    /**
     * 7.[保存退出]按下
     * 
     * @return
     */
    public String doSaveAndExist() {
        // 在最后一道大题点击保存退出
        if (safeList.size() == 1) {
            return doNext();
        }
        status = "exist";
        doSubmit();
        saveDropoutInfo();
        String source = questions.get(0).getSource();
        doclear();
        // 跳转至成绩一览画面
        ExamReportController examReportController = (ExamReportController) SpringAppContextManager
                .getBean("examReportController");
        // 中途退出不显示听力
        examReportController.setMediaFlag("false");
        examReportController.setJtestFlag("false");
        return examReportController.init(source);
    }

    /**
     * 8.数据清除
     */
    private void doclear() {
        title = null;
        subject = null;
        subjectList = null;
        status = null;
        year = null;
        safeList.clear();
        startTime = null;
        questions.clear();
    }

    /**
     * 9.返回【试题选择】画面
     * 
     * @return
     */
    public String goBackToClassify() {
        ExamClassifyController examClassifyController = (ExamClassifyController) SpringAppContextManager
                .getBean("examClassifyController");
        return examClassifyController.init();
    }

    /**
     * 中途退出信息保存（添加year选择后废弃）TODO
     */
    private void saveDropoutInfo() {
        examDropoutService.insertExamDropout(getExamDropoutBean());
    }

    /**
     * 中途退出表登录参数设置
     * 
     * @return
     */
    private TbExamDropoutBean getExamDropoutBean() {
        TbExamDropoutBean bean = new TbExamDropoutBean();
        bean.setUserId(userInfo.getId());
        bean.setYear(year);
        bean.setExam(classifyBean.getExam());
        bean.setJlptLevel(classifyBean.getJlptLevel());
        bean.setJtestLevel(classifyBean.getJtestLevel());
        return bean;
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

    public TbQuestionClassifyBean getClassifyBean() {
        return classifyBean;
    }

    public void setClassifyBean(TbQuestionClassifyBean classifyBean) {
        this.classifyBean = classifyBean;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CopyOnWriteArrayList<ExamModel> getSafeList() {
        return safeList;
    }

    public void setSafeList(CopyOnWriteArrayList<ExamModel> safeList) {
        this.safeList = safeList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

}
