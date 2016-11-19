package zh.co.item.bank.web.user.controller;

import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import zh.co.common.constant.SystemConstants;
import zh.co.common.controller.BaseController;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.SpringAppContextManager;
import zh.co.common.utils.WebUtils;
import zh.co.item.bank.db.entity.TuUserBean;
import zh.co.item.bank.model.entity.UserModel;
import zh.co.item.bank.web.user.service.UserService;

/**
 * <p>[概要] 登录SignInBean.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
@Named("wechatSignInBean")
@Scope("session")
public class WechatSignInBean extends BaseController {

	private final CmnLogger logger = CmnLogger.getLogger(this.getClass());
	
	private String signature;
	private String timestamp;
	private String nonce;
	private String echostr;

	private String name;
	private String password;
    
    public String getPageId() {
        return SystemConstants.PAGE_ITBK_USER_002;
    }
    
    public WechatSignInBean() {
    	//wechat 网页授权获取用户基本信息
    	try {  
    		FacesContext facesContext = FacesContext.getCurrentInstance();
    		HttpServletRequest request = (HttpServletRequest)facesContext.getExternalContext().getRequest();
    		FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.responseComplete();
    		HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            // 开发者提交信息后，微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带参数  
            signature = request.getParameter("signature");// 微信加密签名（token、timestamp、nonce。）  
            timestamp = request.getParameter("timestamp");// 时间戳  
            nonce = request.getParameter("nonce");// 随机数  
            echostr = request.getParameter("echostr");// 随机字符串  
            // 将token、timestamp、nonce三个参数进行字典序排序  
            String[] params = new String[] { "ItemBankWeb", timestamp, nonce };  
            Arrays.sort(params);  
            // 将三个参数字符串拼接成一个字符串进行sha1加密  
            String clearText = params[0] + params[1] + params[2];  
            String algorithm = "SHA-1";  
            String sign = new String(  
                    Hex.encodeHex(MessageDigest.getInstance(algorithm).digest((clearText).getBytes()), true));  
            // 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信  
            if (signature.equals(sign)) {  
            	response.getWriter().write(echostr);
                return;
            } 

        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    /**
     * 登录画面initial
     * @return
     */
    public String init() {

    	pushPathHistory("wechatSignInBean");
    	return SystemConstants.PAGE_ITBK_USER_002;
    	
    }

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

 
}
