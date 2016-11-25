package zh.co.item.bank.web.file.controller;

import java.io.File;

import javax.inject.Named;
import javax.servlet.http.Part;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.prop.PropertiesUtils;
import zh.co.common.utils.FileUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.item.bank.model.entity.PaginatorLogger;
import zh.co.item.bank.model.entity.RepeatPaginator;

/**
 * <p>[概要] 文件管理Bean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("fileManageBean")
@Scope("session")
public class FileManageBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
    
    
    private UploadedFile uploadFile;
    
    private String fileName;
    
    private RepeatPaginator paginator; 

    private PaginatorLogger paginatorLogger = new PaginatorLogger(logger, SystemConstants.PAGE_ITBK_USER_006, "向前翻页","向后翻页", "指定页"); 


	public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_006;
    }
    
    /**
     * 初期化
     * @return
     */
    public String init() {
    	pushPathHistory("fileManageBean");

        return SystemConstants.PAGE_ITBK_USER_006;
    }

	/**
	 * 检索
	 * @return
	 */
	public String doSearch() {
		//检索
        try {

/*            CampaignMangInfoList = null;
            paginator = null;
        	CampaignMangInfoList = campaignCatalogueService.getCampaignMangList(campaignMangInfo);
    		campaignMangInfo.setIsInit(1);
    		paginator = new RepeatPaginator(CampaignMangInfoList, paginatorLogger);*/
        } catch (Throwable e) {
            processForException(logger, e);
        }

        return SystemConstants.PAGE_ITBK_USER_006;
	}
    
    /**
     * 文件上传
     * @param event
     * @return
     */
    public String doUpload(FileUploadEvent event) {
        try {

          
            uploadFile = event.getFile();
            fileName = FileUtils.getCurrentFileName(uploadFile.getFileName());
            
            String filePath = PropertiesUtils.getInstance().getSgValue(SystemConstants.FILEUPLOAD_PATH);
            
            //文件上传
            FileUtils.uploadFile(uploadFile.getInputstream(), filePath, fileName);

            setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0005), "I");
            
        } catch (Throwable e) {
        	
            processForException(logger, e);
        }
        return SystemConstants.PAGE_ITBK_USER_006;
    }
    
    /**
     * 文件下载
     * @return
     */
    public void doDownLoad() {
        try {

            // 文件路径
            String filePath = PropertiesUtils.getInstance().getSgValue(SystemConstants.FILEUPLOAD_PATH);

            String downloadPath = filePath + File.separator;
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

	public UploadedFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadedFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public RepeatPaginator getPaginator() {
		return paginator;
	}

	public void setPaginator(RepeatPaginator paginator) {
		this.paginator = paginator;
	}


}
