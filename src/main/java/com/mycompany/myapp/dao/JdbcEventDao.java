package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
			
			CalendarUser attendee = jcud.getUser(rs.getInt("attendee"));
			CalendarUser owner = jcud.getUser(rs.getInt("owner"));
			
			event.setAttendee(attendee);
			event.setDescription(rs.getString("description"));
			event.setId(rs.getString("id"));
			event.setOwner(owner);
			event.setSummary(rs.getString("summary"));
			event.setWhen(when);
			return user;
		}
	};

	// --- constructors ---
	public JdbcEventDao() {
		this.jcud = new JdbcCalendarUserDao();
	}

	// --- EventService ---
	@Override
	public Event getEvent(int eventId) {
		ApplicationContext context = new GenericXmlApplicationContext(
				"com/mycompany/myapp/applicationContext.xml");
		;
		CalendarUserDao calendarUserDao = context.getBean("calendarUserDao",
				JdbcCalendarUserDao.class);

		Event event = new Event();

		Connection c;
		try {
			c = dataSource.getConnection();

			PreparedStatement ps = c
					.prepareStatement("select * from events where id = ?");
			ps.setString(1, Integer.toString(eventId));

			ResultSet rs = ps.executeQuery();
			rs.next();

			event.setId(Integer.parseInt(rs.getString("id")));

			Calendar when = Calendar.getInstance();
			when.setTimeInMillis(rs.getTimestamp("when").getTime());
			event.setWhen(when);
			event.setSummary(rs.getString("summary"));
			event.setDescription(rs.getString("description"));
			CalendarUser owner = calendarUserDao.getUser(rs.getInt("owner"));
			event.setOwner(owner);
			CalendarUser attendee = calendarUserDao.getUser(rs
					.getInt("attendee"));
			event.setAttendee(attendee);

			rs.close();
			ps.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
	}

	@Override
	public int createEvent(final Event event) {
		Connection c;
		int generatedId = 0;
		try {
			c = dataSource.getConnection();

			PreparedStatement ps = c
					.prepareStatement(
							"insert into events(`when`, summary, description, owner, attendee) values(?,?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);

			Timestamp timestamp = new Timestamp(event.getWhen()
					.getTimeInMillis());

			ps.setTimestamp(1, timestamp);
			ps.setString(2, event.getSummary());
			ps.setString(3, event.getDescription());
			ps.setInt(4, event.getOwner().getId());
			ps.setInt(5, event.getAttendee().getId());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
				generatedId = rs.getInt(1);
			}
			rs.close();
			ps.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generatedId;
	}

	@Override
	public List<Event> findForOwner(int ownerUserId) {
		// Assignment 2
		return null;
	}

	@Override
	public List<Event> getEvents() {
		ApplicationContext context = new GenericXmlApplicationContext(
				"com/mycompany/myapp/applicationContext.xml");
		;

		CalendarUserDao calendarUserDao = context.getBean("calendarUserDao",
				JdbcCalendarUserDao.class);

		List<Event> list = new ArrayList<Event>();

		Connection c;
		try {
			c = dataSource.getConnection();

			PreparedStatement ps = c.prepareStatement("select * from events");

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Event event = new Event();
				event.setId(Integer.parseInt(rs.getString("id")));
				Calendar when = Calendar.getInstance();
				when.setTimeInMillis(rs.getTimestamp("when").getTime());
				event.setWhen(when);
				event.setSummary(rs.getString("summary"));
				event.setDescription(rs.getString("description"));
				CalendarUser owner = calendarUserDao
						.getUser(rs.getInt("owner"));
				event.setOwner(owner);
				CalendarUser attendee = calendarUserDao.getUser(rs
						.getInt("attendee"));
				event.setAttendee(attendee);

				list.add(event);
			}
			rs.close();
			ps.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void deleteAll() {
		// Assignment 2
	}
}
