package zh.co.item.bank.web.user.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] 登录SignInBean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("signInBean")
@Scope("session")
public class SignInBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
	
    @Inject
    private UserService userService;
    
    private TuUserBean userInfo;
    
    private Object fromPageId;

    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_002;
    }
    
    /**
     * 登录画面initial
     * @return
     */
    public String init() {
    	fromPageId = WebUtils.getSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_ID);
    	pushPathHistory("signInBean");
    	userInfo = new TuUserBean();
    	return SystemConstants.PAGE_ITBK_USER_002;
    	
    }

    /**
     * 登录
     */
    public String login() throws Exception {
        try {
        	UserModel loginUserInfo = userService.login(userInfo);
        	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_INFO, loginUserInfo);
        	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_ID, String.valueOf(loginUserInfo.getId()));
    		logger.log(MessageId.ITBK_I_0003);
            //登录成功，跳转到首页
            if(fromPageId == null) {
        		return SystemConstants.PAGE_ITBK_COM_001;
        	} else {
        		String pageId = (String)fromPageId;
        		String controllerName = pageAndControllerMap.get(pageId);
        		if(StringUtils.isNotBlank(controllerName)) {
        			BaseController pageController = (BaseController) SpringAppContextManager.getBean(controllerName);
                    return pageController.init();
        		} else {
        			return SystemConstants.PAGE_ITBK_COM_001;
        		}
        	}

        } catch (Exception e) {
        	processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_USER_002;
    }

	public void setUserInfo(TuUserBean userInfo) {
		this.userInfo = userInfo;
	}

	public TuUserBean getUserInfo() {
		return userInfo;
	}


}
