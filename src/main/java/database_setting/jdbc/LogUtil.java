package database_setting.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
	static final Logger LOG = LogManager.getLogger();

	public static void prnLog(PreparedStatement pstmt) {
		LOG.trace(pstmt.toString().substring(pstmt.toString().lastIndexOf(":") + 2));
	}

	public static void prnLog(String message) {
		LOG.trace(String.format("%s", message));
	}

	public static void prnLog(SQLException e) {
		LOG.trace(String.format("errorCode %s errorMessage %s", e.getErrorCode(), e.getMessage()));
	}
}