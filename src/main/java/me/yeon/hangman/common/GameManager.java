package me.yeon.hangman.common;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import me.yeon.hangman.vo.Question;
import me.yeon.hangman.vo.ResultEntry;
import me.yeon.hangman.vo.User;
import me.yeon.hangman.words.Wordbox;

/**
 * GameManager는 현재 게임 세션을 유지하며, 문제를 출제하며, 출제된 문제를 시간에 따라 진행하고, 결과를 종합한다.
 * 
 * 
 * @author yeon
 *
 */
public class GameManager {
	private Wordbox wb = new Wordbox();
	private Question lastWord = null;
	private ArrayList<User> users = new ArrayList<>();
	private HashMap<String,ResultEntry> reports = new HashMap<>();
	
	/**
	 * 사용자가 새로 접속하면 불린다. 같은 유저 이름이라도 id가 다르게 계속 생성된다.
	 * @param user
	 * @return
	 */
	public String joinUser(String user){
		User u = new User();
		u.setName(user);
		u.setLoginTime(Calendar.getInstance());
		long id = Math.abs(java.util.Objects.hash(user)*ThreadLocalRandom.current().nextInt());
		//이름과 무관하지 않은 무작위 id를 생성한다.
		while(users.contains(new User(String.valueOf(id)))){
			//id는 중복되지 않아야 한다.
			id = Math.abs(java.util.Objects.hash(user)*ThreadLocalRandom.current().nextInt());
		}
		u.setId(String.valueOf(id));
		users.add(u);
		return u.getId();
	}
	
	/**
	 * 새로운 문제를 출제하거나 기존 문제를 요청할 때 불린다.
	 * @return
	 */
	public Question getQuestion(boolean silent){
		Question q = null;
		if(!silent && (lastWord == null || 
				lastWord.getReportDate().before(Calendar.getInstance()))){
			//마지막 단어가 없거나 만료 시간을 지난 경우 신규 출제
			reports.clear();
			q = wb.nextWord();
			lastWord = q;
			q.setBeginDate(Calendar.getInstance());
		} else {
			q = lastWord;
		}
		return q;
	}
	
	public Question getQuestion(){
		return getQuestion(false);
	}
	
	public boolean processAnswer(ResultEntry resp){
		String id = resp.getId();
		if(!users.contains(new User(id)) || reports.containsKey(id) || lastWord == null){
			//출제 상태 아님, 로그인한 적 없는 유저, 기 제출된 유저
			return false;
		}
		int uidx = users.indexOf(new User(id));
		User u = users.get(uidx);
		resp.setName(u.getName());
		//문제 시작 시간보다 늦은 로그인 시간이라면 부분 시간 풀기로 표시
		resp.setPartial(lastWord.getBeginDate().before(u.getLoginTime()));
		u.setAnswerTime(Calendar.getInstance());
		resp.setMs(Math.toIntExact(Calendar.getInstance().getTimeInMillis()-lastWord.getBeginDate().getTimeInMillis()));
		reports.put(id,resp);
		System.out.println("processAnswer 등록됨: "+resp.getName());
		return true;
	}
	
	public ArrayList<ResultEntry> showReport(String uid){
		/*if(!users.contains(new User(uid))){
			//요청한 사람은 로그인 상태가 아니다
			return null;
		}else if(lastWord == null || reports == null ||
				lastWord.getReportDate().after(Calendar.getInstance())){
			//출제 기록이 없거나 보여줄 리포트가 널이거나
			//성적 표기 기한을 지났으면 표시할 수 없다
			return null;
		}
		if(lastWord.getDueDate().after(Calendar.getInstance())){
			HashMap<String,ResultEntry> alResult = new HashMap<>();
			//아직 문제풀이가 끝나지 않았으므로, delayReport가 필요하다.
			alResult.put("wait",null);
			return alResult;
		}*/
		ArrayList<ResultEntry> hs = new ArrayList<>();
		for(Map.Entry<String, ResultEntry> kv : reports.entrySet()){
			hs.add(kv.getValue());
		}
		return hs;
	}
}
