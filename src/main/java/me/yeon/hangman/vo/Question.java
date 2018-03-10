package me.yeon.hangman.vo;

import java.util.Arrays;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Question {
	private String word;
	private String[] questions;
	private String[] answers;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Calendar beginDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Calendar dueDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Calendar reportDate;
	@Override
	public String toString() {
		return "Question [word=" + word + ", questions=" + Arrays.toString(questions) + ", answers="
				+ Arrays.toString(answers) + ", " + ", beginDate=" + beginDate + ", dueDate=" + dueDate
				+ ", reportDate=" + reportDate + "]";
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String[] getQuestions() {
		return questions;
	}
	public void setQuestions(String[] questions) {
		this.questions = questions;
	}
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
	public int getLength() {
		return word.length();
	}
	public Calendar getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Calendar beginDate) {
		this.beginDate = beginDate;
		Calendar newdue = (Calendar)beginDate.clone();
		newdue.add(Calendar.SECOND, Math.max(20, Math.min(60, getLength()*6))); //문제풀이 시간
		Calendar newreport = (Calendar)newdue.clone();
		newreport.add(Calendar.SECOND, 10); //성적표 표시 시간
		this.dueDate = newdue; 
		this.reportDate = newreport;
	}
	public Calendar getDueDate() {
		return dueDate;
	}
	public Calendar getReportDate() {
		return reportDate;
	}
}
