package com.jdbc.statement.dao;

import java.sql.Date;
import java.sql.SQLException;

import com.jdbc.statement.ConnectionManager;
import com.jdbc.statement.dto.Member;

public class MemberDao {

	private ConnectionManager manager;
	
	public MemberDao(ConnectionManager mgr) {
		this.manager = mgr;
	}
	
	public int createMember(Member member) {
		
		if(member == null) {
			throw new IllegalArgumentException();
		}
		
		var sql = "INSERT INTO members VALUES(?, ?, ?, ?, ?)";
		
		try(var conn = manager.getConnection();
				var stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, member.email());
			stmt.setString(2, member.name());
			stmt.setString(3, member.password());
			stmt.setDate(4, Date.valueOf(member.dob()));
			
			var result = stmt.executeUpdate();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public Member findByEmail(String email) {
		
		var sql = "SELECT * FROM members WHERE email=?";
		
		try(var conn = manager.getConnection();
				var stmt = conn.prepareStatement(sql)) {
			
			var result = stmt.executeQuery();
			
			if(result.next()) {
//				return new Member(
//						result.getString("email"),
//						result.getString("name"),
//						result.getString("password"),
//						result.getDate("dob").toLocalDate()
//						);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int changePassword(String email, String oldPassword, String newPassword) {
		
		return 0;
	}
}
