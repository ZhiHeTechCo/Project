package zh.co.item.bank.web.exam.service;

import java.util.List;

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
     * 登录考试记录表
     * 
     * @param examModel
     */
    public void insertExamCollection(ExamModel examModel) {
        examCollectionDao.insertExamCollection(examModel);
    }

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

    public List<ExamModel> getExamReport(String source) {
        return examCollectionDao.getExamReport(source);
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
}
