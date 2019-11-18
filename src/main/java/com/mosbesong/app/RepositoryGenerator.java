package com.mosbesong.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class RepositoryGenerator
{
	private JSONObject json;
	private String repositoryFileName;

	public RepositoryGenerator (JSONObject json)
	{
		this.json = json;
		createRepositoryDirectory();
		createRepositoryFile();
		writeClassToFile();
	}

	/**
	 * Creates an entity directory if one does not already exist
	 *
	 * @return void
	 */
	private void createRepositoryDirectory()
	{
		String directoryPath = new File("").getAbsolutePath();
		File newDirectory = new File(directoryPath, "repositories");

		if(!newDirectory.exists()) newDirectory.mkdir();
	}

	/**
	 * Creates the entity file
	 *
	 * @return void
	 */
	private void createRepositoryFile()
	{
		String directoryPath = new File("").getAbsolutePath();
		repositoryFileName = directoryPath + "/repositories/" + json.get("entity") + "Repository.java";
		Path newFilePath = Paths.get(repositoryFileName);

		try{
			Files.createFile(newFilePath);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	private void writeClassToFile(){
		try{
			String str = getTemplateContent();
			BufferedWriter writer = new BufferedWriter(new FileWriter(repositoryFileName));
			writer.write(str);
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	private String getTemplateContent()
	{
		return "package " + json.get("package").toString() + ".repositories;\n\n" +
		"import java.util.List;\n" +
		"import org.springframework.data.repository.CrudRepository;\n" +
		"import " + json.get("package").toString() + ".entities." + json.get("entity").toString() +";\n\n" +
		"public interface " + json.get("entity").toString() + "Repository extends CrudRepository<" + json.get("entity").toString() + ", Long> {\n" +
		"}\n";
	}
}
