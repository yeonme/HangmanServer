package me.yeon.hangman.common;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

/**
 * Innitial Component는 스프링 서버 실행시 초기화 메서드를 지정한다.
 * @author yeon
 *
 */
@Repository
public class InitialComponent {
	@PostConstruct
	public void init(){
		System.out.println("init method run");
		//UTC 시간대로 기본 변경한다. 이제 Parameter로 받은 시간이 정상적으로 변환된다.
		System.setProperty("user.timezone", "UTC");
	    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
