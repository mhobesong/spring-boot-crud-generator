package com.mosbesong.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class CrudGenerator
{
	private String[] args;
	private String entity = "";

	public CrudGenerator(String[] args)
	{
		this.args = args;
	}

	/**
	 * Start generator
	 */
	public String start()
	{
		InputValidator inputValidator = new InputValidator(args);
		String error = inputValidator.validate();

		if (error != "") return error;

		try {
			Path path = Paths.get(args[0]);
			String read = Files.readAllLines(path).get(0);
			JSONObject jsonObject = new JSONObject(read);
			EntityGenerator entityGenerator = new EntityGenerator(jsonObject);
			RepositoryGenerator repositoryGenerator = new RepositoryGenerator(jsonObject);
			ServiceGenerator serviceGenerator = new ServiceGenerator(jsonObject);
		} catch (Exception e){
			return e.getMessage();
		}

		return "";
	}
}
