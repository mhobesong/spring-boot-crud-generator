package com.mosbesong.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ServiceGeneratorTest 
{
	@Test
	public void should_create_entity_service_file() throws Exception{
		BufferedWriter writer = new BufferedWriter(new FileWriter("file.json"));
		String entityName = "TestEntity" + (int)Math.ceil((1 + (Math.random() * 10)));

		writer.write("{\"entity\":\"" + entityName + "\",\"package\":\"x.y.z\"," + 
				"properties:[" +
				"{\"name\":\"firstname\",\"type\":\"text\",\"length\":255,\"validation\":\"required\"}," +
				"]" +
				"}");

		writer.close();
		String[] arguments = {"file.json"};
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		crudGenerator.start();
		File repositoryFile = new File("services/" + entityName + "Service.java");
		Path path = repositoryFile.toPath();

		String content = "";
		for(int i = 0; i < Files.readAllLines(path).size(); i++ ) content += Files.readAllLines(path).get(i) + "\n";

		Assert.assertTrue(repositoryFile.exists());
		Assert.assertTrue(content.contains("package x.y.z.services;"));
		Assert.assertTrue(content.contains("import x.y.z.entities." + entityName + ";"));
		Assert.assertTrue(content.contains("public class " + entityName + "Service {"));
		Assert.assertTrue(content.contains("private " + entityName + "Repository " + entityName.toLowerCase() + "Repository;"));
		Assert.assertTrue(content.contains("public List<" + entityName + "> findAll() {"));
		Assert.assertTrue(content.contains("return " + entityName.toLowerCase() + "Repository.findAll();"));
		Assert.assertTrue(content.contains("public " + entityName + " findOne(long id) {"));
		Assert.assertTrue(content.contains("return " + entityName.toLowerCase() + "Repository.findOne(id);"));
		Assert.assertTrue(content.contains("public " + entityName + " save(" + entityName + " entity) {"));
		Assert.assertTrue(content.contains("return " + entityName.toLowerCase() + "Repository.save(entity);"));
		Assert.assertTrue(content.contains("public void delete(" + entityName + " entity) {"));
		Assert.assertTrue(content.contains("" + entityName.toLowerCase() + "Repository.delete(entity);"));

		deleteDirectory(new File("services"));
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
