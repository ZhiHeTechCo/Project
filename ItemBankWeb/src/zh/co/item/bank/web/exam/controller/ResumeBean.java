package zh.co.item.bank.web.exam.controller;

import java.lang.reflect.Method;
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
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.web.exam.service.CollectionService;
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

    private List<ExamModel> questions;

    private TuUserBean userInfo;

    private String tableShow;

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
            userInfo = (TuUserBean) WebUtils.getSessionAttribute(WebUtils.SESSION_USER_INFO);
            if (!checkuser()) {
                // 跳转至登录画面
                return SystemConstants.PAGE_ITBK_USER_002;
            }

            pushPathHistory("resumeBean");
            // 检索3天以上的错题
            Integer userId = userInfo.getId();
            questions = resumeService.selectQuestionForError(userId);

            if (questions.size() == 0) {
                // 检索所有错题
                questions = resumeService.selectQuestionForErrorAll(userId);
                if (questions.size() == 0) {
                    tableShow = null;
                    logger.log(MessageId.ITBK_I_0006);
                    throw new CmnBizException(MessageId.ITBK_I_0006);

                } else {
                    tableShow = MessageUtils.getMessage(MessageId.ITBK_I_0007);
                }

            } else {
                tableShow = MessageUtils.getMessage(MessageId.ITBK_I_0008);
            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_EXAM_005;
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

                TbCollectionBean collection = collectionService.selectCollectionForOne(examModel);

                // 用户ID
                collection.setId(userInfo.getId());

                // 试题ID
                collection.setQuestionId(Integer.valueOf(examModel.getNo()));

                // 第几次做
                short count = collection.getCount();
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
        return SystemConstants.PAGE_ITBK_EXAM_001;
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
}
