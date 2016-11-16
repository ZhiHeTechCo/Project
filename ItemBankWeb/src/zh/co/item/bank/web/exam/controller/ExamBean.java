package zh.co.item.bank.web.exam.controller;

import java.lang.reflect.Method;
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
import zh.co.common.utils.SpringAppContextManager;
import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.web.exam.service.CollectionService;
import zh.co.item.bank.web.exam.service.ExamService;

/**
 * 试题类型选择画面
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

    /** 试题 */
    private List<ExamModel> questions;

    /** 题型种别 */
    private TbQuestionClassifyBean classifyBean;

    /** 题目 */
    private String title;

    /** 大题干 */
    private String subject;

    /** 用户信息 */
    private TuUserBean userInfo;

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

            title = "";
            subject = "";
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

                questions = examService.classifySearch(classifyBean, map);
            }

            if (questions == null || questions.size() == 0) {
                // 题库已空
                logger.log(MessageId.ITBK_I_0010);
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0010);
                throw ex;
            }

            if (questions.get(0).getFatherId() != null) {
                // 特殊试题检索
                questions.clear();
                questions = examService.selectSpecialForOne(map);

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
                subject = questions.get(0).getSubject();
            }

            // 取题目
            title = examService.getTitle(questions.get(0).getStructureId());
            // 画面序号
            for (int i = 0; i < questions.size(); i++) {
                questions.get(i).setIndex(i + 1);
            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_EXAM_002;
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
            for (int i = 0; i < questions.size(); i++) {
                TbCollectionBean collection = new TbCollectionBean();

                ExamModel examModel = (ExamModel) questions.get(i);

                collection.setId(userInfo.getId());

                collection.setQuestionId(Integer.valueOf(examModel.getNo()));

                short count = collection.getCount() == null ? 0 : collection.getCount();
                count = (short) (count + 1);
                collection.setCount(count);

                String param = "setResult" + count;

                Method method = collection.getClass().getMethod(param, new Class[] { String.class });
                String choice = StringUtils.isEmpty(examModel.getMyAnswer()) ? "" : examModel.getMyAnswer();
                method.invoke(collection, new Object[] { choice });
                if (examModel.getAnswer().equals(choice)) {
                    collection.setFinish("1");
                }
                collectionService.insertCollection(collection);
            }

            // 跳转至[结果一览]画面
            ExamResultBean examResultBean = (ExamResultBean) SpringAppContextManager.getBean("examResultBean");
            examResultBean.setQuestions(questions);
            examResultBean.setClassifyBean(classifyBean);
            examResultBean.setTitle(title);
            return examResultBean.init();
        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_EXAM_002;
    }

    /**
     * [我的错题]按下
     * @return
     */
    public String doResume() {
        ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
        return resumeBean.init();
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

    public TuUserBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(TuUserBean userInfo) {
        this.userInfo = userInfo;
    }

}
