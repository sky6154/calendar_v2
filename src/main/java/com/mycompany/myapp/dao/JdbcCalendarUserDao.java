package com.mycompany.myapp.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;

@Repository
public class JdbcCalendarUserDao implements CalendarUserDao {

	private DataSource dataSource;

    // --- constructors ---
    public JdbcCalendarUserDao() {

    }
    	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

    // --- CalendarUserDao methods ---
    @Override
    public CalendarUser getUser(int id) {
    	return null;
    }

    @Override
    public CalendarUser findUserByEmail(String email) {
    	return null;
    }

    @Override
    public List<CalendarUser> findUsersByEmail(String email) {
    	return null;
    }

    @Override
    public int createUser(final CalendarUser userToAdd) {
        return 0;
    }
}