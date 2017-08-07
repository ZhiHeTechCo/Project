package zh.co.item.bank.web.forum.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbForumResponseBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ForumModel;
import zh.co.item.bank.model.entity.ForumResponseModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.forum.service.ForumService;

@Named("forumResponseController")
@Scope("session")
public class ForumResponseController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ForumService forumService;

    /** 画面显示变量 */
    private ForumModel forumModel;

    /** 回答·评论 */
    private String myResponse;

    /** --共通变量-- */
    // 当前用户信息
    private UserModel userInfo;

    private Integer currentQuestionId;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_FORUM_002;
    }

    /**
     * 1.画面初始化
     */
    public String init() {
        try {
            pushPathHistory("forumResponseController");

            forumModel = new ForumModel();
            myResponse = null;
            currentQuestionId = null;
            String questionId = WebUtils.getRequestParam("id");

            if (StringUtils.isNotEmpty(questionId)) {
                // a.取当前试题内容
                currentQuestionId = Integer.parseInt(questionId);
                ExamModel question = forumService.selectQuestionByQuestionId(currentQuestionId);
                forumModel.setQuestion(question);
                // 选项格式设置
                question = CmnStringUtils.selectionLayoutSet(question);
                // 大题目和图片
                forumModel.setSubjectList(CmnStringUtils.getSubjectList((question.getSubject())));
                forumModel.setGraphicImage(CmnStringUtils.getGraphicImage(question.getImg()));
                // b.获取提问用户
                forumModel.setAskers(forumService.selectAllAsker(currentQuestionId));

                // c.取回答
                refreshPage();
            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 2.回答问题
     * 
     * @return
     */
    public String responseQuestion() {
        try {
            TbForumResponseBean newResponse = new TbForumResponseBean();
            // 回答·评论
            newResponse.setResponse(myResponse);
            // 用户信息不存在则使用游客身份
            userInfo = WebUtils.getLoginUserInfo();
            int id = userInfo != null ? userInfo.getId() : 0;
            newResponse.setResponser(id);
            // 试题ID
            newResponse.setQuestionId(currentQuestionId);
            forumService.insertResponse(newResponse);

            refreshPage();
            myResponse = null;
            setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0019), MESSAGE_LEVEL_INFO);

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 点赞
     * 
     * @return
     */
    public void doUp() {
        try {
            String id = WebUtils.getRequestParam("id");
            if (StringUtils.isNotEmpty(id)) {
                forumService.doUp(Integer.parseInt(id));
                refreshPage();
            }
        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 返回论坛一览
     * 
     * @return
     */
    public String goBackToForum() {
    	ForumController forumController = (ForumController) SpringAppContextManager.getBean("forumController");
        return forumController.init();
    }

    /**
     * 重置页面信息
     */
    private void refreshPage() {
        List<ForumResponseModel> responses = forumService.selectResponseByQuestionId(currentQuestionId);
        // 换行符显示
        CmnStringUtils.setResponseList(responses);
        forumModel.setResponses(responses);
    }

    // /**
    // * 管理员选择正确答案
    // *
    // * @return
    // */
    // public String selectAsAnswer() {
    // try {
    // String item = WebUtils.getRequestParam("item");
    // if (StringUtils.isNotEmpty(item)) {
    // Map<String, Object> param = new HashMap<String, Object>();
    // param.put("systemChoose", item);
    // param.put("questionId", forumModel.getQuestionId());
    // if (Integer.parseInt(userInfo.getRole()) >= 90) {
    // forumService.updateSystemChoose(param);
    // forumModel.setSystemChoose(item);
    // // 重置页面信息
    // forumModel.setResponses(forumService.selectResponseByQuestionId(Integer.parseInt(id)));
    // }
    // }
    // } catch (Exception e) {
    // processForException(logger, e);
    // }
    // return SystemConstants.PAGE_ITBK_FORUM_002;
    // }
    //
    // /**
    // * 管理员选择取消选择正确答案
    // *
    // * @return
    // */
    // public String moveAsAnswer() {
    // try {
    // if (checkuser(userInfo)) {
    // Map<String, Object> param = new HashMap<String, Object>();
    // param.put("systemChoose", null);
    // param.put("questionId", forumModel.getQuestionId());
    // if (Integer.parseInt(userInfo.getRole()) >= 90) {
    // forumService.updateSystemChoose(param);
    // forumModel.setSystemChoose(null);
    // }
    // }
    // } catch (Exception e) {
    // processForException(logger, e);
    // }
    // return SystemConstants.PAGE_ITBK_FORUM_002;
    // }

    public ForumModel getForumModel() {
        return forumModel;
    }

    public void setForumModel(ForumModel forumModel) {
        this.forumModel = forumModel;
    }

    public String getMyResponse() {
        return myResponse;
    }

    public void setMyResponse(String myResponse) {
        this.myResponse = myResponse;
    }
}
