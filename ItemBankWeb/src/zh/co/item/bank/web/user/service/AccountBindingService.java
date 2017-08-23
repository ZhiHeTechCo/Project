package zh.co.item.bank.web.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zh.co.common.log.CmnLogger;
import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.db.entity.TbMediaCollectionBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.contact.dao.MessageDao;
import zh.co.item.bank.web.exam.dao.CollectionDao;
import zh.co.item.bank.web.exam.dao.CollectionDetailDao;
import zh.co.item.bank.web.exam.dao.ErrorReportDao;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
import zh.co.item.bank.web.exam.dao.ExamDropoutDao;
import zh.co.item.bank.web.exam.dao.MediaDao;
import zh.co.item.bank.web.forum.dao.ForumDao;
import zh.co.item.bank.web.user.dao.UserDao;

/**
 * 账号绑定
 * 
 * @author gyang
 *
 */
@Named
public class AccountBindingService {

    private final CmnLogger logger = CmnLogger.getLogger(this.getClass());

    @Inject
    private ExamCollectionDao examCollectionDao;

    @Inject
    private CollectionDao collectionDao;

    @Inject
    private CollectionDetailDao collectionDetailDao;

    @Inject
    private MediaDao mediaDao;

    @Inject
    private MessageDao messageDao;

    @Inject
    private UserDao userDao;

    @Inject
    private ForumDao forumDao;

    @Inject
    private ExamDropoutDao examDropoutDao;

    @Inject
    private ErrorReportDao errorReportDao;

    /**
     * 取指定ID用户
     * 
     * @param param
     * @return
     */
    public List<UserModel> selectUserByIds(Integer newUserId, Integer oldUserId) {
        List<Integer> param = new ArrayList<Integer>();
        param.add(oldUserId);
        param.add(newUserId);
        return userDao.selectUserByIds(param);
    }

    /**
     * 检索有相同Uuid的用户
     * 
     * @return
     */
    public List<UserModel> selectUserWithSameUuid() {
        return userDao.selectUserWithSameUuid();
    }

    /**
     * 账号绑定
     * 
     * @param olderUser
     * @param newUser
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void accountBinging(UserModel olderUser, UserModel newUser) {
        TuUserBean keepUser = new TuUserBean();
        // 较小的id号留用
        keepUser.setId(olderUser.getId());
        // name
        keepUser.setName(getItemValue(olderUser.getName(), newUser.getName()));
        // nick_name
        keepUser.setNickName(getItemValue(olderUser.getNickName(), newUser.getNickName()));
        // password TODO
        // keepUser.setPassword(getItemValue(olderUser.getPassword(),
        // newUser.getPassword()));
        // role 取较高级别
        String tmpRole = Integer.valueOf(olderUser.getRole()) > Integer.valueOf(newUser.getRole()) ? olderUser.getRole()
                : newUser.getRole();
        keepUser.setRole(tmpRole);
        // birthday
        keepUser.setBirthday(getItemValue(olderUser.getBirthday(), newUser.getBirthday()));
        // telephone
        keepUser.setTelephone(getItemValue(olderUser.getTelephone(), newUser.getTelephone()));
        // email
        keepUser.setEmail(getItemValue(olderUser.getEmail(), newUser.getEmail()));
        // jlpt_level
        keepUser.setJlptLevel(getItemValue(olderUser.getJlptLevel(), newUser.getJlptLevel()));
        // jtest_level
        keepUser.setJtestLevel(getItemValue(olderUser.getJtestLevel(), newUser.getJtestLevel()));

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("newUserId", newUser.getId());
        param.put("oldUserId", olderUser.getId());
        logger.debug(
                String.format("[uuid][%s]:[userId](%s, %s)", newUser.getUuid(), olderUser.getId(), newUser.getId()));
        // 1.更新tb_exam_collection
        updateExamCollection(param);
        // 2.更新tb_collection, tb_collection_detail
        updateCollection(param);
        // 3.更新tb_media_collection
        updateMediaCollection(param);
        // 4.更新tc_message
        updateMessage(param);
        // 5.更新forum
        updateForum(param);
        // 6.更新tb_exam_dropout
        updateExamDropout(param);
        // 7.更新tb_error_report
        updateErrorReport(param);
        // 8.更新tu_user
        updateUser(keepUser, newUser);

    }

    private String getItemValue(String wechatValue, String pcValue) {
        if (StringUtils.isNotEmpty(wechatValue)) {
            return wechatValue;
        } else if (StringUtils.isNotEmpty(pcValue)) {
            return pcValue;
        } else {
            return null;
        }
    }

    private Date getItemValue(Date wechatValue, Date pcValue) {
        if (wechatValue != null) {
            return wechatValue;
        } else if (pcValue != null) {
            return pcValue;
        } else {
            return null;
        }
    }

    /**
     * 更新tb_exam_collection
     * 
     * @param users
     */
    private void updateExamCollection(Map<String, Object> param) {
        logger.debug("1.更新tb_exam_collection");
        // 更新userId
        examCollectionDao.updateUserId(param);
        // 数据删除（id小的删除）
        examCollectionDao.deleteExamCollectionOld(Integer.parseInt(param.get("oldUserId").toString()));
    }

