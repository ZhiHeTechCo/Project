package zh.co.item.bank.web.exam.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.item.bank.db.entity.TbExamListBean;
import zh.co.item.bank.web.exam.service.ExamService;

/**
 * 成绩速查通道
 * 
 * @author gyang
 */
@Named("examScoreIndexController")
@Scope("session")
public class ExamScoreIndexController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamService examService;

    private List<TbExamListBean> sources;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_EXAM_010;
    }

    public String init() {

        try {
            pushPathHistory("examScoreIndexController");

            sources = examService.getQuickSourceForAll();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 
     * @return
     */
    public String doRecord() {
        ExamScoreInputController examScoreInputController = (ExamScoreInputController) SpringAppContextManager
                .getBean("examScoreInputController");
        return examScoreInputController.init();
    }

    public ExamService getExamService() {
        return examService;
    }

    public void setExamService(ExamService examService) {
        this.examService = examService;
    }

    public List<TbExamListBean> getSources() {
        return sources;
    }

    public void setSources(List<TbExamListBean> sources) {
        this.sources = sources;
    }

}
