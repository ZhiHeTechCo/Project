package zh.co.item.bank.web.forum.dao;

import java.util.List;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbTopicListBean;
import zh.co.item.bank.model.entity.TopicListModel;

/**
 * 话题DAO
 * 
 * @author gaoya
 *
 */
@Named
public class TopicListDao extends BaseDao {

    /**
     * 插入一条数据
     * 
     * @param topicListBean
     */
    public void insertSelective(TbTopicListBean topicListBean) {
        getIbatisTemplate().insert("TbTopicList.insertSelective", topicListBean);
    }

    /**
     * 返回登录数据的ID
     */
    public Integer getLastInsertId() {
        return (Integer) getIbatisTemplate().selectOne("TopicList.getLastInsertId");
    }

    /**
     * 根据TopicId检索话题
     * 
     * @param id
     * @return
     */
    public TopicListModel selectTopicListById(Integer id) {
        return (TopicListModel) getIbatisTemplate().selectOne("TopicList.selectTopicListById", id);
    }

    /**
     * 检索全部Topic
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TopicListModel> selectTopicListForAll() {
        return getIbatisTemplate().selectList("TopicList.selectTopicListForAll");
    }
}
