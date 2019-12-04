package penso.stackhat.newtech;

import java.io.IOException;

public class NewTechLauncher{
	/**
	 * input format:String [] args= {String category, String penso_Name, String BW_Name}
	 * path: absolute path of database.xlsx
	 * */
	public static void main(String[] args) throws IOException{
		
		Program program = new Program();

		String[] args2 = { "Mapping", "Mapbox", "Mapbox" };
		String path = "C:\\Users\\myxll\\Desktop\\test\\database.xlsx";
		program.start(args2, path);
		
	}
}
