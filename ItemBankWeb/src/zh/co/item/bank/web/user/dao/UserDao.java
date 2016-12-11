package zh.co.item.bank.web.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.util.StringUtils;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TbFileInfoBean;
import zh.co.item.bank.db.entity.TbFirstLevelDirectoryBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.UserModel;

/**
 * <p>[概要] UserDao.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named
public class UserDao extends BaseDao {

    /**
     * 判断用户名是否已经被使用
     * 
     * @param userAccount
     * @return true:已经被占用 false:没有被占用
     */
    public boolean isUserExist(TuUserBean userInfo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", userInfo.getName());
        if (!StringUtils.isEmpty(userInfo.getPassword())) {
            map.put("password", userInfo.getPassword());
        }
        int count = (Integer) getIbatisTemplate().selectOne("UserManage.countUserAccount", map);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断用户名是否已经被使用
     * 
     * @param userAccount
     * @return true:已经被占用 false:没有被占用
     */
    public boolean isUserExistForWechat(TuUserBean userInfo) {

        int count = (Integer) getIbatisTemplate().selectOne("UserManage.countUserAccountForWechat", userInfo);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 登录用户信息
     * 
     * @param userInfo
     * @return
     */
    public int insertUserInfo(TuUserBean userInfo) {
        return getIbatisTemplate().insert("TuUser.insertSelective", userInfo);
    }

    /**
     * 用户信息取得 登录用
     * 
     * @param userInfo
     * @return
     */
    public UserModel getUserInfo(TuUserBean userInfo) {
        return (UserModel) getIbatisTemplate().selectOne("UserManage.getUserInfo", userInfo);
    }

    /**
     * 用户信息取得 信息显示
     * 
     * @param userInfo
     * @return
     */
    public UserModel selectUserById(Integer id) {
        return (UserModel) getIbatisTemplate().selectOne("UserManage.selectUserById", id);
    }

    /**
     * 登录用户信息
     * 
     * @param userInfo
     * @return
     */
    public int updateUserInfo(TuUserBean userInfo) {
        return getIbatisTemplate().update("TuUser.updateByPrimaryKeySelective", userInfo);
    }

    /**
     * 更新用户上传试题
     * 
     * @param userInfo
     * @return
     */
    public int updateFileInfo(TbFileInfoBean bean) {
        return getIbatisTemplate().update("TbFileInfo.updateByPrimaryKeySelective", bean);
    }

    /**
     * 插入用户上传试题
     * 
     * @param userInfo
     * @return
     */
    public int insertFileInfo(TbFileInfoBean bean) {
        return getIbatisTemplate().insert("TbFileInfo.insertSelective", bean);
    }

    /**
     * 插入图片试题
     * 
     * @param bean 文件信息
     * @return
     * @throws Exception
     */
    public int updateImgInfo(TbFirstLevelDirectoryBean bean) {
        return getIbatisTemplate().insert("TbFirstLevelDirectory.updateByPrimaryKeySelective", bean);
    }

    /**
     * 查询用户上传试题
     * 
     * @param userInfo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TbFileInfoBean> getFileInfoList(TbFileInfoBean bean) {
        return (List<TbFileInfoBean>) getIbatisTemplate().selectList("UserManage.getFileInfoList", bean);
    }

    /**
     * 查询用户上传试题
     * 
     * @param userInfo
     * @return
     */
    public TbFileInfoBean getFileInfoById(TbFileInfoBean bean) {
        return (TbFileInfoBean) getIbatisTemplate().selectOne("TbFileInfo.selectByPrimaryKey", bean);
    }

    /**
     * 取得用户名单
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TuUserBean> selectUserForNickName() {
        return getIbatisTemplate().selectList("UserManage.selectUserForNickName");
    }
}
