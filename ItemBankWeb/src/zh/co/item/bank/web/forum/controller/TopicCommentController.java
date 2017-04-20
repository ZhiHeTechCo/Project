package zh.co.item.bank.web.forum.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbTopicCommentBean;
import zh.co.item.bank.model.entity.TopicModel;
import zh.co.item.bank.web.forum.service.TopicCommentService;

/**
 * 话题详细·评论
 * 
 * @author gaoya
 *
 */
@Named("topicCommentController")
@Scope("session")
public class TopicCommentController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private TopicCommentService topicCommentService;

    private Integer topicId;

    // 画面显示Model
    private TopicModel topicModel;

    // 评论
    private TbTopicCommentBean commentBean;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_FORUM_003;
    }

    /**
     * 1.画面初期化
     */
    public String init() {
        try {
            pushPathHistory("topicCommentController");

            // a.画面初期化
            topicModel = topicCommentService.selectTopicById(topicId);
            commentBean = new TbTopicCommentBean();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 2.发表评论
     * 
     * @return
     */
    public void sendComment() {
        try {
            // 登录评论
            topicCommentService.sendComment(commentBean, WebUtils.getLoginUserInfo(), topicId);
            // 初始化
            topicModel = topicCommentService.selectTopicById(topicId);
            commentBean = new TbTopicCommentBean();

        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 3.点赞
     * 
     * @return
     */
    public void doUp() {
        try {
            String id = WebUtils.getRequestParam("id");
            if (StringUtils.isNotEmpty(id)) {
                topicCommentService.doUp(Integer.parseInt(id));
                refreshPage(topicId);
            }
        } catch (Exception e) {
            processForException(logger, e);
        }
    }

    /**
     * 4.[返回]按钮按下
     */
    public String goBack() {
        ForumController forumController = (ForumController) SpringAppContextManager.getBean("forumController");
        return forumController.init();
    }

    /**
     * 重置页面信息
     */
    private void refreshPage(Integer id) {
        topicModel.setTopicComments(topicCommentService.selectCommetsById(id));
    }

    public TopicModel getTopicModel() {
        return topicModel;
    }

    public void setTopicModel(TopicModel topicModel) {
        this.topicModel = topicModel;
    }

    public TbTopicCommentBean getCommentBean() {
        return commentBean;
    }

    public void setCommentBean(TbTopicCommentBean commentBean) {
        this.commentBean = commentBean;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

}
