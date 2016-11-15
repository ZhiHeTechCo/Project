package zh.co.item.bank.web.exam.dao;

import java.util.List;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbFirstLevelDirectoryBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.model.entity.ExamModel;

/**
 * 考题管理模块
 * 
 * @author gaoya
 *
 */
@Named
public class QuestionDao extends BaseDao {

    /**
     * 登录问题表
     * 
     * @param model
     */
    public void insertQuestion(ExamModel model) {
        getIbatisTemplate().insert("Question.insertQuestion", model);
    }

    /**
     * 登录一级表
     */
    public void insertFirstLevelDirectory(TbFirstLevelDirectoryBean bean) {
        getIbatisTemplate().insert("TbFirstLevelDirectory.insertSelective", bean);
    }

    /**
     * 检索最后插入的数据
     * 
     * @return
     */
    public Integer selectLastInsertId() {
        return (Integer) getIbatisTemplate().selectOne("Question.selectLastInsertId");
    }

    /**
     * 检索考题结构表
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbQuestionStructureBean> selectStructureForAll() {
        return getIbatisTemplate().selectList("Question.selectStructureForAll");
    }

    /**
     * 根据Subject检索数据
     * 
     * @param firstLevelDirectoryBean
     * @return
     */
    public Integer selectFLDByName(TbFirstLevelDirectoryBean firstLevelDirectoryBean) {
        return (Integer) getIbatisTemplate().selectOne("Question.selectFLDByName", firstLevelDirectoryBean);
    }
}
