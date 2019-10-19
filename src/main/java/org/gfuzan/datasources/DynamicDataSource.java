package org.gfuzan.datasources;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


/**
 * 动态数据源
 * @author gaofz
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<Deque<String>> contextHolder = new ThreadLocal<>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public DynamicDataSource(DataSource defaultTargetDataSource, Map<String, ? extends DataSource> dataSourceMap) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources((Map)dataSourceMap);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
    	Deque<String> deque = contextHolder.get();
    	if(deque == null) {
    		deque = new LinkedList<>();
    		contextHolder.set(deque);
    	}
    	deque.push(dataSource);
    }

    public static String getDataSource() {
    	Deque<String> deque = contextHolder.get();
    	if(deque != null) {
    		return deque.peekFirst();
    	}
        return null;
    }

    public static String clearDataSource() {
    	Deque<String> deque = contextHolder.get();
    	if(deque != null && !deque.isEmpty()) {
    		return deque.pop();
    	}
    	return null;
    }

}
