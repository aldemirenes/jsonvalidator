package com.jsonValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class CommonMethods {
	public static File getFile(String filePath) {
		File jsonSchemaFile = new File(filePath);
		if (jsonSchemaFile.exists() && !jsonSchemaFile.isDirectory()) {
			return jsonSchemaFile;
		}
		
		return null;
	}
	
	public static boolean checkFileExists(String filePath) {
		File jsonSchemaFile = new File(filePath);
		if (jsonSchemaFile.exists() && !jsonSchemaFile.isDirectory()) {
			return true;
		}
		
		return false;
	}
	
	public static String fileToString(String filePath) {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(filePath));
			String jsonSchema = new String(encoded, StandardCharsets.UTF_8);
			return jsonSchema;
		} catch (IOException e) {
			return null;
		}
	} 
	
	public static boolean isValidJson(String jsonString) {
		//check if json is in a valid json format
	    //if not converting the string to json will raise an exception
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
	
	
	
	public static boolean jsonToFile(String filePath, String newJsonSchema) {
		try {
			File writingFile = new File(filePath);
	    	writingFile.createNewFile(); // if file already exists will do nothing 
	    	PrintWriter out = new PrintWriter(filePath);
	    	out.println(newJsonSchema);
	    	out.close();
	    	return true; //if everything is okey, return true
	    } catch(IOException e) {
			return false;// else false
	    }
	}
	
	public static ProcessingReport checkJsonAgainstSchema(String jsonSchema, String jsonString) throws JsonProcessingException, IOException, ProcessingException {
		//convert json which is in string format to JsonNode
		ObjectMapper mapper = new ObjectMapper();
		JsonNode schemaNode = mapper.readTree(jsonSchema);
		JsonNode data = mapper.readTree(jsonString);
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		// load the schema and validate
		JsonSchema schema = factory.getJsonSchema(schemaNode);
		ProcessingReport report = schema.validate(data);
		return report;
	} 
	
	public static String getSchemaFilesPath() {
		//read config file and return schemaFilesPath
		String schemaFilesPath = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("/opt/json-validator/config.properties");
			prop.load(input);
			schemaFilesPath = prop.getProperty("schemaFilesPath");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}	
		
		return schemaFilesPath;
	}

}
