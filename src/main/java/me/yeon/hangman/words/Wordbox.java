package me.yeon.hangman.words;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import me.yeon.hangman.vo.Question;

/**
 * Wordbox는 GameManager에 의해 관리되며, 주어진 단어를 무작위로 출제할 수 있으며, 보기까지 생성한다.
 * @author yeon
 *
 */
public class Wordbox {
	
	private ArrayList<Question> alList = new ArrayList<>();
	private final String[] romaji = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n",
			"o","p","q","r","s","t","u","v","w","x","y","z"};
	private int carot = -1;
	
	/**
	 * 생성시 단어를 초기화하고 추가한다.
	 */
	public Wordbox(){
		fillWords();
	}
	
	/**
	 * 단어를 무작위로 넣는 addWord는 단어 개수만큼 호출된다.
	 * @param word
	 */
	private void addWord(String word){
		ArrayList<String> lWord = new ArrayList<>(); //보기 담기
		ArrayList<String> nWord = new ArrayList<>(Arrays.asList(romaji)); //보기에서 빠진 문자
		
		Question q = new Question(); 
		//ArrayList<String> qWord = new ArrayList<>(); //대입될 보기
		ArrayList<String> aWord = new ArrayList<>(); //대입될 정답
		for(char w : word.toCharArray()){
			if(!lWord.contains(String.valueOf(w))){
				//add offset에 random 값을 넣으면 저절로 순서가 무작위로 맞춰진다. 이하 동문.
				lWord.add(ThreadLocalRandom.current().nextInt(0, Math.max(1, lWord.size())),String.valueOf(w));
				aWord.add(ThreadLocalRandom.current().nextInt(0, Math.max(1, aWord.size())),String.valueOf(w));
			}
			nWord.remove(String.valueOf(w));
		}
		for(int i = 0; i < Math.min(7, nWord.size()); i++){ //오답이 들어간다. 최대 7개, 최저 남은 문자열
			int rand_word = ThreadLocalRandom.current().nextInt(0, nWord.size());
			lWord.add(ThreadLocalRandom.current().nextInt(0, Math.max(1, lWord.size())),nWord.get(rand_word));
			nWord.remove(rand_word);
		}
		q.setAnswers(aWord.toArray(new String[aWord.size()]));
		//q.setLength(word.length());
		q.setQuestions(lWord.toArray(new String[lWord.size()]));
		q.setWord(word);
		
		alList.add(ThreadLocalRandom.current().nextInt(0, Math.max(1, alList.size())),q);
	}
	
	/**
	 * 단어 목록을 초기화하고 무작위로 다시 넣을 수 있는 addWords를 호출한다.
	 */
	private void fillWords(){
		alList.clear();
		addWord("eyebrow");
		addWord("window");
		addWord("delivery");
		addWord("transport");
		addWord("willpower");
		addWord("dribble");
		addWord("census");
		addWord("falsify");
		addWord("precision");
		addWord("overlook");
		addWord("refund");
		addWord("person");
		addWord("spokesperson");
		addWord("mourning");
		addWord("transmission");
		addWord("locate");
		addWord("mention");
		addWord("distortion");
		addWord("insurance");
		addWord("missile");
		addWord("celebration");
		addWord("strike");
		addWord("right");
		addWord("wing");
		addWord("capture");
		addWord("depressed");
		addWord("overeat");
		addWord("attraction");
		addWord("record");
		addWord("pastel");
		addWord("collect");
		addWord("entitlement");
		addWord("overview");
		addWord("settle");
		addWord("departure");
		addWord("remain");
		addWord("infect");
		addWord("ignite");
		addWord("canvas");
		addWord("screen");
		addWord("extend");
		addWord("association");
		addWord("election");
		addWord("deserve");
		addWord("sermon");
		addWord("disposition");
		addWord("affinity");
		addWord("modernize");
		addWord("vegetable");
		addWord("seasonal");
		addWord("college");
		addWord("feature");
		addWord("cunning");
		addWord("breakfast");
		addWord("cemetery");
		addWord("exploration");
		addWord("perception");
		addWord("courtesy");
		addWord("bounce");
		addWord("looting");
		addWord("achievement");
		addWord("scandal");
		addWord("ballet");
		addWord("sensitivity");
		addWord("carriage");
		addWord("general");
		addWord("autonomy");
		addWord("evening");
		addWord("chemistry");
		addWord("cell");
		addWord("phone");
		addWord("second");
		addWord("plaster");
		addWord("meaning");
		addWord("staircase");
		addWord("mechanism");
		addWord("presidency");
		addWord("triangle");
		addWord("runner");
		addWord("realize");
		addWord("generation");
		addWord("compromise");
		addWord("characteristic");
		addWord("imposter");
		addWord("wealth");
		addWord("occasion");
		addWord("participate");
		addWord("computer");
		addWord("virus");
		addWord("random");
		addWord("movement");
		addWord("silver");
		addWord("mushroom");
		addWord("landscape");
		addWord("sleeve");
		addWord("residence");
		addWord("qualification");
		addWord("suburb");
		addWord("complex");
		addWord("tragedy");
		addWord("overcharge");
		addWord("passage");
		addWord("opposed");
	}
	
	/**
	 * 다음 단어를 순차적으로 불러올 수 있게 한다.
	 * @return
	 */
	public Question nextWord(){
		if(carot+1 >= alList.size()) {
			fillWords();
			carot = -1;			
		}
		carot = carot+1;
		return alList.get(carot);
	}
	
	
}
