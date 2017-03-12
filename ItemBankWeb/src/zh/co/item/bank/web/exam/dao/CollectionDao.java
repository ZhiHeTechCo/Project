package zh.co.item.bank.web.exam.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.model.entity.ExamModel;

@Named
public class CollectionDao extends BaseDao {
    /**
     * 检索指定做题记录[用户ID][试题ID]
     * 
     * @param examModel
     * @return
     */
    public TbCollectionBean selectCollectionForOne(ExamModel examModel) {
        return (TbCollectionBean) getIbatisTemplate().selectOne("Collection.selectCollectionForOne", examModel);
    }

    /**
     * 批量登录做题记录
     * 
     * @param collections
     */
    public void insertCollections(List<TbCollectionBean> collections) {
        getIbatisTemplate().insert("Collection.insertCollections", collections);
    }

    /**
     * 更新做题记录
     * 
     * @param collection
     */
    public void updateCollection(TbCollectionBean collection) {
        getIbatisTemplate().update("Collection.updateCollection", collection);
    }

    /**
     * 帐号合并-取不要的旧数据
     * 
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbCollectionBean> selectOldCollectionByUsers(Map<String, Object> param) {
        return getIbatisTemplate().selectList("Collection.selectOldCollectionByUsers", param);
    }

    /**
     * 帐号合并-删除不要的数据
     * 
     * @param deleteList
     */
    public void deleteCollectionOld(List<TbCollectionBean> deleteList) {
        getIbatisTemplate().delete("Collection.deleteCollection", deleteList);
    }

    /**
     * 帐号合并-统一用户ID
     * 
     * @param param
     */
    public void updateCollectionUserId(Map<String, Object> param) {
        getIbatisTemplate().update("Collection.updateCollectionUserId", param);
    }

}
