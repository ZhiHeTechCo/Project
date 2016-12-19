package zh.co.item.bank.web.file.controller;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import zh.co.common.constant.CmnContants;
import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.prop.PropertiesUtils;
import zh.co.common.utils.FileUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.item.bank.db.entity.TbFileInfoBean;
import zh.co.item.bank.db.entity.TsCodeBean;
import zh.co.item.bank.model.entity.PaginatorLogger;
import zh.co.item.bank.model.entity.RepeatPaginator;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] 文件检索Bean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("fileSearchBean")
@Scope("session")
public class FileSearchBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
    
	@Inject
    private UserService userService;
	
	private TbFileInfoBean fileBean;
	
	private List<TbFileInfoBean> fileList;
    
    private List<TsCodeBean> reviewFlagList;
    
    private RepeatPaginator paginator; 

    private PaginatorLogger paginatorLogger = new PaginatorLogger(logger, SystemConstants.PAGE_ITBK_USER_007, "向前翻页","向后翻页", "指定页"); 


	public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_007;
    }
    
    /**
     * 初期化
     * @return
     */
    public String init() {
    	pushPathHistory("fileManageBean");
		fileBean = new TbFileInfoBean();
		try {
	    	Map<String, Object> map = new HashMap<String, Object>();
	        map.put("modelId", CmnContants.MODELID_BQD0005);
	        map.put("name", "review");
	        reviewFlagList = userService.getCodelist(map);
	        
	        fileBean.setReviewFlag(SystemConstants.REVIEW_FLAG_0);
	        doSearch();
		} catch (Exception e) {
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_USER_007;
    }

	/**
	 * 检索
	 * @return
	 */
	public String doSearch() {
		//检索
        try {
        	fileList = null;
            paginator = null;
            fileList = userService.getFileInfoList(fileBean);
    		paginator = new RepeatPaginator(fileList, paginatorLogger);
    		
    		Map<String, Object> logMap = new LinkedHashMap<String, Object>();
    		logMap.put("审核状态", fileBean.getReviewFlag());
    		paginatorLogger.setParams(logMap);
        } catch (Throwable e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_USER_007;
	}
    
    /**
     * 文件下载
     * @return
     */
    public void doDownLoad(Integer userId, String fileName) {
        try {

            // 文件路径
            String filePath = PropertiesUtils.getInstance().getSgValue(SystemConstants.FILEUPLOAD_PATH);

            String downloadPath = filePath + File.separator + userId + File.separator;
            if (!FileUtils.checkExistFile(downloadPath + fileName)) {

                throw new CmnSysException(MessageId.COMMON_E_0010, new Object[] { fileName });
            }

            try {
                downloadFile(downloadPath, fileName);
            } catch (Exception e) {
                logger.log(MessageId.COMMON_E_0010, new Object[] { fileName }, e);
                throw new CmnSysException(MessageId.COMMON_E_0010, new Object[] { fileName });
            }
        } catch (Throwable e) {
            processForException(logger, e);
        }
    }
    
    /**
     * 更新审核结果
     * @return
     */
    public void doReview(TbFileInfoBean bean) {
        try {

        	TbFileInfoBean updateBean = new TbFileInfoBean();
        	updateBean.setId(bean.getId());
        	updateBean.setComment(bean.getComment());
        	updateBean.setReviewFlag(bean.getReviewFlag());
        	
        	int count = userService.updateFileInfo(updateBean);
        	
        	//更新成功
        	if(count == 1) {
        		setMessage(MessageUtils.getMessage(MessageId.COMMON_I_0003, new Object[] { "审核" }), "I");
        	} else {
        		throw new CmnBizException(MessageId.COMMON_I_0003, new Object[] { "审核" });
        	}

        } catch (Throwable e) {
            processForException(logger, e);
        }
    }

	public RepeatPaginator getPaginator() {
		return paginator;
	}

	public void setPaginator(RepeatPaginator paginator) {
		this.paginator = paginator;
	}

	public TbFileInfoBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(TbFileInfoBean fileBean) {
		this.fileBean = fileBean;
	}

	public List<TbFileInfoBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<TbFileInfoBean> fileList) {
		this.fileList = fileList;
	}

	public List<TsCodeBean> getReviewFlagList() {
		return reviewFlagList;
	}

	public void setReviewFlagList(List<TsCodeBean> reviewFlagList) {
		this.reviewFlagList = reviewFlagList;
	}

}
