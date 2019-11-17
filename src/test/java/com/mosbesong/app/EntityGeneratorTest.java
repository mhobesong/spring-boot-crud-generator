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

		File entityFile = new File("entities/" + entityName + ".java");
		Assert.assertTrue(entityFile.exists());
		deleteDirectory(new File("entities"));
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

		File entityFile = new File("entities/" + entityName + ".java");
		Path path = entityFile.toPath();
		Assert.assertTrue(Files.readAllLines(path).get(16).
				contains("public class " + entityName + " {"));
		Assert.assertTrue(Files.readAllLines(path).get(0).
				contains("package x.y.z.entities;"));
		
		deleteDirectory(new File("entities"));
	}

	@Test
	public void should_write_entiy_properties() 
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("file.json"));
		String entityName = "TestEntity" + (int)Math.ceil((1 + (Math.random() * 10)));

		writer.write("{\"entity\":\"" + entityName + "\",\"package\":\"x.y.z\"," + 
				"properties:[" +
				"{\"name\":\"firstname\",\"type\":\"text\",\"length\":255,\"validation\":\"required\"}," +
				"{\"name\":\"lastname\",\"type\":\"text\",\"length\":256}," +
				"{\"name\":\"status\",\"type\":\"text\",\"length\":256,\"validation\":\"required|in[acitve,pending]\",\"default\":\"active\"}," +
				"{\"name\":\"age\",\"type\":\"integer\",\"validation\":\"required\",\"default\":25}," +
				"{\"name\":\"email\",\"type\":\"text\",\"validation\":\"required|email|unique\"}," +
				"{\"name\":\"dob\",\"type\":\"date\",\"validation\":\"required|date\"}" +
				"]" +
				"}");

		writer.close();
		String[] arguments = {"file.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		crudGenerator.start();
		File entityFile = new File("entities/" + entityName + ".java");
		Path path = entityFile.toPath();

		String content = "";
		for(int i = 0; i < Files.readAllLines(path).size(); i++ ) content += Files.readAllLines(path).get(i) + "\n";

		Assert.assertTrue(content.contains("@Column(name=\"firstname\", columnDefinition=\"VARCHAR(255) NOT NULL\")\n\tprivate String firstname;"));

		Assert.assertTrue(content.contains("\t@Column(name=\"lastname\", columnDefinition=\"VARCHAR(256)\")\n\tprivate String lastname;"));
		
		Assert.assertTrue(content.contains("\t@Column(name=\"status\", columnDefinition=\"VARCHAR(256) NOT NULL DEFAULT 'active'\")\n\tprivate String status;"));

		Assert.assertTrue(content.contains("\t@Column(name=\"age\", columnDefinition=\"INTEGER NOT NULL DEFAULT 25\")\n\tprivate String age;"));

		Assert.assertTrue(content.contains("\t@Column(name=\"email\", columnDefinition=\"VARCHAR(256) NOT NULL UNIQUE\")\n\tprivate String email;"));

		Assert.assertTrue(content.contains("\t@Column(name=\"dob\", columnDefinition=\"DATE NOT NULL\")\n\tprivate String dob;"));

		deleteDirectory(new File("entities"));
		(new File("file.json")).delete();
	}

	private boolean deleteDirectory(File directory)
	{
		File[] allContents = directory.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directory.delete();
	}
}
