package com.jdbc.statement.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.jdbc.statement.ConnectionManager;
import com.jdbc.statement.DatabaseInitializer;
import com.jdbc.statement.MessageDbException;
import com.jdbc.statement.dao.MemberDao;
import com.jdbc.statement.dto.Member;
import com.jdbc.statement.dto.Member.Role;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberDaoTest {
	
	MemberDao dao;
	static Member input;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		DatabaseInitializer.truncate("message","member");
		input = new Member("polodohorse@gmail.com", "Thura Moe", "323343", LocalDate.of(2000, 12, 12), Role.Member);
	}
	
	@BeforeEach
	void setUp() throws Exception {
		dao = new MemberDao(ConnectionManager.getInstance());
	}

	@Test
	@Order(1)
	void testCreateMemberOk() {
		var result = dao.createMember(input);
		assertEquals(1, result);
	}
	
	@Test
	@Order(2)
	void testCreateMemberDuplicateKey() {
		var exception = assertThrows(IllegalArgumentException.class, () -> dao.createMember(input));
		assertEquals("Email has already been used.", exception.getMessage());
	}
	
	@Test
	@Order(3)
	void testCreateMemberNull() {
		assertThrows(IllegalArgumentException.class, () -> dao.createMember(null));
	}
	
	@Test
	@Order(4)
	void testCreateMemberNullName() {
		var nullData = new Member("polodohorse@gmail.com", null, "323343", LocalDate.of(2000, 12, 12), Role.Member);
		var nullExp = assertThrows(MessageDbException.class, () -> dao.createMember(nullData));
		assertEquals("Member name must not be null.", nullExp.getMessage());
		
		var emptyData = new Member("polodohorse@gmail.com", "", "323343", LocalDate.of(2000, 12, 12), Role.Member);
		var emptyExp = assertThrows(MessageDbException.class, () -> dao.createMember(emptyData));
		assertEquals("Member name must not be empty.", emptyExp.getMessage());
	}
	
	@Test
	@Order(5)
	void testCreateMemberNullEmail() {
		var nullData = new Member(null, "Thura Moe", "323343", LocalDate.of(2000, 12, 12), Role.Member);
		var nullExp = assertThrows(MessageDbException.class, () -> dao.createMember(nullData));
		assertEquals("Email must not be null.", nullExp.getMessage());
		
		var emptyData = new Member("", "Thura Moe", "323343", LocalDate.of(2000, 12, 12), Role.Member);
		var emptyExp = assertThrows(MessageDbException.class, () -> dao.createMember(emptyData));
		assertEquals("Email must not be empty.", emptyExp.getMessage());
	}
	
	@Test
	@Order(6)
	void testCreateMemberNullPassword() {
		var nullData = new Member("polodohorse@gmail.com", "Thura Moe", null, LocalDate.of(2000, 12, 12), Role.Member);
		var nullExp = assertThrows(MessageDbException.class, () -> dao.createMember(nullData));
		assertEquals("Password must not be null.", nullExp.getMessage());
		
		var emptyData = new Member("polodohorse@gmail.com", "Thura Moe", "", LocalDate.of(2000, 12, 12), Role.Member);
		var emptyExp = assertThrows(MessageDbException.class, () -> dao.createMember(emptyData));
		assertEquals("Password must not be empty.", emptyExp.getMessage());
	}
	
	@Test
	@Order(7)
	void testCreateMemberNullDob() {
		var nullData = new Member("polodohorse@gmail.com", "Thura Moe", "23423e", null, Role.Member);
		var nullExp = assertThrows(MessageDbException.class, () -> dao.createMember(nullData));
		assertEquals("Dob must not be null.", nullExp.getMessage());
	}
	
	@Test
	@Order(8)
	void testFindByEmail() {
		var result = dao.findByEmail(input.email());
		assertEquals(input, result);
	}
	
	@Test
	@Order(9)
	void testFindByEmailNotFound() {
		var result = dao.findByEmail("%s1".formatted(input.email()));
		assertNull(result);
	}
	
	@Test
	@Order(10)
	void testFindByEmailNullData() {
		assertThrows(IllegalArgumentException.class, () -> dao.findByEmail(null));
	}
	
	@Test
	@Order(11)
	void testChangePassword() {
		var newPass = "admin";
		var result = dao.changePassword(input.email(), input.password(), newPass);
		assertEquals(1, result);
		
		var member = dao.findByEmail(input.email());
		assertEquals(newPass, member.password());
		assertEquals(input.name(), member.name());
		assertEquals(input.role(), member.role());
		assertEquals(input.dob(), member.dob());
	}
	
	@Test
	@Order(12)
	void testChangePasswordNotFound() {
		var exception = assertThrows(MessageDbException.class, () -> dao.changePassword(("%s1").formatted(input.email()), input.password(), "newPass"));
		assertEquals("Please check email.", exception.getMessage());
	}
	
	@Test
	@Order(13)
	void testChangePasswordNullEmail() {
		var exception = assertThrows(MessageDbException.class, () -> dao.changePassword(null, "admin", "newPass"));
		assertEquals("Email must not be empty.", exception.getMessage());
		
		exception = assertThrows(MessageDbException.class, () -> dao.changePassword("", "admin", "newPass"));
		assertEquals("Email must not be empty.", exception.getMessage());
	}
	
	@Test
	@Order(14)
	void testChangePasswordNullOldPassword() {
		var exception = assertThrows(MessageDbException.class, () -> dao.changePassword(input.email(), null, "newPass"));
		assertEquals("Old password must not be empty.", exception.getMessage());
		exception = assertThrows(MessageDbException.class, () -> dao.changePassword(input.email(), "", "newPass"));
		assertEquals("Old password must not be empty.", exception.getMessage());
	}
	
	@Test
	@Order(15)
	void testChangePasswordNullNewPassword() {
		var exception = assertThrows(MessageDbException.class, () -> dao.changePassword(input.email(), "admin", null));
		assertEquals("New password must not be empty.", exception.getMessage());
		exception = assertThrows(MessageDbException.class, () -> dao.changePassword(input.email(), "admin", ""));
		assertEquals("New password must not be empty.", exception.getMessage());
	}
	
	@Test
	@Order(16)
	void testChangePasswordUnMatchPassword() {
		var exception = assertThrows(MessageDbException.class, () -> dao.changePassword(input.email(), input.password(), "newPass"));
		assertEquals("Please check your old password.", exception.getMessage());
	}
	
	@Test
	@Order(17)
	void testChangePasswordSamePassword() {
		var exception = assertThrows(MessageDbException.class, () -> dao.changePassword(input.email(), "admin", "admin"));
		assertEquals("New and old passwords are the same.", exception.getMessage());
	}

}
