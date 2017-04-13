package zh.co.item.bank.web.user.dao;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TuPointHistoryBean;

/**
 * tu_point_history
 * 
 * @author gyang
 *
 */
@Named
public class PointHistoryDao extends BaseDao {

    /**
     * insertSelective
     * 
     * @param bean
     */
    public void insertPointHistory(TuPointHistoryBean bean) {
        getIbatisTemplate().insert("TuPointHistory.insertSelective", bean);
    }
}
