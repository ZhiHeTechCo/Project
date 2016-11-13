package zh.co.common.ibatis;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

/**
 * This is a callback working with <code>IbatisTemplate</code>.
 * @see IbatisTemplate
 */
public interface IbatisCallback<T> {
    T doInIbatis(SqlSession sqlSession) throws SQLException;
}
