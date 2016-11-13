package zh.co.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>[概要]  Numbers business exceptions implement so-called composite design pattern.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class CmnMultiBizException extends Exception {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = 1L;
    /**
     * the exceptions list
     */
    protected List<CmnBizException> exceptions = new ArrayList<CmnBizException>();
    
    /**
     * the exception flag
     */
    private String flag = null;

    /**
     * constructor
     */
    public CmnMultiBizException() {
        super();
    }

    /**
     * <p>[概要]add exception to the list</p>
     * <p>[备考]</p>
     * @param be exception of business
     */
    public void addException(CmnBizException be) {
        exceptions.add(be);
    }

    /**
     * <p>[概要]add exceptions of mult business exception</p>
     * <p>[备考]</p>
     * 
     * @param bme exception of multi business
     */
    public void addExceptions(CmnMultiBizException bme) {
        exceptions.addAll(bme.getExceptions());
    }

    /**
     * <p>[概要]add exception</p>
     * <p>[备考]</p>
     * 
     * @param msgId msg id
     * @param msgArgs msg args
     */
    public void addException(String msgId, Object[] msgArgs) {
        exceptions.add(new CmnBizException(msgId, msgArgs));
    }

    /**
     * <p>[概要]get exception list</p>
     * <p>[备考]</p>
     * 
     * @return list of exception
     */
    public List<CmnBizException> getExceptions() {
        return this.exceptions;
    }

    /**
     * <p>[概要]get exception flag</p>
     * <p>[备考]</p>
     *
     * @return flag
     */
    
    public String getFlag() {
        return flag;
    }

    /**
     * <p>[概要]set exception flag</p>
     * <p>[备考]</p>
     *
     * @param flag 
     */
    
    public void setFlag(String flag) {
        this.flag = flag;
    }

}
