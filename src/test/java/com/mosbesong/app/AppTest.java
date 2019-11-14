package com.mosbesong.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	@Test
	public void should_ask_user_to_provide_json_description_if_none_is_provided() throws Exception
	{
		String[] arguments = new String[0];
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		Assert.assertEquals("Please provide a json description file!\n", crudGenerator.start());
	}

	@Test
	public void should_notify_user_if_entity_description_file_could_not_be_found() throws Exception
	{
		String[] arguments = {"/path/that/does/not/exist/to/entity.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		Assert.assertEquals("Could not find entity descriptor file!\n", crudGenerator.start());
	}

	@Test
	public void should_notify_user_of_invalid_json_descriptor_file() throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("file.json"));
		writer.write("{name}");
		writer.close();
		String[] arguments = {"file.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		Assert.assertEquals("Invalid json formated file!\n", crudGenerator.start());
		deleteFile("file.json");
	}
	@Test
	public void should_emit_entity_name_not_defined_error_if_entity_name_key_not_defined_in_descriptor_json_file() throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("file.json"));
		writer.write("{\"test\":\"test\"}");
		writer.close();
		String[] arguments = {"file.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		Assert.assertEquals("Entity name key not defined!\n", crudGenerator.start());
		deleteFile("file.json");
	}

	@Test
	public void should_emit_package_name_not_defined_error_if_entity_name_key_not_defined_in_descriptor_json_file() throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("file.json"));
		writer.write("{\"entity\":\"Test\"}");
		writer.close();
		String[] arguments = {"file.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		Assert.assertEquals("Entity package key not defined!\n", crudGenerator.start());
		deleteFile("file.json");
	}


	private void deleteFile(String filePath)
	{
		File file = new File(filePath);
		try{
			file.delete();
		} catch(Exception e){}
	}
}
