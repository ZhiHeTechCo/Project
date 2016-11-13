package zh.co.item.bank.web.user.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TuUserBean;
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

    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_002;
    }
    
    /**
     * 登录画面initial
     * @return
     */
    public String init() {
    	pushPathHistory("signInBean");
    	userInfo = new TuUserBean();
        return SystemConstants.PAGE_ITBK_USER_002;
    }

    /**
     * 登录
     */
    public String login() throws Exception {
        try {
        	userInfo = userService.login(userInfo);
        	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_INFO, userInfo);
        	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_ID, String.valueOf(userInfo.getId()));
    		logger.log(MessageId.ITBK_I_0003);
            //登录成功，跳转到首页
 /*   		PageTopController pageController = (PageTopController) SpringAppContextManager.getBean("pageTopController");
            return pageController.init();*/
            return SystemConstants.PAGE_ITBK_COM_001;

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
