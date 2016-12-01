package zh.co.item.bank.web.exam.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;

@Named
public class ExamCollectionService {

    @Inject
    private ExamCollectionDao examCollectionDao;

    /**
     * 获取试题的考试种别
     * 
     * @param source
     * 
     * @return
     */
    public List<String> getReportTypes(String source) {
        return examCollectionDao.getExamType(source);
    }

    /**
     * 考试成绩一览取得
     * @param map[questionId,userId]
     * @return
     */
    public List<ExamModel> getExamReport(Map<String, Object> map) {
        return examCollectionDao.getExamReport(map);
    }

    /**
     * 获取百分比
     * 
     * @param model[examType source]
     * @return
     */
    public ExamReportModel getPercentage(ExamReportModel model) {
        return examCollectionDao.getPercentage(model);
    }

    /**
     * 批量登录考试记录表
     * 
     * @param examCollections
     */
    public void insertExamCollection(List<ExamModel> examCollections) {
        examCollectionDao.insertExamCollections(examCollections);
    }
}
