package zh.co.item.bank.web.user.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] 变更密码ChangePasswordBean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("changePasswordBean")
@Scope("session")
public class ChangePasswordBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
    
    @Inject
    private UserService userService;
    
    /**旧密码*/
    private String name;
    
    /**旧密码*/
    private String oldPassword;
    
    /**新密码*/
    private String newPassword;

    /**是否设置密码flag*/
    private String setFlag;
    
    private Object fromPageId;
    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_004;
    }
    
    /**
     * 初期化
     * @return
     */
    public String init() {
    	fromPageId = WebUtils.getSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_ID);
    	
    	pushPathHistory("changePasswordBean");
    	if(StringUtils.isEmpty(WebUtils.getLoginUserInfo().getPassword())) {
    		//微信绑定PC登录账号
    		this.setFlag = SystemConstants.FLAG_NO;
    	} else {
    		//修改密码
    		this.setFlag = SystemConstants.FLAG_YES;
    	}
    	
    	//session过期
    	if(WebUtils.getLoginUserInfo() == null) {
    		BaseController pageController = (BaseController) SpringAppContextManager.getBean("signInBean");
            return pageController.init();
    	}
        return SystemConstants.PAGE_ITBK_USER_004;
    }
    
    /**
     * 变更密码
     * @return
     */
    public String changePassword() {
        try {
        	//session过期
        	if(WebUtils.getLoginUserInfo() == null) {
        		BaseController pageController = (BaseController) SpringAppContextManager.getBean("signInBean");
                return pageController.init();
        	}
        	////微信绑定PC登录账号的场合
        	if(SystemConstants.FLAG_NO.equals(setFlag)) {
        		if(StringUtils.isEmpty(name)) {
            		throw new CmnBizException(MessageId.ITBK_E_0007, new Object[]{"用户名"});
            	}
        	}
        	if(StringUtils.isEmpty(newPassword)) {
        		throw new CmnBizException(MessageId.ITBK_E_0007, new Object[]{"密码"});
        	}
        	if(newPassword.length() < 6 ||(!newPassword.matches("[A-Za-z0-9_]+"))) {
        		
        		throw new CmnBizException(MessageId.ITBK_E_0007, new Object[]{"6位以上字母、数字或下划线组成的密码"});
        	}
        	if(SystemConstants.FLAG_YES.equals(setFlag)) {
        		userService.changePassword(WebUtils.getLoginUserInfo(), oldPassword, newPassword);
        	} else {
        		UserModel userInfo = WebUtils.getLoginUserInfo();
        		userInfo.setName(name);
        		userInfo.setPassword(newPassword);
        		userService.bindUserInfo(userInfo);
        	}
        	
        	if(StringUtils.isEmpty(WebUtils.getLoginUserInfo().getPassword())) {
        		//微信绑定PC登录账号
        		this.setFlag = SystemConstants.FLAG_NO;
        	} else {
        		//修改密码
        		this.setFlag = SystemConstants.FLAG_YES;
        	}
        	newPassword = "";
        	setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0004), "I");
        } catch (Exception e) {
        	processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_USER_004;
    }
    
    /**
     * 返回
     * @return
     */
    public String goBack() {
        try {
        	//session过期
        	if(WebUtils.getLoginUserInfo() == null) {
        		BaseController pageController = (BaseController) SpringAppContextManager.getBean("signInBean");
                return pageController.init();
        	}
        	if(fromPageId != null) {
        		String pageId = (String)fromPageId;
        		String controllerName = pageAndControllerMap.get(pageId);
        		if(!StringUtils.isEmpty(controllerName)) {
        			BaseController pageController = (BaseController) SpringAppContextManager.getBean(controllerName);
                    return pageController.init();
        		} else {
        			return SystemConstants.PAGE_ITBK_COM_001;
        		}
        	}
        } catch (Exception e) {
        	processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_COM_001;
    }


	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getSetFlag() {
		return setFlag;
	}

	public void setSetFlag(String setFlag) {
		this.setFlag = setFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
