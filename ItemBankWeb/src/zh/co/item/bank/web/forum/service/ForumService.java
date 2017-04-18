package zh.co.item.bank.web.forum.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.common.constant.SystemConstants;
import zh.co.item.bank.db.entity.TbForumAskerBean;
import zh.co.item.bank.db.entity.TbForumResponseBean;
import zh.co.item.bank.db.entity.TbTopicListBean;
import zh.co.item.bank.db.entity.TuPointHistoryBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ForumListModel;
import zh.co.item.bank.model.entity.ForumResponseModel;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.forum.dao.ForumDao;
import zh.co.item.bank.web.forum.dao.TopicListDao;
import zh.co.item.bank.web.user.dao.PointHistoryDao;

/**
 * 听力模块
 * 
 * @author gaoya
 *
 */
@Named
public class ForumService {

    @Inject
    private ForumDao forumDao;

    @Inject
    private PointHistoryDao pointHistoryDao;

    @Inject
    private TopicListDao topicListDao;

    /**
     * 用户提问登录
     * 
     * @param model
     * @param onlyAsker
     * @return
     */
    public void insertForumAsker(TbForumAskerBean bean) {
        // 首次有用户询问此道问题:登录
        if (forumDao.selectForumForOne(bean.getQuestionId()) != 1) {
            forumDao.insertForumAsker(bean);
        }
    }

    /**
     * 显示所有尚未解答提问
     * 
     * @return
     */
    public List<ForumListModel> selectForumForAll() {
        return forumDao.selectForumForAll();
    }

    /**
     * 取当前问题
     * 
     * @return
     */
    public ExamModel selectQuestionByQuestionId(Integer questionId) {
        return forumDao.selectQuestionByQuestionId(questionId);
    }

    /**
     * 显示当前用户提问
     * 
     * @param id
     * @return
     */
    public List<ForumListModel> selectForumByAsker(Integer id) {
        return forumDao.selectForumByAsker(id);
    }

    /**
     * 点赞
     * 
     * @return
     */
    public int doUp(Integer id) {
        return forumDao.doUp(id);
    }

    /**
     * 回答问题登录
     * 
     * @param model
     * @return
     */
    public void insertResponse(TbForumResponseBean newResponse) {
        // 登录tb_forum_response
        forumDao.insertResponse(newResponse);
        // 更新tb_forum_asker
        Map<String, Object> param = new HashMap<String, Object>();
        // 已回答
        param.put("status", "1");
        param.put("questionId", newResponse.getQuestionId());
        forumDao.updateStatus(param);

        // 积分
        if (newResponse.getResponser() != 0) {
            TuPointHistoryBean bean = new TuPointHistoryBean();
            bean.setUserId(newResponse.getResponser());
            bean.setEvent(SystemConstants.EVENT_002);
            pointHistoryDao.insertPointHistory(bean);
        }
    }

    /**
     * 检索所有提问者
     * 
     * @param questionId
     */
    public String selectAllAsker(Integer questionId) {
        List<TuUserBean> nickNames = forumDao.selectAllAsker(questionId);
        String tmp = null;
        for (TuUserBean user : nickNames) {
            if (user != null) {
                // 画面上显示所有提问者，用;隔开
                tmp = tmp == null ? user.getNickName() : tmp + SystemConstants.SEMIKOMA + user.getNickName();
            }
        }
        return tmp;
    }

    /**
     * 管理員选择答案
     * 
     * @param item
     */
    public void updateSystemChoose(Map<String, Object> param) {
        forumDao.updateSystemChoose(param);
    }

    /**
     * 根据试题ID取所有回答
     * 
     * @param id 试题ID
     * @return
     */
    public List<ForumResponseModel> selectResponseByQuestionId(Integer id) {
        return forumDao.selectResponseByQuestionId(id);
    }

    /**
     * 登录一条话题并返回ID
     * 
     * @param tbTopicListBean
     * @param userInfo
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer insertTopic(TbTopicListBean tbTopicListBean, UserModel userInfo) {
        Integer owner = userInfo == null ? 0 : userInfo.getId();
        tbTopicListBean.setOwner(owner);
        if (StringUtils.isEmpty(tbTopicListBean.getTitle())) {
            tbTopicListBean.setTitle(null);
        }
        topicListDao.insertSelective(tbTopicListBean);
        return topicListDao.getLastInsertId();
    }

}
