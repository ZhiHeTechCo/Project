package zh.co.common.dao;

import javax.annotation.Resource;

import zh.co.common.ibatis.IbatisDaoSupport;
import zh.co.common.ibatis.IbatisTemplate;
import zh.co.common.ibatis.IbatisTemplate;

/**
 * <p>[概要] Base DAO class. All dao classes should not direct extends this class.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class BaseDao extends IbatisDaoSupport {

    /**
     * <p>[概要]设定ibatisTemplate</p>
     * <p>[备考]</p>
     * @param ibatisTemplate ibatisTemplate
     */
    @Resource(name = "sqlSessionTemplate")
    public void setIbatisTemplate(IbatisTemplate ibatisTemplate) {
        super.setIbatisTemplate(ibatisTemplate);
    }
    
    
    /**
     * <p>[概要]取得ibatisTemplate</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public IbatisTemplate getIbatisTemplate(){
        return (IbatisTemplate)super.getIbatisTemplate();
    }
}
