package zh.co.item.bank.web.exam.dao;

import javax.inject.Named;
import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbExamDropoutBean;

@Named
public class ExamDropoutDao extends BaseDao {

    /**
     * 检索中途退出记录
     * 
     * @param tbExamDropoutBean
     */
    public String getDropoutForYear(TbExamDropoutBean tbExamDropoutBean) {
        return (String) getIbatisTemplate().selectOne("ExamDropout.getDropoutForYear", tbExamDropoutBean);
    }

    /**
     * 中途退出登录
     * 
     * @param bean
     */
    public void insert(TbExamDropoutBean bean) {
        getIbatisTemplate().insert("ExamDropout.insertSelective", bean);
    }

    /**
     * 删除中途退出记录
     * 
     * @param bean
     */
    public void deleteExamDropout(TbExamDropoutBean bean) {
        getIbatisTemplate().delete("ExamDropout.deleteExamDropout", bean);
    }
}
