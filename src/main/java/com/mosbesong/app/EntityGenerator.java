package com.mosbesong.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.json.JSONArray;
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
		File newDirectory = new File(directoryPath, "entities");

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
		Path newFilePath = Paths.get(directoryPath + "/entities/" + json.get("entity") + ".java");

		try{
			Files.createFile(newFilePath);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		entityFileName = directoryPath + "/entities/" + json.get("entity") + ".java";
	}

	private void writeClassToFile(){
		try{
			String str = getTemplateContent().
				replace("{{entity-name}}", json.get("entity").toString()).
				replace("{{package-name}}", json.get("package").toString() + ".entities");
			str = addClassProperties(str);
			BufferedWriter writer = new BufferedWriter(new FileWriter(entityFileName));
			writer.write(str);
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private String addClassProperties(String content)
	{
		if (!json.has("properties")) return content;

		//"{\"name\":\"firstname\",\"type\":\"text\",\"length\":255,\"validation\":\"required\"}," +
		//"\t@Column(name=\"firstname\", columnDefinition=\"VARCHAR(255) NOT NULL\")\n\tprivate String firstname;"
		
		JSONArray properties = json.getJSONArray("properties"); 
		String classProperties = "";

		for (int i=0; i < properties.length(); i++)
		{
			JSONObject property = (JSONObject)properties.get(i);
			classProperties += "\t@Column(";

			if (property.has("name")) classProperties += "name=\"" + (String)property.get("name") + "\"";

			classProperties += ", columnDefinition=\"";

			//Set column type
			if (property.has("type")) {
				String type = (String)property.get("type");

				if (type.equals("text")){
					classProperties += "VARCHAR(" + (property.has("length") ? 
						(int)property.get("length") : 256) + ") ";
				}

				if (type.equals("integer")){
					classProperties += "INTEGER ";
				}

				if (type.equals("date")){
					classProperties += "DATE ";
				}
			};

			//Set column data constraint
			if (property.has("validation")) {
				String validation = (String)property.get("validation");
				String[] validations = validation.split("\\|");

				for (int j=0; j<validations.length; j++)
				{
					if (validations[j].equals("required")) classProperties += "NOT NULL ";
					
					if (validations[j].equals("unique")) classProperties += "UNIQUE ";
				}

			};

			//Set default value
			if (property.has("default")) {
				if (property.get("type").equals("integer"))
					classProperties += "DEFAULT " + property.get("default") + " ";
				else
					classProperties += "DEFAULT '" + property.get("default") + "' ";
			};

			classProperties = classProperties.substring(0, classProperties.length() -1);

			classProperties += "\")\n\tprivate String "+ (String)property.get("name")+";\n\n";
		}

		content = content.replace("{{entity-properties}}", classProperties);
		return content;
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
		"\n" +
		"{{entity-properties}}" +
		"}\n";
	}
}
