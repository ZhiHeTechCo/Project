package zh.co.item.bank.web.exam.controller;

import java.lang.reflect.Method;
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
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbCollectionBean;
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
     * initial
     * 
     * @return
     */
    public String init() {
        try {
            userInfo = (UserModel) WebUtils.getLoginUserInfo();
            if (!checkuser()) {
                // 跳转至登录画面
                return SystemConstants.PAGE_ITBK_USER_002;
            }

            pushPathHistory("resumeBean");
            title = "";
            subjectList = new ArrayList<String>();
            subject = "";
            graphicImage = "";
            // 检索符合记忆曲线的错题，取第一件
            Integer userId = userInfo.getId();
            questions = resumeService.selectQuestionForError(userId);

            // 未检索到符合记忆曲线的题，则检索该用户所有错题，取第一件
            if (questions.size() == 0) {
                questions = resumeService.selectQuestionForErrorAll(userId);
                // 当前用户没有错题，则报错
                if (questions.size() == 0) {
                    tableShow = null;
                    logger.log(MessageId.ITBK_I_0006);
                    throw new CmnBizException(MessageId.ITBK_I_0006);

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

        return SystemConstants.PAGE_ITBK_EXAM_005;
    }

    /**
     * 判断fatherId生成试题
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
     * [确认]按钮点下
     * 
     * @return
     */
    public String doResumeSubmit() {
        try {
            if (!checkuser()) {
                return SystemConstants.PAGE_ITBK_USER_002;
            }

            for (int i = 0; i < questions.size(); i++) {
                // 取当前用户的错题记录
                ExamModel examModel = (ExamModel) questions.get(i);
                examModel.setUserId(userInfo.getId());

                TbCollectionBean collection = collectionService.selectCollectionForOne(examModel);
                if (collection == null) {
                    logger.log(MessageId.COMMON_E_0005);
                    Exception ex = new Exception(MessageId.COMMON_E_0005);
                    throw ex;
                }

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
                // finish
                boolean flag = examModel.getAnswer().equals(examModel.getMyAnswer());
                String finish = collection.getFinish();
                if ("1".equals(finish)) {
                    finish = flag ? "9" : "0";
                } else if ("0".equals(finish)) {
                    finish = flag ? "2" : "0";
                } else if ("2".equals(finish)) {
                    finish = flag ? "3" : "0";
                } else if ("3".equals(finish)) {
                    finish = flag ? "1" : "2";
                }

                collection.setFinish(finish);

                // 更新做题记录
                collectionService.updateCollection(collection);
            }

            ExamResultBean examResultBean = (ExamResultBean) SpringAppContextManager.getBean("examResultBean");
            examResultBean.setQuestions(questions);
            examResultBean.setSubject(subject);
            examResultBean.setSubjectList(subjectList);
            examResultBean.setGraphicImage(graphicImage);
            examResultBean.setResume(true);
            return examResultBean.init();

        } catch (Exception e) {
            processForException(logger, e);
            // 留在本画面
            tableShow = null;
        }
        return SystemConstants.PAGE_ITBK_EXAM_005;
    }

    /**
     * [去做题]按钮按下
     * 
     * @return
     */
    public String toClassify() {
        ExamClassifyBean examClassifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
        return examClassifyBean.init();
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
