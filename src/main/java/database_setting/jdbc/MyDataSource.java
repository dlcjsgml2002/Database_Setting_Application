package database_setting.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class MyDataSource {
	public static final MyDataSource instance = new MyDataSource();
	private String propPath;
	private DataSource dataSource;
	private Properties properties;

	public static MyDataSource getInstance() {
		return instance;
	}

	public static MyDataSource getInstance(String propPath) {
		instance.setPropPath(propPath);
		return instance;
	}

	public MyDataSource() {}

	public void initDataSource() {
		properties = loadProperties();
		try {
			DataSource ds_unpooled = DataSources.unpooledDataSource(properties.getProperty("url"), properties);
			Map<String, Object> overrides = new HashMap<>();
			overrides.put("maxStatements", "200");
			overrides.put("maxPoolSize", new Integer(50));
			dataSource = DataSources.pooledDataSource(ds_unpooled, overrides);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setPropPath(String propPath) {
		this.propPath = propPath;
		initDataSource();
	}

	public Properties getProperties() {
		return properties;
	}

	private Properties loadProperties() {
		Properties properties = new Properties();
		try (InputStream is = ClassLoader.getSystemResourceAsStream(propPath)) {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
}