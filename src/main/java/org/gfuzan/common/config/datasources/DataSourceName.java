package org.gfuzan.common.config.datasources;

/**
 * 多数据源名
 * @author GFuZan
 *
 */
public enum DataSourceName {
	
	FIRST("first",true),
	SECOND("second");

	/**
	 * 数据源名
	 */
	private String name;
	private boolean isDefault;
	
	DataSourceName(String name){
		this.name = name;
	}
	
	DataSourceName(String name,boolean isDefault){
		this.name = name;
		this.isDefault = isDefault;
	}

	public String getName() {
		return name;
	}

	public boolean isDefault() {
		return isDefault;
	}
	
	public static DataSourceName getDefaultDataSourceName() {
		DataSourceName[] dataSourceNames = DataSourceName.values();
		if(dataSourceNames != null) {
			for(DataSourceName dsn: dataSourceNames) {
				if(dsn.isDefault()) {
					return dsn;
				}
			}
			
			// 未设置默认值,则默认第一个
			return dataSourceNames[0];
		}
		
		return null;
	}
}
