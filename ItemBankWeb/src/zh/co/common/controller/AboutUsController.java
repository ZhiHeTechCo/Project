package zh.co.common.controller;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.log.CmnLogger;

/**
 * <p>[概要] 关于我们Controller</p>
 * <p>[详细] 关于我们</p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("aboutUsController")
@Scope("session")
public class AboutUsController extends BaseController {
    
    /* (non-Javadoc)
     * @see zh.co.common.controller.BaseController#getPageId()
     */
    public String getPageId(){
        return SystemConstants.PAGE_ITBK_COM_002;
    }

    // CmnLogger
    private CmnLogger logger = CmnLogger.getLogger(AboutUsController.class);


    /**
     * <p>
     * [概要]关于我们
     * </p>
     * <p>
     * [备考]关于我们初期化
     * </p>
     * 
     * @return
     * @throws Exception
     */
    public String init() {

        try {
        	pushPathHistory("aboutUsController");
            //log
            this.initLog(logger, null);
            
        } catch (Throwable e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_COM_002;
    }
    
}
