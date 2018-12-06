package com.dinesh.task.dto;

import java.util.ArrayList;
import java.util.List;

import com.dinesh.task.utilities.Question;

public class UploadDto {
	
	private String uploadId;
	private List<Question>questions = new ArrayList<Question>(0);
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	
	

}
