package zh.co.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import sun.misc.BASE64Encoder;
import zh.co.common.constant.CmnContants;
import zh.co.common.constant.SystemConstants;
import zh.co.common.prop.PropertiesUtils;
import zh.co.item.bank.model.entity.ExamModel;
import zh.co.item.bank.model.entity.ForumModel;
import zh.co.item.bank.model.entity.MediaModel;
import zh.co.item.bank.model.entity.TbQuestionStructure;

/**
 * <p>[概 要]string process utilities</p>
 * <p>[備 考]</p>
 * 
 * <p></p>
 * @author wangfei
 */
public final class CmnStringUtils {

    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[A-Za-z0-9_]$";
    
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_NICKNAME = "^[\u4e00-\u9fa5_a-zA-Z0-9]+$";
 
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,10}$";
 
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
 
    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
 
    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";
 
    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
 
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
 
    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * constructor
     */
    private CmnStringUtils() {
    }

    /**
     * if the string is end with another string
     * @param strObj the source string object
     * @param ends the end string object
     * @return true or false
     */
    public static boolean endsWithIgnoreCase(String strObj, String ends) {
        return StringUtils.endsWithIgnoreCase(strObj, ends);
    }

    /**
     * if the string is start with another string
     * @param strObj the source string object
     * @param starts the end string object
     * @return true or false
     */
    public static boolean startsWithIgnoreCase(String strObj, String starts) {
        return StringUtils.startsWithIgnoreCase(strObj, starts);
    }

    /**
     * check if the string is empty
     * @param str the string to check
     * @return true if empty
     */
    public static boolean isEmptyStr(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * null string to empty string
     * @param str the string to change
     * @return string if empty to ""
     */
    public static String nullToEmptyStr(String str) {
        if (isEmptyStr(str)) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 結果が n バイトになるように文字列 c1 の右に c2 を埋め込んだ値を求める c1, n [ , c2 ]
     * @param str the string to append
     * @param length the append length
     * @param unit the append symbol
     * @return String 埋め込んだ値
     */
    public static String rpad(String str, int length, String unit) {
        String resultStr = "";
        StringBuffer appendStr = new StringBuffer();
        if (str != null) {
            appendStr.append(str);
        }
        for (int i = 0; i < length; i++) {
            appendStr.append(unit);
        }
        resultStr = appendStr.substring(0, length);
        return resultStr;
    }

    /**
     * 結果が n バイトになるように文字列 c1 の左に c2 を埋め込んだ値を求める c1, n [ , c2 ]
     * @param str the string to append
     * @param length the append length
     * @param unit the append symbol
     * @return String 埋め込んだ値
     */
    public static String lpad(String str, int length, String unit) {
        String resultStr = "";
        StringBuffer appendStr = new StringBuffer();
        for (int i = 0; i < length; i++) {
            appendStr.append(unit);
        }
        int oldLength = 0;
        if (str != null) {
            appendStr.append(str);
            oldLength = str.length();
        }
        resultStr = appendStr.substring(oldLength);
        return resultStr;
    }

    /**
     * 文字列を指定のバイト数に切り捨てる.
     * @param str 切り捨てる文字列
     * @param maxLen バイト数
     * @return 切り捨てた文字列
     */
    public static String cutStr(String str, int maxLen) {

        if (null == str) {
            return "";
        }

        if (str.length() <= maxLen / 2) {
            return str;
        }

        int pos = maxLen / 2 + 1;
        for (; pos <= str.length(); pos++) {
            try {
                int byteLen = str.substring(0, pos).getBytes("MS932").length;
                if (byteLen > maxLen) {
                    break;
                }
            } catch (UnsupportedEncodingException e) {
                // MS932はエラーにならない
            }
        }

        if (pos > str.length()) {
            return str;
        } else {
            return str.substring(0, pos - 1) + "...";
        }
    }

    /**
     * escape indicated chars in string example:to escape the backslash(\) in
     * "111\n33" escapeString("111\n33", new char[]{'\\'}) the result string is
     * "111\\n33"
     * @param str source string
     * @param escChar chars want to be escaped
     * @return string was escaped
     */
    public static String escapeString(String str, char[] escChar) {
        if (str == null || escChar == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < escChar.length; j++) {
                if (c[i] == escChar[j]) {
                    sb.append("\\");
                }
            }
            if (c[i] == '\r') {
                sb.append("\\r");
            } else if (c[i] == '\n') {
                sb.append("\\n");
            } else {
                sb.append(c[i]);
            }
        }
        return sb.toString();
    }

    public static String escapeStringHtml(String str, char[] escChar) {
        if (str == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            boolean meet = false;
            for (int j = 0; j < escChar.length; j++) {
                if (c[i] == escChar[j]) {
                    switch (escChar[j]) {
                    case '\\':
                        sb.append("&#92;");
                        break;
                    case '\'':
                        sb.append("&#39;");
                        break;
                    case '"':
                        sb.append("&quot;");
                        break;
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    case ' ':
                        sb.append("&nbsp;");
                        break;
                    default:
                        sb.append(c[i]);
                        break;
                    }
                    meet = true;
                    break;
                }
            }
            if (!meet) {
                sb.append(c[i]);
            }
        }
        return sb.toString();
    }

    public static String escapeStringHtml(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            switch (c[i]) {
            case '\\':
                sb.append("&#92;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case ' ':
                sb.append("&#32;");
                break;

            default:
                sb.append(c[i]);
                break;
            }
        }
        return sb.toString();
    }

    public static String escapeStringXML(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            switch (c[i]) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&apos;");
                break;

            default:
                sb.append(c[i]);
                break;
            }
        }
        return sb.toString();
    }

    public static Integer objToInteger(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Integer) {
            return (Integer) o;
        }

        if (o instanceof String) {
            String str = (String) o;
            if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
                return null;
            }
            return Integer.valueOf(String.valueOf(o));
        }

        return null;
    }

    public static String replaceStr(String reg, String replacement, String src) {
        Pattern pattern = Pattern.compile(reg);
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(src);
        while (m.find()) {
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String formatIP4(String ipStr) {
        String[] ipv4Array = ipStr.split("\\.");
        // 不要な「0」を削除する
        StringBuffer ipv4Buffer = new StringBuffer();

        for (int i = 0; i < ipv4Array.length; i++) {
            ipv4Buffer.append(Integer.parseInt(ipv4Array[i]));
            if (i != ipv4Array.length - 1) {
                ipv4Buffer.append(".");
            }
        }
        return ipv4Buffer.toString();
    }

    public static String objToStr(Object o) {
        if (o == null) {
            return "";
        }
        return String.valueOf(o);
    }
    
    public static byte[] strToByte(String str){
        byte[] bytes = null;
        try {
            bytes = str.getBytes(SystemConstants.FILE_ENCODING);
        } catch (UnsupportedEncodingException e) {
        }
        return bytes;
    }
    
    public static String byteToStr(byte[] bytes){
        String str = null;
        try {
            str = new String(bytes, SystemConstants.FILE_ENCODING);
        } catch (UnsupportedEncodingException e) {
        }
        return str;
    }
    
    public static String replaceString(String src, String oldReplacement, String newReplacement) {
    	if(src == null || src.trim().length() == 0) {
    		return "";
    	} else {
    		return src.replace(oldReplacement, newReplacement);
    	}
    }
    /**
     * リストをログに出力する
     * @param paramList
     * @return
     */
    public static <T> String toString(List<T> paramList) {
        StringBuilder sb = new StringBuilder();
        for(T item : paramList) {
            if(!isEmptyStr(item.toString())) {
                sb.append(item.toString() + ",");
            }
        }
        if(sb.lastIndexOf(",") > -1) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        return sb.toString();
    }
    /**
     * 半角数字チェック
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str != null && !"".equals(str.trim()))  
            return str.matches("^[0-9]*$");  
        else  
            return false; 
    }
    
    /**
     * 用户名チェック
     * @param str
     * @return
     */
    public static boolean isUserName(String str) {
        if (str != null && !"".equals(str.trim()))  
            return str.matches(REGEX_USERNAME);  
        else  
            return false; 
    }
    
    /**
     * 昵称チェック
     * @param str
     * @return
     */
    public static boolean isNickName(String str) {
        if (str != null && !"".equals(str.trim()))  
            return str.matches(REGEX_NICKNAME);  
        else  
            return false; 
    }
    
    /**
     * 手机号チェック
     * @param str
     * @return
     */
    public static boolean isPhoneNumber(String str) {
        if (str != null && !"".equals(str.trim()))  
            return str.matches(REGEX_MOBILE);  
        else  
            return false; 
    }
    
    /**
     * 邮箱チェック
     * @param str
     * @return
     */
    public static boolean isMail(String str) {
        if (str != null && !"".equals(str.trim()))  
            return str.matches(REGEX_EMAIL);  
        else  
            return false; 
    }
    
    /**
     * 密码チェック
     * @param str
     * @return
     */
    public static boolean isPassword(String str) {
        if (str != null && !"".equals(str.trim()))  
            return str.matches(REGEX_PASSWORD);  
        else  
            return false; 
    }
    
    /**
     * 半角英数字チェックREGEX_PASSWORD
     * @param str
     * @return
     */
    public static boolean isNumericOrChar(String str) {
        if (str != null && !"".equals(str.trim()))  
            return str.matches("^[A-Za-z0-9]*$");  
        else  
            return false; 
    }
    
    /**
     * 指定した文字列が全角文字のみか判断する
     *
     * @param source 対象文字列
     * @return trueなら全角文字のみ 空の場合は常にtrueとなる
     */
    public static boolean isZenkakuOnly(String source) {
        if (source != null && !"".equals(source.trim()))  
            return source.matches("^[^ -~｡-ﾟ]+$");  
        else  
            return false; 
    }
    
    /**
     * 指定した文字列が半角文字のみか判断する
     *
     * @param source 対象文字列
     * @return trueなら半角文字のみ 空の場合は常にtrueとなる
     */
    public static boolean isSimpleHankakuOnly(String source) {
    	char[] chars = source.toCharArray();
    	for (char c : chars) {
    		if (!((c >= '\u0020' && c <= '\u007e') || (c >= '\uff61' && c <= '\uff9f'))) {
                return false;
    		}
    	}
    	return true;
    }
    

    public static boolean equals(String str1, String str2) {
        if ((str1 == null || str1 == "") && (str2 != null && str2 != ""))  
            return false;
        else if((str2 == null || str2 == "") && (str1 != null && str1 != "")) {
            return false;
        } else if ((str1 == null || str1 == "") && (str2 == null || str2 == "")){
            return true;
        } else if(str1 != null && str2 != null && str1.equals(str2)) {
            return true;
        }
        return false; 
    }

    /**
     * 3桁ごとに、,を付与して表示する。
     * @param arg2
     * @return
     */
    public static String setCommaToStr(Object arg2) {
        if(arg2==null ){
            return null;
        }
        String arg = getStrWithNoComma(arg2.toString());

        // 符号があるのを判定する
        String sign = null;
        Pattern p = Pattern.compile("(-?)[1-9]d*");
        Matcher m = p.matcher(arg);
        if (m.find()) {
            sign = m.group(1);
        }

        if (!CmnStringUtils.isEmptyStr(sign) && arg.contains(sign)) {
            arg = arg.substring(arg.indexOf(sign) + 1);
        }

        String value = new StringBuilder(arg).reverse().toString();
        StringBuilder sb = new StringBuilder();  
        for(int i=0;i<value.length();i+=3){  
            if(i+3>value.length()){  
                sb.append(value.substring(i, value.length()));  
                break;
            }
                sb.append(value.substring(i, i+3)+",");
        }
        String newValue = sb.toString();
        if(newValue.endsWith(",")){
            newValue = newValue.substring(0, newValue.length()-1);  
        }
        String result = new StringBuilder(newValue).reverse().toString();
        if (!CmnStringUtils.isEmptyStr(sign)) {
            result = sign + result;
        }
        return result;
    }

    public static String getStrWithNoComma(String arg2) {
        return arg2.replace(",", "");
    }
    
    /**
     * <p>[概 要]レコードのフォーマットチェック</p>
     * <p>[備 考]</p>
     * 
     * @param data
     * @return チェック結果
     */
	public static boolean checkDataFormat (String pattern, String[] data) {
		//String a = "\"[\\s\\S]*\"";
		Pattern p = Pattern.compile(pattern); 

		List<String> data1 = Arrays.asList(data);
		for (String col:data1) {
			Matcher m = p.matcher(col);
			if (!m.find()){
				return false;
			}
		}
		return true;
	}
	
    /**
     * <p>[概 要]文字列にクオーテーションを付ける</p>
     * <p>[備 考]</p>
     * 
     * @param str
     * @return String
     */
    public static String setQuotationToStr(String str) {
        if(str == null ){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(str).append("\"");
        return sb.toString();
    }
    
    /**
     * <p>[概 要]文字列にクオーテーションを 取り除く</p>
     * <p>[備 考]</p>
     * 
     * @param obj
     * @return String
     */
    public static String trimQuotationFromStr(Object obj) {
        if(obj == null ){
            return null;
        }
        String str = obj.toString();
        if (str.indexOf("\"") > -1) {
        	str = str.substring(str.indexOf("\"") + 1);
        }
        if (str.lastIndexOf("\"") > -1) {
        	str = str.substring(0, str.lastIndexOf("\""));
        }
        return str;
    }
    
    /**
     * <p>[概 要]</p>
     * <p>[備 考]</p>
     * 
     * @param srcStr
     * @return
     */
    public static String prepareForSqlParamValue(String srcStr){
        if(srcStr == null){
            return "";
        }
        return srcStr.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\%", "\\\\%").replaceAll("\\_", "\\\\_");
    }

    /**
     * 全角数字->　半角数字 
     */  
    public static String full2Half(String src) {
        if (src == null) {  
            return "";  
        }  
        StringBuilder buf = new StringBuilder(src.length());  
        char[] ca = src.toCharArray();  
        for (int i = 0; i < src.length(); i++) {  
            if (ca[i] >= 65281 && ca[i] <= 65374) {
                buf.append((char) (ca[i] - 65248)); 
            // 全角スベース
            } else if (ca[i] == 12288) { // 如果是全角空格  
                buf.append(" ");  
            } else {
                buf.append(ca[i]);  
            }  
        }
        return buf.toString();  
    }

    /**
     * XX月XX日->MMDD
     * @param date
     * @return
     */
    public static String[] changeFormatForMMDD(String date) {
        String month = "";
        String day = "";
        date = CmnStringUtils.full2Half(date).trim();

        if (CmnStringUtils.isEmptyStr(date)) {
            return null;
        }

        if (!date.contains("月") || !date.contains("日")) {
            return null;
        }

        String mouthStr = date.substring(0, date.indexOf("月")).trim();
        String dayStr = date.substring(date.indexOf("月") + 1, date.indexOf("日")).trim();
        month = (mouthStr.length() == 1) ? "0" + mouthStr : mouthStr;
        day = (dayStr.length() == 1) ? "0" + dayStr : dayStr;

        return new String[]{month, day};
    }
    
    /**
     * 阅读折行显示
     * @param subject
     * @return
     */
	public static List<String> getSubjectList(String subject) {
		List<String> list = new ArrayList<String>();
		if(!StringUtils.isEmpty(subject)) {
			String[] array = subject.split("<br/>");
			list = Arrays.asList(array);
		}
		
		return list;
	}

	public static String getFileSizeByteFromM() {
		String size = PropertiesUtils.getInstance().getSgValue(SystemConstants.FILEUPLOAD_SIZE_LIMIT);
		if(StringUtils.isEmpty(size)) {
			size = "10";
		}
		
		long limit = Integer.valueOf(size) * 1024 * 1024;
		
		return String.valueOf(limit);
	}
	
	/**
	 * 将数据库二进制图片转换为base64编码
	 * @param img
	 * @return
	 */
	public static String getGraphicImage(byte[] img) {
		if(img != null && img.length > 0) {
			BASE64Encoder encoder = new BASE64Encoder();  
	        return "data:image/jpg;base64," + encoder.encode(img);
		} else {
			return "";
		}
	}
	
	/**
	 * 将二进制mp3转换为base64编码
	 * @param 文件路径
	 * 
	 * @return
	 * @throws IOException 
	 */
	public static String getMedia(String path) throws IOException {
		
    	//根据文件路径取得byte[]
    	String filePath = PropertiesUtils.getInstance().getSgValue(SystemConstants.MEDIA_FILE_PATH)
        		+ File.separator + path;
    	 File file = new File(filePath);
    	 if(!file.exists()) {
    		 return SystemConstants.EMPTY;
    	 }
    	 FileInputStream fis = null;
    	 ByteArrayOutputStream bos = null;  
    	 try {
    		 fis = new FileInputStream(file);
        	 bos = new ByteArrayOutputStream(); 
        	 int read = 0;
        	 byte[] bytes = new byte[1024];
             while ((read = fis.read(bytes)) != -1) {
            	 bos.write(bytes, 0, read);
             }
             bos.flush();
             BASE64Encoder encoder = new BASE64Encoder();  
 	        return "data:audio/mp3;base64," + encoder.encode(bos.toByteArray());
    	 } catch (IOException e) {
             throw e;
         } finally {
             if (fis != null) {
            	 fis.close();
             }
             if (bos != null) {
            	 bos.close();
             }
         }
	}
	
    /**
     * 设置序号
     * 设置显示格式
     * @param questions
     * @return
     */
	public static List<ExamModel> answerLayoutSet(List<ExamModel> questions) {
    	
		if(questions != null && questions.size() > 0) {
	        for (int i = 0; i < questions.size(); i++) {
	            questions.get(i).setRowNo(i + 1);
	            if (WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT) != null && SystemConstants.AGENT_FLAG
	                    .equals((String) WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT))) {
	                questions.get(i).setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(questions.get(i).getA())
	                    && questions.get(i).getA().length() > CmnContants.FOLDING_LINE) {
	                questions.get(i).setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(questions.get(i).getB())
	                    && questions.get(i).getB().length() > CmnContants.FOLDING_LINE) {
	                questions.get(i).setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(questions.get(i).getC())
	                    && questions.get(i).getC().length() > CmnContants.FOLDING_LINE) {
	                questions.get(i).setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(questions.get(i).getD())
	                    && questions.get(i).getD().length() > CmnContants.FOLDING_LINE) {
	                questions.get(i).setLayoutStyle("pageDirection");
	            } else {
	                questions.get(i).setLayoutStyle("lineDirection");
	            }
	
	            if ("lineDirection".equals(questions.get(i).getLayoutStyle())) {
	                questions.get(i).setRadioClass("radioTable1");
	            } else {
	                questions.get(i).setRadioClass("radioTable2");
	            }
	        }
		}
        return questions;
    }

	/**
	 * 设置序号
	 * 设置显示格式
	 * @param questions
	 * @return
	 */
	public static ForumModel selectionLayoutSet(ForumModel forumModel) {
	    
	    if(forumModel != null) {
	            if (WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT) != null && SystemConstants.AGENT_FLAG
	                    .equals((String) WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT))) {
	                forumModel.setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(forumModel.getA())
	                    && forumModel.getA().length() > CmnContants.FOLDING_LINE) {
	                forumModel.setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(forumModel.getB())
	                    && forumModel.getB().length() > CmnContants.FOLDING_LINE) {
	                forumModel.setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(forumModel.getC())
	                    && forumModel.getC().length() > CmnContants.FOLDING_LINE) {
	                forumModel.setLayoutStyle("pageDirection");
	            } else if (!StringUtils.isEmpty(forumModel.getD())
	                    && forumModel.getD().length() > CmnContants.FOLDING_LINE) {
	                forumModel.setLayoutStyle("pageDirection");
	            } else {
	                forumModel.setLayoutStyle("lineDirection");
	            }
	            
	            if ("lineDirection".equals(forumModel.getLayoutStyle())) {
	                forumModel.setRadioClass("radioTable1");
	            } else {
	                forumModel.setRadioClass("radioTable2");
	            }
	        }
	    return forumModel;
	}
	
	/**
	 * 设置序号
	 * 设置显示格式
	 * @param questions
	 * @return
	 */
	public static void selectionLayoutSet(List<TbQuestionStructure> structures) {
		
		if(structures != null && structures.size() > 0) {
			 for(TbQuestionStructure item : structures) {
				if(item.getQuestion() != null && item.getQuestion().size() > 0 ) {
					List<MediaModel> questions = item.getQuestion();
					if(questions != null && questions.size() > 0) {
						for (int i = 0; i < questions.size(); i++) {
							if (WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT) != null && SystemConstants.AGENT_FLAG
									.equals((String) WebUtils.getSessionAttribute(WebUtils.SESSION_USER_AGENT))) {
								questions.get(i).setLayoutStyle("pageDirection");
							} else if (!StringUtils.isEmpty(questions.get(i).getA())
									&& questions.get(i).getA().length() > CmnContants.FOLDING_LINE) {
								questions.get(i).setLayoutStyle("pageDirection");
							} else if (!StringUtils.isEmpty(questions.get(i).getB())
									&& questions.get(i).getB().length() > CmnContants.FOLDING_LINE) {
								questions.get(i).setLayoutStyle("pageDirection");
							} else if (!StringUtils.isEmpty(questions.get(i).getC())
									&& questions.get(i).getC().length() > CmnContants.FOLDING_LINE) {
								questions.get(i).setLayoutStyle("pageDirection");
							} else if (!StringUtils.isEmpty(questions.get(i).getD())
									&& questions.get(i).getD().length() > CmnContants.FOLDING_LINE) {
								questions.get(i).setLayoutStyle("pageDirection");
							} else {
								questions.get(i).setLayoutStyle("lineDirection");
							}
							
							if ("lineDirection".equals(questions.get(i).getLayoutStyle())) {
								questions.get(i).setRadioClass("radioTable1");
							} else {
								questions.get(i).setRadioClass("radioTable2");
							}
						}
					}
				}
			}
		}
	}
}
