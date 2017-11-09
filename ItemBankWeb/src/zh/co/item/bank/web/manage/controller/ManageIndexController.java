package zh.co.item.bank.web.manage.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.VbExamRateBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.controller.QuestionInsertController;
import zh.co.item.bank.web.file.controller.FileManageBean;
import zh.co.item.bank.web.user.service.AccountBindingService;
import zh.co.item.bank.web.user.service.MergeDataService;

@Named("manageIndexController")
@Scope("session")
public class ManageIndexController extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private AccountBindingService accountBindingService;
    
    @Inject
    private MergeDataService mergeDataService;
    
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
                // 取关联帐号
                CopyOnWriteArrayList<UserModel> safeList = new CopyOnWriteArrayList<UserModel>();
                safeList.addAll(users);
                while (safeList.size() > 0) {
                    UserModel user1 = safeList.get(0);
                    safeList.remove(0);
                    UserModel user2 = new UserModel();
                    for (UserModel user : safeList) {
                        if (user.getUuid().equals(user1.getUuid())) {
                            user2 = user;
                            safeList.remove(user);
                            break;
                        }
                    }
                    // 执行绑定
                    accountBindingService.accountBinging(user1, user2);
                }
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

    /**
     * 去考题列表
     * 
     * @return
     */
    public String toExamListShow() {
        try {
            ExamListController examListController = (ExamListController) SpringAppContextManager
                    .getBean("examListController");
            return examListController.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 文件管理
     * 
     * @return
     */
    public String toFileManageBean() {
        try {
            FileManageBean fileManageBean = (FileManageBean) SpringAppContextManager.getBean("fileManageBean");
            return fileManageBean.init();

        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 修改密码
     * @return
     */
    public String changePassword() {
        try {
//            TuUserBean user = new TuUserBean();
//            user.setId(57);
//            user.setPassword("1");
//            accountBindingService.changePassword(user);
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }
    
    /**
     * 整理考试重复数据(删除考生在tb_exam_collection中被重复记录的数据)
     */
    public String mergeData(){
        try {
            // a.获取包含重复试题的试卷
            List<VbExamRateBean> examRateBeans = mergeDataService.getDuplicateSource();
            // b.删除重复数据
            mergeDataService.deleteDuplicateCollections(examRateBeans);
        } catch (Exception e) {
            processForException(logger, e);
        }
        return getPageId();
    }

    /**
     * 成绩折线
     * @return
     */
    public String showLineChart(){
        return SystemConstants.PAGE_ITBK_EXAM_015;
    }
}
