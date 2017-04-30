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
	String schemaFilesPath = CommonMethods.getSchemaFilesPath();;
	
	@GET
	@Path("/{param}")
	public Response getJsonSchema(@PathParam("param") String schemaID) throws JSONException, IOException {
		
		JSONObject resultJson = new JSONObject();
		resultJson.put("action", "uploadSchema");
		resultJson.put("id", schemaID);
	    
		//create file path according to the schemaID
		String currFilePath = schemaFilesPath + "/" + schemaID + ".json";
	    
		if(!CommonMethods.checkFileExists(currFilePath)) {
			//if wanted json schema does not exist, return error
			String errorMsg = "Does not exist schema with this id";
			resultJson.put("status", "error");
			resultJson.put("message", errorMsg);
			return Response.status(404).entity(resultJson.toString()).build(); 
		}
	    		
		String jsonSchema = CommonMethods.fileToString(currFilePath);
		return Response.status(200).entity(jsonSchema).build(); 
	}
	
	@POST
	@Path("/{param}")
	public Response upload(@PathParam("param") String schemaID, String newJsonSchema) throws JSONException {
		JSONObject resultJson = new JSONObject();
	    resultJson.put("action", "uploadSchema");
	    resultJson.put("id", schemaID);
	    
	    if (!CommonMethods.isValidJson(newJsonSchema)) {
	    	String errorMsg = "Invalid JSON";
		    resultJson.put("status", "error");
		    resultJson.put("message", errorMsg);
			return Response.status(404).entity(resultJson.toString()).build();
	    }
	    
	    //create file path according to the schemaID
	    String currFilePath = schemaFilesPath + "/" + schemaID + ".json";
		
	    if(CommonMethods.checkFileExists(currFilePath)) { 
	    	//if there exists schema with the same schemaID, return error
	    	String errorMsg = "Exist schema with the same id";
	    	resultJson.put("status", "error");
	    	resultJson.put("message", errorMsg);
	    	return Response.status(409).entity(resultJson.toString()).build(); 
	    }
		
	    //write json schema to file
	    if (!CommonMethods.jsonToFile(currFilePath, newJsonSchema)) {
	    	resultJson.put("status", "error");
	    	resultJson.put("message", "occur problem while writing to file");	   
	    	return Response.status(404).entity(resultJson.toString()).build();
	    }
	    
	    resultJson.put("status", "success");	
	    return Response.status(201).entity(resultJson.toString()).build();
	}
}
