package com.corbin.app.filemover.FileMover.Model.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.UnzipUtility;

@Service
public class FileService {
	
	
	UnzipUtility util = new UnzipUtility();
	
	public void makeJSON() {
		 //First Employee
        JSONObject employeeDetails = new JSONObject();
        employeeDetails.put("FileName", "blue-flower.jpg");
        employeeDetails.put("NewName", "big-blue-flower.jpg");
        employeeDetails.put("destination", "TestDir/flowers/");
         
        JSONObject employeeObject = new JSONObject();
        employeeObject.put("file", employeeDetails);
         
        //Second Employee
        JSONObject employeeDetails2 = new JSONObject();
        employeeDetails2.put("FileName", "bright-close-up.jpg");
        employeeDetails2.put("NewName", "big-bright.jpg");
        employeeDetails2.put("destination", "TestDir/bright/");
         
        JSONObject employeeObject2 = new JSONObject();
        employeeObject2.put("file", employeeDetails2);
         
        //Add employees to list
        JSONArray employeeList = new JSONArray();
        employeeList.add(employeeObject);
        employeeList.add(employeeObject2);
         
        //Write JSON file
        try (FileWriter file = new FileWriter("FileMovDef.json")) {
 
            file.write(employeeList.toJSONString());
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    
	}
	

	@SuppressWarnings("unchecked")
	public JSONArray parseJson(String location) {
		makeJSON();
	// JSON parser object to parse read file
	JSONParser jsonParser = new JSONParser();

	try(
			FileReader reader = new FileReader("TestDir/Staging/FileMovDef.json"))
	{
		// Read JSON file
		Object obj = jsonParser.parse(reader);

		JSONArray employeeList = (JSONArray) obj;
		System.out.println(employeeList);

		// Iterate over employee array
		employeeList.forEach(emp -> parseEmployeeObject((JSONObject) emp));
		return employeeList;
	}catch(
			FileNotFoundException e)
	{
		e.printStackTrace();
	}catch(
			IOException e)
	{
		e.printStackTrace();
	}catch(
			ParseException e)
	{
		e.printStackTrace();
	}
		return null;
}
	
	
public void readInAndProcessJson(String location) {
	JSONParser jsonParser = new JSONParser();

	try(FileReader reader = new FileReader(location))
	{
		// Read JSON file
		Object obj = jsonParser.parse(reader);
		
		JSONArray fileList = (JSONArray) obj;
		
		fileList.forEach(fil -> {
			try {
				createFileStructure((JSONObject) fil);
				MoveAndRenameFile((JSONObject) fil );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}catch (FileNotFoundException e)
	{
		e.printStackTrace();
	}catch(
			IOException e)
	{
		e.printStackTrace();
	}catch(
			ParseException e)
	{
		e.printStackTrace();
	}
	
}
	
public boolean createFileStructure(JSONObject inputArr) throws IOException {
	boolean complete = false;
	
	JSONObject fileObj = (JSONObject) inputArr.get("file");
	
	String dest = (String) fileObj.get("destination");
	
	File destDir = new File(dest);
    if (!destDir.exists()) {
    	 Path dir = Paths.get(dest);
    	    Files.createDirectories(dir);
    }else {
    	complete =true;
    }
 
	return complete;
}

public void MoveAndRenameFile(JSONObject inputArr) throws IOException {
	JSONObject fileObj = (JSONObject) inputArr.get("file");
	
	String dest = (String) fileObj.get("destination");
	String newName = (String) fileObj.get("NewName"); 
	String nowName = (String) fileObj.get("FileName");
	
	File imageFile= new File(dest+newName);
	File existingFile = new File(nowName);
	copyFile(existingFile, imageFile);

	
}
	
public void openZip() throws IOException {
	
	try {
	util.unzip("TestDir.zip", "Staging/");
	}catch (Exception e){
		e.printStackTrace();
	}
}

private static void parseEmployeeObject(JSONObject employee)
{
	//Get employee object within list
	JSONObject employeeObject = (JSONObject) employee.get("employee");

	//Get employee first name
	String firstName = (String) employeeObject.get("FileName");   
	System.out.println(firstName);

	//Get employee last name
	String lastName = (String) employeeObject.get("NewName"); 
	System.out.println(lastName);

	//Get employee website name
	String website = (String) employeeObject.get("destination");   
	System.out.println(website);
}

public  void copyFile(File sourceFile, File destFile) throws IOException {
    if(!destFile.exists()) {
     destFile.createNewFile();
    }

    FileChannel source = null;
    FileChannel destination = null;
    try {
     source = new RandomAccessFile(sourceFile,"rw").getChannel();
     destination = new RandomAccessFile(destFile,"rw").getChannel();

     long position = 0;
     long count    = source.size();

     source.transferTo(position, count, destination);
    }
    finally {
     if(source != null) {
      source.close();
     }
     if(destination != null) {
      destination.close();
     }
   }
}

}
