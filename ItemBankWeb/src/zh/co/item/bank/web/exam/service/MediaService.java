package zh.co.item.bank.web.exam.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.item.bank.db.entity.TbMediaCollectionBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.MediaModel;
import zh.co.item.bank.model.entity.MediaQuestionStructure;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
import zh.co.item.bank.web.exam.dao.MediaDao;

/**
 * 听力模块
 * 
 * @author gaoya
 *
 */
@Named
public class MediaService {

    @Inject
    private MediaDao mediaDao;

    @Inject
    private ExamCollectionDao examCollectionDao;

    /**
     * 获取当前用户未做过或未做完的听力
     * 
     * @param map
     * @return
     */
    public MediaModel getMedia(Map<String, Object> map) {
        return mediaDao.getMedia(map);
    }

    /**
     * 检索ClassifyId
     * 
     * @param classifyBean
     * @return
     */
    public List<Integer> getClssifyId(TbQuestionClassifyBean classifyBean) {
        return mediaDao.getClssifyId(classifyBean);
    }

    /**
     * 检索听力试题
     * 
     * @param mediaId
     * @return
     */
    public List<MediaModel> getMediaQuestions(Integer mediaId) {
        return mediaDao.getMediaQuestions(mediaId);
    }

    /**
     * 检索大题目
     * 
     * @param classifyId
     * @return
     */
    public List<TbQuestionStructureBean> selectForStructures(Integer classifyId) {
        return mediaDao.getStructures(classifyId);
    }

    /**
     * 听力检索
     * 
     * @param map
     * @return
     */
    public List<MediaQuestionStructure> selectMediaQuestions(Map<String, Object> map) {
        return mediaDao.selectMediaQuestions(map);
    }

    /**
     * 检索当前试题的听力音频
     * 
     * @param source
     * @return
     */
    public MediaModel selectMediaBySource(String source) {
        return mediaDao.selectMediaBySource(source);
    }

    /**
     * 登录听力做题记录
     * 
     * @param mediaQuestions
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void doInsertCollections(List<MediaQuestionStructure> mediaQuestions, UserModel userInfo,
            MediaModel mediaModel, String status) {
        // a.更新听力记录表
        List<TbMediaCollectionBean> list = new ArrayList<TbMediaCollectionBean>();
        // 考试模式需登录考试表
        List<ExamModel> examCollections = new ArrayList<ExamModel>();
        for (MediaQuestionStructure model : mediaQuestions) {
            List<MediaModel> questions = model.getQuestion();
            for (MediaModel question : questions) {
                if (question != null) {
                    TbMediaCollectionBean collection = new TbMediaCollectionBean();
                    ExamModel examModel = new ExamModel();
                    // 音频ID
                    collection.setMediaId(question.getMediaId());
                    // 用户ID
                    collection.setUserId(userInfo.getId());
                    examModel.setUserId(userInfo.getId());
                    // 试题ID
                    collection.setQuestionId(question.getNo());
                    examModel.setNo(question.getNo());
                    // 状态1：已完成
                    collection.setStatus("1");
                    // 用户答案
                    collection.setMyAnswer(question.getMyAnswer());
                    examModel.setMyAnswer(question.getMyAnswer());
                    // 正确答案
                    examModel.setAnswer(question.getAnswer());
                    // 试题来源
                    examModel.setSource(mediaModel.getSource());
                    // StructureId
                    examModel.setStructureId(question.getStructureId());
                    // 题型种别[6:听力]
                    examModel.setExamType("6");
                    list.add(collection);
                    examCollections.add(examModel);
                }
            }
        }
        mediaDao.insertMediaCollections(list);
        // 批量登录考试做题记录表
        if (StringUtils.isNotEmpty(status) && examCollections.size() != 0) {
            examCollectionDao.insertExamCollections(examCollections);
        }
    }
}
