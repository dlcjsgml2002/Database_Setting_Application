package database_setting.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
	static final Logger LOG = LogManager.getLogger();

	public static void prnLog(PreparedStatement pstmt) {
		String msg = pstmt.toString();
		int startIndex = msg.lastIndexOf(":") + 2;
		int endIndex = msg.length() - 1;
		LOG.trace(msg.substring(startIndex, endIndex));
	}

	public static void prnLog(String message) {
		LOG.trace(String.format("%s", message));
	}

	public static void prnLog(Object object) {
		LOG.trace(object.toString());
	}

	public static void prnLog(SQLException e) {
		LOG.trace(String.format("Error Code : %d%nSQL STATE : %s%nMessage : %s%n", e.getErrorCode(), e.getSQLState(),
				e.getMessage()));
	}
}