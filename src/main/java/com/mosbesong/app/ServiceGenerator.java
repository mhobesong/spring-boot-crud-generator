package com.mosbesong.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class ServiceGenerator
{
	private JSONObject json;
	private String serviceFileName;

	public ServiceGenerator (JSONObject json)
	{
		this.json = json;
		createServiceDirectory();
		createServiceFile();
		writeClassToFile();
	}

	/**
	 * Creates an entity directory if one does not already exist
	 *
	 * @return void
	 */
	private void createServiceDirectory()
	{
		String directoryPath = new File("").getAbsolutePath();
		File newDirectory = new File(directoryPath, "services");

		if(!newDirectory.exists()) newDirectory.mkdir();
	}

	/**
	 * Creates the entity file
	 *
	 * @return void
	 */
	private void createServiceFile()
	{
		String directoryPath = new File("").getAbsolutePath();
		serviceFileName = directoryPath + "/services/" + json.get("entity") + "Service.java";
		Path newFilePath = Paths.get(serviceFileName);

		try{
			Files.createFile(newFilePath);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	private void writeClassToFile(){
		try{
			String str = getTemplateContent();
			BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFileName));
			writer.write(str);
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	private String getTemplateContent()
	{
		return "package " + json.get("package").toString() + ".services;\n\n" +

		"import org.springframework.beans.factory.annotation.Autowired;\n" +
		"import org.springframework.data.jpa.repository.JpaRepository;\n" +
		"import org.springframework.stereotype.Service;\n" +
		"import " + json.get("package").toString() + ".entities." + json.get("entity").toString() + ";\n" +
		"import com.example.repository.UserRepository;\n" +
		"import " + json.get("package").toString() + ".repositories." + json.get("entity").toString() + "Repository;\n" +
		"import org.springframework.transaction.annotation.Transactional;\n" +
		"import java.util.List;\n\n" +

		"@Service\n" +
		"@Transactional(readOnly = true)\n" +
		"public class " + json.get("entity").toString() + "Service {\n\n" +

			"\t@Autowired\n" +
			"\tprivate " + json.get("entity").toString() + "Repository " + json.get("entity").toString().toLowerCase() + "Repository;\n\n" +

			"\tpublic List<" + json.get("entity").toString() + "> findAll() {\n" +
				"\t\treturn " + json.get("entity").toString().toLowerCase() + "Repository.findAll();\n" +
			"\t}\n\n" +

			"\tpublic " + json.get("entity").toString() + " findOne(Integer id) {\n" +
				"\t\treturn " + json.get("entity").toString().toLowerCase() + "Repository.findOne(id);\n" +
			"\t}\n\n" +

			"\t@Transactional(readOnly = false)\n" +
			"\tpublic " + json.get("entity").toString() + " save(" + json.get("entity").toString() + " entity) {\n" +
				"\t\treturn " + json.get("entity").toString().toLowerCase() + "Repository.save(entity);\n" +
			"\t}\n\n" +

			"\t@Transactional(readOnly = false)\n" +
			"\tpublic void delete(" + json.get("entity").toString() + " entity) {\n" +
				"\t\t" + json.get("entity").toString().toLowerCase() + "Repository.delete(entity);\n" +
			"\t}\n" +
		"}\n";
	}
}
