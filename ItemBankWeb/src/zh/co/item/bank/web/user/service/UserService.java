package zh.co.item.bank.web.user.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import zh.co.common.constant.SystemConstants;
import zh.co.common.dao.CodeDao;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.item.bank.db.entity.TbFileInfoBean;
import zh.co.item.bank.db.entity.TbFirstLevelDirectoryBean;
import zh.co.item.bank.db.entity.TbMediaQuestionBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.user.dao.UserDao;

/**
 * <p>
 * [概要] UserService.
 * </p>
 * <p>
 * [详细]
 * </p>
 * <p>
 * [备考]
 * </p>
 * <p>
 * [环境] JRE7.0
 * </p>
 * 
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
    
    @Inject
    private AccountBindingService accountBindingService;

    /**
     * 取得Codelist
     * 
     * @return list
     */
    public List<TsCodeBean> getCodelist(Map<String, Object> map) throws Exception {
        return codeDao.selectCode(map);
    }

    /**
     * 用户注册
     * 
     * @return 登录件数
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int InsertUserInfo(TuUserBean userInfo) throws Exception {
        int count = 0;
        // check当前用户名是否已经存在
        TuUserBean newUser = new TuUserBean();
        newUser.setName(userInfo.getName());
        if (userDao.isUserExist(newUser)) {
            // 存在的场合，报重复message
            logger.log(MessageId.ITBK_E_0001);
            CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0001);
            throw ex;
        } else {
            userInfo.setWechat(SystemConstants.PC_FLAG);
            userInfo.setNickName(userInfo.getName());
            // 不存在的场合，登录DB
            count = userDao.insertUserInfo(userInfo);
        }
        return count;
    }

    /**
     * 用户登录
     * 
     * @param userInfo
     * @return
     * @throws Exception
     */
    public UserModel login(TuUserBean userInfo) throws Exception {
        UserModel user = new UserModel();
        // check当前用户是否已经存在
        if (userDao.isUserExist(userInfo)) {
            // 存在的场合，将用户的信息全部检索出来,放在Session里
            user = userDao.getUserInfo(userInfo);
        } else {
            // 不存在的场合，登录DB
            logger.log(MessageId.ITBK_E_0002);
            CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0002);
            throw ex;
        }
        return user;
    }

    /**
     * 取得用户信息
     * 
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int changePassword(UserModel userInfo, String oldPassword, String newPassword) throws Exception {
        int count = 0;
        userInfo.setPassword(oldPassword);
        TuUserBean checkuser = new TuUserBean();
        checkuser.setName(userInfo.getName());
        checkuser.setPassword(userInfo.getPassword());
        if (userDao.isUserExist(checkuser)) {
            // 存在的场合，更新密码
            TuUserBean newUser = new TuUserBean();
            // ID
            newUser.setId(userInfo.getId());
            // 新密码
            newUser.setPassword(newPassword);
            // 更新时间
            newUser.setUpdateTime(new Date());
            count = userDao.updateUserInfo(newUser);

        } else {
            // 不存在的场合，
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateUserInfo(UserModel userInfo) throws Exception {
        int count = 0;
        TuUserBean newUser = new TuUserBean();
        newUser.setId(userInfo.getId());
        // 昵称
        newUser.setNickName(userInfo.getNickName());
        // 邮箱
        newUser.setEmail(userInfo.getEmail());
        // 手机
        newUser.setTelephone(userInfo.getTelephone());
        // 生日
        newUser.setBirthday(userInfo.getBirthday());
        // 日语等级（JLTP）
        newUser.setJlptLevel(userInfo.getJlptLevel());
        // 日语等级（J.TEST）
        newUser.setJtestLevel(userInfo.getJtestLevel());
        // 更新时间
        newUser.setUpdateTime(new Date());
        count = userDao.updateUserInfo(newUser);
        return count;
    }

    /**
     * wechat用户自动注册登录
     * 
     * @param userInfo
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserModel loginForWechat(TuUserBean userInfo) {
        UserModel user = new UserModel();
        // check当前用户名是否已经存在
        if (userDao.isUserExistForWechat(userInfo)) {
        	//更新用户基本信息
        	TuUserBean newUser = new TuUserBean();
        	newUser.setSex(userInfo.getSex());
        	newUser.setCountry(userInfo.getCountry());
        	newUser.setProvince(userInfo.getProvince());
        	newUser.setCity(userInfo.getCity());
        	newUser.setHeadimgurl(userInfo.getHeadimgurl());
        	newUser.setPrivilege(userInfo.getPrivilege());
        	
            //首先用uuid取数据
            if(!CmnStringUtils.isEmptyStr(userInfo.getUuid())) {
            	logger.debug("开始取得uuid：" + userInfo.getUuid() + " 的数据");
            	List<UserModel> userList = userDao.getUserCountByUuid(userInfo);
            	//取到的数据件数是 大于 1的场合，进行合并
	            if(userList != null && userList.size() > 1) {
	            	logger.debug("件数>1  ：" + userList.size() + " 开始合并");
	            	//更新基本信息
	            	newUser.setId(userList.get(0).getId());
	            	userDao.updateUserInfo(newUser);
	            	//合并
	            	accountBindingService.accountBinging(userList.get(0), userList.get(1));

	            } else if(userList != null && userList.size() == 1){
	            	logger.debug("件数=1");
	            	logger.debug("开始取得openid：" + userInfo.getOpenId() + " 的数据");
	            	//件数等于1的场合
	            	UserModel userDB = userDao.getUserInfo(userInfo);
	            	if(userDB != null && CmnStringUtils.isEmptyStr(userDB.getUuid())) {
	            		logger.debug("该当数据的uuid为空");
		                newUser.setId(userDB.getId());
		                // UnionID
		                newUser.setUuid(userInfo.getUuid());
		                // 更新时间
		                newUser.setUpdateTime(new Date());
		                //更新uuid
		                int count = userDao.updateUserInfo(newUser);
		                logger.debug("该当数据的uuid更新件数：" + count);
		                logger.debug("第二次取得uuid：" + userInfo.getUuid() + " 的数据");
		                userList = userDao.getUserCountByUuid(userInfo);
		                
		                if(userList != null && userList.size() > 1) {
		                	logger.debug("第二次取得uuid：" + userInfo.getUuid() + " 的数据件数：" + userList.size() + " 开始合并");
			                //合并
			            	accountBindingService.accountBinging(userList.get(0), userList.get(1));
		                }

	            	} else {
	            		logger.debug("该当数据的uuid不为空");
	            		//更新信息
	            		newUser.setId(userList.get(0).getId());
	            		userDao.updateUserInfo(newUser);
	            	}

	            } else {
	            	logger.debug("件数=0");
	            	UserModel userDB = userDao.getUserInfo(userInfo);
	            	//件数小于1的场合，更新uuid字段
	                newUser.setId(userDB.getId());
	                logger.debug("id:" + newUser.getId());
	                // UnionID
	                newUser.setUuid(userInfo.getUuid());
	                // 更新时间
	                newUser.setUpdateTime(new Date());
	                //更新uuid
	                int count = userDao.updateUserInfo(newUser);
	                logger.debug("该当数据的uuid更新件数：" + count);
	            }
	            logger.debug("第三次取得uuid：" + userInfo.getUuid() + " 的数据");
            	//取得用户信息
            	user = userDao.getUserCountByUuid(userInfo).get(0);
            	
            } else {
            	user = userDao.getUserInfo(userInfo);
            	//更新信息
            	newUser.setId(user.getId());
            	userDao.updateUserInfo(newUser);
            	
            	user = userDao.getUserInfo(userInfo);
            }
        } else {
            if (!CmnStringUtils.isNickName(userInfo.getNickName())) {
                logger.log(MessageId.ITBK_I_0001, new Object[] { userInfo.getName() });
                userInfo.setNickName(SystemConstants.EMPTY);
            }
            // 不存在的场合，注册，自动登录
            int count = userDao.insertUserInfo(userInfo);
            if (count > 0) {
                logger.log(MessageId.ITBK_I_0001, new Object[] { userInfo.getName() });
                user = userDao.getUserInfo(userInfo);
            } else {
                return null;
            }
        }
        return user;
    }

    /**
     * 微信用户绑定PC登录
     * 
     * @param userInfo
     * @param newPassword
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int bindUserInfo(UserModel userInfo) throws Exception {
        int count = 0;
        TuUserBean newUser = new TuUserBean();

        // 用户
        newUser.setName(userInfo.getName());
        // 如果用户名已经存在
        if (userDao.isUserExist(newUser)) {
            logger.log(MessageId.ITBK_E_0001);
            CmnBizException ex = new CmnBizException(MessageId.ITBK_E_0001);
            throw ex;
        }

        newUser.setId(userInfo.getId());
        // 密码
        newUser.setPassword(userInfo.getPassword());

        // 更新时间
        newUser.setUpdateTime(new Date());
        count = userDao.updateUserInfo(newUser);
        return count;
    }

    /**
     * 更新用户上传试题
     * 
     * @param bean 文件信息
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateFileInfo(TbFileInfoBean bean) throws Exception {
        int count = 0;
        // 更新时间
        bean.setUpdateTime(new Date());
        count = userDao.updateFileInfo(bean);
        return count;
    }

    /**
     * 插入用户上传试题
     * 
     * @param bean 文件信息
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int insertFileInfo(TbFileInfoBean bean) throws Exception {
        int count = 0;
        Date now = new Date();
        // 创建时间
        bean.setCreateTime(now);
        // 更新时间
        bean.setUpdateTime(now);
        count = userDao.insertFileInfo(bean);
        return count;
    }

    /**
     * 插入图片试题
     * 
     * @param bean 文件信息
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateImgInfo(TbFirstLevelDirectoryBean bean) throws Exception {
        int count = 0;
        count = userDao.updateImgInfo(bean);
        return count;
    }

    /**
     * 插入听力图片试题
     * 
     * @param bean 文件信息
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int updateMediaImgInfo(TbMediaQuestionBean bean) throws Exception {
        int count = 0;
        count = userDao.updateMediaImgInfo(bean);
        return count;
    }

    /**
     * 用户上传试题信息
     * 
     * @param bean 文件信息
     * @return List<TbFileInfoBean>
     * @throws Exception
     */
    public List<TbFileInfoBean> getFileInfoList(TbFileInfoBean bean) throws Exception {
        return userDao.getFileInfoList(bean);
    }

    /**
     * 用户上传试题信息
     * 
     * @param bean 文件信息
     * @return TbFileInfoBean
     * @throws Exception
     */
    public TbFileInfoBean getFileInfoById(TbFileInfoBean bean) throws Exception {
        return userDao.getFileInfoById(bean);
    }

    /**
     * 判断用户是否设置密码
     * 
     * @param userInfo 用户信息
     * @return true:密码已设置 false:密码未设置
     * @throws Exception
     */
    public boolean isPasswordExist(Integer userId) throws Exception {
        UserModel user = new UserModel();
        user = userDao.selectUserById(userId);
        if (StringUtils.isEmpty(user.getPassword())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检索所有用户昵称
     */
    public Map<Integer, String> selectUserForNickName() {
        List<TuUserBean> users = userDao.selectUserForNickName();
        Map<Integer, String> userName = new HashMap<>();
        for (TuUserBean user : users) {
            userName.put(user.getId(), user.getNickName());
        }
        return userName;
    }

}
