package form.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private static Database instance = new Database();
	
	private Connection conn;
	
	private Database(){
		
	}
	
	public static Database getInstance(){
		return instance;
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	public void connect() throws Exception{
		if(conn!=null){
			return;
		}
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException exc){
			exc.printStackTrace();
		}
		
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patterns", "root", "");
		
	}
	
	public void disconnect(){
		if(conn != null){
			try{
				conn.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		conn = null;
	}
}
