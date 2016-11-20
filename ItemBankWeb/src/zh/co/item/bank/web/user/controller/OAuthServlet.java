package zh.co.item.bank.web.user.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import zh.co.common.constant.SystemConstants;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.SNSUserInfo;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.model.entity.WeixinOauth2Token;
import zh.co.item.bank.web.user.service.UserService;

/**
 * Servlet implementation class OAuthServlet
 */
@WebServlet("/OAuthServlet")
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private UserService userService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OAuthServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        // 用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        
        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 获取网页授权access_token
            WeixinOauth2Token weixinOauth2Token = WebUtils.getOauth2AccessToken("wx83bc8453af909375", "d55015caf4f35e3433c8256371a468a8", code);
            // 网页授权接口访问凭证
            String accessToken = weixinOauth2Token.getAccessToken();
            // 用户标识
            String openId = weixinOauth2Token.getOpenId();
            // 获取用户信息
            SNSUserInfo snsUserInfo = WebUtils.getSNSUserInfo(accessToken, openId);
            
            TuUserBean userInfo = new TuUserBean();
            userInfo.setName(snsUserInfo.getOpenId());
            userInfo.setNickName(snsUserInfo.getNickname());
            userInfo.setWechat(SystemConstants.WECHAT_FLAG);
            ServletContext servletContext = this.getServletContext();  
            WebApplicationContext context =   
                    WebApplicationContextUtils.getWebApplicationContext(servletContext);  
            userService = (UserService) context.getBean("userService"); 
            
            UserModel model = userService.loginForWechat(userInfo);
            if(model != null) {
            	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_INFO, model);
            	WebUtils.setSessionAttribute(WebUtils.SESSION_USER_ID, String.valueOf(model.getId()));
            }

            // 设置要传递的参数
            request.setAttribute("snsUserInfo", snsUserInfo);
            request.setAttribute("state", state);
        }
        // 跳转到index.jsp
        response.sendRedirect(request.getContextPath() + "/xhtml/common/index.xhtml");
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
