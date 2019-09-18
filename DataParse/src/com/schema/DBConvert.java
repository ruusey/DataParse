package schema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConvert {
	static final Logger LOGGER = LogManager.getLogger(DBConvert.class);
	private Connection con;
	public static void main(String[] args) {
		DBConvert db = new DBConvert("lb","root","",3306);
		//db.createTable(table, keys, types)
		
	}
	public DBConvert(String schema, String username, String password, int port) {
		this.con=createConnection(schema, username, password, port);
	}
	private Connection createConnection(String schema, String username, String password, int port) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
		Scanner creds = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:"+port+"/"+schema;
		
		java.sql.Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			creds.close();
		}
		return null;
	}
	private boolean insertRecords(String table, Hashtable<String, List<String>> values) {
		return false;
	}
	private boolean createTable(String table, List<String> keys, List<String> types) {
		String sql = "CREATE TABLE "+table+"(";
		for(int i = 0; i<types.size();i++) {
			if(i==0) {
				sql+=keys.get(i)+" "+types.get(i);
			}else {
				sql+=", "+keys.get(i)+" "+types.get(i);
			}
		}
		sql+=");";
		Statement st = null;
		try {
			st = this.con.createStatement();
			st.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
