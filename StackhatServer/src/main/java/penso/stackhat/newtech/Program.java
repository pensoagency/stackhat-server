package penso.stackhat.newtech;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Program {
	public static String pathDatabase = "/home/bitnami/stackhat-server-files/database.xlsx";
	
	public void start(String[] args, String pathDatabase) throws IOException {
		// open stream connect to database
		File myDatabase = new File(pathDatabase);
		FileInputStream inData = new FileInputStream(myDatabase);
		XSSFWorkbook wbData = new XSSFWorkbook(inData);
		XSSFSheet sheetDatabase = wbData.getSheet("Database");
		XSSFSheet sheetSummary = wbData.getSheet("All Webs");
		Tech Tech = new Tech();

//		String [] Categories= {"SSL certificates","Advertising","Email Hosting Providers"};
//		String [] PENSO_Names= {"GlobalSign","AppNexus","Autopilot SPF"};
//		String [] bwNames= {"GlobalSign","AppNexus","Autopilot SPF"};
//		
//		for(int i=0;i<3;i++) {
//			if(Tech.checkBWName(bwNames[i], sheet)) {
//				System.out.println(bwNames[i]+" already exists in database");
//				continue;
//			}
//			Tech.addNewTech(sheet, Categories[i], PENSO_Names[i], bwNames[i]);
//			System.out.println("Cagegory:"+Categories[i]+", PENSO NAME:"+PENSO_Names[i]+", BW Name:"+bwNames[i]+" has been added");
//		}
		if (Tech.checkBWName(args[2], sheetDatabase)) {
			System.out.println(args[2] + " already exists in database");
		} else if (!Tech.checkBWName(args[2], sheetDatabase)) {
			Tech.addNewTech(wbData,sheetDatabase,sheetSummary, args[0], args[1], args[2]);
			System.out.println(
					"Cagegory:" + args[0] + ", PENSO NAME:" + args[1] + ", BW Name:" + args[2] + " has been added");
		}
		FileOutputStream out = new FileOutputStream(myDatabase);
		wbData.write(out);
		wbData.close();
		out.close();
	}
}
