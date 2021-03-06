package database;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class UserDao {
	// Database credentials
	final String DB_URL = "jdbc:mysql://localhost/test";
	final String USERNAME = "root";
	final String PASS = "root";

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	public HashMap<String, String> getUserInfo(String username) {

		HashMap<String, String> map = new HashMap<String, String>();
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);

			// Execute SQL query
			String sql = "SELECT regid, uuid FROM users WHERE username=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();

			String regid = null;
			String uuid = null;

			// Extract data from result set
			while (rs.next()) {
				regid = rs.getString("regid");
				uuid = rs.getString("uuid");
			}

			map.put("regid", regid);
			map.put("uuid", uuid);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (rs != null)
					rs.close();
			} catch (SQLException se2) {
			}
		} // end try
		return map;
	}

	private boolean setUuid(String uuid, String username) {
		boolean successful = false;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);

			// Execute SQL query
			String sql = "UPDATE users SET uuid=? WHERE username=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, uuid);
			stmt.setString(2, username);
			stmt.executeUpdate();
			successful = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
			}
		}
		return successful;
	}

	// username komt binnen vanaf dll
	public void createUuid(String username) {
		// Generate UUID
		Long uuidGet = UUID.randomUUID().getMostSignificantBits();

		String uuid = Long.toString(uuidGet);

		this.setUuid(uuid, username);
	}

	public boolean checkInfo(String uuid, String username) {
		int counter = -1;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);

			// Execute SQL query
			String sql = "SELECT COUNT(*) as total FROM users WHERE uuid=? AND username=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, uuid);
			stmt.setString(2, username);
			rs = stmt.executeQuery();

			// Read count
			while (rs.next()) {
				counter = rs.getInt("total");
			}

			if (counter <= 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void deleteUuid(String username, String uuid) {
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);

			// Execute SQL query
			String sql = "SELECT COUNT(*) as total FROM users WHERE uuid=? AND username=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, uuid);
			stmt.setString(2, username);
			rs = stmt.executeQuery();
			
			// Read count
			int counter = 0;
			while (rs.next()) {
				counter = rs.getInt("total");
			}
			
			if (counter > 0) {
				sql = "UPDATE users SET uuid=0 WHERE username=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, username);
				stmt.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setRegId(String username, String regid) {
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);

			// Execute SQL query
			String sql = "UPDATE users SET regid=? WHERE username=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, regid);
			stmt.setString(2, username);
			stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
			}
		}
	}
}
