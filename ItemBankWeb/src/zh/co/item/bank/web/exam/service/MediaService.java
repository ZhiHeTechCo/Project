package zh.co.item.bank.web.exam.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TbMediaCollectionBean;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.model.entity.MediaModel;
import zh.co.item.bank.model.entity.MediaQuestionStructure;
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
     * 登录做题记录
     * 
     * @param beans
     */
    public void insertMediaCollections(List<TbMediaCollectionBean> beans) {
        mediaDao.insertMediaCollections(beans);
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
}
