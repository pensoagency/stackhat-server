package penso.stackhat.builtwith;

import java.io.IOException;

public class BuiltWithLauncher {

	/**
	 * input:   a list of URLs path: absolute path of template.xlsx pathDatabase:
	 * absolute path of database.xlsx
	 */
	public static void main(String[] args2) throws IOException, Exception {

		String [] args= {
				"conservatives.com"
//				"https://aws.amazon.com/cn/",
//				"https://github.com/",
//				"unimelb.edu.au",
//				"www.coles.com.au",
//				"pensoagency.com"
		};
		
		String path = "C:\\Users\\myxll\\Desktop\\test\\tmp.xlsx";
		String pathDatabase = "C:\\Users\\myxll\\Desktop\\test\\database.xlsx";

		String APIKey = "c8fb6490-26a8-46bd-88a9-ac26d7994656"; // API

		Program program = new Program();
		program.start(args, path, pathDatabase, APIKey);
	}

}