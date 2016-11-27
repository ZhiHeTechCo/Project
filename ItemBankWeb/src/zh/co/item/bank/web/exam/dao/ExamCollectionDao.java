package zh.co.item.bank.web.exam.dao;

import java.util.List;

import javax.inject.Named;
import zh.co.common.dao.BaseDao;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;

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
     * @param source
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamModel> getExamReport(String source) {
        return getIbatisTemplate().selectList("ExamCollection.getExamReport", source);
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

}
