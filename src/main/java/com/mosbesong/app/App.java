package com.mosbesong.app;

/**
 * Hello world!
 *
 */
public class App 
{
	/**
	 * Path to etity description json file
	 */
	private static String entityDescriptionJsonPath;

    public static void main( String[] args )
    {
		if (args.length == 0) noPathToJsonError();
		else{
			entityDescriptionJsonPath = args[0];
			System.out.println(args[0]);
		}
    }

	private static void noPathToJsonError()
	{
		System.out.println("Please provided an entity description file!");
	}
}
