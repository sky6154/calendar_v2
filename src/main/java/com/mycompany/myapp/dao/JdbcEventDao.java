package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

@Repository
public class JdbcEventDao implements EventDao {
	private JdbcTemplate jdbcTemplate;
	private JdbcCalendarUserDao jcud;
	

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		jcud.setDataSource(dataSource);
	}

	private RowMapper<Event> userMapper = new RowMapper<Event>() {
		public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
			Event event = new Event();
			
			Calendar when = Calendar.getInstance();
			when.setTimeInMillis(rs.getTimestamp("when").getTime());
			
			event.setAttendee(jcud.getUser(rs.getInt("attendee")));
			event.setDescription(rs.getString("description"));
			event.setId(rs.getInt("id"));
			event.setOwner(jcud.getUser(rs.getInt("owner")));
			event.setSummary(rs.getString("summary"));
			event.setWhen(when);
			return event;
		}
	};

	// --- constructors ---
	public JdbcEventDao() {
		this.jcud = new JdbcCalendarUserDao();
	}

	// --- EventService ---
	@Override
	public Event getEvent(int eventId) {
		return this.jdbcTemplate.queryForObject("select * from events where id = ?", new Object[] {eventId}, this.userMapper);
	}

	@Override
	public int createEvent(final Event event) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		this.jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(
								"insert into events(`when`, summary, description, owner, attendee) values(?,?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
				
				Timestamp timestamp = new Timestamp(event.getWhen()
						.getTimeInMillis());
				
				ps.setTimestamp(1, timestamp);
				ps.setString(2, event.getSummary());
				ps.setString(3, event.getDescription());
				ps.setInt(4, event.getOwner().getId());
				ps.setInt(5, event.getAttendee().getId());
				
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<Event> findForOwner(int ownerUserId) {
		// Assignment 2
		
//		ApplicationContext context = new GenericXmlApplicationContext(
//				"com/mycompany/myapp/applicationContext.xml");
//		;
//
//		CalendarUserDao calendarUserDao = context.getBean("calendarUserDao",
//				JdbcCalendarUserDao.class);
		
		
		List<Event> events = new ArrayList<Event>();

		String sql;

		if (ownerUserId < 0)
			sql = "select * from events";
		else
			sql = "select * from events where owner = " + ownerUserId;

		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);

		for (Map row : rows) {
			Event event = new Event();
			
			Calendar when = Calendar.getInstance();
			when.setTimeInMillis(((Timestamp)row.get("when")).getTime());
			
			event.setAttendee(jcud.getUser(Integer.parseInt(String.valueOf(row.get("attendee")))));
			event.setDescription(row.get("description").toString());
			event.setId(Integer.parseInt(String.valueOf(row.get("id"))));
			event.setOwner(jcud.getUser(Integer.parseInt(String.valueOf(row.get("owner")))));
			event.setSummary(row.get("summary").toString());
			event.setWhen(when);
			events.add(event);
		}
		
		return events;
	}

	@Override
	public List<Event> getEvents() {
		List<Event> events = new ArrayList<Event>();

		String sql = "select * from events";

		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);

		for (Map row : rows) {
			Event event = new Event();
			
			Calendar when = Calendar.getInstance();
			when.setTimeInMillis(((Timestamp)row.get("when")).getTime());
			
			event.setAttendee(jcud.getUser(Integer.parseInt(String.valueOf(row.get("attendee")))));
			event.setDescription(row.get("description").toString());
			event.setId(Integer.parseInt(String.valueOf(row.get("id"))));
			event.setOwner(jcud.getUser(Integer.parseInt(String.valueOf(row.get("owner")))));
			event.setSummary(row.get("summary").toString());
			event.setWhen(when);
			events.add(event);
		}
		
		return events;
	}

	@Override
	public void deleteAll() {
		// Assignment 2
		this.jdbcTemplate.update("delete from events");
	}
}
