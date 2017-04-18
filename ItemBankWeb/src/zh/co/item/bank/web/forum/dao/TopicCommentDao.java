package zh.co.item.bank.web.forum.dao;

import java.util.List;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbTopicCommentBean;
import zh.co.item.bank.model.entity.TopicCommentModel;

/**
 * 评论Dao
 * 
 * @author gaoya
 *
 */
@Named
public class TopicCommentDao extends BaseDao {

    /**
     * 插入一条评论
     * 
     * @param tbTopicCommentBean
     */
    public void insertSelective(TbTopicCommentBean tbTopicCommentBean) {
        getIbatisTemplate().insert("TbTopicComment.insertSelective", tbTopicCommentBean);
    }

    /**
     * 根据TopicId检索评论
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TopicCommentModel> selectTopicComment(Integer id) {
        return getIbatisTemplate().selectList("TopicComment.selectTopicCommentById", id);
    }

    /**
     * 点赞数加一
     * 
     * @param topicId
     */
    public void updateCount(Integer topicId) {
        getIbatisTemplate().update("TopicComment.doUp", topicId);
    }

}
