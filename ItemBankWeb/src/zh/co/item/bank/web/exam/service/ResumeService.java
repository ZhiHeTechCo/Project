package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.web.exam.dao.ResumeDao;

/**
 * 错题库
 * 
 * @author gaoya
 *
 */
@Named
public class ResumeService {

    @Inject
    private ResumeDao resumeDao;

    public List<ExamModel> selectQuestionForError(Integer userId) {
        return resumeDao.selectQuestionForError(userId);
    }

    public List<ExamModel> selectQuestionForErrorAll(Integer userId) {
        return resumeDao.selectQuestionForErrorAll(userId);
    }

    public List<ExamModel> selectErrorByFatherId(Map<String, Object> map) {
        return resumeDao.selectErrorByFatherId(map);
    }

    /**
     * 用户错题集
     * 
     * @param examType
     * @param userId
     * 
     * @return
     */
    public List<QuestionStructure> searchCorrelationErrorQuestions(String examType, Integer userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("examType", examType);
        map.put("userId", userId);
        return resumeDao.searchCorrelationErrorQuestions(map);
    }
}
