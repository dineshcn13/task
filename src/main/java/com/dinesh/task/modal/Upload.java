package com.dinesh.task.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;


@Entity
@Table(name="uploads")
public class Upload {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "uploadId", updatable = false, nullable = false)
	private String uploadId;
	
//	@Type(type = "jsonb-node")
	@Column(name = "uploadData")
	private String uploadData;

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getUploadData() {
		return uploadData;
	}

	public void setUploadData(String uploadData) {
		this.uploadData = uploadData;
	}

	
	
	
	
	
	
	

}
