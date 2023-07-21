package com.jdbc.statement;

import java.sql.SQLException;

public class DatabaseInitializer {
	
	public static void truncate(String ...tables) {
				
		try(var conn = ConnectionManager.getInstance().getConnection()) {
			
			var stmt = conn.createStatement();
			stmt.execute("SET foreign_key_check = 0");
			
			for(String table : tables) {
				stmt.execute("truncate table %s".formatted(table));
			}
			
			stmt.execute("SET foreign_key_check = 1");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
