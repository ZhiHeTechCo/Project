package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.model.entity.ExamModel;

/**
 * 考试模块
 * 
 * @author gaoya
 *
 */
@Named
public class ExamDao extends BaseDao {

    /**
     * 检索做题记录，取得当前用户做题信息
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> selectCollectionByUserId(Map<String, Object> map) {
        return (List<ExamModel>) getIbatisTemplate().selectList("Question.selectCollectionByUserId", map);
    }

    /**
     * 当前用户所选类别的题
     * 
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> selectQuestionBySelection(Map<String, Object> map) {
        return (List<ExamModel>) getIbatisTemplate().selectList("Question.selectQuestionBySelection", map);
    }

    /**
     * 检索文字、阅读类试题
     * 
     * @param structureId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> selectSpecialForOne(Map<String, Object> map) {
        return (List<ExamModel>) getIbatisTemplate().selectList("Question.selectSpecialForOne", map);
    }

    /**
     * 检索指定试题
     * 
     * @param questionId
     * @return
     */
    public ExamModel selectQuestionByQuestionId(Integer questionId) {
        return (ExamModel) getIbatisTemplate().selectOne("Question.selectQuestionByQuestionId", questionId);
    }

    /**
     * 取得ClassifyID
     * 
     * @param classityBean
     * @return
     */
    public Integer selectClassifyId(TbQuestionClassifyBean classityBean) {
        return (Integer) getIbatisTemplate().selectOne("Question.selectClassifyId", classityBean);
    }

    /**
     * 检索试题题目
     * 
     * @param structureId
     * @return
     */
    public String selectTitleById(Integer structureId) {
        return (String) getIbatisTemplate().selectOne("Question.selectTitleById", structureId);
    }

    @SuppressWarnings("unchecked")
    public List<ExamModel> selectQuestionByFatherId(Integer fatherId) {
        return getIbatisTemplate().selectList("Question.selectQuestionByFatherId", fatherId);
    }

    /**
     * 获取试题结构
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> getTestQuestions(TbQuestionClassifyBean classifyBean) {
        return getIbatisTemplate().selectList("Question.getTestQuestions", classifyBean);
    }

    /**
     * 获取对应试题
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> getTestQuestion(Map<String, Object> map) {
        return getIbatisTemplate().selectList("Question.getTestQuestion", map);
    }

    /**
     * 删除本次做题记录
     * @param source
     * @return
     */
    public void deleteExamCollectionBySource(String source) {
        getIbatisTemplate().delete("ExamCollection.deleteBySource", source);
    }

}
