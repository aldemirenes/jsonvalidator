package com.jsonValidator;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;	

import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONException;

import java.nio.charset.StandardCharsets;

@Path("/schema")
public class JsonSchemaHandler {
	
	//directory path of the json schema files
	//should be changed accordingly
	String schemaFilesPath = "/home/aldemirenes/temp/jsonfiles";
	
	@GET
	@Path("/{param}")
	public Response getJsonSchema(@PathParam("param") String schemaID) throws JSONException, IOException {
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("action", "uploadSchema");
		resultJson.put("id", schemaID);
	    
		//create file path according to the schemaID
		String currFilePath = schemaFilesPath + "/" + schemaID + ".json";
	    
		File jsonSchemaFile = new File(currFilePath);
		if(!jsonSchemaFile.exists()) {
			//if wanted json schema does not exist, return error
			String errorMsg = "Does not exist schema with this id";
			resultJson.put("status", "error");
			resultJson.put("message", errorMsg);
			return Response.status(404).entity(resultJson.toString()).build(); 
		}
	    
		byte[] encoded = Files.readAllBytes(Paths.get(currFilePath));
		String jsonSchema = new String(encoded, StandardCharsets.UTF_8);
		return Response.status(200).entity(jsonSchema).build(); 
	}
	
	@POST
	@Path("/{param}")
	public Response upload(@PathParam("param") String schemaID, String newJsonSchema) throws JSONException {
		JSONObject resultJson = new JSONObject();
	    resultJson.put("action", "uploadSchema");
	    resultJson.put("id", schemaID);
	    
	    //check if new json is in a valid json format
	    //if not converting the string to json will raise an exception
	    try{
			JSONObject jsonObj = new JSONObject(newJsonSchema);
		} catch(org.json.JSONException e) {
			String errorMsg = "Invalid JSON";
		    resultJson.put("status", "error");
		    resultJson.put("message", errorMsg);
			return Response.status(404).entity(resultJson.toString()).build();
		}
	    
	    //create file path according to the schemaID
	    String currFilePath = schemaFilesPath + "/" + schemaID + ".json";
		
	    File jsonSchemaFile = new File(currFilePath);
	    if(jsonSchemaFile.exists()) { 
	    	//if there exists schema with the same schemaID, return error
	    	String errorMsg = "Exist schema with the same id";
	    	resultJson.put("status", "error");
	    	resultJson.put("message", errorMsg);
	    	return Response.status(409).entity(resultJson.toString()).build(); //change response to json
	    }
		
	    //write json schema to file
	    try {
	    	jsonSchemaFile.createNewFile(); // if file already exists will do nothing 
	    	PrintWriter out = new PrintWriter(currFilePath);
	    	out.println(newJsonSchema);
	    	out.close();
	    } catch(IOException e) {
			//TODO
	    }
	    
	    resultJson.put("status", "success");	
	    return Response.status(201).entity(resultJson.toString()).build();
	}
}
