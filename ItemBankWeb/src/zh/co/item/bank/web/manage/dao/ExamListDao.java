package zh.co.item.bank.web.manage.dao;

import java.util.List;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbExamListBean;

@Named
public class ExamListDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public List<TbExamListBean> selectAll() {
        return getIbatisTemplate().selectList("ExamList.selectAll");
    }
}
