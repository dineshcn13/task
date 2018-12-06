package com.dinesh.task.utilities;

public class Answer {
	private String answer;
	private String answerNumber;
	public Answer() { }
	public Answer(String answerNumber, String answer) {
		this.answerNumber=answerNumber;
		this.answer=answer;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswerNumber() {
		return answerNumber;
	}
	public void setAnswerNumber(String answerNumber) {
		this.answerNumber = answerNumber;
	}	
}
