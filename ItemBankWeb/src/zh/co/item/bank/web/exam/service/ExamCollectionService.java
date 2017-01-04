package zh.co.item.bank.web.exam.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.web.exam.controller.ScoreModel;
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
     * 
     * @param map[questionId,userId]
     * @return
     */
    public List<ExamModel> getExamReport(Map<String, Object> map) {
        return examCollectionDao.getExamReport(map);
    }

    /**
     * 获取百分比
     * 
     * @param model[examType
     *            source]
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

    /**
     * J.TEST成绩单取得
     * 
     * @param param
     * @return
     */
    public ScoreModel getMyScore(Map<String, Object> param) {
        ScoreModel scoreModel = examCollectionDao.getMyScore(param);
        // c-1.得点率设置
        if (scoreModel.getTotalScore() != 0) {
            String tmp = String.valueOf(scoreModel.getMyScore() / scoreModel.getTotalScore());
            if (tmp.length() > 4) {
                tmp = tmp.substring(0, 4);
            }
            scoreModel.setPercentage(tmp);
        }
        // c-2.评价设置
        float tmp = Float.parseFloat(scoreModel.getPercentage());
        String level = "";
        if (scoreModel.getSource().contains("A-D")) {
            if (tmp >= 93) {
                level = "特A";
            } else if (tmp >= 90) {
                level = "A";
            } else if (tmp >= 85) {
                level = "準A";
            } else if (tmp >= 80) {
                level = "B";
            } else if (tmp >= 70) {
                level = "準B";
            } else if (tmp >= 60) {
                level = "C";
            } else if (tmp >= 50) {
                level = "D";
            } else if (tmp >= 40) {
                level = "準D";
            } else {
                level = "評価なし";
            }
        } else {
            // TODO
        }
        scoreModel.setLevel(level);
        return scoreModel;
    }
}
