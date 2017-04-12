package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbFirstLevelDirectoryBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.QuestionStructure;

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

    /**
     * 登录一级表
     */
    public void insertFirstLevelDirectory(TbFirstLevelDirectoryBean bean) {
        getIbatisTemplate().insert("TbFirstLevelDirectory.insertSelective", bean);
    }

    /**
     * 更新一级表
     * 
     * @param bean
     */
    public void updateFirstLevelDirectory(TbFirstLevelDirectoryBean bean) {
        getIbatisTemplate().update("TbFirstLevelDirectory.updateByPrimaryKeySelective", bean);
    }

    /**
     * 更新问题表
     * 
     * @param model
     */
    public void updateQuestion(ExamModel model) {
        getIbatisTemplate().update("Question.updateQuestion", model);
    }

    /**
     * 根据选择获取大题目
     * 
     * @param classifyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbQuestionStructureBean> getStructuresByClassifyId(Integer classifyId) {
        return getIbatisTemplate().selectList("Media.selectStructuresByClassifyId", classifyId);
    }

    /**
     * 非听力题检索
     * 
     * @param map[source]
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QuestionStructure> selectQuestionsStructure(Map<String, Object> map) {
        return getIbatisTemplate().selectList("Question.selectQuestionsStructure", map);
    }

    /**
     * 关联试题查询
     * 
     * @param map[key,questionId]
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QuestionStructure> searchCorrelationQuestions(Map<String, Object> map) {
        return getIbatisTemplate().selectList("Question.searchCorrelationQuestions", map);
    }

}
