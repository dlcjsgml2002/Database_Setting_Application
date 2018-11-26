package database_setting.service;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

import database_setting.jdbc.ConnectionProvider;
import database_setting.jdbc.LogUtil;
import database_setting.jdbc.MyDataSource;

public class ImportService {
	public void service(String dirPath) {
		loadData(dirPath);
	}

	private void loadData(String dirPath) {
		try (Connection conn = ConnectionProvider.getConnection("db.properties");
				Statement stmt = conn.createStatement()) {
			stmt.addBatch("use " + MyDataSource.getInstance("db.properties").getProperties().getProperty("dbname"));
			stmt.addBatch("SET FOREIGN_KEY_CHECKS = 0");
			String path = null;
			String fileName = null;
			String sql = null;
			File sqlDir = new File(dirPath);
			for (File sqlFile : sqlDir.listFiles()) {
				path = sqlFile.getAbsolutePath().replace("\\", "/");
				fileName = sqlFile.getName().substring(0, sqlFile.getName().lastIndexOf(".txt"));
				sql = String.format(
						"LOAD DATA LOCAL INFILE '%s' IGNORE INTO TABLE %s character set 'UTF8' fields TERMINATED by ','",
						path, fileName);
				stmt.addBatch(sql);
				LogUtil.prnLog(sql);
			}
			stmt.addBatch("SET FOREIGN_KEY_CHECKS = 1");
			int[] res = stmt.executeBatch();
			LogUtil.prnLog(Arrays.toString(res));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
