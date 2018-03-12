package me.yeon.hangman.common;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import me.yeon.hangman.vo.Question;
import me.yeon.hangman.vo.Result;
import me.yeon.hangman.vo.ResultEntry;

@Controller
public class RequestController {
	private static final Logger logger = LoggerFactory.getLogger(RequestController.class);
	GameManager gm = new GameManager();
	final String APIKEY = "ae2f41f8e65344f196cb3d4cdbfd42bd"; //부정한 로그인을 방지한다.
	
	@RequestMapping(value="test", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Result testScreen(){
		/*HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_HTML);
		return new ResponseEntity<String>("<!doctype html><html><head><meta charset=utf-8/></head><body>" + gm.getQuestion().toString() + "</body></html>",
				responseHeaders, HttpStatus.CREATED);*/
		Question q = gm.getQuestion();
		if(q.getDueDate().compareTo(Calendar.getInstance()) < 0){
			//due 시간은 지났다. 문제 풀이가 불가능하다.
			return new Result("error","question-overdue",q);
		}
		//문제를 반환한다.
		return new Result(q != null?"ok":"error","question",q);
	}
	
	/**
	 * 최초 로그인시 대화명을 기록하고 ID를 발급한다.
	 * @return
	 */
	@RequestMapping(value="login", method=RequestMethod.GET)
	public @ResponseBody Result loginSession(String username)
	{
		if(username == null || username.isEmpty()){
			return new Result("error","login-invaild username",null);
		}
		String id = gm.joinUser(username);
		return new Result(id != null ? "ok" : "error","login",id);
	}
	
	/**
	 * 정답시 클라이언트의 보고
	 * (중도참가yn, 시도 횟수, 오답 횟수, id)
	 * id가 login을 거치지 않은 경우, login 과정을 내부적으로 수행해준다.
	 * @return
	 */	
	@RequestMapping(value="finish", method=RequestMethod.GET)
	public @ResponseBody Result acceptAnswer(ResultEntry resp)
	{
		boolean isAdded = gm.processAnswer(resp);
		return new Result(isAdded?"ok":"error","sendAnswer",null);
	}
	
	/**
	 * finish 후 dueTime 이후 보낸 경우에 한해 결과표를 보내준다.
	 * dueTime이 도래하지 않았다면 응답을 보류 후 반환한다.
	 * @return
	 */
	@RequestMapping(value="report", method=RequestMethod.GET)
	public @ResponseBody Result showResult(String id)
	{
		ArrayList<ResultEntry> hm = gm.showReport(id);
		if(hm == null){
			return new Result("error","report-not available",null);
		}else{
			return new Result("ok","report",hm);
		}
	}
	
	/**
	 * 시간 보정값을 제공한다.
	 * @param client_now
	 * @param api
	 * @param usrkey
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "timecompare", method = RequestMethod.GET)
	public String adjustTime(@RequestParam(value="fromDate") @DateTimeFormat(pattern="yyyy-MM-dd@HH:mm:ss.SSSZ") Calendar client_now, String api, String usrkey)
	{
		Calendar utc_now = Calendar.getInstance();
		Duration d = Duration.between(client_now.toInstant(), utc_now.toInstant());
		long difference = d.toMillis();
		return String.valueOf(difference);
	}
	
	private boolean isVaildApi(String api){
		return APIKEY.equals(api);
	}
	
}
