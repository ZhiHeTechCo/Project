package zh.co.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;










import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import zh.co.common.constant.SystemConstants;
import zh.co.common.exception.CmnSysException;
import zh.co.common.prop.PropertiesUtils;
import zh.co.item.bank.db.entity.TuUserBean;

/**
 * <p>[概要] WebUtils类</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class WebUtils {

    public static final String SESSION_USER_ID = "session-user-id";

    public static final String SESSION_USER_INFO = "session-user-info";

    public static final String SESSION_MAIN_TOKEN = "session-main-token";

    public static final String SESSION_PAGE_TOKEN = "session-page-token";

    public static final String SESSION_PATH_HISTORY = "session-path-history";

    public static final String SESSION_CURRENT_PAGE_ID = "session-current-page-id";

    public static final String SESSION_CURRENT_PAGE_TITLE = "session-current-page-title";

    public static final String REQUEST_MESSAGE_CONTENT = "request-message-content";

    public static final String REQUEST_MESSAGE_LEVEL = "request-message-level";
    
    private static final ThreadLocal sessionContext = new ThreadLocal();
    
    private static final ThreadLocal sessionUserId = new ThreadLocal();
    
    private static final ThreadLocal sessionUserInfo = new ThreadLocal();
    
    private static final ThreadLocal sessionPageId = new ThreadLocal();
    
    public static void setSessionContext(HttpSession session){
        sessionContext.set(session);
    }
    
    public static void removeSessionContext(){
        sessionContext.remove();
    }
    
    public static void setSessionUserIdContext(String userId){
        sessionUserId.set(userId);
    }
    
    public static void removeSessionUserIdContext(){
        sessionUserId.remove();
    }
    
    public static void setSessionUserInfoContext(TuUserBean userInfo){
        sessionUserInfo.set(userInfo);
    }
    
    public static void removeSessionUserInfoContext(){
        sessionUserInfo.remove();
    }
    
    public static void setSessionPageIdContext(String pageId){
        sessionPageId.set(pageId);
    }
    
    public static void removeSessionPageIdContext(){
        sessionPageId.remove();
    }
    
    public static HttpSession getSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletRequestAttributes ra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (facesContext != null) {
            //JSF
            ExternalContext extContext = facesContext.getExternalContext();
            return (HttpSession) extContext.getSession(true);
        } else if ( ra != null ){
            //SpringMvc
            HttpServletRequest request = ra.getRequest();
            return request.getSession();
        } else {
            //Filter
            return (HttpSession)sessionContext.get();
        }
    }
    
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param attributeName
     * @return
     */
    public static Object getSessionAttribute(String attributeName) {
        return getSession().getAttribute(attributeName);
    }

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param attributeName
     * @param attributeValue
     */
    public static void setSessionAttribute(String attributeName, Object attributeValue) {
        getSession().setAttribute(attributeName, attributeValue);
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param attributeName
     * @param attributeValue
     */
    public static void removeSessionAttribute(String attributeName) {
        getSession().removeAttribute(attributeName);
    }

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public static String getLoginUserId() {
        HttpSession session = getSession();
        if(session == null){
            return (String)sessionUserId.get();
        }
        String loginUserId = String.valueOf(session.getAttribute(WebUtils.SESSION_USER_ID));
        return loginUserId;
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public static TuUserBean getLoginUserInfo() {
        HttpSession session = getSession();
        if(session == null){
            return (TuUserBean)sessionUserInfo.get();
        }
        TuUserBean loginUserInfo = (TuUserBean) session.getAttribute(WebUtils.SESSION_USER_INFO);
        return loginUserInfo;
    }
    
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public static String getCurrentPageId() {
        HttpSession session = getSession();
        if(session == null){
            return (String)sessionPageId.get();
        }
        String currentPageId = (String) session.getAttribute(WebUtils.SESSION_CURRENT_PAGE_ID);
        return currentPageId;
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param attributeName
     * @param attributeValue
     */
    public static void setRequestAttribute(String attributeName, Object attributeValue) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
        request.setAttribute(attributeName, attributeValue);

    }

    

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     */
    public static void clearPathHistory() {
        setSessionAttribute(SESSION_PATH_HISTORY, null);
    }

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param attributeName
     * @return
     */
    public static String getRequestParam(String paramName) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        return extContext.getRequestParameterMap().get(paramName);
    }

    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     */
    public static void invalidateSession(){
        getSession().invalidate();
    }
    
    /**
     * 
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param obj
     * @return
     */
    public static Map<String, Object> transBeanToMap(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new CmnSysException(e);
        }
        return map;
    }

    /**
     * 
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param map
     * @param obj
     */
    public static void transMapToBean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }

        } catch (Exception e) {
            throw new CmnSysException(e);
        }
        return;
    }

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param eventId
     * @param outputMap
     * @return
     */
    public static String formatOutput(String eventId, Map<String, Object> outputMap) {
        StringBuffer sb = new StringBuffer();
        sb.append(eventId).append(" ");
        int count = 0;
        if (outputMap != null) {
            for (String key : outputMap.keySet()) {
                if (count != 0) {
                    sb.append(",").append(" ");
                }
                sb.append(key).append("=").append(outputMap.get(key) == null ? "" : outputMap.get(key));
                count++;
            }
        }
        return sb.toString();
    }

    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param outputMap
     * @return
     */
    public static String formatOutputForInitParam(Map<String, Object> outputMap) {
        StringBuffer sb = new StringBuffer();
        sb.append("INIT");
        int count = 0;
        sb.append("(");
        if (outputMap != null) {
            for (String key : outputMap.keySet()) {
                if (count != 0) {
                    sb.append(",").append(" ");
                }
                sb.append(key).append("=").append(outputMap.get(key) == null ? "" : outputMap.get(key));
                count++;
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * <p>[概要]最大检索件数</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public static Integer getMaxSearchCount() {
        String maxSearchCountStr = PropertiesUtils.getInstance().getSgValue(
        		SystemConstants.PAGE_CONTROL_MAX_SEARCH_COUNT);
        Integer maxSearchCount = maxSearchCountStr == null ? 1000 : Integer.valueOf(maxSearchCountStr);
        return maxSearchCount;
    }

    /**
     * <p>[概要]最大表示件数</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public static Integer getMaxSearchDisplayCount() {
        String maxDisplayCountStr = PropertiesUtils.getInstance().getSgValue(
                SystemConstants.PAGE_CONTROL_MAX_DISPLAY_COUNT);
        Integer maxDisplayCount = maxDisplayCountStr == null ? 20 : Integer.valueOf(maxDisplayCountStr);
        return maxDisplayCount;
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param jsonObject
     * @param key
     * @return
     */
    public static String getJsonStrValue(JSONObject jsonObject, String key){
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            return null;
        }
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param jsonObject
     * @param key
     * @return
     */
    public static Integer getJsonIntValue(JSONObject jsonObject, String key){
        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
            return null;
        }
    }
    
    /**
     * <p>[概要]</p>
     * <p>[备考]</p>
     * 
     * @param jsonObject
     * @param key
     * @return
     */
    public static Long getJsonLongValue(JSONObject jsonObject, String key){
        try {
            return jsonObject.getLong(key);
        } catch (JSONException e) {
            return null;
        }
    }
}
