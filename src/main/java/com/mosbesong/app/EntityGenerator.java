package com.mosbesong.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class EntityGenerator
{
	private JSONObject json;
	private String entityFileName;

	public EntityGenerator (JSONObject json)
	{
		this.json = json;
		createEntitDirectory();
		createEntityFile();
		writeClassToFile();
	}

	/**
	 * Creates an entity directory if one does not already exist
	 *
	 * @return void
	 */
	private void createEntitDirectory()
	{
		String directoryPath = new File("").getAbsolutePath();
		File newDirectory = new File(directoryPath, "entity");

		if(!newDirectory.exists()) newDirectory.mkdir();
	}

	/**
	 * Creates the entity file
	 *
	 * @return void
	 */
	private void createEntityFile()
	{
		String directoryPath = new File("").getAbsolutePath();
		Path newFilePath = Paths.get(directoryPath + "/entity/" + json.get("entity") + ".java");

		try{
			Files.createFile(newFilePath);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		entityFileName = directoryPath + "/entity/" + json.get("entity") + ".java";
	}

	private void writeClassToFile(){
		try{
			String str = getTemplateContent().
				replace("{{entity-name}}", json.get("entity").toString()).
				replace("{{package-name}}", json.get("package").toString());
			BufferedWriter writer = new BufferedWriter(new FileWriter(entityFileName));
			writer.write(str);
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	private String getTemplateContent()
	{
		return "package {{package-name}};\n" +
		"\n" +
		"import java.text.SimpleDateFormat;\n" +
		"import java.util.Date;\n" +
		"\n" +
		"import javax.persistence.Entity;\n" +
		"import javax.persistence.GeneratedValue;\n" +
		"import javax.persistence.GenerationType;\n" +
		"import javax.persistence.Id;\n" +
		"\n" +
		"import lombok.Data;\n" +
		"import lombok.NoArgsConstructor;\n" +
		"\n" +
		"@Entity\n" +
		"@NoArgsConstructor\n" +
		"@Data\n" +
		"public class {{entity-name}} {\n" +
		"\n" +
		"\t@Id\n" +
		"\t@GeneratedValue(strategy=GenerationType.SEQUENCE)\n" +
		"\tprivate long id;\n" +
		"}\n";
	}
}
