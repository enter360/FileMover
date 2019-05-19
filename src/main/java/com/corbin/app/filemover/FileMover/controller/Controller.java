package com.corbin.app.filemover.FileMover.controller;

import java.awt.List;
import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.corbin.app.filemover.FileMover.Model.service.FileService;

@RestController
public class Controller {

	
	@Autowired
	FileService srvc;
    
    // Find
    @GetMapping("/makeJson")
    String findAll() {
       
       JSONArray arr = srvc.parseJson("");
       return arr.toString();
    }

    @GetMapping("/moveFile")
    String moveThem() {
    	srvc.readInAndProcessJson("Staging/TestDir/FileMoveDef.json");
    	return "moved";
    }
    
    @GetMapping("/openZip")
    String openZip() throws IOException {
    	
    	srvc.openZip();
    	return "opened";
    }

   


}

