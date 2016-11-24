package zh.co.item.bank.web.contact.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TcMessageBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.contact.service.MessageService;

/**
 * [留言板]画面
 * 
 * @author gyang
 *
 */
@Named("messageBean")
@Scope("session")
public class MessageBean extends BaseController {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private MessageService messageService;

    private UserModel userInfo;

    private TcMessageBean message;

    public String getPageId() {
        return SystemConstants.PAGE_ITBK_COM_003;
    }

    /**
     * 初期化
     * 
     * @return
     */
    public String init() {
        try {
            pushPathHistory("messageBean");

            // 取用户信息并显示，没有则是游客身份
            userInfo = WebUtils.getLoginUserInfo();
            message = new TcMessageBean();

            if (userInfo == null) {
                // 游客
                userInfo = new UserModel();
                message.setUserId(0);

                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0011);
                throw ex;

            } else {
                message.setUserId(userInfo.getId());
            }

        } catch (Exception e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_COM_003;
    }

    /**
     * 按下[发送消息]
     * 
     * @return
     */
    public String sendMessage() {
        try {
            // 联系方式为空则返回并显示错误消息
            if (StringUtils.isEmpty(userInfo.getEmail()) && StringUtils.isEmpty(userInfo.getTelephone())) {
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0012);
                throw ex;
            }
            if (StringUtils.isEmpty(message.getMessage())) {
                CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0013);
                throw ex;
            }
            if (message.getUserId() == 0) {
                // 游客
                message.setName(userInfo.getNickName());
                message.setEmail(userInfo.getEmail());
                message.setTelephone(userInfo.getTelephone());
            }
            messageService.sendMessage(message);

            // 成功提醒
            message.setMessage(null);
            CmnBizException ex = new CmnBizException(MessageId.ITBK_I_0014);
            throw ex;

        } catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_COM_003;
    }

    public UserModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
    }

    public TcMessageBean getMessage() {
        return message;
    }

    public void setMessage(TcMessageBean message) {
        this.message = message;
    }

}
