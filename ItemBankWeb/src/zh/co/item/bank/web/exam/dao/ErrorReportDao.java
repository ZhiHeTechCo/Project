package zh.co.item.bank.web.exam.dao;

import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;

/**
 * 试题报错表
 * 
 * @author gyang
 *
 */
@Named
public class ErrorReportDao extends BaseDao {

    /**
     * 帐号绑定-更改用户ID
     * 
     * @param map
     */
    public void updateUserId(Map<String, Object> map) {
        getIbatisTemplate().update("ErrorReport.updateUserId", map);
    }
}
