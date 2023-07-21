package com.jdbc.statement.dao;

import java.util.List;

import com.google.protobuf.Message;
import com.jdbc.statement.ConnectionManager;
import com.jdbc.statement.dto.Member;

public class MessageDao {
	
	private ConnectionManager manager;
	
	public MessageDao(ConnectionManager manager) {
		this.manager = manager;
	}
	
	public Message create(Message message) {
		return null;
	}
	
	public List<Message> search(String memberName, String keyword) {
		return null;
	}
	
	public List<Message> searchByMember(Member member) {
		return null;
	}
	
	public int save(Message message) {
		return 0;
	}
	
	public Message findById(int id) {
		return null;
	}
}
