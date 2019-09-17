package schema;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.owlike.genson.Genson;

public class SchemaParse {
	static final Logger LOGGER = LogManager.getLogger(SchemaParse.class);
	static Genson gen = new Genson();

	public static void main(String[] args) {
		LOGGER.info("[SchemaParser] Initializing Schema mapper ");
		mapTable("lb", "service_provider");
	}

	public static Hashtable<String, List<String>> mapTable(String schema, String table) {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[SchemaParser] Mapping Schema '" + schema + "." + table);
		java.sql.Connection conn = initDbConnection(schema);
		String SQL = "SELECT * FROM " + table;
		try {
			java.sql.Statement st = conn.createStatement();
			List<String> columnHeaders = new ArrayList<String>();
			ResultSet rs = st.executeQuery(SQL);
			ResultSetMetaData rsmd = rs.getMetaData();
			Hashtable<String, List<String>> data = new Hashtable<String, List<String>>(0);
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				columnHeaders.add(rsmd.getColumnName(i));
			}
			// LOGGER.info("[SchemaParser] Table headers '"+gen.serialize(columnHeaders));
			while (rs.next()) {
				for (String header : columnHeaders) {
					if (!data.containsKey(header)) {
						List<String> newArray = new ArrayList<String>();
						newArray.add(rs.getString(header));
						data.put(header, newArray);
					} else {
						data.get(header).add(rs.getString(header));
					}
				}
			}
			LOGGER.info("[SchemaParser] Mapped schema in [" + (System.currentTimeMillis() - startTime) + "ms]");
			// LOGGER.info("[SchemaParser] Table data '"+gen.serialize(data));
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static java.sql.Connection initDbConnection(String schema) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
		Scanner creds = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/"+schema;
		String USER = "";
		String PASS = "";
		java.sql.Connection conn = null;
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			LOGGER.info("[SchemaParser] Enter username for '"+url+"'");
			USER=creds.nextLine();
			LOGGER.info("[SchemaParser] Enter password for '"+url+"'");
			PASS=creds.nextLine();
			conn = DriverManager.getConnection(url, USER, PASS);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			creds.close();
		}
		return null;
	}
}
