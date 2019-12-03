package penso.stackhat.builtwith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Program {
	public static String pathDatabase = "/home/bitnami/stackhat-server-files/database.xlsx";
	public static String APIKey = "c8fb6490-26a8-46bd-88a9-ac26d7994656";

	public void start(String[] args, String path, String pathDatabase, String APIKey)
			throws IOException, InvalidFormatException {
		long start = System.currentTimeMillis();

		String databaseSheet = "DATABASE";
		String newTechSheet = "NewTech Each Web";
		String techBWName = "Tech BW Name";
		String summary = "All WEBs";

		// fixed parameters
		int totalNumDomain = args.length;
		int rowNum = 1; // coordinate index
		int colNum = 2; // coordinate index
		int current = 1; // current index
		
		Database db = new Database();
		ArrayList<String> categories;
		categories=db.getAllCategories(pathDatabase, summary);
		// update database
		Excel ex = new Excel();
		System.out.println("updating database... (step 1/5)");
		ex.updateTemplate(pathDatabase);// update "ALL WEBs" in database.xlsx
		ex.copyDatabase(pathDatabase, path);
		ex.copyTemplate(pathDatabase, path);// copy "ALL WEBs" from "database.xlsx" to "tmp.xlsx"
		ex.copyTemplateStyle(path, summary,categories);
		System.out.println("updating complete (step 1/5)");

		// check URLs,
		// correct URLs: get all technologies used and write them into excel sheet.
		// wrong URLs: write the URLs into excel sheet under 'wrong urls' column
		ArrayList<String> wrongURLs = new ArrayList<String>();
		wrongURLs.add("Wrong URLs");
		HashMap<String, Integer> map = new HashMap<String, Integer>(); // use map to count technologies
		XSSFWorkbook wb = ex.openExcelFile(path); // open streams
		//insert domains into the excel
		ex.insertDomain(args, path, wb, summary, newTechSheet, techBWName, databaseSheet);
		JSon JSon = new JSon();
		int tmp = rowNum;
		for (int i = 0; i < totalNumDomain; i++) {
			String domain = ex.ReadExcelFile(rowNum, colNum, wb, techBWName);
			System.out.println("analysing domain " + current++ + ": " + domain + "(step 2/5)");
			String json = JSon.getJson(domain, APIKey); // get JSon of a domain
			if (JSon.checkWrongURL(json) != null) {
				wrongURLs.add(JSon.checkWrongURL(json));
				colNum++;
				continue;
			}
			ArrayList<String> list = JSon.getInterestedTechNames(json); // the arraylist contains all tech with specific
																		// tags
			ex.writeTechName(list, rowNum, colNum, wb, techBWName); // write the tech to spreadsheet "Tech BW Name"
			JSon.countTech(list, map); // count technologies
			rowNum = tmp;
			colNum++;
		}
		ArrayList<String> allNewTech = new ArrayList<String>();
		allNewTech = db.ifExistInDatabase(map, wb);

		ex.writeTechCount(map, rowNum, colNum, wb, techBWName);
		colNum++;
		colNum++;

		int colTech = 2;// fixed parameter, tech start from column D in "Database"
		int colData = 6;// fixed parameter, data start from column G in "Database"
		int b = 1;
		ArrayList<String> newTech = new ArrayList<String>();
		ArrayList<String> PensoNames = new ArrayList<String>();
		JSon.setWrongURLs(wrongURLs);
		System.out.println("matching database (step 3/5)");
		for (int i = 0; i < totalNumDomain; i++) {
			newTech = db.matchDatabase(wb, colTech, colData);
			// write new tech for each web in a separate sheet "NewTech Each Web"
			ex.writeNewTech(newTech, 1, b, wb, newTechSheet);
			PensoNames = db.getPENSONames(wb, databaseSheet, colData);
			db.matchPENSOName(PensoNames, b, wb, summary);
			colTech++;
			colData++;
			b++;
		}

		System.out.println("writing into template... (step 4/5)");
		ex.writeNewTech(allNewTech, rowNum - 1, colNum, wb, techBWName);

		ex.writeWrongURLs(wrongURLs, --rowNum, ++colNum, wb, techBWName);

		ex.insertFormula(totalNumDomain, 5, summary, wb,categories); // insert formula to calculate SUM and AVERAGE

		System.out.println("deleting irrelevant rows... (step 5/5)");
		ex.deleteRow(wb, summary); // delete irrelevant rows which has count = 0;

		ex.closeAndWrite(wb, path);
		System.out.println("success");
		long end = System.currentTimeMillis();
		System.out.println("time taken: " + (end - start));
	}
}
