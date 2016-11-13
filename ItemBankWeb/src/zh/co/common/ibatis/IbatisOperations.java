package zh.co.common.ibatis;

import java.util.List;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * Defines IbatisTemplate convenience methods that mirror the iBatis3
 * {@link org.apache.ibatis.session.SqlSession} methods.
 * 
 */
public interface IbatisOperations {
    Object selectOne(String statement);

    Object selectOne(String statement, Object parameter);

    List selectList(String statement);

    List selectList(String statement, Object parameter);

    List selectList(String statement, Object parameter, RowBounds rowBounds);

    void select(String statement, Object parameter, ResultHandler handler);

    void select(String statement, Object parameter, RowBounds rowBounds,
            ResultHandler handler);

    int insert(String statement);

    int insert(String statement, Object parameter);

    int update(String statement);

    int update(String statement, Object parameter);

    int delete(String statement);

    int delete(String statement, Object parameter);
}
