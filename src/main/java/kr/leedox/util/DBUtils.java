package kr.leedox.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtils {

	private static final Logger logger  = Logger.getLogger(DBUtils.class.getName());

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				logger.log(Level.FINEST, "Could not close JDBC ResultSet", ex);
			} catch (Throwable ex) {
				logger.log(Level.FINEST, "Unexptected exception on closing JDBC ResultSet", ex);
			}
		}
	}

	public static void closeStatement(PreparedStatement pst) {
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException ex) {
				logger.log(Level.FINEST, "Could not close JDBC ResultSet", ex);
			} catch (Throwable ex) {
				logger.log(Level.FINEST, "Unexptected exception on closing JDBC ResultSet", ex);
			}
		}
	}

	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException ex) {
				logger.log(Level.FINEST, "Could not close JDBC ResultSet", ex);
			} catch (Throwable ex) {
				logger.log(Level.FINEST, "Unexptected exception on closing JDBC ResultSet", ex);
			}
		}
	}

}
