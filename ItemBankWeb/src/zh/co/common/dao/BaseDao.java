package zh.co.common.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import zh.co.common.ibatis.IbatisTemplate;

/**
 * <p>[概要] Base DAO class. All dao classes should not direct extends this class.</p>
 * <p>[详细] </p>
 * <p>[备考] </p>
 * <p>[环境] JRE7.0</p>
 * @author 王飞
 * @since 1.0
 */
public class BaseDao extends SqlSessionDaoSupport {

    /**
     * <p>[概要]设定ibatisTemplate</p>
     * <p>[备考]</p>
     * @param ibatisTemplate ibatisTemplate
     */
    @Resource(name = "sqlSession")
    public void setIbatisTemplate(IbatisTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }
    
    
    /**
     * <p>[概要]取得ibatisTemplate</p>
     * <p>[备考]</p>
     * 
     * @return
     */
    public IbatisTemplate getIbatisTemplate(){
        return (IbatisTemplate)super.getSqlSession();
    }
}
