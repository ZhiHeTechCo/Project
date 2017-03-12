package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.model.entity.MediaQuestionStructure;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
import zh.co.item.bank.web.exam.dao.MediaDao;
import zh.co.item.bank.web.exam.dao.QuestionDao;

@Named
public class ExamScoreInputService {

    @Inject
    private QuestionDao questionDao;

    @Inject
    private MediaDao mediaDao;

    @Inject
    private ExamCollectionDao examCollectionDao;

    /**
     * 非听力题检索
     * 
     * @param source
     * @return
     */
    public List<QuestionStructure> selectQuestionsStructure(String source) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", source);
        map.put("deleteFlag", "1");
        return questionDao.selectQuestionsStructure(map);
    }

    /**
     * 听力题检索
     * 
     * @param source
     * @return
     */
    public List<MediaQuestionStructure> selectMediaStructure(String source) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", source);
        map.put("deleteFlag", "1");
        return mediaDao.selectMediaStructure(map);
    }

    /**
     * 做题记录是否存在
     * 
     * @param userId
     * @param source
     * @return
     */
    public boolean selectExamCollectionForCount(Integer userId, String source) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("source", source);
        map.put("userId", userId);
        Integer num = examCollectionDao.selectExamCollectionForCount(map);
        return num > 0;
    }

}
