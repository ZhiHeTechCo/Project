package zh.co.item.bank.web.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.CmnContants;
import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] 注册SignUpBean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("signUpBean")
@Scope("session")
public class SignUpBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
	
    @Inject
    private UserService userService;

	private List<TsCodeBean> jlptLevels;

    private List<TsCodeBean> jtestLevels;
    
    private TuUserBean userInfo;
    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_001;
    }
    
    /**
     * 注册画面initial
     * @return
     */
    public String init() {
    	 try {
    		pushPathHistory("signUpBean");
	    	Map<String, Object> map = new HashMap<String, Object>();
	        map.put("modelId", CmnContants.MODELID_BQD0003);
	        map.put("name", "jlpt_level");
	        jlptLevels = userService.getCodelist(map);
	        
	        map.put("modelId", CmnContants.MODELID_BQD0003);
	        map.put("name", "jtest_level");
	        jtestLevels = userService.getCodelist(map);
	        
	        userInfo = new TuUserBean();
	    	userInfo.setName("用户名");
	    	userInfo.setPassword("密码");
    	 } catch (Throwable e) {
             processForException(logger, e);
         }
        return SystemConstants.PAGE_ITBK_USER_001;
    }

    /**
     * 注册
     */
    public String insertUserInfo() throws Exception {
        try {
        	if(StringUtils.isEmpty(userInfo.getName())) {
        		throw new CmnBizException(MessageId.ITBK_E_0007, new Object[]{"用户名"});
        	}
        	if(StringUtils.isEmpty(userInfo.getPassword())) {
        		throw new CmnBizException(MessageId.ITBK_E_0007, new Object[]{"密码"});
        	}
        	if(userInfo.getPassword().length() < 6 ||(!userInfo.getPassword().matches("[A-Za-z0-9_]+"))) {
        		
        		throw new CmnBizException(MessageId.ITBK_E_0007, new Object[]{"6位以上字母、数字或下划线组成的密码"});
        	}
            int count = userService.InsertUserInfo(userInfo);
            //注册成功，跳转到登录界面
            if(count > 0) {
            	return SystemConstants.PAGE_ITBK_USER_002;
            }

        } catch (Exception e) {
        	processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_USER_001;
    }
    
    public List<TsCodeBean> getJlptLevels() {
		return jlptLevels;
	}

	public void setJlptLevels(List<TsCodeBean> jlptLevels) {
		this.jlptLevels = jlptLevels;
	}

	public List<TsCodeBean> getJtestLevels() {
		return jtestLevels;
	}

	public void setJtestLevels(List<TsCodeBean> jtestLevels) {
		this.jtestLevels = jtestLevels;
	}

	public void setUserInfo(TuUserBean userInfo) {
		this.userInfo = userInfo;
	}

	public TuUserBean getUserInfo() {
		return userInfo;
	}

}
