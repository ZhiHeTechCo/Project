package zh.co.item.bank.web.user.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.SNSUserInfo;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.model.entity.WeixinOauth2Token;
import zh.co.item.bank.web.user.service.UserService;

/**
 * Servlet implementation class OAuthServlet
 */
@WebServlet("/WebOAuthServlet")
public class WebOAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static HashMap<String, Calendar> oAuthCodeCollection = new HashMap<String, Calendar>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebOAuthServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized(this) {
			CmnLogger logger = CmnLogger.getLogger(BaseController.class);
			try {
				logger.debug("微信授权请求参数：" + request.getQueryString());
				request.setCharacterEncoding("utf-8");
		        response.setCharacterEncoding("utf-8");
		
		
		        // 用户同意授权后，能获取到code
		        String code = request.getParameter("code");
		        String state = request.getParameter("state");
	
		        //"wx1e2b360eac9f3475", "b434b412fcb306c02f83f8e16e87a637" 志和网页登录
		        // 用户同意授权
		        if (!"authdeny".equals(code) && !oAuthCodeCollection.containsKey(code)) {
		        	Calendar now = Calendar.getInstance();
		        	oAuthCodeCollection.put(code, now);
		            // 获取网页授权access_token
		            WeixinOauth2Token weixinOauth2Token = WebUtils.getOauth2AccessToken("wx1e2b360eac9f3475", "b434b412fcb306c02f83f8e16e87a637", code);
		            if(weixinOauth2Token != null) {
			            // 网页授权接口访问凭证
			            String accessToken = weixinOauth2Token.getAccessToken();
			            // 用户标识
			            String openId = weixinOauth2Token.getOpenId();
			            // 获取用户信息
			            TuUserBean userInfo = WebUtils.getSNSUserInfo(accessToken, openId);
			            userInfo.setWechat(SystemConstants.WECHAT_FLAG);
			            
			            ServletContext servletContext = this.getServletContext();  
			            WebApplicationContext context =   
			                    WebApplicationContextUtils.getWebApplicationContext(servletContext);  
			            UserService userService = (UserService) context.getBean("userService"); 
			            
			            UserModel model = userService.loginForWechat(userInfo);
			            if(model != null) {
			            	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_INFO, model);
			            	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_ID, String.valueOf(model.getId()));
			            }
			            
		            } else {
		            	response.sendRedirect(request.getContextPath() + "/xhtml/common/index.xhtml");
		            }
			        // 跳转到index.jsp
			        BaseController pageController = (BaseController) SpringAppContextManager.getBean("pageTopController");
			        if(pageController != null && WebUtils.getLoginUserInfo() != null) {
				        pageController.init();
	
			        } 
			        response.sendRedirect(request.getContextPath() + "/xhtml/common/index.xhtml");
		        }
		        if(oAuthCodeCollection.containsKey(code)) {
		        	Calendar calendar = Calendar.getInstance();
		        	calendar.add(Calendar.HOUR_OF_DAY, -1);
		        	if(oAuthCodeCollection.get(code).compareTo(calendar) < 0) {
		        		oAuthCodeCollection.remove(code);
		        	}
		        }
			} catch (Exception ex) {
	        	logger.debug(ex.getMessage(), ex);
	        }

        }
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
