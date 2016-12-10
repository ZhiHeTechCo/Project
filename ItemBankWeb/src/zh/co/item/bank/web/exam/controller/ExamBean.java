package zh.co.item.bank.web.exam.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

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
import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.db.entity.TbExamDropoutBean;
import zh.co.item.bank.db.entity.TbMediaCollectionBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.MediaModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.service.CollectionService;
import zh.co.item.bank.web.exam.service.ExamCollectionService;
import zh.co.item.bank.web.exam.service.ExamDropoutService;
import zh.co.item.bank.web.exam.service.ExamService;
import zh.co.item.bank.web.exam.service.MediaService;

/**
 * 考试画面
 * 
 * @author gaoya
 *
 */
@Named("examBean")
@Scope("session")
public class ExamBean extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamService examService;

    @Inject
    private CollectionService collectionService;

    @Inject
    private ExamCollectionService examCollectionService;

    @Inject
    private ExamDropoutService examDropoutService;

    @Inject
    private MediaService mediaService;

    /** 试题 */
    private List<ExamModel> questions;

    /** 题型种别 */
    private TbQuestionClassifyBean classifyBean;

    /** 题目 */
    private String title;

    /** 大题干 */
    private String subject;

    /** 大题干List */
    private List<String> subjectList;

    /** 图片 */
    private String graphicImage;

    /** --共通变量-- */
    // 用户信息
    private UserModel userInfo;

    /** --考试模式用变量-- */
    // 试题结构
    CopyOnWriteArrayList<ExamModel> safeList;
    // 考试进行状态
    private String status;
    // 考卷年
    String year = null;
    // 开始做题时间
    Date startTime = null;

    /** --听力模式用变量-- */
    // 音频
    MediaModel mediaModel;
    // 听力试题
    List<MediaModel> mediaQuestions;
    // 大题目
    List<TbQuestionStructureBean> structures;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * initial
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("examBean");
            userInfo = WebUtils.getLoginUserInfo();

            title = "";
            subjectList = new ArrayList<String>();
            subject = "";
            graphicImage = "";
            status = null;
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userInfo.getId());
            // 智能选题
            if (classifyBean == null) {
                if (!StringUtils.isEmpty(userInfo.getJlptLevel())) {
                    map.put("jlptLevel", userInfo.getJlptLevel());
                }
                if (!StringUtils.isEmpty(userInfo.getJtestLevel())) {
                    map.put("jtestLevel", userInfo.getJtestLevel());
                }
                questions = examService.smartSearch(map);

            } else {
                // 题型选题
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

                    // 选择了听力
                } else if ("6".equals(classifyBean.getExamType())) {
                    // 初始化
                    mediaModel = null;
                    mediaQuestions = new ArrayList<MediaModel>();
                    structures = new ArrayList<TbQuestionStructureBean>();

                    mediaQuestions = selectForMediaQuestions();
                    if (mediaQuestions == null || mediaQuestions.size() == 0) {
                        // 题库已空
                        logger.log(MessageId.ITBK_I_0010);
                        CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0010);
                        throw ex;
                    }
                    // 画面序号和显示设置
                    mediaQuestions = CmnStringUtils.selectionLayoutSet(mediaQuestions);
                    return SystemConstants.PAGE_ITBK_EXAM_007;

                    // 正常条件检索
                } else {
                    questions = examService.classifySearch(classifyBean, map);
                }
            }

            if (questions == null || questions.size() == 0) {
                // 题库已空
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

        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * 检索听力试题
     * 
     * @return
     * @throws IOException 
     */
    private List<MediaModel> selectForMediaQuestions() throws IOException {
        // 获取ClassifyId
        List<Integer> classifyIds = mediaService.getClssifyId(classifyBean);
        if (classifyIds == null || classifyIds.size() == 0) {
            return null;
        }

        // 获取音频
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userInfo.getId());
        for (Integer classifyId : classifyIds) {
            map.put("classifyId", classifyId);
            mediaModel = mediaService.getMedia(map);
            if (mediaModel == null) {
                continue;
            } else {
            	mediaModel.setMediaPath(CmnStringUtils.getMedia(mediaModel.getMediaPath()));
                break;
            }
        }
        if (mediaModel == null) {
            return null;
        }
        // 获取大题目
        structures = mediaService.selectForStructures((Integer) map.get("classifyId"));

        // 获取听力试题
        return mediaService.getMediaQuestions(mediaModel.getId());
    }

    /**
     * 听力提交
     * 
     * @return
     */
    public String submitMedia() {
        // 中途退出保存（暂不支持）TODO

        // 更新听力记录表
        List<TbMediaCollectionBean> list = new ArrayList<TbMediaCollectionBean>();
        for (MediaModel model : mediaQuestions) {

            TbMediaCollectionBean collection = new TbMediaCollectionBean();
            // 音频ID
            collection.setMediaId(model.getMediaId());
            // 用户ID
            collection.setUserId(userInfo.getId());
            // 试题ID
            collection.setQuestionId(model.getNo());
            // 状态1：已完成
            collection.setStatus("1");
            // 答案
            collection.setMyAnswer(model.getMyAnswer());
            list.add(collection);
        }
        mediaService.insertMediaCollections(list);

        // 去结果一览画面
        ExamResultBean examResultBean = (ExamResultBean) SpringAppContextManager.getBean("examResultBean");
        return examResultBean.mediaReport();
    }

    /**
     * 听力退出
     * 
     * @return
     */
    public String doMediaExit() {

        // 返回试题一览画面
        ExamClassifyBean examClassifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
        return examClassifyBean.init();
    }

    /**
     * 考试模式
     * 
     * @return
     */
    public String examSearch() {
        try {
            // 初始化变量
            title = "";
            subjectList = new ArrayList<String>();
            subject = "";
            graphicImage = "";
            if (!"ing".equals(status)) {
                status = "";
            }
            if (startTime == null) {
                startTime = new Date();
            }
            if (userInfo == null) {
                userInfo = WebUtils.getLoginUserInfo();
            }
            // 检索用Map
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userInfo.getId());
            // J.TEST，JLPT区分，order By控制
            map.put("exam", classifyBean.getExam());

            // 检测是否有中途退出的试题存在
            if (year == null) {
                year = examDropoutService.getYear(getExamDropoutBean());
            }
            // 获得试题结构(一次考试只获取一次)
            if (safeList == null || safeList.size() == 0) {
                List<ExamModel> examStructure = examService.getStructure(classifyBean);
                safeList.addAll(examStructure);
            }
            for (ExamModel model : safeList) {
                map.put("structureId", model.getStructureId());

                // TODO 添加年限选择后废弃
                if (year == null) {
                    // 获取题库中最新一年的试题year
                    year = examService.getYear(map);
                    if (year == null) {
                        safeList.remove(model);
                        continue;
                    }
                }
                // TODO 添加年限选择后废弃
                map.put("year", year);
                questions = examService.getTestQuestion(map);

                // 当前大题已做完
                if (questions == null || questions.size() == 0) {
                    safeList.remove(model);
                    continue;
                }

                Integer fatherId = questions.get(0).getFatherId();
                if (fatherId != null) {
                    // 特殊试题检索
                    questions.clear();
                    questions = examService.selectQuestionByFatherId(fatherId);
                    subject = questions.get(0).getSubject();
                    graphicImage = CmnStringUtils.getGraphicImage(questions.get(0).getImg());
                }
                // 画面序号 折行
                prepareData(subject);
                title = model.getTitle();

                return SystemConstants.PAGE_ITBK_EXAM_002;
            }
            if ("ing".equals(status)) {
                status = "end";
                // 删除中途退出表 TODO
                examDropoutService.deleteExamDropout(getExamDropoutBean());
                doclear();
                // 考试完成，显示成绩
                ExamResultBean examResultBean = (ExamResultBean) SpringAppContextManager.getBean("examResultBean");
                return examResultBean.examReport();
            } else {
                logger.log(MessageId.ITBK_I_0015);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0015);
                throw ex;
            }

        } catch (CmnBizException ex) {
            processForException(logger, ex);
            ExamClassifyBean classifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
            return classifyBean.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * 画面序号,折行
     * 
     * @param subject 题干
     */
    private void prepareData(String subject) {
        // 画面序号和显示设置
        questions = CmnStringUtils.answerLayoutSet(questions);
        // 折行
        subjectList = CmnStringUtils.getSubjectList(subject);
    }

    /**
     * [确认]按钮点下
     * 
     * @return
     */
    public String doSubmit() {
        try {
            if (!checkuser()) {
                return SystemConstants.PAGE_ITBK_USER_002;
            }
            // 批量登录数据用
            List<TbCollectionBean> collections = new ArrayList<TbCollectionBean>();
            List<ExamModel> examCollections = new ArrayList<ExamModel>();
            for (int i = 0; i < questions.size(); i++) {

                ExamModel examModel = (ExamModel) questions.get(i);
                examModel.setUserId(userInfo.getId());
                TbCollectionBean collection = collectionService.selectCollectionForOne(examModel);

                // 用户ID
                collection.setId(userInfo.getId());

                // 试题ID
                collection.setQuestionId(Integer.valueOf(examModel.getNo()));

                // 第几次做
                short count = collection.getCount() == null ? 0 : collection.getCount();
                count = (short) (count + 1);
                collection.setCount(count);

                // resultX
                String param = "setResult" + count;

                Method method = collection.getClass().getMethod(param, new Class[] { String.class });
                String choice = StringUtils.isEmpty(examModel.getMyAnswer()) ? "" : examModel.getMyAnswer();
                method.invoke(collection, new Object[] { choice });
                if (examModel.getAnswer().equals(choice)) {
                    collection.setFinish("1");
                } else {
                    collection.setFinish("0");
                }
                // 错题表登录·更新
                if (count == 1) {
                    collections.add(collection);
                } else {
                    collectionService.updateCollection(collection);
                }
                // 考试记录表登录
                if ("ing".equals(status) || "exist".equals(status)) {
                    examCollections.add(examModel);
                }
            }
            // 批量登录做题记录表
            if (collections.size() != 0) {
                collectionService.insertCollections(collections);
            }
            // 批量登录考试做题记录表
            if (examCollections.size() != 0) {
                examCollectionService.insertExamCollection(examCollections);
            }

            if ("ing".equals(status) || "exist".equals(status)) {
                // 考试继续
                return null;
            }
            // 跳转至[结果一览]画面
            ExamResultBean examResultBean = (ExamResultBean) SpringAppContextManager.getBean("examResultBean");
            examResultBean.setQuestions(questions);
            examResultBean.setClassifyBean(classifyBean);
            examResultBean.setTitle(title);
            examResultBean.setSubject(subject);
            examResultBean.setSubjectList(subjectList);
            examResultBean.setGraphicImage(graphicImage);
            return examResultBean.init();
        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * [我的错题]按下
     * 
     * @return
     */
    public String doResume() {
        ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
        return resumeBean.init();
    }

    /**
     * [下一题]按下
     * 
     * @return
     */
    public String doNext() {
        try {
            // 考试进行中
            status = "ing";
            doSubmit();

            // 继续下一道题
            return examSearch();
        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * [直接退出]按下
     * 
     * @return
     */
    public String doExist() {
        // 清空本次做题记录
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        String source = request.getParameter("source");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", source);
        map.put("startTime", startTime);
        examService.deleteExamCollectionBySource(map);
        doclear();
        // 跳转至试题选择
        ExamClassifyBean examClassifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
        return examClassifyBean.init();
    }

    /**
     * [保存退出]按下
     * 
     * @return
     */
    public String doSaveAndExist() {
        status = "exist";
        doSubmit();
        saveDropoutInfo();
        doclear();
        // 跳转至成绩一览画面
        ExamResultBean examResultBean = (ExamResultBean) SpringAppContextManager.getBean("examResultBean");
        return examResultBean.examReport();
    }

    /**
     * 数据清除
     */
    private void doclear() {
        status = null;
        year = null;
        safeList.clear();
        startTime = null;
        questions.clear();
    }

    /**
     * 中途退出信息保存（添加year选择后废弃）TODO
     */
    private void saveDropoutInfo() {
        examDropoutService.insertExamDropout(getExamDropoutBean());
    }

    /**
     * ExamDropOutBean
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

    private boolean checkuser() {
        if (userInfo == null) {
            logger.log(MessageId.COMMON_E_0009);
            CmnBizException ex = new CmnBizException(MessageId.COMMON_E_0009);
            processForException(logger, ex);
            return false;
        }
        return true;
    }

    public ExamService getExamService() {
        return examService;
    }

    public void setExamService(ExamService examService) {
        this.examService = examService;
    }

    public CollectionService getCollectionService() {
        return collectionService;
    }

    public void setCollectionService(CollectionService collectionService) {
        this.collectionService = collectionService;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<String> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        this.subjectList = subjectList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGraphicImage() {
        return graphicImage;
    }

    public void setGraphicImage(String graphicImage) {
        this.graphicImage = graphicImage;
    }

    public CopyOnWriteArrayList<ExamModel> getSafeList() {
        return safeList;
    }

    public void setSafeList(CopyOnWriteArrayList<ExamModel> safeList) {
        this.safeList = safeList;
    }

    public MediaModel getMediaModel() {
        return mediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        this.mediaModel = mediaModel;
    }

    public List<TbQuestionStructureBean> getStructures() {
        return structures;
    }

    public void setStructures(List<TbQuestionStructureBean> structures) {
        this.structures = structures;
    }

    public List<MediaModel> getMediaQuestions() {
        return mediaQuestions;
    }

    public void setMediaQuestions(List<MediaModel> mediaQuestions) {
        this.mediaQuestions = mediaQuestions;
    }

}
