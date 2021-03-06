package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbExamCollectionBean;
import zh.co.item.bank.db.entity.VbExamRateBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.model.entity.MediaQuestionStructure;
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
     * 根据试题结构取指定试题
     * 
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<QuestionStructure> selectReportStructure(Map<String, Object> param) {
        return getIbatisTemplate().selectList("ExamCollection.selectReportStructure", param);
    }

    /**
     * 帐号合并-更新用户ID
     * 
     * @param param
     */
    public void updateUserId(Map<String, Object> param) {
        getIbatisTemplate().update("ExamCollection.updateUserId", param);
    }

    /**
     * 帐号合并-删除用户旧数据
     * 
     * @param userId
     */
    public void deleteExamCollectionOld(Integer userId) {
        getIbatisTemplate().delete("ExamCollection.deleteExamCollectionOld", userId);
    }

    /**
     * 检索听力做题结果
     * 
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MediaQuestionStructure> selectMediaResult(Map<String, Object> param) {
        return getIbatisTemplate().selectList("Media.selectMediaResult", param);
    }

    /**
     * 取考试记录中存在重复数据的考卷
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<VbExamRateBean> getDuplicateSource() {
        return getIbatisTemplate().selectList("ExamCollection.getDuplicateSource");
    }

    /**
     * 取考卷记录中的重复数据
     * 
     * @param examRateList
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbExamCollectionBean> getDuplicateCollections(List<VbExamRateBean> qList) {
        return getIbatisTemplate().selectList("ExamCollection.getDuplicateCollections", qList);
    }

    /**
     * 删除重复数据
     * @param examCollectionBeans
     */
    public void deleteDuplicateCollections(List<TbExamCollectionBean> examCollectionBeans) {
        getIbatisTemplate().delete("ExamCollection.deleteByPrimaryKey", examCollectionBeans);
    }


}
