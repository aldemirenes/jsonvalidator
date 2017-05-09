package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.jsonValidator.CommonMethods;

public class CommonMethodsTest {

	CommonMethods tester = new CommonMethods();
	String testDirectoryPath = "./src/test/TestFolder";

	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@BeforeClass
	public static void init() {
		
	}
	
	
	@Test
	public void testGetFileWithExistingFile() {
		String existingFilePath = testDirectoryPath + "/existingFile";
		File existingFile = new File(existingFilePath);
		File methodRes = tester.getFile(existingFilePath);
		//assertEquals(existingFile.getAbsolutePath(), methodRes.getAbsolutePath());
		assertEquals(existingFile, methodRes);
	}
	
	
	@Test
	public void testGetFileWithNotExistingFile() {
		String notExistingFilePath = testDirectoryPath + "/notExistingFile";
		File methodRes = tester.getFile(notExistingFilePath);
		assertNull(methodRes);
	}
	

	@Test
	public void testCheckFileExistsWithExistingFile() {
		String existingFilePath = testDirectoryPath + "/existingFile";
		assertTrue(tester.checkFileExists(existingFilePath));
	}
	
	@Test
	public void testCheckFileExistsWithNotExistingFile() {
		String notExistingFilePath = testDirectoryPath + "/notExistingFile";
		assertFalse(tester.checkFileExists(notExistingFilePath));
	}

	@Test
	public void testFileToStringWithExistingFile() {
		String existingFilePath = testDirectoryPath + "/fileToString";
		String fileToStringExpected = "examplestring\n";
		String actualString = tester.fileToString(existingFilePath);		
		assertEquals(fileToStringExpected, actualString);
	}
	
	@Test
	public void testFileToStringWithNotExistingFile() {
		String notExistingFilePath = testDirectoryPath + "/notExistingFile";
		String methodRes = tester.fileToString(notExistingFilePath);		
		assertNull(methodRes);
	}

	@Test
	public void testIsValidJsonWithValidJsonFile() {
		String validJsonFile = testDirectoryPath + "/validFormat.json";
		String validJsonString = tester.fileToString(validJsonFile);
		assertTrue(tester.isValidJson(validJsonString));
	}

	@Test
	public void testIsValidJsonWithInvalidJsonFile() {
		String invalidJsonFile = testDirectoryPath + "/invalidFormat.json";
		String invalidJsonString = tester.fileToString(invalidJsonFile);
		assertFalse(tester.isValidJson(invalidJsonFile));
	}
	
	@Test
	public void testJsonToFile() {
		String jsonFilePath = testDirectoryPath + "/example.json";
		String jsonString = tester.fileToString(jsonFilePath);
		
		String newJsonFilePath = testDirectoryPath + "/newExample.json";
		
		assertTrue(tester.jsonToFile(newJsonFilePath, jsonString));
		assertTrue(tester.checkFileExists(newJsonFilePath));
		
		File newJsonFile = new File(newJsonFilePath);
		newJsonFile.delete();
	}

	@Test
	public void testCheckJsonAgainstSchemaWithValidJson() throws JsonProcessingException, IOException, ProcessingException {
		String validJsonPath = testDirectoryPath + "/good.json";
		String jsonSchemaPath = testDirectoryPath + "/jsonSchema.json";
		
		String validJsonString = tester.fileToString(validJsonPath);
		String jsonSchemaString = tester.fileToString(jsonSchemaPath);
		
		ProcessingReport report = tester.checkJsonAgainstSchema(jsonSchemaString, validJsonString);
		assertTrue(report.isSuccess());
	}
	
	@Test
	public void testCheckJsonAgainstSchemaWithiInvalidJson() throws JsonProcessingException, IOException, ProcessingException {
		String invalidJsonPath = testDirectoryPath + "/bad.json";
		String jsonSchemaPath = testDirectoryPath + "/jsonSchema.json";
		
		String invalidJsonString = tester.fileToString(invalidJsonPath);
		String jsonSchemaString = tester.fileToString(jsonSchemaPath);
	
		ProcessingReport report = tester.checkJsonAgainstSchema(jsonSchemaString, invalidJsonString);
		assertFalse(report.isSuccess());
	}


}
