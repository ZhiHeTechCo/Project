package zh.co.item.bank.web.exam.dao;

import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;

@Named
public class ExamCollectionHistoryDao extends BaseDao {

    /**
     * 保存做题履历
     * 
     * @param param
     */
    public void insertExamCollectionHistory(Map<String, Object> param) {
        getIbatisTemplate().insert("ExamCollectionHistory.insertExamCollectionHistory", param);
    }
}
