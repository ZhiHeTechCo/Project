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

    // 提问人
    private String askers;

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
            userInfo = WebUtils.getLoginUserInfo();
            if (!checkuser(userInfo)) {
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0018), MESSAGE_LEVEL_INFO);
            }

            forumModels = forumService.selectForumForAll();

        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
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
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                    .getRequest();
            String questionId = request.getParameter("questionId");
            // 游客身份
            int id = 0;
            if (!checkuser(userInfo)) {
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0018), MESSAGE_LEVEL_INFO);
            } else {
                id = userInfo.getId();
            }
            if (StringUtils.isNotEmpty(questionId)) {
                Integer no = Integer.parseInt(questionId);
                forumModel = forumService.selectForumByQuestionId(no);
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
                        methodSetResponse.invoke(forumModel, forumModel.getMyResponse());
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
                forumModel = forumService.selectForumByQuestionId(no);
                setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0019), MESSAGE_LEVEL_INFO);
            }
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

}
