package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbExamCollectionBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.ScoreModel;

@Named
public class ExamCollectionDao extends BaseDao {

    /**
     * 批量登录考试记录表
     * 
     * @param examCollections
     */
    public void insertExamCollections(List<ExamModel> examCollections) {
        getIbatisTemplate().insert("ExamCollection.insertExamCollections", examCollections);
    }

    /**
     * 取本次考试种别
     * 
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getExamTypes(String source) {
        return getIbatisTemplate().selectList("ExamCollection.getExamTypes", source);
    }

    /**
     * 取本次考试大题
     * 
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getStructureIds(String source) {
        return getIbatisTemplate().selectList("ExamCollection.getStructureIds", source);
    }

    /**
     * 取本次成绩表
     * 
     * @param map[questionId,userId]
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> getExamReport(Map<String, Object> map) {
        return getIbatisTemplate().selectList("ExamCollection.getExamReport", map);
    }

    /**
     * 取成绩百分比
     * 
     * @param model
     * @return
     */
    public ExamReportModel getPercentage(ExamReportModel model) {
        return (ExamReportModel) getIbatisTemplate().selectOne("ExamCollection.getPercentage", model);
    }

    /**
     * J.TEST分项成绩取得[examType]
     * 
     * @param param
     * @return
     */
    public ScoreModel getMyScoreByExamType(Map<String, Object> param) {
        return (ScoreModel) getIbatisTemplate().selectOne("ExamCollection.getMyScoreByExamType", param);
    }

    /**
     * J.TEST大题成绩取得[structureId]
     * 
     * @param param
     * @return
     */
    public ScoreModel getMyScoreByStructureId(Map<String, Object> param) {
        return (ScoreModel) getIbatisTemplate().selectOne("ExamCollection.getMyScoreByStructureId", param);
    }

    /**
     * 取读解总分
     * 
     * @param param
     * @return
     */
    public ScoreModel getReadingTotal(Map<String, Object> param) {
        return (ScoreModel) getIbatisTemplate().selectOne("ExamCollection.getReadingTotal", param);
    }

    /**
     * 取听力完成度
     * 
     * @param paramMap
     */
    public String getMediaRate(Map<String, Object> paramMap) {
        return (String) getIbatisTemplate().selectOne("ExamCollection.getMediaRate", paramMap);
    }

    /**
     * 删除本次做题记录
     * 
     * @param map
     * @return
     */
    public void deleteExamCollectionBySource(Map<String, Object> map) {
        getIbatisTemplate().delete("ExamCollection.deleteBySource", map);
    }

    /**
     * 查询是否已做过
     * 
     * @param map
     * @return
     */
    public Integer selectExamCollectionForCount(Map<String, Object> map) {
        return (Integer) getIbatisTemplate().selectOne("ExamCollection.selectExamCollectionForCount", map);
    }

    /**
     * 帐号合并-检索冲突数据
     * 
     * @param users
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbExamCollectionBean> selectExamCollectionByUsers(List<Integer> users) {
        return getIbatisTemplate().selectList("ExamCollection.selectExamCollectionByUsers", users);
    }

    /**
     * 帐号合并-删除用户旧数据
     * 
     * @param deleteList
     */
    public void deleteExamCollectionOld(List<TbExamCollectionBean> deleteList) {
        getIbatisTemplate().delete("ExamCollection.deleteExamCollectionOld", deleteList);
    }

    /**
     * 根据试题结构取指定试题
     * 
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QuestionStructure> selectReportStructure(Map<String, Object> param) {
        return getIbatisTemplate().selectList("ExamCollection.selectReportStructure", param);
    }
}
