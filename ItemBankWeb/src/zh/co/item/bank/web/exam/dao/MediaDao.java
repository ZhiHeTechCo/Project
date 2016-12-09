package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbQuestionClassifyBean;
import zh.co.item.bank.db.entity.TbQuestionStructureBean;
import zh.co.item.bank.model.entity.MediaModel;

/**
 * 听力模块
 * 
 * @author gaoya
 *
 */
@Named
public class MediaDao extends BaseDao {

    /**
     * 获取当前用户未做过或未做完的听力
     * 
     * @param map
     * @return
     */
    public MediaModel getMedia(Map<String, Object> map) {
        return (MediaModel) getIbatisTemplate().selectOne("Media.selectMedia", map);
    }

    /**
     * 检索ClassifyId
     * 
     * @param classifyBean
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Integer> getClssifyId(TbQuestionClassifyBean classifyBean) {
        return getIbatisTemplate().selectList("Media.selectClassifyId", classifyBean);
    }

    /**
     * 检索听力试题
     * 
     * @param mediaId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MediaModel> getMediaQuestions(Integer mediaId) {
        return getIbatisTemplate().selectList("Media.selectQuestionByMediaId", mediaId);
    }

    /**
     * 检索大题目
     * 
     * @param classifyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbQuestionStructureBean> getStructures(Integer classifyId) {
        return getIbatisTemplate().selectList("Media.selectStructuresByClassifyId", classifyId);
    }
}
