package zh.co.item.bank.web.user.dao;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.springframework.util.StringUtils;

import zh.co.common.dao.BaseDao;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.db.entity.VuUserModelBean;

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
     * @param userAccount
     * @return true:已经被占用 false:没有被占用
     */
    public boolean isUserExist(TuUserBean userInfo) {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("account", userInfo.getName());
    	if(!StringUtils.isEmpty(userInfo.getPassword())) {
    		map.put("password", userInfo.getPassword());
    	}
    	int count = (Integer)getIbatisTemplate().selectOne("UserManage.countUserAccount", map);
    	if(count == 0) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    /**
     * 登录用户信息
     * @param userInfo
     * @return
     */
    public int insertUserInfo(TuUserBean userInfo) {
        return getIbatisTemplate().insert("TuUser.insertSelective", userInfo);
    }
    
    /**
     * 用户信息取得 登录用
     * @param userInfo
     * @return
     */
    public TuUserBean getUserInfo(TuUserBean userInfo) {
    	return (TuUserBean)getIbatisTemplate().selectOne("UserManage.getUserInfo", userInfo);
    }
    
    /**
     * 用户信息取得 信息显示
     * @param userInfo
     * @return
     */
    public VuUserModelBean selectUserById(Integer id) {
    	return (VuUserModelBean)getIbatisTemplate().selectOne("UserManage.selectUserById", id);
    }
    
    /**
     * 登录用户信息
     * @param userInfo
     * @return
     */
    public int updateUserInfo(TuUserBean userInfo) {
        return getIbatisTemplate().insert("TuUser.updateByPrimaryKeySelective", userInfo);
    }

}
