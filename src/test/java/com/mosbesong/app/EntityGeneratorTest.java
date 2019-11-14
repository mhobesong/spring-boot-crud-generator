package com.mosbesong.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class EntityGeneratorTest 
{
	@Test
	public void should_create_entity_file_from_entity_key() throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("file.json"));
		String entityName = "TestEntity" + (int)Math.ceil((1 + (Math.random() * 10)));
		writer.write("{\"entity\":\"" + entityName + "\",\"package\":\"x.y.z\"}");
		writer.close();
		String[] arguments = {"file.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		crudGenerator.start();

		File entityFile = new File("entity/" + entityName + ".java");
		Assert.assertTrue(entityFile.exists());
		deleteFile("file.json");
		deleteFile("entity/" + entityName + ".java");
		deleteFile("entity");
	}

	@Test
	public void should_write_entity_class_to_entity_file() 
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("file.json"));
		String entityName = "TestEntity" + (int)Math.ceil((1 + (Math.random() * 10)));
		writer.write("{\"entity\":\"" + entityName + "\",\"package\":\"x.y.z\"}");
		writer.close();
		String[] arguments = {"file.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		crudGenerator.start();

		File entityFile = new File("entity/" + entityName + ".java");
		Path path = entityFile.toPath();
		Assert.assertTrue(Files.readAllLines(path).get(16).
				contains("public class " + entityName + " {"));
		Assert.assertTrue(Files.readAllLines(path).get(0).
				contains("package x.y.z;"));
		
		deleteFile("file.json");
		deleteFile("entity/" + entityName + ".java");
		deleteFile("entity");
	}

	private void deleteFile(String filePath)
	{
		File file = new File(filePath);
		try{
			file.delete();
		} catch(Exception e){}
	}
}
