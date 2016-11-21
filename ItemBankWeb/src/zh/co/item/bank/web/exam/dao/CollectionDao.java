package zh.co.item.bank.web.exam.dao;

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
     * 登录做题记录
     * 
     * @param collection
     */
    public void insertCollection(TbCollectionBean collection) {
        getIbatisTemplate().insert("Collection.insertCollection", collection);
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
     * 登录考试记录表
     * 
     * @param examModel
     */
    public void insertExamCollection(ExamModel examModel) {
        getIbatisTemplate().insert("ExamCollection.insertExamCollection", examModel);
    }

}
