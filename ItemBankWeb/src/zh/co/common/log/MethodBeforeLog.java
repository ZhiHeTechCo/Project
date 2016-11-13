package zh.co.common.log;

import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * <p>[概要] 在方法开始的时候将参数打印到log文件</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class MethodBeforeLog implements MethodBeforeAdvice {

    /** log对象 */
    private static final CmnLogger LOG = CmnLogger.getLogger(MethodBeforeLog.class);

    /** 换行符 */
    private static final String SEPARATOR = System.getProperty("line.separator");

    /** 空格 */
    private static final String SPACE = "    ";

    /**
     * {@inheritDoc}
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {

        StringBuilder buf = new StringBuilder();
        buf.append(target.getClass().getName()).append("#").append(method.getName());

        if (args != null) {
            buf.append(SEPARATOR);
            for (Object arg : args) {
                if (arg != null) {
                    if (arg.getClass().isArray()) {
                        buf.append(SPACE).append(ArrayUtils.toString(arg)).append(
                            SEPARATOR);
                        continue;
                    }
                }

                buf.append(SPACE).append(arg).append(SEPARATOR);
            }
        }

        LOG.debug(buf.toString());
    }
}
