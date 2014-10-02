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

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.Event;

@Repository
public class JdbcEventDao implements EventDao {
	private JdbcTemplate jdbcTemplate;
	
	// CalendarUser호출을 위한 객체
	// 코드의 재사용을 줄여보기 위해 사용하였습니다.
	// 이 class에 쿼리문을 또 작성한다면 쿼리문 변경시에 두개의 쿼리문을 변경해야 할 것 같았습니다.
	private JdbcCalendarUserDao jcud;
	

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
		// 초기화
		jcud.setDataSource(dataSource);
	}

	// 유저를 DB에서 얻어올때 사용할 Mapper객체
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
		// 생성과 동시에 초기화
		this.jcud = new JdbcCalendarUserDao();
	}

	// --- EventService ---
	@Override
	public Event getEvent(int eventId) {
		return this.jdbcTemplate.queryForObject("select * from events where id = ?", new Object[] {eventId}, this.userMapper);
	}

	@Override
	public int createEvent(final Event event) {
		// Auto Increment되는 id를 받아오기 위한 객체
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

		// -1 이하의 값이 들어올 경우 모든 event를 가져오도록 하였습니다.
		if (ownerUserId < 0)
			sql = "select * from events";
		else
			sql = "select * from events where owner = " + ownerUserId;

		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);

		// Event들을 받아옵니다.
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

		// Event들을 받아옵니다.
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
