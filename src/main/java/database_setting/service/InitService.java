package database_setting.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

import database_setting.jdbc.ConnectionProvider;
import database_setting.jdbc.LogUtil;

public class InitService {
	public void service(String dirPath) {
		File sqlDir = new File(dirPath);
		File[] sqlFiles = sqlDir.listFiles();
		Arrays.sort(sqlFiles);
		for (File sqlFile : sqlFiles) {
			execSqlStatement(sqlFile);
		}
	}

	private void execSqlStatement(File execSqlFile) {
		LogUtil.prnLog("execSqlStataement()");
		try (Connection conn = ConnectionProvider.getConnection("db.properties");
				Statement stmt = conn.createStatement();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(new FileInputStream(execSqlFile), "UTF-8"))) {
			StringBuilder statement = new StringBuilder();
			boolean isProcedureOfTrigger = false;

			for (String line; (line = br.readLine()) != null;) {
				if (line.startsWith("BEGIN") || line.startsWith("begin"))
					isProcedureOfTrigger = true;
				if (line.startsWith("--"))
					continue;
				if (line.contains("--")) {
					statement.append(line.substring(0, line.lastIndexOf("-- ")) + "\r\n");
				} else {
					statement.append(line + "\r\n");
				}
				if (isProcedureOfTrigger) {
					if (line.endsWith("END;") || line.endsWith("end;")) {
						stmt.addBatch(statement.toString());
						LogUtil.prnLog(statement);
						statement.setLength(0);
						isProcedureOfTrigger = false;
					}
				} else {
					if (line.endsWith(";")) {
						stmt.addBatch(statement.toString());
						LogUtil.prnLog(statement);
						statement.setLength(0);
					}
				}
			}
			stmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
