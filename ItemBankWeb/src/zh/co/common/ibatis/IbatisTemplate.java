package zh.co.common.ibatis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import zh.co.common.controller.BaseController;
import zh.co.common.exception.CmnBizException;
import zh.co.common.exception.CmnSysException;
import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.common.utils.CmnStringUtils;
import zh.co.common.utils.MessageUtils;
import zh.co.common.utils.TransactionUtils;
import zh.co.common.utils.WebUtils;

/**
 * This is meant to be used with managed transaction factory, especially using
 * {@link org.springframework.jdbc.datasource.DataSourceTransactionManager} as
 * the external transaction manager.
 * 
 */
public class IbatisTemplate extends SqlSessionTemplate {

    public IbatisTemplate(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}

	private CmnLogger logger = CmnLogger.getLogger(this.getClass());

    public int delete(String statement) {
        return delete(statement, null);
    }

    public int delete(final String statement, final Object parameter) {
        return super.delete(statement, parameter);
    }

    public int insert(String statement) {
        return insert(statement, null);
    }

    public int insert(final String statement, final Object parameter) {
        return super.insert(statement, parameter);
    }

    public void select(String statement, Object parameter, ResultHandler handler) {
        select(statement, parameter, RowBounds.DEFAULT, handler);
    }

    public void select(final String statement, final Object parameter,
            final RowBounds rowBounds, final ResultHandler handler) {
        super.select(statement, parameter, rowBounds, handler);
    }

    public List selectList(String statement) {
        return selectList(statement, null);
    }

    public List selectList(String statement, Object parameter) {
        return selectList(statement, parameter, RowBounds.DEFAULT);
    }

    public List selectList(final String statement, final Object parameter,
            final RowBounds rowBounds) {
        return super.selectList(statement, parameter, rowBounds);
    }
    
    public List selectPageList(final String statement, final Object parameter) throws CmnBizException {
        String countStatement = statement+"_count";
        return selectPageList(countStatement, statement, parameter);
    }

    public List selectPageList(final String countStatement, final String statement, final Object parameter) throws CmnBizException {
        Integer resultCount = (Integer)this.selectOne(countStatement, parameter);
        if (resultCount > WebUtils.getMaxSearchCount()) {
            logger.log(MessageId.COMMON_E_0006, new String[] { String.valueOf(WebUtils.getMaxSearchCount()) });
            throw new CmnBizException(MessageId.COMMON_E_0006, new String[] { String.valueOf(WebUtils.getMaxSearchCount()) });
        } else if (resultCount > WebUtils.getMaxSearchDisplayCount()) {
            logger.log(
                    MessageId.COMMON_W_0001,
                    new String[] { String.valueOf(resultCount),
                            String.valueOf(WebUtils.getMaxSearchDisplayCount()) });
            BaseController.setMessage(
                    MessageUtils.getMessage(
                            MessageId.COMMON_W_0001,
                            new String[] { String.valueOf(resultCount),
                                    String.valueOf(WebUtils.getMaxSearchDisplayCount()) }), BaseController.MESSAGE_LEVEL_WARN);
        }
        return selectList(statement, parameter, RowBounds.DEFAULT);
        
    }

    public Object selectOne(String statement) {
        return selectOne(statement, null);
    }

    public Object selectOne(final String statement, final Object parameter) {
        return super.selectOne(statement, parameter);
    }

    public int update(String statement) {
        return update(statement, null);
    }

    public int update(final String statement, final Object parameter) {
    	return super.update(statement, parameter);
    }
    
    public List<Map<String, Object>> executeQuerySql(String sql) {

        Connection connection = null;
        Statement stmt = null;
        ResultSet rset = null;
        String errorCode = "";
        try {
            DataSource dataSource = super.getSqlSessionFactory().getConfiguration().getEnvironment().getDataSource();
            connection = TransactionUtils.getConnection(dataSource);

            stmt = connection.createStatement();
            rset = stmt.executeQuery(sql);
            int rowCount = 0;
            int colCount = 0;
            ResultSetMetaData rsmd = null;
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            while (rset.next()) {
                if (rowCount == 0) {
                    rsmd = rset.getMetaData();
                    colCount = rsmd.getColumnCount();
                }
                rowCount++;
                Map<String, Object> resultMap = new HashMap<String, Object>();
                for (int i = 1; i <= colCount; i++) {
                    resultMap.put(rsmd.getColumnLabel(i), rset.getObject(i));
                }
                result.add(resultMap);
            }
            if (rowCount == 0) {
                return null;
            }
            return result;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            
            if (cause instanceof SQLException) {
                SQLException sqle = (SQLException) cause;
                errorCode = CmnStringUtils.objToStr(sqle.getErrorCode());
            }
            
            logger.log(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
            throw new CmnSysException(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                    errorCode = CmnStringUtils.objToStr(e.getErrorCode());
                    logger.log(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
                    throw new CmnSysException(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    errorCode = CmnStringUtils.objToStr(e.getErrorCode());
                    logger.log(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
                    throw new CmnSysException(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    errorCode = CmnStringUtils.objToStr(e.getErrorCode());
                    logger.log(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
                    throw new CmnSysException(MessageId.COMMON_E_0005, new Object[] { String.valueOf(errorCode) }, e);
                }
            }
        }
    }
}
