package com.mycompany.myapp.dao;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="../applicationContext.xml")

public class DaoJUnitTest {
	@Autowired
	private CalendarUserDao calendarUserDao;	
	
	@Autowired
	private EventDao eventDao;
	
	private CalendarUser[] calendarUsers = null;
	private Event[] events = null;
	
	@Before
	public void setUp() {
		calendarUsers = new CalendarUser[3];
		events = new Event[3];
		
		this.calendarUserDao.deleteAll();
		this.eventDao.deleteAll();
		
		/* [참고]
		insert into calendar_users(`id`,`email`,`password`,`name`) values (1,'user1@example.com','user1','User1');
		insert into calendar_users(`id`,`email`,`password`,`name`) values (2,'admin1@example.com','admin1','Admin');
		insert into calendar_users(`id`,`email`,`password`,`name`) values (3,'user2@example.com','user2','User1');

		insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (100,'2013-10-04 20:30:00','Birthday Party','This is going to be a great birthday',1,2);
		insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (101,'2013-12-23 13:00:00','Conference Call','Call with the client',3,1);
		insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (102,'2014-01-23 11:30:00','Lunch','Eating lunch together',2,3);
		*/
		
		// 1. SQL 코드에 존재하는 3개의 CalendarUser와 Event 각각을 Fixture로서 인스턴스 변수 calendarUsers와 events에 등록하고 DB에도 저장한다. 
		// [주의 1] 모든 id 값은 입력하지 않고 DB에서 자동으로 생성되게 만든다. 
		// [주의 2] Calendar를 생성할 때에는 DB에서 자동으로 생성된 id 값을 받아 내어서 Events를 만들 때 owner와 attendee 값으로 활용한다.
		
		
//		insert into calendar_users(`id`,`email`,`password`,`name`) values (1,'user1@example.com','user1','User1');
//		insert into calendar_users(`id`,`email`,`password`,`name`) values (2,'admin1@example.com','admin1','Admin');
//		insert into calendar_users(`id`,`email`,`password`,`name`) values (3,'user2@example.com','user2','User1');
		calendarUsers[0].setEmail("user1@example.com");
		calendarUsers[0].setName("User1");
		calendarUsers[0].setPassword("user1");
		
		calendarUsers[1].setEmail("user2@example.com");
		calendarUsers[1].setName("User2");
		calendarUsers[1].setPassword("user2");
		
		calendarUsers[2].setEmail("user3@example.com");
		calendarUsers[2].setName("User3");
		calendarUsers[2].setPassword("user3");
		
		int id1 = this.calendarUserDao.createUser(calendarUsers[0]);
		int id2 = this.calendarUserDao.createUser(calendarUsers[1]);
		int id3 = this.calendarUserDao.createUser(calendarUsers[2]);
		
		
//		insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (100,'2013-10-04 20:30:00','Birthday Party','This is going to be a great birthday',1,2);
//		insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (101,'2013-12-23 13:00:00','Conference Call','Call with the client',3,1);
//		insert into events (`id`,`when`,`summary`,`description`,`owner`,`attendee`) values (102,'2014-01-23 11:30:00','Lunch','Eating lunch together',2,3);

		
		Calendar date1 = Calendar.getInstance();
		date1.set(Calendar.YEAR, 2013);
		date1.set(Calendar.MONTH, 10);
		date1.set(Calendar.DAY_OF_MONTH, 4);
		date1.set(Calendar.HOUR_OF_DAY, 20);
		date1.set(Calendar.MINUTE, 30);
		date1.set(Calendar.SECOND, 0);
		
		Calendar date2 = Calendar.getInstance();
		date2.set(Calendar.YEAR, 2013);
		date2.set(Calendar.MONTH, 12);
		date2.set(Calendar.DAY_OF_MONTH, 23);
		date2.set(Calendar.HOUR_OF_DAY, 13);
		date2.set(Calendar.MINUTE, 0);
		date2.set(Calendar.SECOND, 0);
		
		Calendar date3 = Calendar.getInstance();
		date3.set(Calendar.YEAR, 2014);
		date3.set(Calendar.MONTH, 1);
		date3.set(Calendar.DAY_OF_MONTH, 23);
		date3.set(Calendar.HOUR_OF_DAY, 11);
		date3.set(Calendar.MINUTE, 30);
		date3.set(Calendar.SECOND, 0);
		
		
		events[0].setAttendee(calendarUserDao.getUser(id2));
		events[0].setDescription("This is going to be a great birthday");
		events[0].setOwner(calendarUserDao.getUser(id1));
		events[0].setSummary("Birthday Party");
		events[0].setWhen(date1);
		
		events[1].setAttendee(calendarUserDao.getUser(id1));
		events[1].setDescription("Call with the client");
		events[1].setOwner(calendarUserDao.getUser(id3));
		events[1].setSummary("Conference Call");
		events[1].setWhen(date2);
		
		events[2].setAttendee(calendarUserDao.getUser(id3));
		events[2].setDescription("Eating lunch together");
		events[2].setOwner(calendarUserDao.getUser(id2));
		events[2].setSummary("Lunch");
		events[2].setWhen(date3);
	}
	
