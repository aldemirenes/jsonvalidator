package com.jsonValidator;
 
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;


@Path("/validate")
public class ValidateSchemaClass {
	
	//directory path of the json schema files
	//should be changed accordingly
	String schemaFilesPath = CommonMethods.getSchemaFilesPath();
 
	@POST
	@Path("/{param}")
	public Response validateJson(@PathParam("param") String schemaID, String newJson) throws JsonProcessingException, IOException, JSONException, ProcessingException {
		
		JSONObject resultJson = new JSONObject();
	    resultJson.put("action", "validateDocument");
	    resultJson.put("id", schemaID);
		
	    //check if new json is in a valid json format
	    //if not converting the string to json will raise an exception
	    if (!CommonMethods.isValidJson(newJson)) {
	    	String errorMsg = "Invalid JSON";
		    resultJson.put("status", "error");
		    resultJson.put("message", errorMsg);
			return Response.status(404).entity(resultJson.toString()).build();
	    }
	    
	    //create file path according to the schemaID
	    String currFilePath = schemaFilesPath + "/" + schemaID + ".json";
	    
	    if(!CommonMethods.checkFileExists(currFilePath)) { 
	    	//if wanted json schema does not exist, return error
	    	String errorMsg = "Does not exist schema with this id";
	    	resultJson.put("status", "error");
	    	resultJson.put("message", errorMsg);
	    	return Response.status(404).entity(resultJson.toString()).build(); 
	    }
	   
	    //convert content of the json file to string    
	    String jsonSchema = CommonMethods.fileToString(currFilePath);
	    ProcessingReport report = CommonMethods.checkJsonAgainstSchema(jsonSchema, newJson);

	    
	    if (report.isSuccess()) {
	    	resultJson.put("status", "success");
	    } else {
	    	String errorMsgs = "";
			
	    	for (ProcessingMessage pm : report ) {
	    		errorMsgs += pm.getMessage() + "\n";
	    	}
	    	resultJson.put("status", "error");
	    	resultJson.put("message", errorMsgs);
	    }

	    return Response.status(200).entity(resultJson.toString()).build();
	
	}
}