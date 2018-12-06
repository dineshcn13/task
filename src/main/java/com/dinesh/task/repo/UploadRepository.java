package com.dinesh.task.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dinesh.task.modal.Upload;

public interface UploadRepository extends JpaRepository<Upload, String>{

}
