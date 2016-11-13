package zh.co.common.ibatis;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.NestedResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import zh.co.common.utils.CollectionUtils;

/**
 * This dynamic configuration supports dynamically changing of map of
 * <ode>MappedStatement</code>. Since the existing <code>Configuration</code>
 * uses an <code>Configuration.StrictMap</code>, while adding an entry is
 * allowed, it is not allowed to replace, remove an existing entry.
 * 
 * 
 * 
 */
public class DynaConfiguration extends Configuration {

    public DynaConfiguration() {

    }

    public DynaConfiguration(Environment environment) {
        super(environment);
    }

    protected final Map<String, MappedStatement> mappedStatements = new ConcurrentHashMap<String, MappedStatement>();
    protected final Map<String, ResultMap> resultMaps = new ConcurrentHashMap<String, ResultMap>();
    protected final Map<String, ParameterMap> parameterMaps = new ConcurrentHashMap<String, ParameterMap>();

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public Collection<String> getMappedStatementNames() {
        return mappedStatements.keySet();
    }

    public Collection<MappedStatement> getMappedStatements() {
        return mappedStatements.values();
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public boolean hasStatement(String statementName) {
        return mappedStatements.containsKey(statementName);
    }

    public void removeMappedStatement(String id) {
        mappedStatements.remove(id);
    }

    public void addResultMap(ResultMap rm) {
        resultMaps.put(rm.getId(), rm);
    }

    public Collection<String> getResultMapNames() {
        return resultMaps.keySet();
    }

    public Collection<ResultMap> getResultMaps() {
        return resultMaps.values();
    }

    public ResultMap getResultMap(String id) {
        return resultMaps.get(id);
    }

    public boolean hasResultMap(String id) {
        return resultMaps.containsKey(id);
    }

    public void removeResultMap(String id) {
        resultMaps.remove(id);
    }

    public void addParameterMap(ParameterMap pm) {
        parameterMaps.put(pm.getId(), pm);
    }

    public Collection<String> getParameterMapNames() {
        return parameterMaps.keySet();
    }

    public Collection<ParameterMap> getParameterMaps() {
        return parameterMaps.values();
    }

    public ParameterMap getParameterMap(String id) {
        return parameterMaps.get(id);
    }

    public boolean hasParameterMap(String id) {
        return parameterMaps.containsKey(id);
    }

    public void removeParameterMap(String id) {
        parameterMaps.remove(id);
    }

    public void removeSqlMapper(String name) {
        String prefix = name + ".";
        CollectionUtils.removeWithKeyPrefix(mappedStatements, prefix);
        CollectionUtils.removeWithKeyPrefix(resultMaps, prefix);
        CollectionUtils.removeWithKeyPrefix(parameterMaps, prefix);
    }

    public void addLoadedResource(String resource) {
        // do not record resources that are loaded since resources can be
        // reloaded later.
    }
    
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql) {
        ResultSetHandler resultSetHandler = mappedStatement.hasNestedResultMaps() ?
            new NestedResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds)
            : new ExtendedFastResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
      }
}
