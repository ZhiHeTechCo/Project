package zh.co.item.bank.web.forum.controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

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
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.ForumModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.forum.service.ForumService;
import zh.co.item.bank.web.user.service.UserService;

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

    @Inject
    private UserService userService;

    /** --共通变量-- */
    // 当前用户信息
    private UserModel userInfo;

    // 用户昵称
    private Map<Integer, String> userName;

    /** 一览画面显示用 */
    private List<ForumModel> forumModels;

    /** 一览画面显示用 */
    private ForumModel forumModel;

    // 我的回答
    private String myResponse;

    // 提问人
    private String askers;

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
            if ("true".equals(justShowMine)) {
                return showAllMyQuestion();
            }
            pushPathHistory("forumController");
            userInfo = WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0018), MESSAGE_LEVEL_INFO);
            }

            forumModels = forumService.selectForumForAll();
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

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 显示当前提问
     * 
     * @return
     */
    public String showQuestionDetail() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            String questionId = request.getParameter("questionId");
            if (StringUtils.isNotEmpty(questionId)) {
                Integer id = Integer.parseInt(questionId);
                forumModel = forumService.selectForumByQuestionId(id);
                // 选项格式设置
                forumModel = CmnStringUtils.selectionLayoutSet(forumModel);
                // 获取提问用户
                searchAsker(id);
                Map<Integer, String> userName = userService.selectUserForNickName();
                userName.put(0, "游侠");
                forumModel.setUserName(userName);
            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_FORUM_002;
    }

    /**
     * 回答问题
     * 
     * @return
     */
    public String responseQuestion() {
        try {
            // 用户信息不存在则使用游客身份
            int id = userInfo != null ? userInfo.getId() : 0;

            // 设置ResponserX和ResponseX的值
            for (int i = 1; i <= 10; i++) {
                String param = "getResponse" + i;
                Method methodGet = forumModel.getClass().getMethod(param);
                String setResponse = "setResponse" + i;
                String setResponser = "setResponser" + i;
                if (methodGet.invoke(forumModel) == null) {
                    forumModel.setCount((short) i);
                    // 待回答→待确认
                    forumModel.setStatus("1");
                    Method methodSetResponse = forumModel.getClass().getMethod(setResponse, String.class);
                    methodSetResponse.invoke(forumModel, myResponse);
                    Method methodSetResponser = forumModel.getClass().getMethod(setResponser, Integer.class);
                    methodSetResponser.invoke(forumModel, id);
                    if (i == 10) {
                        // 待审核
                        forumModel.setStatus("3");
                    }
                    break;
                }
            }

            forumService.updateForumResponse(forumModel);
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
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            String questionId = request.getParameter("questionId");
            String upResponse = request.getParameter("upResponse");
            if (StringUtils.isNotEmpty(questionId) && StringUtils.isNotEmpty(upResponse)) {
                Integer no = Integer.parseInt(questionId);
                // countX
                String param = "setCount" + upResponse;
                TbForumResponseBean bean = new TbForumResponseBean();
                Method method = bean.getClass().getMethod(param, Integer.class);
                method.invoke(bean, -1);
                bean.setQuestionId(no);

                forumService.doUp(bean);
                forumModel = forumService.selectForumByQuestionId(no);
            }
        } catch (Exception e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_FORUM_002;
    }

    /**
     * 获取当前问题所有提问者昵称
     * 
     * @param questionId
     */
    private void searchAsker(Integer questionId) {
        List<TuUserBean> nickNames = forumService.selectAllAsker(questionId);
        for (TuUserBean user : nickNames) {
            if (user != null) {
                // 画面上显示所有提问者，用;隔开
                askers = askers != null ? user.getNickName() : askers + SystemConstants.SEMIKOMA + user.getNickName();
            }
        }
    }

    public Map<Integer, String> getUserName() {
        return userName;
    }

    public void setUserName(Map<Integer, String> userName) {
        this.userName = userName;
    }

    public List<ForumModel> getForumModels() {
        return forumModels;
    }

    public void setForumModels(List<ForumModel> forumModels) {
        this.forumModels = forumModels;
    }

    public ForumModel getForumModel() {
        return forumModel;
    }

    public void setForumModel(ForumModel forumModel) {
        this.forumModel = forumModel;
    }

    public String getAskers() {
        return askers;
    }

    public void setAskers(String askers) {
        this.askers = askers;
    }

    public String getMyResponse() {
        return myResponse;
    }

    public void setMyResponse(String myResponse) {
        this.myResponse = myResponse;
    }

    public String getJustShowMine() {
        return justShowMine;
    }

    public void setJustShowMine(String justShowMine) {
        this.justShowMine = justShowMine;
    }

}
