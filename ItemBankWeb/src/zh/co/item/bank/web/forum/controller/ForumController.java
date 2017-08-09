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
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbTopicListBean;
import zh.co.item.bank.model.entity.ForumListModel;
import zh.co.item.bank.model.entity.PaginatorLogger;
import zh.co.item.bank.model.entity.RepeatPaginator;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.forum.service.ForumService;

/**
 * 交流区画面
 * 
 * @author gaoya
 *
 */
@Named("forumController")
@Scope("session")
public class ForumController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ForumService forumService;

    // 分页
    private RepeatPaginator paginator;

    private PaginatorLogger paginatorLogger = new PaginatorLogger(logger, SystemConstants.PAGE_ITBK_USER_007, "向前翻页",
            "向后翻页", "指定页");

    /** --共通变量-- */
    // 当前用户信息
    private UserModel userInfo;

    /** 一览画面显示用 */
    private List<ForumListModel> forumModels;

    private String justShowMine;

    /** 发表话题用 */
    private TbTopicListBean tbTopicListBean;

    /** 关键字 */
    private String searchKey;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_FORUM_001;
    }

    /**
     * 1.画面初始化
     * 
     * @return
     */
    public String init() {
        try {

            pushPathHistory("forumController");
            searchKey = "";

            if ("true".equals(justShowMine)) {
                return showAllMyQuestion();
            }

            userInfo = WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0018), MESSAGE_LEVEL_INFO);
            }

            forumModels = forumService.selectForumForAll();
            // 分页
            paginator = new RepeatPaginator(forumModels, paginatorLogger, null);
            justShowMine = "false";
            tbTopicListBean = new TbTopicListBean();

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 2.显示全部问题
     * 
     * @return
     */
    public String showAllQuestion() {
        justShowMine = "false";
        return init();
    }

    /**
     * 3.查看当前用户提问
     * 
     * @return
     */
    public String showAllMyQuestion() {
        try {
            // session过期，去用户登录
            if (!checkuser(userInfo)) {
                return SystemConstants.PAGE_ITBK_USER_002;
            }

            justShowMine = "true";
            forumModels = forumService.selectForumByAsker(userInfo.getId());
            paginator = new RepeatPaginator(forumModels, paginatorLogger, null);

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 4.显示问题详细
     * 
     * @return
     */
    public String showDetail() {
        try {

            String mode = WebUtils.getRequestParam("mode");
            // mode=1,考题类
            if ("1".equals(mode)) {
                ForumResponseController forumResponseController = (ForumResponseController) SpringAppContextManager
                        .getBean("forumResponseController");
                return forumResponseController.init();
            } else if ("2".equals(mode)) {
                TopicCommentController topicCommentController = (TopicCommentController) SpringAppContextManager
                        .getBean("topicCommentController");
                topicCommentController.setTopicId(Integer.parseInt(WebUtils.getRequestParam("id")));
                return topicCommentController.init();
            }
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 5.发表一条话题
     * 
     * @return
     */
    public String sendTopic() {
        try {

            // a.登录话题并获取ID
            Integer topicId = forumService.insertTopic(tbTopicListBean, userInfo);

            // b.初期化model
            tbTopicListBean = new TbTopicListBean();

            // c.跳转至话题评论页
            TopicCommentController topicCommentController = (TopicCommentController) SpringAppContextManager
                    .getBean("topicCommentController");
            topicCommentController.setTopicId(topicId);
            return topicCommentController.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 6.关键字检索
     * 
     * @return
     */
    public String search() {
        try {
            if (StringUtils.isNotEmpty(searchKey)) {
                forumModels = forumService.selectForumBySearcyKey(searchKey);
                // 检索结果0件
                if(forumModels.size()==0){
                    setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0016), MESSAGE_LEVEL_INFO);
                }
                // 分页
                paginator = new RepeatPaginator(forumModels, paginatorLogger, null);
                justShowMine = "false";
                tbTopicListBean = new TbTopicListBean();
            }

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    public RepeatPaginator getPaginator() {
        return paginator;
    }

    public void setPaginator(RepeatPaginator paginator) {
        this.paginator = paginator;
    }

    public List<ForumListModel> getForumModels() {
        return forumModels;
    }

    public void setForumModels(List<ForumListModel> forumModels) {
        this.forumModels = forumModels;
    }

    public String getJustShowMine() {
        return justShowMine;
    }

    public void setJustShowMine(String justShowMine) {
        this.justShowMine = justShowMine;
    }

    public TbTopicListBean getTbTopicListBean() {
        return tbTopicListBean;
    }

    public void setTbTopicListBean(TbTopicListBean tbTopicListBean) {
        this.tbTopicListBean = tbTopicListBean;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

}
