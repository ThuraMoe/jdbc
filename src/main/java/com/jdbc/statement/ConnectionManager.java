package com.jdbc.statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ConnectionManager {
    
	String URL = "jdbc:mysql://localhost:3306/message";
	String USER = "root";
	String PASS = "";
	
	Connection getConnection() throws SQLException;
	
	public static ConnectionManager getInstance() {
		return () -> DriverManager.getConnection(URL, USER, PASS);
	}
}
