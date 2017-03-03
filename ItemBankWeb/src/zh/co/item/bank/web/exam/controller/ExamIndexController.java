package zh.co.item.bank.web.exam.controller;

import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;

/**
 * 试题类型选择画面
 * 
 * @author gaoya
 *
 */
@Named("examIndexController")
@Scope("session")
public class ExamIndexController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_000;
    }

    /**
     * 1.初始化
     * 
     * @return
     */
    public String init() {
        pushPathHistory("examIndexController");

        return getPageId();
    }

    /**
     * 2.模式选择跳转
     * 
     * @param currentMode
     * @return
     */
    public String index(String currentMode) {
        try {

            ExamClassifyController examClassifyController = (ExamClassifyController) SpringAppContextManager
                    .getBean("examClassifyController");
            examClassifyController.setMode(currentMode);
            return examClassifyController.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        // 留在当前画面
        return getPageId();
    }

    /**
     * 3.复习错题
     * 
     * @return
     */
    public String resumeSearch() {
        try {

            ResumeBean resumeBean = (ResumeBean) SpringAppContextManager.getBean("resumeBean");
            return resumeBean.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        // 留在当前画面
        return getPageId();
    }

}
