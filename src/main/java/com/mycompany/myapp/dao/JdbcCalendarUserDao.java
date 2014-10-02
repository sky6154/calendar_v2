package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;

@Repository
public class JdbcCalendarUserDao implements CalendarUserDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// CalendarUser를 결과와 매핑해줄 Mapper
	private RowMapper<CalendarUser> userMapper = new RowMapper<CalendarUser>() {
		public CalendarUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			CalendarUser user = new CalendarUser();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	};

	// --- constructors ---
	public JdbcCalendarUserDao() {
	}

	// --- CalendarUserDao methods ---
	@Override
	public CalendarUser getUser(int id){
		return this.jdbcTemplate.queryForObject("select * from calendar_users where id = ?", new Object[] {id}, this.userMapper);
	}

	@Override
	public CalendarUser findUserByEmail(String email) {
		return this.jdbcTemplate.queryForObject("select * from calendar_users where email = ?", new Object[] {email}, this.userMapper);
	}

	@Override
	public List<CalendarUser> findUsersByEmail(String email) {
		List<CalendarUser> calendarUsers = new ArrayList<CalendarUser>();
		
		String sql;
		
		// null일경우 전체를 반환합니다.
		if(email == null)
			sql = "select * from calendar_users";
		else
			 sql= "select * from calendar_users where email like '%" + email + "%'";
		
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
		
		// 결과에 부합하는 CalendarUser들을 받아옵니다.
		for(Map row : rows){
			CalendarUser cu = new CalendarUser();
			cu.setId(Integer.parseInt(String.valueOf(row.get("id"))));
			cu.setName(row.get("name").toString());
			cu.setPassword(row.get("password").toString());
			cu.setEmail(row.get("email").toString());
			
			calendarUsers.add(cu);
		}
		
		return calendarUsers;
	}

	@Override
	public int createUser(final CalendarUser userToAdd){
		// 자동으로 증가된 id값을 받아오기 위해 사용합니다.
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("insert into calendar_users(name, password, email) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, userToAdd.getName());
				ps.setString(2, userToAdd.getPassword());
				ps.setString(3, userToAdd.getEmail());
				return ps;
			}
		}, keyHolder);
		
		// 받아온 id를 반환합니다.
		return keyHolder.getKey().intValue();
	}
	
	@Override
	public void deleteAll() {
		// Assignment 2
		this.jdbcTemplate.update("delete from calendar_users");
	}
}