package zh.co.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import zh.co.common.constant.SystemConstants;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.CmnMultiBizException;
import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.prop.PropertiesUtils;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.FileUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.PathElement;
import zh.co.item.bank.model.entity.UserModel;

/**
 * <p>[概要] 共通Controller</p>
 * <p>[详细] 所有Controller都要继承该类</p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public abstract class BaseController {

    /**
     * ItemBankLogger
     */
    private CmnLogger logger = CmnLogger.getLogger(BaseController.class);
    
    public static String MESSAGE_LEVEL_INFO = "I";
    
    public static String MESSAGE_LEVEL_WARN = "W";
    
    public static String MESSAGE_LEVEL_ERROR = "E";
    
    public static final String PAGE_FORWARD_ERROR = "page_forward_error";

    /** 文件上传最大限制 */
    private String sizeLimit = CmnStringUtils.getFileSizeByteFromM();
    /** 文件上传最大限制的message */
    private String sizeLimitMessage = MessageUtils.getMessage(MessageId.COMMON_E_0008,
            new Object[] { sizeLimit });
    
    public final static Map<String, String> pageAndControllerMap = new HashMap<String, String>();
    static {
    	pageAndControllerMap.put(SystemConstants.PAGE_ITBK_EXAM_001, "examClassifyBean");
    	pageAndControllerMap.put(SystemConstants.PAGE_ITBK_COM_003, "messageBean");
    	pageAndControllerMap.put(SystemConstants.PAGE_ITBK_USER_003, "userInfoBean");
    	pageAndControllerMap.put(SystemConstants.PAGE_ITBK_EXAM_005, "resumeBean");
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public abstract String init();

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public abstract String getPageId();

    /**
     * <p>[概要]文件下载</p>
     * <p>[备考]</p>
     * 
     * @param path
     * @param fileName
     * @throws Exception
     */
    protected void downloadFile(String path, String fileName) throws Exception {
        downloadFile(path, fileName, fileName);
    }

    /**
     * <p>[概要]文件下载</p>
     * <p>[备考]</p>
     * 
     * @param path
     * @param fileName
     * @param downloadFileName 下载客户端表示的文件名
     * @throws Exception
     */
    protected void downloadFile(String path, String fileName, String downloadFileName) throws Exception {
        try {
            logger.debug("file download");
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.responseComplete();
            String contentType = "application/x-download";

            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            response.setContentType(contentType);
            StringBuffer contentDisposition = new StringBuffer();
            contentDisposition.append("attachment;");
            contentDisposition.append("filename=\"");
            // System.getProperty("file.encoding")
            contentDisposition.append(new String(downloadFileName.getBytes("Windows-31J"), "ISO-8859-1"));
            contentDisposition.append("\"");
            response.setHeader("Content-Disposition", contentDisposition.toString());
            // response.setHeader(
            // "Content-Disposition",
            // contentDisposition.toString());
            // response.setContentType("application/x-download");
            String mimeType = "application/octet-stream-dummy";
            response.setContentType(mimeType);
            ServletOutputStream out = response.getOutputStream();
            byte[] bytes = new byte[0xffff];
            InputStream is = new FileInputStream(new File(path + fileName));
            int b = 0;
            while ((b = is.read(bytes, 0, 0xffff)) > 0) {
                out.write(bytes, 0, b);
            }
            is.close();
            out.flush();
            ctx.responseComplete();
        } catch (Exception e) {
        	logger.debug(e.getMessage(), e);
            throw e;
        }
    }
    

    /**
     * <p>[概要]上传文件</p>
     * <p>[备考]</p>
     * 
     * @param fileName
     * @param in
     * @param path
     * @throws IOException
     */
    public static void uploadFile(InputStream in, String path, String fileName) throws IOException {
    	FileUtils.uploadFile(in, path, fileName);
    }

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     */
    protected void sendRedirect(String externalUrl) throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) extContext.getResponse();
        response.sendRedirect(externalUrl);
    }

    /**
     * <p>[概要]message表示设定</p>
     * <p>[备考]</p>
     *
     * @param message message
     */
    protected static void processForException(CmnLogger logger, Throwable t) {
        if(t instanceof CmnBizException){
            setMessage(((CmnBizException)t).getMessage(), MESSAGE_LEVEL_WARN);
        } else if(t instanceof CmnMultiBizException){
            List<CmnBizException> exList = ((CmnMultiBizException)t).getExceptions();
            StringBuffer messageStr = new StringBuffer();
            for (int i = 0; i < exList.size(); i++) {
                CmnBizException kex = exList.get(i);
                if(i!=0){
                    messageStr.append(SystemConstants.LINE_SEPERATOR_BR);
                }
                messageStr.append(CmnStringUtils.escapeStringHtml(kex.getMessage()));
            }
            setMessage(messageStr.toString(), MESSAGE_LEVEL_WARN, false);
        } else if(t instanceof CmnSysException){
            setMessage(((CmnSysException)t).getMessage(), MESSAGE_LEVEL_ERROR);
        } else if(t instanceof CmnSysException){
            setMessage(((CmnSysException)t).getMessage(), MESSAGE_LEVEL_ERROR);
        } else {
            logger.log(MessageId.COMMON_E_0001, null, t);
            setMessage(MessageUtils.getMessage(MessageId.COMMON_E_0001), MESSAGE_LEVEL_ERROR);
        }
        logger.debug(t.getMessage(), t);
    }
    
    /**
     * <p>[概要]messeage设定</p>
     * <p>[备考]</p>
     *
     * @param message message
     */
    public static void setMessage(String message, String messageLevel) {
        setMessage(message, messageLevel, false);
    }
    
    /**
     * <p>[概要]messeage设定</p>
     * <p>[备考]</p>
     *
     * @param message message
     */
    protected void setMessage(List<String> messageList, String messageLevel) {
        if(messageList == null || messageList.size() == 0){
            return;
        }
        StringBuffer messageStr = new StringBuffer();
        for (int i = 0; i < messageList.size(); i++) {
            if(i!=0){
                messageStr.append(SystemConstants.LINE_SEPERATOR_BR);
            }
            messageStr.append(CmnStringUtils.escapeStringHtml(messageList.get(i)));
        }
        setMessage(messageStr.toString(), messageLevel, false);
    }
    
    /**
     * <p>[概要]messeage设定</p>
     * <p>[备考]</p>
     *
     * @param message message
     */
    private static void setMessage(String message, String messageLevel, boolean escape) {
        if(escape){
            message = CmnStringUtils.escapeStringHtml(message);
        }
        WebUtils.setRequestAttribute(WebUtils.REQUEST_MESSAGE_CONTENT, message);
        if(MESSAGE_LEVEL_INFO.equals(messageLevel)){
            WebUtils.setRequestAttribute(WebUtils.REQUEST_MESSAGE_LEVEL, "infoMessage");
        } else if(MESSAGE_LEVEL_WARN.equals(messageLevel)){
            WebUtils.setRequestAttribute(WebUtils.REQUEST_MESSAGE_LEVEL, "warnMessage");
        } else if (MESSAGE_LEVEL_ERROR.equals(messageLevel)) {
            WebUtils.setRequestAttribute(WebUtils.REQUEST_MESSAGE_LEVEL, "errorMessage");
        }
    }

    /**
     * <p>[概要]check检索件数</p>
     * <p>[备考]</p>
     * 
     * @param countResult
     * @return 続き処理フラグ（真：次の検索処理を続く；偽：次の検索処理を終了）
     */
    protected boolean processForSearchResultCount(int countResult) {
        if (countResult > WebUtils.getMaxSearchCount()) {
            logger.log(MessageId.COMMON_E_0006, new String[] { String.valueOf(WebUtils.getMaxSearchCount()) });
            setMessage(MessageUtils.getMessage(MessageId.COMMON_E_0006,
                    new String[] { String.valueOf(WebUtils.getMaxSearchCount()) }), MESSAGE_LEVEL_ERROR);
            return false;
        } else if (countResult > WebUtils.getMaxSearchDisplayCount()) {
            logger.log(
                    MessageId.COMMON_W_0001,
                    new String[] { String.valueOf(countResult),
                            String.valueOf(WebUtils.getMaxSearchDisplayCount()) });
            setMessage(MessageUtils.getMessage(
                    MessageId.COMMON_W_0001,
                    new String[] { String.valueOf(countResult),
                            String.valueOf(WebUtils.getMaxSearchDisplayCount()) }), MESSAGE_LEVEL_WARN);
        }
        return true;
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param eventId
     * @param outputMap
     */
    protected void eventLog(CmnLogger logger, String eventId, Map<String, Object> outputMap){
        eventLog(logger, getPageId(), eventId, outputMap);
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param eventId
     * @param outputMap
     */
    public static void eventLog(CmnLogger logger, String pageId, String eventId, Map<String, Object> outputMap){
        String output = WebUtils.formatOutput(eventId, outputMap);
        logger.log(MessageId.COMMON_I_0001, new String[] { output });
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param initParamMap
     */
    protected void initLog(CmnLogger logger, Map<String, Object> initParamMap){
        initLog(logger, getPageId(), initParamMap);
    }
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param initParamMap
     */
    public static void initLog(CmnLogger logger, String pageId, Map<String, Object> initParamMap){
        String initParam = WebUtils.formatOutputForInitParam(initParamMap);
        logger.log(MessageId.COMMON_I_0001, new String[] { initParam });
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param pageId
     * @param pageAction
     */
    protected void pushPathHistory(String pageController) {
        String pageId = getPageId();
        pushPathHistory(pageId, pageController);
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param pageId
     * @param pageAction
     */
    protected void pushPathHistory(String pageId, String pageController) {
        String pageTitle = PropertiesUtils.getInstance().getPageTitle(pageId);
        pushPathHistory(pageId, pageTitle, pageController);
    }

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param pageId
     * @param pageTitle
     * @param pageAction
     */
    @SuppressWarnings("unchecked")
    public static void pushPathHistory(String pageId, String pageTitle, String pageController) {
        if (SystemConstants.PAGE_ITBK_COM_001.equals(pageId)) {
            WebUtils.clearPathHistory();
            WebUtils.setSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_ID, pageId);
            WebUtils.setSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_TITLE, pageTitle);
            return;
        }
        List<PathElement> pathHistory = (List<PathElement>) WebUtils.getSessionAttribute(WebUtils.SESSION_PATH_HISTORY);
        PathElement pathElement = new PathElement();
        pathElement.setPageId(pageId);
        pathElement.setPageTitle(pageTitle);
        pathElement.setPageController(pageController);
        if (pathHistory == null) {
            pathHistory = new ArrayList<PathElement>();
            WebUtils.setSessionAttribute(WebUtils.SESSION_PATH_HISTORY, pathHistory);
        } else {
            List<PathElement> tails = new ArrayList<PathElement>();
            for (int i = pathHistory.size() - 1; i >= 0; i--) {
                PathElement tail = pathHistory.get(i);
                tails.add(tail);
                if (tail.getPageId().equals(pageId)) {
                    pathHistory.removeAll(tails);
                    break;
                }
            }
        }
        pathHistory.add(pathElement);
        WebUtils.setSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_ID, pageId);
        WebUtils.setSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_TITLE, pageTitle);
    }
    
    public String getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(String sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public String getSizeLimitMessage() {
        return sizeLimitMessage;
    }

    public void setSizeLimitMessage(String sizeLimitMessage) {
        this.sizeLimitMessage = sizeLimitMessage;
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param controllerName
     * @return
     */
    protected BaseController getController(String controllerName){
        BaseController controller = (BaseController) SpringAppContextManager.getBean(controllerName);
        return controller;
        
    }
    /**
     * 
     * @param pageId
     * @return
     */
    protected BaseController getControllerByPageId(String pageId){
    	String controllerName = pageAndControllerMap.get(pageId);
        BaseController controller = (BaseController) SpringAppContextManager.getBean(controllerName);
        return controller;
        
    }

    /**
     * session中是否存在用户信息
     * @param userInfo
     * @return
     */
    protected boolean checkuser(UserModel userInfo) {
        if (userInfo == null) {
            logger.log(MessageId.COMMON_E_0009);
            CmnBizException ex = new CmnBizException(MessageId.COMMON_E_0009);
            processForException(logger, ex);
            return false;
        }
        return true;
    }
}
