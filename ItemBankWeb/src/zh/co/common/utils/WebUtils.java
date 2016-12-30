package zh.co.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnSysException;
import zh.co.common.log.CmnLogger;
import zh.co.common.prop.PropertiesUtils;
import zh.co.item.bank.model.entity.SNSUserInfo;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.model.entity.WeixinOauth2Token;

/**
 * <p>[概要] WebUtils类</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class WebUtils {
	private static  CmnLogger logger = CmnLogger.getLogger(BaseController.class);
    public static final String SESSION_USER_ID = "session-user-id";

    public static final String SESSION_USER_INFO = "session-user-info";

    public static final String SESSION_MAIN_TOKEN = "session-main-token";

    public static final String SESSION_PAGE_TOKEN = "session-page-token";

    public static final String SESSION_PATH_HISTORY = "session-path-history";

    public static final String SESSION_CURRENT_PAGE_ID = "session-current-page-id";

    public static final String SESSION_CURRENT_PAGE_TITLE = "session-current-page-title";

    public static final String REQUEST_MESSAGE_CONTENT = "request-message-content";

    public static final String REQUEST_MESSAGE_LEVEL = "request-message-level";
    
    public static final String SESSION_USER_AGENT = "session-user-agent";
    
    public final static Map<String, String> controllerAndPageMap = new HashMap<String, String>();
    static {
    	controllerAndPageMap.put("examClassifyController", "/xhtml/examination/ExamClassify.xhtml");
    	controllerAndPageMap.put("resumeBean", "/xhtml/examination/Resume.xhtml");
    	controllerAndPageMap.put("forumController", "/xhtml/forum/Forum.xhtml");
    }
    
    
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
    
    public static void setSessionUserInfoContext(UserModel userInfo){
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
    public static UserModel getLoginUserInfo() {
        HttpSession session = getSession();
        if(session == null){
            return (UserModel)sessionUserInfo.get();
        }
        UserModel loginUserInfo = (UserModel) session.getAttribute(WebUtils.SESSION_USER_INFO);
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
        ServletRequestAttributes ra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (facesContext != null) {
            //JSF
        	ExternalContext extContext = facesContext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
            request.setAttribute(attributeName, attributeValue);
        } else if ( ra != null ){
            //SpringMvc
            HttpServletRequest request = ra.getRequest();
            request.setAttribute(attributeName, attributeValue);
        } 
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
    
    /**
     * 获取网页授权凭证
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     * @throws JSONException 
     */
    public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) throws JSONException {
        WeixinOauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                logger.debug("获取网页授权凭证失败 errcode:{" + errorCode + "} errmsg:{"+ errorMsg + "}", e);
                if(42001 == errorCode)  {
                	String accessToken = getWeixinAccessToken(appId, appSecret);
            		if(!StringUtils.isEmpty(accessToken)) {
            			wat = getOauth2AccessToken(appId, appSecret, code);
            		}
                }
            }
        }
        return wat;
    }
    
    private static String getWeixinAccessToken(String appId, String appSecret) {
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        // 获取网页授权凭证
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
        String accessToken = "";
        if (null != jsonObject) {
            try {
            	//access_token超时，获取access_token
            	if("42001".equals(jsonObject.getString("access_token"))) {
            		accessToken = null;
            		int errorCode = jsonObject.getInt("errcode");
                    String errorMsg = jsonObject.getString("errmsg");
                    logger.debug("获取网页授权凭证失败 errcode:{" + errorCode + "} errmsg:{"+ errorMsg + "}");
            	} else {
            		accessToken =  jsonObject.getString("access_token");
            	}

            } catch (Exception e) {
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                logger.debug("获取网页授权凭证失败 errcode:{" + errorCode + "} errmsg:{"+ errorMsg + "}", e);

            }
        }
        return accessToken;
    }
    /**
     * 发送https请求
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    private static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            logger.debug("微信请求返回：" + buffer.toString());
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
        	logger.debug("连接超时：{}", ce);
        } catch (Exception e) {
        	logger.debug("https请求异常：{}", e);
        }
        return jsonObject;
    }
    
    /**
     * 通过网页授权获取用户信息
     * 
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
    @SuppressWarnings( { "deprecation", "unchecked" })
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        SNSUserInfo snsUserInfo = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                snsUserInfo = new SNSUserInfo();
                // 用户的标识
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex(jsonObject.getInt("sex"));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // 用户特权信息
                snsUserInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));
            } catch (Exception e) {
                snsUserInfo = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                logger.debug("获取用户信息失败 errcode:{" + errorCode + "} errmsg:{"+ errorMsg + "}", e);
            }
        }
        return snsUserInfo;
    }
    
    /**
     * 判断 android、iphone等手机浏览器
     * @param request
     * @return
     */
    public static boolean judgeIsMoblie() {
    	boolean isMoblie = false;
    	
    	try{
    	FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
		
		String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			for (String mobileAgent : mobileAgents) {
				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = true;
					break;
				}
			}
		}
    	} catch (Exception e) {
    		logger.debug("浏览器判断失败:" + e.getMessage(), e);
    	}
		return isMoblie;
	}
}
