package zh.co.item.bank.web.forum.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.ForumModel;
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

    //分页
    private RepeatPaginator paginator; 

    private PaginatorLogger paginatorLogger = new PaginatorLogger(logger, SystemConstants.PAGE_ITBK_USER_007, "向前翻页","向后翻页", "指定页"); 

    /** --共通变量-- */
    // 当前用户信息
    private UserModel userInfo;

    /** 一览画面显示用 */
    private List<ForumModel> forumModels;

    private String justShowMine;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_FORUM_001;
    }

    /**
     * initial
     * 
     * @return
     */
    public String init() {
        try {
        	
        	pushPathHistory("forumController");
        	
            if ("true".equals(justShowMine)) {
                return showAllMyQuestion();
            }
            
            userInfo = WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0018), MESSAGE_LEVEL_INFO);
            }

            forumModels = forumService.selectForumForAll();
            paginator = new RepeatPaginator(forumModels, paginatorLogger);
            justShowMine = "false";

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 显示全部问题
     * 
     * @return
     */
    public String showAllQuestion() {
        justShowMine = "false";
        return init();
    }

    /**
     * 查看当前用户提问
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
            paginator = new RepeatPaginator(forumModels, paginatorLogger);

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

    public List<ForumModel> getForumModels() {
        return forumModels;
    }

    public void setForumModels(List<ForumModel> forumModels) {
        this.forumModels = forumModels;
    }

    public String getJustShowMine() {
        return justShowMine;
    }

    public void setJustShowMine(String justShowMine) {
        this.justShowMine = justShowMine;
    }

}
