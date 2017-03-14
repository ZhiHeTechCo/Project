package zh.co.item.bank.web.manage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.controller.QuestionInsertController;
import zh.co.item.bank.web.user.service.AccountBindingService;

@Named("manageIndexController")
@Scope("session")
public class ManageIndexController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private AccountBindingService accountBindingService;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_MANAGE_001;
    }

    /**
     * 1.画面初始化
     */
    public String init() {
        pushPathHistory("manageIndexController");
        return getPageId();
    }

    /**
     * [绑定]按钮按下
     * 
     * @return
     */
    public String accountBingding() {
        try {

            // 前台做Check：为数字且不为空
            String oldUserId = WebUtils.getRequestParam("oldUserId");
            String newUserId = WebUtils.getRequestParam("newUserId");

            List<UserModel> users = new ArrayList<UserModel>();
            // a-1.指定用户ID进行数据绑定
            if (StringUtils.isNotEmpty(oldUserId) && StringUtils.isNotEmpty(newUserId)) {
                // 取指定ID用户信息
                users = accountBindingService.selectUserByIds(Integer.parseInt(oldUserId), Integer.parseInt(newUserId));

            } else {
                // a-2.后台检索执行绑定
                users = accountBindingService.selectUserWithSameUuid();

            }

            // b.超过两个用户，对头两条数据执行绑定
            if (users.size() >= 2) {
                accountBindingService.accountBinging(users.get(0), users.get(1));
            }
        } catch (Exception e) {
            processForException(logger, e);
        }

        return getPageId();
    }

    /**
     * 去试题上传
     * 
     * @return
     */
    public String toQuestionInsert() {
        try {
            QuestionInsertController questionInsertController = (QuestionInsertController) SpringAppContextManager
                    .getBean("questionInsertController");
            return questionInsertController.init();
            
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

}
