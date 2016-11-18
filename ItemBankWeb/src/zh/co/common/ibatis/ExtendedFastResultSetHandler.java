package zh.co.common.ibatis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.FastResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;

public class ExtendedFastResultSetHandler extends FastResultSetHandler {

	public ExtendedFastResultSetHandler(Executor executor,
			MappedStatement mappedStatement, ParameterHandler parameterHandler,
			ResultHandler resultHandler, BoundSql boundSql, RowBounds rowBounds) {
		super(executor, mappedStatement, parameterHandler, resultHandler,
				boundSql, rowBounds);
	}

	protected boolean applyAutomaticMappings(ResultSet rs,
			List<String> unmappedColumnNames, MetaObject metaObject,  String columnPrefix, ResultColumnCache resultColumnCache)
			throws SQLException {
		boolean foundValues = false;
		for (String columnName : unmappedColumnNames) {
			final String property = metaObject.findProperty(columnName, true);
			if (property != null) {
				final Class propertyType = metaObject.getSetterType(property);
				if (typeHandlerRegistry.hasTypeHandler(propertyType)) {
					final TypeHandler typeHandler = typeHandlerRegistry
							.getTypeHandler(propertyType);
					final Object value = typeHandler.getResult(rs, columnName);
					if (value != null || IbatisContext.isCallSettersOnNulls()) {
						metaObject.setValue(property, value);
						foundValues = true;
					}
				}
			}
		}
		return foundValues;
	}

}
