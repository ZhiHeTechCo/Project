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

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_FORUM_002;
    }

    public String init() {
        try {
            pushPathHistory("forumResponseController");

            String questionId = WebUtils.getRequestParam("questionId");

            if (StringUtils.isNotEmpty(questionId)) {
                // a.取当前试题内容
                Integer id = Integer.parseInt(questionId);
                ExamModel question = forumService.selectQuestionByQuestionId(id);
                forumModel.setQuestion(question);
                // 选项格式设置
                question = CmnStringUtils.selectionLayoutSet(question);
                // b.获取提问用户
                forumModel.setAskers(forumService.selectAllAsker(id));

                // c.取回答
                List<ForumResponseModel> responses = forumService.selectResponseByQuestionId(id);
                forumModel.setResponses(responses);

            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 回答问题
     * 
     * @return
     */
    public String responseQuestion() {
        try {
            TbForumResponseBean newResponse = new TbForumResponseBean();
            newResponse.setResponse(myResponse);
            // 用户信息不存在则使用游客身份
            int id = userInfo != null ? userInfo.getId() : 0;
            newResponse.setResponser(id);
            forumService.insertResponse(newResponse);

            // 重置页面信息
            forumModel.setResponses(forumService.selectResponseByQuestionId(id));
            myResponse = null;
            setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0019), MESSAGE_LEVEL_INFO);

        } catch (Exception e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_FORUM_002;
    }

    /**
     * 点赞
     * 
     * @return
     */
    public String doUp() {
        try {
            String id = WebUtils.getRequestParam("id");
            if (StringUtils.isNotEmpty(id)) {
                forumService.doUp(Integer.parseInt(id));
                // 重置页面信息
                forumModel.setResponses(forumService.selectResponseByQuestionId(Integer.parseInt(id)));
            }
        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

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

//    /**
//     * 管理员选择正确答案
//     *
//     * @return
//     */
//    public String selectAsAnswer() {
//        try {
//            String item = WebUtils.getRequestParam("item");
//            if (checkuser(userInfo) && StringUtils.isNotEmpty(item)) {
//                Map<String, Object> param = new HashMap<String, Object>();
//                param.put("systemChoose", item);
//                param.put("questionId", forumModel.getQuestionId());
//                if (Integer.parseInt(userInfo.getRole()) >= 90) {
//                    forumService.updateSystemChoose(param);
//                    forumModel.setSystemChoose(item);
//                }
//            }
//        } catch (Exception e) {
//            processForException(logger, e);
//        }
//        return SystemConstants.PAGE_ITBK_FORUM_002;
//    }
//
//    /**
//     * 管理员选择取消选择正确答案
//     *
//     * @return
//     */
//    public String moveAsAnswer() {
//        try {
//            if (checkuser(userInfo)) {
//                Map<String, Object> param = new HashMap<String, Object>();
//                param.put("systemChoose", null);
//                param.put("questionId", forumModel.getQuestionId());
//                if (Integer.parseInt(userInfo.getRole()) >= 90) {
//                    forumService.updateSystemChoose(param);
//                    forumModel.setSystemChoose(null);
//                }
//            }
//        } catch (Exception e) {
//            processForException(logger, e);
//        }
//        return SystemConstants.PAGE_ITBK_FORUM_002;
//    }

}
