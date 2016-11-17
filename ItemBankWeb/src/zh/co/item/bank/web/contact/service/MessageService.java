package zh.co.item.bank.web.contact.service;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TcMessageBean;
import zh.co.item.bank.web.contact.dao.MessageDao;

/**
 * 留言板
 * 
 * @author gyang
 *
 */
@Named
public class MessageService {

    @Inject
    private MessageDao messageDao;

    /**
     * 登录留言
     * 
     * @param message
     */
    public void sendMessage(TcMessageBean message) {
        messageDao.insertMessage(message);
    }

}
