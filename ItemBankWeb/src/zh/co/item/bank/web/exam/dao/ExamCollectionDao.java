package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import zh.co.common.dao.BaseDao;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.web.exam.controller.ScoreModel;

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
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getExamType(String source) {
        return getIbatisTemplate().selectList("ExamCollection.getExamType", source);
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
     * J.TEST成绩单取得
     * 
     * @param param
     * @return
     */
    public ScoreModel getMyScore(Map<String, Object> param) {
        return (ScoreModel) getIbatisTemplate().selectOne("ExamCollection.getMyScore", param);
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

}