	@Test
	public void createCalendarUserAndCompare() {
		// 2. 새로운 CalendarUser 2명 등록 및 각각 id 추출하고, 추출된 id와 함께 새로운 CalendarUser 2명을 DB에서 가져와 (getUser 메소드 사용) 
		//    방금 등록된 2명의 사용자와 내용 (이메일, 이름, 패스워드)이 일치하는 지 비교
		
		CalendarUser createUser1 = new CalendarUser();
		CalendarUser createUser2 = new CalendarUser();
		int createUser1Id;
		int createUser2Id;
		
		createUser1.setEmail("createUser1@spring.book");
		createUser1.setPassword("createUser1");
		createUser1.setName("createUser1");
		createUser2.setEmail("createUser2@spring.book");
		createUser2.setPassword("createUser2");
		createUser2.setName("createUser2");
		
		createUser1Id = calendarUserDao.createUser(createUser1);
		createUser2Id = calendarUserDao.createUser(createUser2);
		
		System.out.println("createUser1 Id: " + createUser1Id);
		System.out.println("createUser2 Id: " + createUser2Id);
		
		CalendarUser compareUser1 = calendarUserDao.getUser(createUser1Id);
		CalendarUser compareUser2 = calendarUserDao.getUser(createUser2Id);
		
		assertThat(createUser1.getName(), is(compareUser1.getName()));
		assertThat(createUser1.getEmail(), is(compareUser1.getEmail()));
		assertThat(createUser1.getPassword(), is(compareUser1.getPassword()));
		
		assertThat(createUser2.getName(), is(compareUser2.getName()));
		assertThat(createUser2.getEmail(), is(compareUser2.getEmail()));
		assertThat(createUser2.getPassword(), is(compareUser2.getPassword()));
	}
	
	@Test
	public void createEventUserAndCompare() {
		// 3. 새로운 Event 2개 등록 및 각각 id 추출하고, 추출된 id와 함께 새로운 Event 2개를 DB에서 가져와 (getEvent 메소드 사용) 
		//    방금 추가한 2개의 이벤트와 내용 (summary, description, owner, attendee)이 일치하는 지 비교
		// [주의 1] when은 비교하지 않아도 좋다.
		// [주의 2] owner와 attendee는 @Before에서 미리 등록해 놓은 3명의 CalendarUser 중에서 임의의 것을 골라 활용한다.
		
		Event createEvent1 = new Event();
		Event createEvent2 = new Event();
		int createEvent1Id;
		int createEvent2Id;
		
		createEvent1.setWhen(Calendar.getInstance());
		createEvent1.setSummary("event1 - summary");
		createEvent1.setDescription("event1 - description");
		createEvent1.setOwner(calendarUserDao.getUser(1));
		createEvent1.setAttendee(calendarUserDao.getUser(2));
		
		createEvent2.setWhen(Calendar.getInstance());
		createEvent2.setSummary("event2 - summary");
		createEvent2.setDescription("event2 - description");
		createEvent2.setOwner(calendarUserDao.getUser(3));
		createEvent2.setAttendee(calendarUserDao.getUser(1));
		
		createEvent1Id = eventDao.createEvent(createEvent1);
		createEvent2Id = eventDao.createEvent(createEvent2);
		
		System.out.println("createEvent1 Id: " + createEvent1Id);
		System.out.println("createEvent2 Id: " + createEvent2Id);
		
		Event compareEvent1 = eventDao.getEvent(createEvent1Id);
		Event compareEvent2 = eventDao.getEvent(createEvent2Id);
		
		assertThat(createEvent1.getAttendee(), is(compareEvent1.getAttendee()));
		assertThat(createEvent1.getDescription(), is(compareEvent1.getDescription()));
		assertThat(createEvent1.getOwner(), is(compareEvent1.getOwner()));
		assertThat(createEvent1.getSummary(), is(compareEvent1.getSummary()));
	}
	
	@Test
	public void getAllEvent() {
		// 4. 모든 Events를 가져오는 eventDao.getEvents()가 올바로 동작하는 지 (총 3개를 가지고 오는지) 확인하는 테스트 코드 작성  
		// [주의] fixture로 등록된 3개의 이벤트들에 대한 테스트
		List<Event> eList = eventDao.getEvents();
		
		assertThat(eList.size(), is(3));
	}
	
	@Test
	public void getEvent() {
		// 5. owner ID가 3인 Event에 대해 findForOwner가 올바로 동작하는 지 확인하는 테스트 코드 작성  
		// [주의] fixture로 등록된 3개의 이벤트들에 대해서 owner ID가 3인 것인 1개의 이벤트뿐임 
		
		List<Event> testEvent = eventDao.findForOwner(3);
		
		assertThat(testEvent.get(0).getOwner().getId(), is(3));
	}
	
	@Test
	public void getOneUserByEmail() {
		// 6. email이 'user1@example.com'인 CalendarUser가 존재함을 확인하는 테스트 코드 작성 (null인지 아닌지)
		// [주의] public CalendarUser findUserByEmail(String email)를 테스트 하는 코드
		
		CalendarUser test6 = calendarUserDao.findUserByEmail("user1@example.com");
		
		assertTrue(test6 != null);
	}
	
	@Test
	public void getTwoUserByEmail() {
		// 7. partialEmail이 'user'인 CalendarUser가 2명임을 확인하는 테스크 코드 작성
		// [주의] public List<CalendarUser> findUsersByEmail(String partialEmail)를 테스트 하는 코드
		
		List<CalendarUser> cList = calendarUserDao.findUsersByEmail("user");
		
		assertThat(cList.size(), is(2));
	}
}