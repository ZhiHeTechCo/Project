package zh.co.item.bank.model.entity;

import java.util.Date;

import zh.co.common.constant.SystemConstants;
import zh.co.common.utils.WebUtils;

/**
 * Forum.xhtml列表显示
 * 
 * @author gaoya
 *
 */
public class ForumListModel {

    private Integer id;

    private Integer owner;

    private String status;

    private Date createTime;

    private String statusName;

    private String context;

    private String contextKey;

    private String contextAfter;

    private String mode;

    // 回答评论数
    private int num;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getContext() {
        if (WebUtils.judgeIsMoblie() && context.length() > SystemConstants.FORUM_LIST_LENGTH_MOBILE) {
            // 手机
            context = context.substring(0, SystemConstants.FORUM_LIST_LENGTH_MOBILE - 1) + "...";
        } else if (!WebUtils.judgeIsMoblie() && context.length() > SystemConstants.FORUM_LIST_LENGTH) {
            // PC
            context = context.substring(0, SystemConstants.FORUM_LIST_LENGTH) + "...";
        }
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContextKey() {
        return contextKey;
    }

    public void setContextKey(String contextKey) {
        this.contextKey = contextKey;
    }

    public String getContextAfter() {
        return contextAfter;
    }

    public void setContextAfter(String contextAfter) {
        this.contextAfter = contextAfter;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
