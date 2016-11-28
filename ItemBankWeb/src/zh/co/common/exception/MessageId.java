package zh.co.common.exception;

public class MessageId {
    // ------------------------Common Message------------------------

    // ------------------------Info------------------------
    /** eventlog */
    public static final String COMMON_I_0001 = "COMMON_I_0001";
    /** 文件上传成功! */
    public static final String COMMON_I_0002 = "COMMON_I_0002";
    
    /** {0}处理执行成功! */
    public static final String COMMON_I_0003 = "COMMON_I_0003";

    // ------------------------Qestion------------------------
    /** 备用 */
    public static final String COMMON_Q_0001 = "COMMON_Q_0001";

    // ------------------------Warnning------------------------
    /** 检索结果件数超过{0}件，为提高检索速度，建议修改检索条件！ */
    public static final String COMMON_W_0001 = "COMMON_W_0002";

    // ------------------------Error------------------------
    /** 系统发生异常，请联系管理员！ */
    public static final String COMMON_E_0001 = "COMMON_E_0001";

    /** 文件读取失败，文件路径：{0} */
    public static final String COMMON_E_0002 = "COMMON_E_0002";

    /** 消息级别读取失败，消息ID：{0} */
    public static final String COMMON_E_0003 = "COMMON_E_0003";

    /** SQL文加载失败，SQLID：{0} */
    public static final String COMMON_E_0004 = "COMMON_E_0004";

    /** 数据库操作失败，请联系管理员！{0} */
    public static final String COMMON_E_0005 = "COMMON_E_0005";

    /** 检索结果件数超过最大限制{0}件，请修改检索条件后再检索！ */
    public static final String COMMON_E_0006 = "COMMON_E_0006";

    /** SQL类型不明，请联系管理员！{0} */
    public static final String COMMON_E_0007 = "COMMON_E_0007";

    /** 上传文件的大小超过最大限制{0}，请压缩文件大小！ */
    public static final String COMMON_E_0008 = "COMMON_E_0008";

    /** Session过期，请重新登录 */
    public static final String COMMON_E_0009 = "COMMON_E_0009";
    
    /** 文件已经被删除！ */
    public static final String COMMON_E_0010 = "COMMON_E_0010";
    
    /** {0}处理执行失败! */
    public static final String COMMON_E_0011 = "COMMON_E_0011";

    // ------------------------业务 Message------------------------

    // ------------------------Info------------------------
    /** 亲爱的{0}，欢迎你加入志合！ */
    public static final String ITBK_I_0001 = "ITBK_I_0001";

    /** 您已退出登录！ */
    public static final String ITBK_I_0002 = "ITBK_I_0002";

    /** 登录 */
    public static final String ITBK_I_0003 = "ITBK_I_0003";

    /** 新密码已经修改成功，请退出以后重新登录！ */
    public static final String ITBK_I_0004 = "ITBK_I_0004";

    /** 信息更新成功！ */
    public static final String ITBK_I_0005 = "ITBK_I_0005";

    /** 错题太少啦，点击页面右上角[用户名]→[试题库]开始做题吧。（注：当天的错题不会被推荐。） */
    public static final String ITBK_I_0006 = "ITBK_I_0006";

    /** 为您查询到以下错题（非记忆曲线推荐，建议先去题库做题）： */
    public static final String ITBK_I_0007 = "ITBK_I_0007";

    /** 为您查询到以下错题（记忆曲线推荐）： */
    public static final String ITBK_I_0008 = "ITBK_I_0008";

    /** 未查询到您的日语等级，我们将无法给您提供最准确的试题推送，建议您先去[用户]→[个人信息]修改日语等级。 */
    public static final String ITBK_I_0009 = "ITBK_I_0009";

    /** 太厉害啦！你已将题库掏空！去复习错题如何？ */
    public static final String ITBK_I_0010 = "ITBK_I_0010";

    /** 您现在是游客身份 ，登陆后可自动获取联络信息。 */
    public static final String ITBK_I_0011 = "ITBK_I_0011";

    /** 联系信息（电话，邮箱任意一项）不能为空。 */
    public static final String ITBK_I_0012 = "ITBK_I_0012";

    /** 留言不能为空。 */
    public static final String ITBK_I_0013 = "ITBK_I_0013";

    /** 感谢您的反馈！ */
    public static final String ITBK_I_0014 = "ITBK_I_0014";

    /** 此类考题已做完，请重新选择或试试智能推题。 */
    public static final String ITBK_I_0015 = "ITBK_I_0015";

    // ------------------------Qestion------------------------
    /** 您确定要立刻结束此次模拟考试么？ */
    public static final String ITBK_Q_0001 = "ITBK_Q_0001";

    // ------------------------Warnning------------------------
    /** 非常抱歉，该用户名已经被使用，请选用其他的用户名进行注册！ */
    public static final String ITBK_W_0001 = "ITBK_W_0001";

    // ------------------------Error------------------------
    /** 非常抱歉，该用户名已经被使用，请选用其他的用户名进行注册！ */
    public static final String ITBK_E_0001 = "ITBK_E_0001";

    /** 用户名或密码错误！ */
    public static final String ITBK_E_0002 = "ITBK_E_0002";

    /** 旧密码错误，请重新输入！ */
    public static final String ITBK_E_0003 = "ITBK_E_0003";

    /** 试题库中尚未收录所选类型的试题，请重新选择或使用智能推题。 */
    public static final String ITBK_E_0004 = "ITBK_E_0004";

    /** 您未选择任何题型，请选择后继续。 */
    public static final String ITBK_E_0005 = "ITBK_E_0005";

    /** 使用考试模式，需要选择考题种别，然后再在弹出的考试级别中选择对应等级 */
    public static final String ITBK_E_0006 = "ITBK_E_0006";
    
    /** 请输入{0}！ */
    public static final String ITBK_E_0007 = "ITBK_E_0007";
}
