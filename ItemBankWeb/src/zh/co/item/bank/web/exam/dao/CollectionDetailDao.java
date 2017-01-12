package zh.co.item.bank.web.exam.dao;

import java.util.List;

import javax.inject.Named;
import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbCollectionDetailBean;

@Named
public class CollectionDetailDao extends BaseDao {

    /**
     * 登录做题记录详细
     * 
     * @param collection
     */
    public void insertCollectionDetail(TbCollectionDetailBean collection) {
        getIbatisTemplate().insert("TbCollectionDetail.insertSelective", collection);
    }

    /**
     * 批量登录做题详细
     * @param collectionDetails
     */
    public void insertCollectionDetails(List<TbCollectionDetailBean> collectionDetails) {
        getIbatisTemplate().insert("CollectionDetail.insertDetailCollections", collectionDetails);
    }

}
