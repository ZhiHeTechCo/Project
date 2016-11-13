package zh.co.common.controller;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;

/**
 * <p>[概要] 首页Controller</p>
 * <p>[详细] 首页</p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("pageTopController")
@Scope("session")
public class PageTopController extends BaseController {
    
    /* (non-Javadoc)
     * @see zh.co.common.controller.BaseController#getPageId()
     */
    public String getPageId(){
        return SystemConstants.PAGE_ITBK_COM_001;
    }

    // CmnLogger
    private CmnLogger logger = CmnLogger.getLogger(PageTopController.class);

    
    private String firstFlg;

    /**
     * <p>
     * [概要]首页
     * </p>
     * <p>
     * [备考]首页初期化
     * </p>
     * 
     * @return
     * @throws Exception
     */
    public String init() {

        try {
        	pushPathHistory("pageTopController");
            //log
            this.initLog(logger, null);
            
        } catch (Throwable e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_COM_001;
    }


    /**
     * <p>
     * [概要]
     * </p>
     * <p>
     * [备考]
     * </p>
     * 
     * @return
     */
    public String pageForward() {
        String pageControllerName = WebUtils.getRequestParam("pageController");
        if(StringUtils.isNotBlank(pageControllerName)){
            BaseController pageController = (BaseController) SpringAppContextManager.getBean(pageControllerName);
            return pageController.init();
        } else {
            String pageId = WebUtils.getRequestParam("pageId");
            pushPathHistory(pageId, "");
            return pageId;
        }
    }
    
    /**
     * <p>
     * [概要]
     * </p>
     * <p>
     * [备考]
     * </p>
     * 
     * @return
     */
    public String pageNavigator() {
        WebUtils.removeSessionAttribute(WebUtils.SESSION_PATH_HISTORY);
        String pageControllerName = WebUtils.getRequestParam("pageController");
        if(StringUtils.isNotBlank(pageControllerName)){
            BaseController pageController = (BaseController) SpringAppContextManager.getBean(pageControllerName);
            return pageController.init();
        } else {
            String pageId = WebUtils.getRequestParam("pageId");
            pushPathHistory(pageId, "");
            return pageId;
        }
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public String logOut() {
        logger.log(MessageId.ITBK_I_0002);
        WebUtils.invalidateSession();
        return SystemConstants.PAGE_ITBK_COM_001;
    }

    public String getFirstFlg() {
        return firstFlg;
    }

    public void setFirstFlg(String firstFlg) {
        this.firstFlg = firstFlg;
    }
    
}
