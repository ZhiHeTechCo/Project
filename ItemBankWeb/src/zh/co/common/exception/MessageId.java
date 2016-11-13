package zh.co.common.exception;

/**
 * <p>[概要] MessageId常量类.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class MessageId {
	//------------------------Common Message------------------------
	
	//------------------------Info------------------------
	/** eventlog */
    public static final String COMMON_I_0001 = "COMMON_I_0001";
    
    
	//------------------------Qestion------------------------    
	/** 备用*/
    public static final String COMMON_Q_0001 = "COMMON_Q_0001";
    
    
	//------------------------Warnning------------------------
	/** 检索结果件数超过{0}件，为提高检索速度，建议修改检索条件！*/
    public static final String COMMON_W_0001 = "COMMON_W_0002";
    
    
	//------------------------Error------------------------
	/** 系统发生异常，请联系管理员！*/
    public static final String COMMON_E_0001 = "COMMON_E_0001";
    
	/** 文件读取失败，文件路径：{0}*/
    public static final String COMMON_E_0002 = "COMMON_E_0002";
    
	/** 消息级别读取失败，消息ID：{0}*/
    public static final String COMMON_E_0003 = "COMMON_E_0003";
    
	/** SQL文加载失败，SQLID：{0}*/
    public static final String COMMON_E_0004 = "COMMON_E_0004";
    
	/** 数据库操作失败，请联系管理员！{0}*/
    public static final String COMMON_E_0005 = "COMMON_E_0005";
    
	/** 检索结果件数超过最大限制{0}件，请修改检索条件后再检索！*/
    public static final String COMMON_E_0006 = "COMMON_E_0006";
    
	/** SQL类型不明，请联系管理员！{0}*/
    public static final String COMMON_E_0007 = "COMMON_E_0007";
    
	/** 上传文件的大小超过最大限制{0}，请压缩文件大小！*/
    public static final String COMMON_E_0008 = "COMMON_E_0008";
    
    
    
    //------------------------业务 Message------------------------
    
    //------------------------Info------------------------
	/** 亲爱的{0}，欢迎你加入志合！*/
    public static final String ITBK_I_0001 = "ITBK_I_0001";
    
	/** 您已退出登录！*/
    public static final String ITBK_I_0002 = "ITBK_I_0002";
    
    /** 登录*/
    public static final String ITBK_I_0003 = "ITBK_I_0003";
    
    /** 新密码已经修改成功，请退出以后重新登录！*/
    public static final String ITBK_I_0004 = "ITBK_I_0004";
    
    /** 信息更新成功！*/
    public static final String ITBK_I_0005 = "ITBK_I_0005";
    
    
	//------------------------Qestion------------------------    
	/** 您确定要立刻结束此次模拟考试么？*/
    public static final String ITBK_Q_0001 = "ITBK_Q_0001";
    
    
	//------------------------Warnning------------------------
	/** 非常抱歉，该用户名已经被使用，请选用其他的用户名进行注册！*/
    public static final String ITBK_W_0001 = "ITBK_W_0001";
    
    
	//------------------------Error------------------------
	/** 非常抱歉，该用户名已经被使用，请选用其他的用户名进行注册！*/
    public static final String ITBK_E_0001 = "ITBK_E_0001";
    /** 用户名或密码错误！*/
    public static final String ITBK_E_0002 = "ITBK_E_0002";
    /** 旧密码错误，请重新输入！*/
    public static final String ITBK_E_0003 = "ITBK_E_0003";
    


}
