package zh.co.common.log;

import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.aop.AfterReturningAdvice;

/**
 * <p>[概要] 在方法结束的时候将返回值打印到log文件</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class MethodAfterLog implements AfterReturningAdvice {

    /** log对象 */
    private static final CmnLogger LOG = CmnLogger.getLogger(MethodAfterLog.class);

    /** 换行符 */
    private static final String SEPARATOR = System.getProperty("line.separator");

    /** 空格 */
    private static final String SPACE = "    ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args,
        Object target) throws Throwable {

        StringBuilder buf = new StringBuilder();
        buf.append(target.getClass().getName()).append("#").append(method.getName())
            .append(SEPARATOR).append(SPACE).append("returnValue = ");
        Object obj = null;
        if (returnValue != null) {
            if (returnValue.getClass().isArray()) {
                obj = ArrayUtils.toString(returnValue);
            } else {
                obj = returnValue;
            }
        } else {
            obj = returnValue;
        }
        buf.append(obj);

        LOG.debug(buf.toString());
    }
}