    /**
     * 更新tb_collection,tb_collection_detail
     * 
     * @param param
     */
    private void updateCollection(Map<String, Object> param) {
        logger.debug("2.更新tb_collection, tb_collection_detail");
        // OldUser的更新时间小于NewUser更新时间的试题
        List<TbCollectionBean> deleteList = collectionDao.selectOldCollectionByUsers(param);
        // 删除
        if (deleteList.size() > 0) {
            logger.debug(String.format("tb_collection删除执行...(%s件删除)", deleteList.size()));
            collectionDao.deleteCollectionOld(deleteList);
        }
        // 更新NewUser的ID为OldUser的Id并保持update_time不变
        collectionDao.updateCollectionUserId(param);
        collectionDetailDao.updateDetailUserId(param);
    }

    /**
     * 更新tb_media_collection
     * 
     * @param param
     */
    private void updateMediaCollection(Map<String, Object> param) {
        logger.debug("3.更新tb_media_collection");
        // 1.检索重复做过的数据
        List<TbMediaCollectionBean> deleteList = mediaDao.selectMediaIdByUsers(param);
        // 2.删除检索到的数据
        if (deleteList.size() > 0) {
            logger.debug(String.format("tb_media_collection删除执行...(%s件删除)", deleteList.size()));
            mediaDao.deleteMediaCollectionOld(deleteList);
        }
        // 更新NewUser的ID为OldUser的Id
        mediaDao.updateUserId(param);
    }

    /**
     * 更新tc_message
     */
    private void updateMessage(Map<String, Object> map) {
        logger.debug("4.更新tc_message");
        // NewUserId改成OldUser
        messageDao.updateUserId(map);
    }

    /**
     * 更新tb_forum
     */
    private void updateForum(Map<String, Object> map) {
        logger.debug("5.更新tb_forum_asker,tb_forum_response");
        forumDao.updateAskerId(map);
        forumDao.updateResponserId(map);
    }

    /**
     * 更新tb_exam_dropout
     * 
     * @param param
     */
    private void updateExamDropout(Map<String, Object> param) {
        logger.debug("6.更新tb_exam_dropout");
        examDropoutDao.updateUserId(param);
    }

    /**
     * 更新tb_error_report
     * 
     * @param param
     */
    private void updateErrorReport(Map<String, Object> param) {
        logger.debug("7.更新tb_error_report");
        errorReportDao.updateUserId(param);
    }

    /**
     * 更新tu_user
     * 
     * @param keepUser
     * @param newUser
     */
    private void updateUser(TuUserBean keepUser, UserModel newUser) {
        logger.debug("8.更新tu_user");
        userDao.deleteUserdeleteByPrimaryKey(newUser.getId());
        userDao.updateUserByPrimaryKeySelective(keepUser);
    }

    /**
     * 更改密码
     */
    public void changePassword(TuUserBean user) {
        userDao.changePasswordForce(user);
    }

    /**
     * 数据整合
     */
    public void updateTbCollection() {
        // 检索不具合数据
        List<TbCollectionBean> collections = collectionDao.selectCollection();
        collectionDao.updateCollections(collections);
    }
}
