package zh.co.item.bank.web.file.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

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
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TbFileInfoBean;
import zh.co.item.bank.db.entity.TbFirstLevelDirectoryBean;
import zh.co.item.bank.db.entity.TbMediaQuestionBean;
import zh.co.item.bank.web.user.service.UserService;

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
    
	@Inject
    private UserService userService;
	
    private UploadedFile uploadFile;
    
    private String fileName;
    
    private String adminFlag;
    
    private Integer imgId;

	private boolean checked;
    

	public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_006;
    }
    
    /**
     * 初期化
     * @return
     */
    public String init() {
    	pushPathHistory("fileManageBean");
    	if("90".equals(WebUtils.getLoginUserInfo().getRole())) {
    		adminFlag = SystemConstants.FLAG_YES;
    	} else {
    		adminFlag = SystemConstants.FLAG_NO;
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
	            if (!PhaseId.INVOKE_APPLICATION.equals(event.getPhaseId())) {
	                event.setPhaseId(PhaseId.INVOKE_APPLICATION);
	                event.queue();
	                logger.debug("第一次上传");
	            } else {
		            uploadFile = event.getFile();
		            fileName = FileUtils.getCurrentFileName(uploadFile.getFileName());
		            
		            //图片导入
		            if(SystemConstants.FLAG_YES.equals(adminFlag)) {
		            	BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputstream());  
		            	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
		                ImageIO.write(bufferedImage, "jpg", outputStream);  
		                byte[] img = outputStream.toByteArray();
		                if(checked) {
		                	TbMediaQuestionBean bean = new TbMediaQuestionBean();
		                	bean.setNo(imgId);
		                	bean.setContextImg(img);
		                	userService.updateMediaImgInfo(bean);
		                } else {
			                TbFirstLevelDirectoryBean bean = new TbFirstLevelDirectoryBean();
			                bean.setId(imgId);
			                bean.setImg(img);
			                userService.updateImgInfo(bean);
		                }
		                
		            } else {
			            String filePath = PropertiesUtils.getInstance().getSgValue(SystemConstants.FILEUPLOAD_PATH)
			            		+ File.separator + WebUtils.getLoginUserId();
			            
			            //文件上传
			            FileUtils.uploadFile(uploadFile.getInputstream(), filePath, fileName);
			
			            //文件检查，是否存在
			            TbFileInfoBean bean = new TbFileInfoBean();
			            bean.setUserId(Integer.valueOf(WebUtils.getLoginUserId()));
			            bean.setFileName(fileName);
			            List<TbFileInfoBean> list = userService.getFileInfoList(bean);
			            //未审核
			        	bean.setReviewFlag(SystemConstants.REVIEW_FLAG_0);
			        	//备考清除
			        	bean.setComment("");
			            if(list != null && list.size() > 0) {
			            	bean.setId(list.get(0).getId());
			            	userService.updateFileInfo(bean);
			            } else {
			            	userService.insertFileInfo(bean);
			            }
		            }
		            setMessage(MessageUtils.getMessage(MessageId.ITBK_I_0005), "I");
            }
        } catch (Throwable e) {
        	fileName = "";
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
            String filePath = PropertiesUtils.getInstance().getSgValue(SystemConstants.FILEUPLOAD_PATH)
            		+ File.separator + WebUtils.getLoginUserId();

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

	public Integer getImgId() {
		return imgId;
	}

	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}

	public String getAdminFlag() {
		return adminFlag;
	}

	public void setAdminFlag(String adminFlag) {
		this.adminFlag = adminFlag;
	}
	
    public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
