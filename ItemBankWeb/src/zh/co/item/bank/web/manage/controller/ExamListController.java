package zh.co.item.bank.web.manage.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.item.bank.db.entity.TbExamListBean;
import zh.co.item.bank.web.manage.service.ExamListService;

/**
 * 考题列表管理画面
 * 
 * @author gyang
 *
 */
@Named("examListController")
@Scope("session")
public class ExamListController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamListService examListService;

    /**
     * 画面显示变量
     */
    private List<TbExamListBean> examListBeans;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_MANAGE_002;
    }

    /**
     * 1.画面初始化
     */
    public String init() {
        pushPathHistory("examListController");
        try {
            examListBeans = examListService.selectAll();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    public String goBack() {
        return SystemConstants.PAGE_ITBK_MANAGE_001;
    }

    public List<TbExamListBean> getExamListBeans() {
        return examListBeans;
    }

    public void setExamListBeans(List<TbExamListBean> examListBeans) {
        this.examListBeans = examListBeans;
    }

}
