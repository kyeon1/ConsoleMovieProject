package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {
	
	/**
	 * 별도로 분리해논 config.properties 초기화
	 */
	private static Properties prop = FileLoaderUtil.getInstance().getConfigProperties();


	/**
	 * 데이터 베이스랑 연결하는 메서드
	 * @return	conn
	 */
	public static Connection connect() {
		Connection conn=null;
		try {
			Class.forName(prop.getProperty("driverName"));
			conn=DriverManager.getConnection(prop.getProperty("url"),prop.getProperty("user"),prop.getProperty("pw"));
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
