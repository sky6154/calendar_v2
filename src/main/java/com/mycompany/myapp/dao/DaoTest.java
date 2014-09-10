package com.mycompany.myapp.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;

public class DaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = null;
		
		CalendarUserDao calendarUserDao = null;
		EventDao eventDao = null;
		
		//1. 디폴트로 등록된 CalendarUser 3명 출력 (패스워드 제외한 모든 내용 출력)
		//2. 디폴트로 등록된 Event 3개 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력) 
		
		//3. 새로운 CalendarUser 2명 등록 및 각각 id 추출
		//4. 추출된 id와 함께 새로운 CalendarUser 2명을 DB에서 가져와 (getUser 메소드 사용) 방금 등록된 2명의 사용자와 내용 (이메일, 이름, 패스워드)이 일치하는 지 비교
		//5. 5명의 CalendarUser 모두 출력 (패스워드 제외한 모든 내용 출력)
		
		//6. 새로운 Event 2개 등록 및 각각 id 추출
		//7. 추출된 id와 함께 새로운 Event 2개를 DB에서 가져와 (getEvent 메소드 사용) 방금 추가한 2개의 이벤트와 내용 (when, summary, description, owner, attendee)이 일치하는 지 비교
		//8. 5개의 Event 모두 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력)
	}
}
