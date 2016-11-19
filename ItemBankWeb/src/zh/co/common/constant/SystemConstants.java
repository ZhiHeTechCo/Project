package zh.co.common.constant;

/**
 * <p>[概要] 系統常量定义类</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class SystemConstants {

	/**--------------------系统常量----------------------------------
    /** 改行*/
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**文件编码*/
    public static final String FILE_ENCODING = "UTF-8";
    /**页面内容换行*/
    public static final String LINE_SEPERATOR_BR = "<br/>";
    /**微信注册flag*/
    public static final String WECHAT_FLAG = "1";
    /**PC注册flag*/
    public static final String PC_FLAG = "0";

    /**---------------------properties文件key------------------------------
    /**检索最大件数*/
    public static final String PAGE_CONTROL_MAX_SEARCH_COUNT = "page_control_max_search_count";
    /**最大显示件数*/
    public static final String PAGE_CONTROL_MAX_DISPLAY_COUNT = "page_control_max_display_count";
    /**加密串*/
    public static final String DB_ENCRYPTION_PASSWORD = "db_encryption_password";
    /**文件上传大小限制*/
    public static final String FILEUPLOAD_SIZE_LIMIT = "fileupload_size_limit";


    /**---------------------画面ID------------------------------
    /**系统index画面*/
    public static final String PAGE_ITBK_COM_001 = "ITBK_COM_001";
    /**关于我们画面*/
    public static final String PAGE_ITBK_COM_002 = "ITBK_COM_002";
    /**留言板画面*/
    public static final String PAGE_ITBK_COM_003 = "ITBK_COM_003";

    /**用户注册画面*/
    public static final String PAGE_ITBK_USER_001 = "ITBK_USER_001";
    /**用户登录画面*/
    public static final String PAGE_ITBK_USER_002 = "ITBK_USER_002";
    /**个人信息画面*/
    public static final String PAGE_ITBK_USER_003 = "ITBK_USER_003";
    /**修改密码画面*/
    public static final String PAGE_ITBK_USER_004 = "ITBK_USER_004";
    /**错题库*/
	/** 选择题型 */
	public static final String PAGE_ITBK_EXAM_001 = "ITBK_EXAM_001";
	/** 试题库 */
	public static final String PAGE_ITBK_EXAM_002 = "ITBK_EXAM_002";
	/** 结果一览 */
	public static final String PAGE_ITBK_EXAM_003 = "ITBK_EXAM_003";
	/** 试题详细 */
	public static final String PAGE_ITBK_EXAM_004 = "ITBK_EXAM_004";
	/** 错题库 */
	public static final String PAGE_ITBK_EXAM_005 = "ITBK_EXAM_005";
	/** 试题登录 */
	public static final String PAGE_ITBK_QUE_001 = "ITBK_QUE_001";
    
}
