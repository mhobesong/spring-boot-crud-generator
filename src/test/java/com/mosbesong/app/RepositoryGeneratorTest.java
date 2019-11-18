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
public class RepositoryGeneratorTest 
{
	@Test
	public void should_create_entity_repository_file() throws Exception{
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
		File repositoryFile = new File("repositories/" + entityName + "Repository.java");
		Path path = repositoryFile.toPath();

		String content = "";
		for(int i = 0; i < Files.readAllLines(path).size(); i++ ) content += Files.readAllLines(path).get(i) + "\n";

		Assert.assertTrue(repositoryFile.exists());
		Assert.assertTrue(content.contains("package x.y.z.repositories;"));
		Assert.assertTrue(content.contains("import java.util.List;"));
		Assert.assertTrue(content.contains("import org.springframework.data.repository.CrudRepository;"));
		Assert.assertTrue(content.contains("import x.y.z.entities." + entityName + ";"));
		Assert.assertTrue(content.contains("public interface " + entityName + "Repository extends CrudRepository<" + entityName + ", Long> {"));

		deleteDirectory(new File("repositories"));
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
