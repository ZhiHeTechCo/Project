package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbErrorReportBean;
import zh.co.item.bank.db.entity.TbExamListBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.model.entity.ExamListModel;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.MediaModel;

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
     * 检索指定试题（试题模式）
     * 
     * @param map[questionId,userId]
     * @return
     */
    public ExamModel selectReportDetailByQuestionId(Map<String, Object> map) {
        return (ExamModel) getIbatisTemplate().selectOne("Question.selectReportDetailByQuestionId", map);
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
    public List<ExamModel> getStructure(TbQuestionClassifyBean classifyBean) {
        return getIbatisTemplate().selectList("Question.getStructure", classifyBean);
    }

    /**
     * 获取对应试题
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> getTestQuestionBySource(Map<String, Object> map) {
        return getIbatisTemplate().selectList("Question.getTestQuestionBySource", map);
    }

    /**
     * 检索题库中最新一年的试题的年份
     * 
     * @param map
     * @return
     */
    public String getYear(Map<String, Object> map) {
        return (String) getIbatisTemplate().selectOne("Question.getYear", map);
    }

    /**
     * 试题报错
     */
    public void insertErrorReport(TbErrorReportBean bean) {
        getIbatisTemplate().insert("TbErrorReport.insertSelective", bean);
    }

    /**
     * 检索考卷表
     * 
     * @param userId
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamListModel> getExamListForUser(Integer userId) {
        return getIbatisTemplate().selectList("ExamList.getExamListForUser", userId);
    }

    /**
     * 获取成绩快速查询相关试题
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbExamListBean> getQuickSourceForAll() {
        return getIbatisTemplate().selectList("ExamList.getQuickSourceForAll");
    }

    /**
     * 听力列表
     * 
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MediaModel> getMediaList(Map<String, Object> param) {
        return getIbatisTemplate().selectList("Media.selectMediaListByUsers", param);
    }
}
