package com.dinesh.task.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dinesh.task.dto.UploadDto;
import com.dinesh.task.repo.UploadRepository;
import com.dinesh.task.utilities.UploadReader;
import com.fasterxml.uuid.Generators;

@RestController
@RequestMapping("/upload")
public class UploadController {
	
	@Autowired private UploadRepository uploadRepo;
	
	@PostMapping("/save")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile, UploadDto dto) {

		String data="";
        if (uploadfile.isEmpty()) {
            return new ResponseEntity<>("Please select a file!", HttpStatus.OK);
        }
        try {
            String uploadedFile = saveUploadedFiles(Arrays.asList(uploadfile));
            data=UploadReader.parseToJson(uploadedFile, "./uploads/images");
            
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity
        		.status(HttpStatus.OK)
        		.body(data);
    }
	
	private String saveUploadedFiles(List<MultipartFile> files){
    	String file_location="";
    	try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; //next pls
                }
                byte[] bytes = file.getBytes();
                UUID uuid2 = Generators.randomBasedGenerator().generate();
                file_location="./uploads/" + uuid2 + file.getOriginalFilename();                
                Path path = Paths.get(file_location);
                Files.write(path, bytes);
            }
            
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	return file_location;
    }	

}
