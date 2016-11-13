package zh.co.item.bank.web.user.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.WebUtils;
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
    private String oldPassword;
    
    /**新密码*/
    private String newPassword;

    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_004;
    }
    
    /**
     * 初期化
     * @return
     */
    public String init() {
    	pushPathHistory("changePasswordBean");

        return SystemConstants.PAGE_ITBK_USER_004;
    }
    
    /**
     * 变更密码
     * @return
     */
    public String changePassword() {
        try {
        	userService.changePassword(WebUtils.getLoginUserInfo(), oldPassword, newPassword);
        	setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0004), "I");
        } catch (Exception e) {
        	processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_USER_004;
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

}
