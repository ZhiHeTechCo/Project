package zh.co.item.bank.web.forum.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbForumAskerBean;
import zh.co.item.bank.db.entity.TbForumResponseBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.ForumModel;

@Named
public class ForumDao extends BaseDao {

    /**
     * 用户提问登录
     * 
     * @param model
     * @return
     */
    public void insertForumAsker(TbForumAskerBean model) {
        getIbatisTemplate().insert("TbForumAsker.insertSelective", model);
    }

    /**
     * 登录论坛回答
     */
    public void insertForumResponse(Integer questionId) {
        getIbatisTemplate().insert("Forum.insertForumResponse", questionId);
    }

    /**
     * 根据用户名和问题ID检索当前问题是否已经被提问
     * 
     * @param model
     * @return
     */
    public int selectForumAskerForOne(ForumModel model) {
        return (int) getIbatisTemplate().selectOne("Forum.selectForumAskerForOne", model);
    }

    /**
     * 显示所有尚未解答提问
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ForumModel> selectForumForAll() {
        return getIbatisTemplate().selectList("Forum.selectForumForAll");
    }

    /**
     * 查询当前问题是否已在论坛
     * 
     * @param questionId
     * @return
     */
    public int selectForumForOne(Integer questionId) {
        return (int) getIbatisTemplate().selectOne("Forum.selectForumForOne", questionId);
    }

    /**
     * 显示当前问题
     * 
     * @return
     */
    public ForumModel selectForumByQuestionId(Integer questionId) {
        return (ForumModel) getIbatisTemplate().selectOne("Forum.selectForumByQuestionId", questionId);
    }

    /**
     * 显示当前用户提问
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ForumModel> selectForumByAsker(Integer id) {
        return getIbatisTemplate().selectList("Forum.selectForumByAsker", id);
    }

    /**
     * 点赞
     * 
     * @return
     */
    public int doUp(TbForumResponseBean bean) {
        return getIbatisTemplate().update("Forum.doUp", bean);
    }

    /**
     * 回答问题登录
     * 
     * @param model
     * @return
     */
    public int updateForumResponse(ForumModel model) {
        return getIbatisTemplate().update("Forum.updateForumResponse", model);
    }

    /**
     * 检索所有提问者
     * 
     * @param questionId
     */
    @SuppressWarnings("unchecked")
    public List<TuUserBean> selectAllAsker(Integer questionId) {
        return getIbatisTemplate().selectList("Forum.selectAllAsker", questionId);
    }

    /**
     * 推荐选择答案
     * 
     * @param item
     */
    public void updateSystemChoose(Map<String, Object> param) {
        getIbatisTemplate().update("Forum.updateSystemChoose", param);
    }

    /**
     * 账号合并-更新asker
     * 
     * @param map
     */
    public void updateUserId(Map<String, Object> map) {
        getIbatisTemplate().update("Forum.updateUserId", map);
    }
}
