package zh.co.item.bank.web.contact.dao;

import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TcMessageBean;

/**
 * Message
 * 
 * @author gyang
 *
 */
@Named
public class MessageDao extends BaseDao {

    /**
     * 留言登录
     * 
     * @param message
     */
    public void insertMessage(TcMessageBean message) {
        getIbatisTemplate().insert("TcMessage.insertSelective", message);
    }

    /**
     * 账号合并-更新UserId
     * 
     * @param map
     */
    public void updateUserId(Map<String, Object> map) {
        getIbatisTemplate().update("Message.updateUserId", map);
    }

}
