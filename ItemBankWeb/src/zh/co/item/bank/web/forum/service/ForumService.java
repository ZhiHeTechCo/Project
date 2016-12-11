package zh.co.item.bank.web.forum.service;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.item.bank.db.entity.TbForumAskerBean;
import zh.co.item.bank.db.entity.TbForumResponseBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.ForumModel;
import zh.co.item.bank.web.forum.dao.ForumDao;

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

    /**
     * 用户提问登录
     * 
     * @param model
     * @param onlyAsker
     * @return
     */
    public void insertForumAsker(ForumModel model, boolean onlyAsker) {
        if (!onlyAsker) {
            forumDao.insertForumResponse(model.getQuestionId());
        }
        TbForumAskerBean bean = new TbForumAskerBean();
        bean.setAsker(model.getAsker());
        bean.setQuestionId(model.getQuestionId());
        forumDao.insertForumAsker(bean);
    }

    /**
     * 根据用户名和问题ID检索当前问题是否已经被提问
     * 
     * @param model
     * @return
     */
    public int selectForumAskerForOne(ForumModel model) {
        return forumDao.selectForumAskerForOne(model);
    }

    /**
     * 显示所有尚未解答提问
     * 
     * @return
     */
    public List<ForumModel> selectForumForAll() {
        return forumDao.selectForumForAll();
    }

    /**
     * 查询当前问题是否已在论坛
     * 
     * @param questionId
     * @return
     */
    public int checkQuestionIsExist(Integer questionId) {
        return forumDao.selectForumForOne(questionId);
    }

    /**
     * 显示当前问题
     * 
     * @return
     */
    public ForumModel selectForumByQuestionId(Integer questionId) {
        return forumDao.selectForumByQuestionId(questionId);
    }

    /**
     * 显示当前用户提问
     * 
     * @param id
     * @return
     */
    public List<ForumModel> selectForumByAsker(Integer id) {
        return forumDao.selectForumByAsker(id);
    }

    /**
     * 点赞
     * 
     * @return
     */
    public int doUp(TbForumResponseBean bean) {
        return forumDao.doUp(bean);
    }

    /**
     * 回答问题登录
     * 
     * @param model
     * @return
     */
    public int updateForumResponse(ForumModel model) {
        return forumDao.updateForumResponse(model);
    }

    /**
     * 检索所有提问者
     * 
     * @param questionId
     */
    public List<TuUserBean> selectAllAsker(Integer questionId) {
        return forumDao.selectAllAsker(questionId);
    }
}
