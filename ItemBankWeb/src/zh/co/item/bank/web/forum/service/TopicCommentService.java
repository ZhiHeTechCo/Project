package zh.co.item.bank.web.forum.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.common.utils.CmnStringUtils;
import zh.co.item.bank.db.entity.TbTopicCommentBean;
import zh.co.item.bank.model.entity.TopicCommentModel;
import zh.co.item.bank.model.entity.TopicListModel;
import zh.co.item.bank.model.entity.TopicModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.forum.dao.TopicCommentDao;
import zh.co.item.bank.web.forum.dao.TopicListDao;

@Named
public class TopicCommentService {

    @Inject
    private TopicCommentDao topicCommentDao;

    @Inject
    private TopicListDao topicListDao;

    /**
     * 根据TopicId检索话题
     * 
     * @param id
     * @return
     */
    public TopicModel selectTopicById(Integer id) {
        TopicModel topicModel = new TopicModel();
        TopicListModel topicListModel = topicListDao.selectTopicListById(id);
        topicModel.setCurrentTopic(topicListModel);
        // 折行设置
        topicModel.setContextList(CmnStringUtils.getSubjectList(topicListModel.getContext()));
        topicModel.setTopicComments(selectCommetsById(id));
        return topicModel;
    }

    /**
     * 发表评论
     * 
     * @param commentBean
     * @param userModel
     * @param topicId
     */
    public void sendComment(TbTopicCommentBean commentBean, UserModel userModel, Integer topicId) {
        // 话题ID
        commentBean.setTopicId(topicId);
        // 评论者
        Integer responser = userModel == null ? 0 : userModel.getId();
        commentBean.setResponser(responser);
        topicCommentDao.insertSelective(commentBean);
    }

    /**
     * 点赞数加一
     * 
     * @param topicId
     */
    public void doUp(Integer topicId) {
        topicCommentDao.updateCount(topicId);
    }

    /**
     * 获取评论
     * 
     * @param id
     * @return
     */
    public List<TopicCommentModel> selectCommetsById(Integer id) {
        return topicCommentDao.selectTopicComment(id);
    }
}
