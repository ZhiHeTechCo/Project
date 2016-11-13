package zh.co.common.ibatis;

/**
 * Home-built DAO support for IBATIS3, since Spring hasn't offer support IBATIS3
 * yet.
 * 
 * <p>
 * Typical usage is as follows, which is vary similar to HibernateDaoSupport.
 * 
 * <pre class="code">
 * public class DeviceDaoImpl extends IbatisDaoSupport implements DeviceDao {
 *     public void updateDevice(final Map devInfo) {
 *         getIbatisTemplate().execute(new IbatisCallback() {
 *             public Object doInIbatis(SqlSession sqlSession) {
 *                 sqlSession.update(&quot;sqlId&quot;, devInfo);
 *                 return null;
 *             }
 *         });
 *     }
 * }
 * </pre>
 * 
 * @see IbatisTemplate
 * @see IbatisCallback
 */
public class IbatisDaoSupport {

    private IbatisTemplate ibatisTemplate;

    public IbatisTemplate getIbatisTemplate() {
        return this.ibatisTemplate;
    }

    public void setIbatisTemplate(IbatisTemplate ibatisTemplate) {
        this.ibatisTemplate = ibatisTemplate;
    }

}
