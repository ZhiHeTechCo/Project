package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ExamReportModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.model.entity.ScoreModel;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
import zh.co.item.bank.web.exam.dao.ExamCollectionHistoryDao;
import zh.co.item.bank.web.exam.dao.MediaDao;

@Named
public class ExamCollectionService {

    @Inject
    private ExamCollectionDao examCollectionDao;

    @Inject
    private MediaDao mediaDao;

    @Inject
    private ExamCollectionHistoryDao examCollectionHistoryDao;

    /**
     * 获取试题的考试种别
     * 
     * @param source
     * @return
     */
    public List<String> getReportTypes(String source) {
        return examCollectionDao.getExamTypes(source);
    }

    /**
     * 取本次考试大题
     * 
     * @param source
     * @return
     */
    public List<String> getStructureIds(String source) {
        return examCollectionDao.getStructureIds(source);
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

    /**
     * J.TEST成绩单取得
     * 
     * @param param
     * @return
     */
    public ScoreModel getMyScoreByExamType(Map<String, Object> param) {
        ScoreModel scoreModel = examCollectionDao.getMyScoreByExamType(param);
        setDetail(scoreModel);
        return scoreModel;
    }

    /**
     * J.TEST成绩单取得
     * 
     * @param param
     * @return
     */
    public ScoreModel getMyScoreByStructureId(Map<String, Object> param) {
        ScoreModel scoreModel = examCollectionDao.getMyScoreByStructureId(param);
        setDetail(scoreModel);
        return scoreModel;
    }

    /**
     * 取读解总分
     * 
     * @param param
     * @return
     */
    // public ScoreModel getReadingTotal(Map<String, Object> param) {
    // ScoreModel scoreModel = examCollectionDao.getReadingTotal(param);
    // setDetail(scoreModel);
    // return scoreModel;
    // }

    /**
     * 设置成绩单部分信息
     * 
     * @param scoreModel
     */
    public void setDetail(ScoreModel scoreModel) {
        if (scoreModel == null) {
            // 数据错误的情况
            return;
        }
        // c-1.得点率设置
        if (scoreModel.getTotalScore() != 0) {
            float percentageFloat = (float) scoreModel.getMyTotalScore() / (float) scoreModel.getTotalScore() * 100;
            String tmp = String.valueOf(percentageFloat);
            System.out.println(tmp);
            if (tmp.indexOf(".") == 3) {
                // 100.0
                tmp = tmp.substring(0, 3);
            } else if (tmp.length() > 4) {
                tmp = tmp.substring(0, 4);
            }
            scoreModel.setPercentage(tmp);
        } else {
            scoreModel.setPercentage("0");
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
        } else if (scoreModel.getSource().contains("E-F")) {
            if (tmp >= 70) {
                level = "E";
            } else if (tmp >= 50) {
                level = "F";
            } else if (tmp >= 40) {
                level = "準F";
            } else {
                level = "評価なし";
            }
        }
        scoreModel.setLevel(level);
    }

    /**
     * 总分设置
     * 
     * @param scoreModels
     * @param myReadingTotal
     * @param readingTotal
     * @param myListenTotal
     * @param listenTotal
     * @param source
     */
    public void setTotalScore(List<ScoreModel> scoreModels, int myReadingTotal, int readingTotal, int myListenTotal,
            int listenTotal, String source) {
        // 读解总分
        ScoreModel reading = setModelValue(myReadingTotal, readingTotal, source, "readingTotal");
        scoreModels.add(reading);
        // 听力总分
        ScoreModel listen = setModelValue(myListenTotal, listenTotal, source, "listenTotal");
        scoreModels.add(listen);
        // 总分
        ScoreModel total = setModelValue(myReadingTotal + myListenTotal, readingTotal + listenTotal, source, "total");
        scoreModels.add(total);
    }

    /**
     * ScoreModel设值
     * 
     * @param myScore
     * @param total
     * @param source
     * @param examType
     * @return
     */
    private ScoreModel setModelValue(int myScore, int total, String source, String examType) {
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setMyTotalScore(myScore);
        scoreModel.setTotalScore(total);
        scoreModel.setSource(source);
        scoreModel.setExamType(examType);
        setDetail(scoreModel);
        return scoreModel;
    }

    /**
     * 取听力完成度
     * 
     * @param paramMap
     * @return
     */
    public String getMediaRate(Map<String, Object> paramMap) {
        return examCollectionDao.getMediaRate(paramMap);
    }

    /**
     * 删除当前用户本套试题听力记录
     * 
     * @param param
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteMediaCollectionBySource(Map<String, Object> param) {
        examCollectionDao.deleteExamCollectionBySource(param);
        mediaDao.deleteMediaCollectionBySource(param);
    }

    /**
     * 重做指定题型TODO
     * 
     * @param userId
     * @param structureIds
     */
    public void reDo(Integer userId, Integer[] structureIds) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("structureId", structureIds);
    }

    /**
     * 根据试题结构取指定做题记录
     * 
     * @param param
     * @return
     */
    public List<QuestionStructure> selectReportStructure(Integer id, String source, String examType) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", id);
        param.put("source", source);
        param.put("examType", examType);

        return examCollectionDao.selectReportStructure(param);
    }

    /**
     * 清空指定试题并保留履历
     * 
     * @param userId 用户ID
     * @param source 考卷
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void reDoExam(Integer userId, String source) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("source", source);
        // 1.保存履历
        examCollectionHistoryDao.insertExamCollectionHistory(param);
        // 2.删除本套做题记录
        examCollectionDao.deleteExamCollectionBySource(param);
        mediaDao.deleteMediaCollectionBySource(param);
    }

}
