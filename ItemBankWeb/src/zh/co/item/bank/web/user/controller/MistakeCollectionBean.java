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
import zh.co.item.bank.db.entity.VuUserModelBean;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] 错题集MistakeCollectionBean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("mistakeCollectionBean")
@Scope("session")
public class MistakeCollectionBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
    
    @Inject
    private UserService userService;
    
    private VuUserModelBean userInfo;

    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_005;
    }
    
    /**
     * 错题集信息
     * @return
     */
    public String init() {
    	pushPathHistory("mistakeCollectionBean");
    	userInfo = new VuUserModelBean();
        try {
        	//错题检索

        } catch (Exception e) {
        	processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_USER_005;
    }

    /**
     * 更新错题集
     * @return
     */
    public String update() {
    	return SystemConstants.PAGE_ITBK_USER_005;
    }

	public void setUserInfo(VuUserModelBean userInfo) {
		this.userInfo = userInfo;
	}

	public VuUserModelBean getUserInfo() {
		return userInfo;
	}


}
