package com.mycompany.myapp.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.Event;

@Repository
public class JdbcEventDao implements EventDao {
    private DataSource dataSource;

    // --- constructors ---
    public JdbcEventDao() {
    }

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

    // --- EventService ---
    @Override
    public Event getEvent(int eventId) {
        return null;
    }

    @Override
    public int createEvent(final Event event) {
        return 0;
    }

    @Override
    public List<Event> findForUser(int userId) {
        return null;
    }

    @Override
    public List<Event> getEvents() {
        return null;
    }

    /*
    private static final String EVENT_QUERY = "select e.id, e.summary, e.description, e.when, " +
            "owner.id as owner_id, owner.email as owner_email, owner.password as owner_password, owner.name as owner_name, " +
            "attendee.id as attendee_id, attendee.email as attendee_email, attendee.password as attendee_password, attendee.name as attendee_name " +
            "from events as e, calendar_users as owner, calendar_users as attendee " +
            "where e.owner = owner.id and e.attendee = attendee.id";
     */
}
