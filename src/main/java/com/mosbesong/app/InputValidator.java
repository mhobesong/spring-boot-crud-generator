package com.mosbesong.app;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class InputValidator
{
	private String[] args;
	private File file = null;
	private Path path = null;
	private JSONObject jo = null;

	public InputValidator(String[] args)
	{
		this.args = args;
	}

	/**
	 * Validates input supplied input file
	 */
	public String validate()
	{
		String error = "";

		error = isFilePathProvided();
		if (error != "") return error;

		path = Paths.get(args[0]);
		file = new File(args[0]);

		error = isDescriptorFileFound();
		if (error != "") return error;

		error = isValidJson();
		if (error != "") return error;

		error = entityKeyExists();
		if (error != "") return error;

		error = packageKeyExists();
		if (error != "") return error;

		return error;
	}

	/**
	 * Checks if a json file path is provided
	 *
	 * @return String Error message if file is not provided. Empty string otherwise.
	 */
	private String isFilePathProvided(){
		if (args.length == 0) {
			return "Please provide a json description file!\n";
		}

		return "";
	}

	/**
	 * Checks if provided json description file can be found
	 *
	 * @return String Error message if file is not provided. Empty string otherwise.
	 */
	private String isDescriptorFileFound()
	{
		if (!file.exists()) {
			return "Could not find entity descriptor file!\n";
		}

		return "";
	}

	/**
	 * Checks if provided json is of valid format
	 *
	 * @return String Error message if file is not provided. Empty string otherwise.
	 */
	private String isValidJson()
	{
		try {
			String read = Files.readAllLines(path).get(0);
			jo = new JSONObject(read);
        } catch (Exception ex) {
            return "Invalid json formated file!\n";
        }

		return "";
	}
	
	/**
	 * Checks if the entity key is defined in the json file
	 *
	 * @return String Error message if file is not provided. Empty string otherwise.
	 */
	private String entityKeyExists()
	{
		try {
			jo.get("entity");
        } catch (Exception ex) {
            return "Entity name key not defined!\n";
        }

		return "";
	}
	
	/**
	 * Checks if the entity key is defined in the json file
	 *
	 * @return String Error message if file is not provided. Empty string otherwise.
	 */
	private String packageKeyExists()
	{
		try {
			jo.get("package");
        } catch (Exception ex) {
            return "Entity package key not defined!\n";
        }

		return "";
	}
}
