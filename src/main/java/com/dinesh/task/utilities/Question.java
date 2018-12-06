package com.dinesh.task.utilities;

import java.util.ArrayList;
import java.util.List;

public class Question {
	private String sectionNumber;
	private String sectionName;
	private String question; 
	private String questionNumber;	
	private List<Answer> answers=new ArrayList<Answer>();
	private Answer correctAnswer;
//	public Question() { }
	public Question(String questionNumber, String question) {
		this.questionNumber=questionNumber;
		this.question=question;
	}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public Answer getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(Answer correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public String getSectionNumber() {
		return sectionNumber;
	}
	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	
	
}
