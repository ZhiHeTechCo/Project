package zh.co.item.bank.web.exam.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.web.exam.dao.CollectionDao;

@Named
public class CollectionService {
    @Inject
    private CollectionDao collectionDao;

    public TbCollectionBean selectCollectionForOne(ExamModel examModel) {
        TbCollectionBean collection = collectionDao.selectCollectionForOne(examModel);
        collection = collection == null ? new TbCollectionBean() : collection;
        return collection;
    }

    public void updateCollection(TbCollectionBean collection) {
        collectionDao.updateCollection(collection);
    }

    /**
     * 批量登录做题记录
     * @param insertList
     */
    public void insertCollections(List<TbCollectionBean> insertList) {
        collectionDao.insertCollections(insertList);
    }

}
