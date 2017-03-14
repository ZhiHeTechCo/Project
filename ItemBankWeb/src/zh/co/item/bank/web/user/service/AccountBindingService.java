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

import zh.co.item.bank.db.entity.TbCollectionBean;
import zh.co.item.bank.db.entity.TbExamCollectionBean;
import zh.co.item.bank.db.entity.TbMediaCollectionBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.contact.dao.MessageDao;
import zh.co.item.bank.web.exam.dao.CollectionDao;
import zh.co.item.bank.web.exam.dao.ExamCollectionDao;
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

    @Inject
    private ExamCollectionDao examCollectionDao;

    @Inject
    private CollectionDao collectionDao;

    @Inject
    private MediaDao mediaDao;

    @Inject
    private MessageDao messageDao;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private ForumDao forumDao;

    /**
     * 取指定ID用户
     * 
     * @param param
     * @return
     */
    public List<UserModel> selectUserByIds(Integer newUserId,Integer oldUserId) {
        List<Integer> param = new ArrayList<Integer>();
        param.add(oldUserId);
        param.add(newUserId);
        return userDao.selectUserByIds(param);
    }

    /**
     * 检索有相同Uuid的用户
     * @return
     */
    public List<UserModel> selectUserWithSameUuid() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 账号绑定
     * 
     * @param olderUser
     * @param newUser
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void accountBinging(UserModel olderUser, UserModel  newUser) {
        TuUserBean keepUser = new TuUserBean();
        // 较小的id号留用
        keepUser.setId(olderUser.getId());
        // name
        keepUser.setName(getItemValue(olderUser.getName(), newUser.getName()));
        // nick_name
        keepUser.setNickName(getItemValue(olderUser.getNickName(), newUser.getNickName()));
        // password
        keepUser.setPassword(getItemValue(olderUser.getPassword(), newUser.getPassword()));
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
        param.put("newUserId", olderUser.getId());
        param.put("oldUserId", newUser.getId());
        // 1.更新tb_exam_collection
        List<Integer> users = new ArrayList<Integer>();
        users.add(newUser.getId());
        users.add(olderUser.getId());
        updateExamCollection(users);
        // 2.更新tb_collection
        updateCollection(param);
        // 3.更新tb_media_collection
        updateMediaCollection(param);
        // 4.更新tc_message
        updateMessage(param);
        // 6.更新forum
        updateForum(param);
        // 5.更新tu_user
        userDao.updateUserByPrimaryKeySelective(keepUser);
        userDao.deleteUserdeleteByPrimaryKey(newUser.getId());
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
        if (wechatValue == null) {
            return wechatValue;
        } else if (pcValue == null) {
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
    private void updateExamCollection(List<Integer> users) {
        List<TbExamCollectionBean> tbExamCollectionBeans = examCollectionDao.selectExamCollectionByUsers(users);
        List<String> sources = new ArrayList<String>();
        List<TbExamCollectionBean> deleteList = new ArrayList<TbExamCollectionBean>();
        for (TbExamCollectionBean bean : tbExamCollectionBeans) {
            if (sources.contains(bean.getSource())) {
                deleteList.add(bean);
            } else {
                // 最新的一件保留
                sources.add(bean.getSource());
            }
        }

        // 数据删除
        examCollectionDao.deleteExamCollectionOld(deleteList);
    }

    /**
     * 更新tb_collection
     * 
     * @param param
     */
    private void updateCollection(Map<String, Object> param) {
        // OldUser的更新时间小于NewUser更新时间的试题
        List<TbCollectionBean> deleteList = collectionDao.selectOldCollectionByUsers(param);
        // 删除
        collectionDao.deleteCollectionOld(deleteList);
        // 更新NewUser的ID为OldUser的Id
        collectionDao.updateCollectionUserId(param);
    }

    /**
     * 更新tb_media_collection
     * 
     * @param param
     */
    private void updateMediaCollection(Map<String, Object> param) {
        List<TbMediaCollectionBean> deleteList = mediaDao.selectMediaIdByUsers(param);
        // 删除检索到的数据
        mediaDao.deleteMediaCollectionOld(deleteList);
    }

    /**
     * 更新tc_message
     */
    private void updateMessage(Map<String, Object> map) {
        // NewUserId改成OldUser
        messageDao.updateUserId(map);
    }
    
    /**
     * 更新tc_froum
     */
    private void updateForum(Map<String, Object> map){
        forumDao.updateUserId(map);
    }

}
