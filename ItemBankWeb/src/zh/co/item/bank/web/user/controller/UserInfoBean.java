package zh.co.item.bank.web.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.CmnContants;
import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.db.entity.VuUserModelBean;
import zh.co.item.bank.web.exam.controller.ExamClassifyBean;
import zh.co.item.bank.web.exam.controller.ResumeBean;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] 登录SignInBean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("userInfoBean")
@Scope("session")
public class UserInfoBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
    
    @Inject
    private UserService userService;
    
    private VuUserModelBean userInfo;
    
	private List<TsCodeBean> jlptLevels;

    private List<TsCodeBean> jtestLevels;

    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_003;
    }
    
    /**
     * 获取用户信息
     * @return
     */
    public String init() {
        pushPathHistory("userInfoBean");
        userInfo = new VuUserModelBean();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("modelId", CmnContants.MODELID_BQD0003);
            map.put("name", "jlpt_level");
            jlptLevels = userService.getCodelist(map);
            
            map.put("modelId", CmnContants.MODELID_BQD0003);
            map.put("name", "jtest_level");
            jtestLevels = userService.getCodelist(map);
            
            userInfo = userService.getUserInfo(Integer.valueOf(WebUtils.getLoginUserId()));

        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_USER_003;
    }

    /**
     * 更新用户信息
     * 
     * @return
     */
    public String updateUserInfo() {

        try {
            userService.updateUserInfo(userInfo);
            userInfo = userService.getUserInfo(Integer.valueOf(WebUtils.getLoginUserId()));
            setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0005), "I");
        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_USER_003;
    }

    /**
     * [更换密码]按下
     * 
     * @return
     */
    public String changePassword() {
        ChangePasswordBean changePasswordBean = (ChangePasswordBean) SpringAppContextManager.getBean("changePasswordBean");
        return changePasswordBean.init();
    }

    /**
     * [去试题库]按下
     * 
     * @return
     */
    public String toExam() {
        ExamClassifyBean classifyBean = (ExamClassifyBean) SpringAppContextManager.getBean("examClassifyBean");
        return classifyBean.init();
    }

    /**
     * [去错题库]按下
     * 
     * @return
     */
    public String toResume() {
        ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
        return resumeBean.init();
    }

	public void setUserInfo(VuUserModelBean userInfo) {
		this.userInfo = userInfo;
	}

	public VuUserModelBean getUserInfo() {
		return userInfo;
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


}
