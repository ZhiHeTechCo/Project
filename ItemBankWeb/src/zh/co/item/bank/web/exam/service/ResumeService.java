package zh.co.item.bank.web.exam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.db.entity.TbCollectionDetailBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.QuestionStructure;
import zh.co.item.bank.web.exam.dao.CollectionDao;
import zh.co.item.bank.web.exam.dao.CollectionDetailDao;
import zh.co.item.bank.web.exam.dao.ExamDao;
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

    @Inject
    private CollectionDao collectionDao;

    @Inject
    private CollectionDetailDao collectionDetailDao;

    @Inject
    private ExamDao examDao;

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

    /**
     * 从错题集里面删除指定试题
     * 
     * @param userId 用户ID
     * @param questionId 试题ID
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeErrorQuestion(Integer userId, Integer questionId) {
        // 检索试题
        ExamModel examModel = examDao.selectQuestionByQuestionId(questionId);
        examModel.setUserId(userId);

        // 记录表更新用
        TbCollectionBean collection = new TbCollectionBean();
        // 做题详细表更新用
        TbCollectionDetailBean collectionDetail = new TbCollectionDetailBean();

        // 用户ID
        collection.setId(examModel.getUserId());
        collectionDetail.setUserId(examModel.getUserId());

        // 试题ID
        collection.setQuestionId(questionId);
        collectionDetail.setQuestionId(questionId);

        // 我的答案（设为正确答案）
        collectionDetail.setMyAnswer(examModel.getAnswer());

        // finish（默认完成）
        collection.setFinish("9");

        collectionDao.updateCollection(collection);
        collectionDetailDao.insertCollectionDetail(collectionDetail);
    }
}
