package zh.co.item.bank.web.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.user.controller.SignInBean;

/**
 * <p>[概要] AccessFilter.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class AccessFilter implements Filter {

    // Logger
    private CmnLogger logger = CmnLogger.getLogger(AccessFilter.class);

    /**
     * 
     * <p>
     * [概要] Token生成
     * </p>
     * <p>
     * [详细] Token生成
     * </p>
     * <p>
     * [区分] 生成
     * </p>
     * <p>
     * [备考]
     * </p>
     * 
     * @param session
     *                HttpSession
     * @return String 毫秒
     */
    public static String getNewToken(HttpSession session) {
        long token = System.currentTimeMillis();
        session.setAttribute("mytoken", token);
        return String.valueOf(token);
    }

    /**
     * 
     * <p>
     * [概要] 取得Token
     * </p>
     * <p>
     * [详细] 取得Token
     * </p>
     * <p>
     * [区分] 读取
     * </p>
     * <p>
     * [备考]
     * </p>
     * 
     * @param session
     *                HttpSession
     * @return String セッションに登録されたトークン
     */
    public static String getToken(HttpSession session) {
        return session.getAttribute("mytoken").toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * 取得uuid
     * @return
     */
    private synchronized String generateTokenId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private String getNewUrlForToken(HttpServletRequest request, String token) {
        StringBuffer url = request.getRequestURL();
        url.append('?');
        if (request.getQueryString() != null) {
            url.append(request.getQueryString());
        }
        url.append('&').append("token=").append(token);
        return url.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        HttpSession session = request.getSession();
        WebUtils.setSessionContext(session);
        

        String URI = request.getRequestURI();
        String URL = request.getRequestURL().toString();
        String path = request.getContextPath();
        String method = request.getMethod();

        // System.out.println("URI:" + URI + ";URL:" + URL + ";path:" +
        // path+";queryString:"+request.getQueryString());
        logger.debug("URI:" + URI);
        logger.debug("URL:" + URL);
        logger.debug("ContextPath:" + path);
        
        if (URI.contains(".css.") || URI.contains(".js.") || URI.contains("javax.faces.resource")) {
            chain.doFilter(arg0, arg1);
            return;
        } else if (URI.endsWith("/xhtml/common/page_forward_error.xhtml")) {
            chain.doFilter(arg0, arg1);
            return;
        } 
        Object agentFlag = WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT);
        if ("GET".equals(method)) {
        	if(!SystemConstants.AGENT_FLAG.equals(agentFlag)) {
        		if (URI.endsWith("/index.xhtml") 
        				|| URI.endsWith("/home.xhtml")
        				|| URI.endsWith(path + "/")) {
                    
                    session.removeAttribute(WebUtils.SESSION_PATH_HISTORY);
                    session.removeAttribute(WebUtils.SESSION_CURRENT_PAGE_ID);
                    session.removeAttribute(WebUtils.SESSION_CURRENT_PAGE_TITLE);
                    /*session.removeAttribute(WebUtils.SESSION_PAGE_TOKEN);
                    String newToken = this.generateTokenId();
                    logger.debug("index reset Token as :" + newToken);
                    session.setAttribute(WebUtils.SESSION_PAGE_TOKEN, newToken);*/
                } else if( !URI.endsWith("/signIn.xhtml")){
                	response.sendRedirect(path + "/xhtml/common/index.xhtml");
                    return;
                }
        	}
        	
        }
        else {
        	UserModel userInfo =  WebUtils.getLoginUserInfo();
        	if(userInfo == null) {

        		if (!URI.endsWith("/index.xhtml") 
        				&& !URI.endsWith("/home.xhtml")
        				&& !URI.endsWith("/signIn.xhtml")
        				&& !URI.endsWith("/signUp.xhtml") 
        				&& !URI.endsWith("/Forum.xhtml")
        				&& !URI.endsWith("/ForumQuestionDetail.xhtml")
        				&& !URI.endsWith("/contact.xhtml")) {
	        		SignInBean signInBean = (SignInBean) SpringAppContextManager.getBean("signInBean");
	                signInBean.init();
	        		response.sendRedirect(path + "/xhtml/user/signIn.xhtml");
	                return;
	                
        		} else {
            		String param = request.getParameter("pageController");
            		//试题库、错题库
        			if("examIndexController".equals(param) || "resumeBean".equals(param)) {
        				if("examIndexController".equals(param)) {
        					WebUtils.setSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_ID, SystemConstants.PAGE_ITBK_EXAM_000);
        				} else {
        					WebUtils.setSessionAttribute(WebUtils.SESSION_CURRENT_PAGE_ID, SystemConstants.PAGE_ITBK_EXAM_005);
        				}
    	        		SignInBean signInBean = (SignInBean) SpringAppContextManager.getBean("signInBean");
    	                signInBean.init();
    	        		response.sendRedirect(path + "/xhtml/user/signIn.xhtml");
    	                return;
        			}
        		}
        	}
/*            String tokenValidator = request.getParameter("tokenValidator");
            String pageToken = request.getParameter("pageToken");
            if ("1".equals(tokenValidator)) {
                String sessionPageToken = (String) session.getAttribute(WebUtils.SESSION_PAGE_TOKEN);
                logger.debug("sessionPageToken :" + sessionPageToken);
                logger.debug("pageToken :" + pageToken);
                if(sessionPageToken == null) {
                	response.sendRedirect(request.getContextPath() + "/xhtml/common/index.xhtml");
                    return;
                }
                if (StringUtils.isNotBlank(sessionPageToken) && !sessionPageToken.equals(pageToken)) {
                    response.sendRedirect(request.getContextPath() + "/xhtml/common/page_forward_error.xhtml");
                    return;
                } else {
                    String newToken = this.generateTokenId();
                    logger.debug("reset Token as :" + newToken);
                    session.setAttribute(WebUtils.SESSION_PAGE_TOKEN, newToken);
                }
            }*/
        }

        chain.doFilter(arg0, arg1);
        WebUtils.removeSessionContext();
        // HTTP 1.1
        response.setHeader("Cache-Control", "no-store");
        // HTTP 1.0
        response.setHeader("Pragma", "no-cache");
        // prevents caching at the proxy server
        response.setDateHeader("Expires", 0);
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException {
    }

}