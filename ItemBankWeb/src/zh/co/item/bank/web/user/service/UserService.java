package zh.co.item.bank.web.user.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import zh.co.common.dao.CodeDao;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.user.dao.UserDao;

/**
 * <p>[概要] UserService.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named
public class UserService {
	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
	
    @Inject
    private CodeDao codeDao;
    
    @Inject
    private UserDao userDao;
    
    /**
     * 取得Codelist
     * 
     * @return　list
     */
    public List<TsCodeBean> getCodelist(Map<String, Object> map) throws Exception {
        return codeDao.selectCode(map);
    }
    
    /**
     * 用户注册
     * 
     * @return　登录件数
     */
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int InsertUserInfo(TuUserBean userInfo) throws Exception {
    	int count = 0;
    	//check当前用户名是否已经存在
    	TuUserBean newUser = new TuUserBean();
    	newUser.setName(userInfo.getName());
    	if(userDao.isUserExist(newUser)) {
    		//存在的场合，报重复message
    		logger.log(MessageId.ITBK_E_0001);
            CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0001);
            throw ex;
    	} else {
    		//不存在的场合，登录DB
    		count = userDao.insertUserInfo(userInfo);
    	}
        return count;
    }
    /**
     * 用户登录
     * @param userInfo
     * @return
     * @throws Exception
     */
    public UserModel login(TuUserBean userInfo) throws Exception {
    	UserModel user = new UserModel();
    	//check当前用户是否已经存在
    	if(userDao.isUserExist(userInfo)) {
    		//存在的场合，将用户的信息全部检索出来,放在Session里
    		user = userDao.getUserInfo(userInfo);
    	} else {
    		//不存在的场合，登录DB
    		logger.log(MessageId.ITBK_E_0002);
    		CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0002);
            throw ex;
    	}
        return user;
    }
    
    /**
     * 取得用户信息
     * @param id
     * @return
     * @throws Exception
     */
    public UserModel getUserInfo(Integer id) throws Exception {
    	UserModel user = new UserModel();
    	user = userDao.selectUserById(id);
        return user;
    }
    
    /**
     * 密码变更
     * 
     * @param userInfo
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws Exception
     */
    public int changePassword(UserModel userInfo, String oldPassword, String newPassword) throws Exception {
    	int count = 0;
    	userInfo.setPassword(oldPassword);
    	TuUserBean checkuser= new TuUserBean();
    	checkuser.setName(userInfo.getName());
    	checkuser.setPassword(userInfo.getPassword());
    	if(userDao.isUserExist(checkuser)) {
    		//存在的场合，更新密码
    		TuUserBean newUser = new TuUserBean();
    		//ID
    		newUser.setId(userInfo.getId());
    		//新密码
    		newUser.setPassword(newPassword);
    		//更新时间
    		newUser.setUpdateTime(new Date());
    		count = userDao.updateUserInfo(newUser);
    		
    	} else {
    		//不存在的场合，
    		logger.log(MessageId.ITBK_E_0003);
    		CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0003);
            throw ex;
    	}
    	return count;
    }
    
    /**
     * 用户信息变更
     * 
     * @param userInfo
     * @param oldPassword
     * @param newPassword
     * @return
     * @throws Exception
     */
    public int updateUserInfo(UserModel userInfo) throws Exception {
    	int count = 0;
    	TuUserBean newUser = new TuUserBean();
    	newUser.setId(userInfo.getId());
    	//邮箱
    	newUser.setEmail(userInfo.getEmail());
    	//手机
    	newUser.setTelephone(userInfo.getTelephone());
    	//生日
    	newUser.setBirthday(userInfo.getBirthday());
    	//日语等级（JLTP）
    	newUser.setJlptLevel(userInfo.getJlptLevel());
    	//日语等级（J.TEST）
    	newUser.setJtestLevel(userInfo.getJtestLevel());
    	//更新时间
    	newUser.setUpdateTime(new Date());
    	count = userDao.updateUserInfo(newUser);
    	return count;
    }
 
}
