package penso.stackhat.builtwith;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Program {
	public static String pathDatabase = "/home/bitnami/stackhat-server-files/database.xlsx";
	public static String pathTemplate = "/home/bitnami/stackhat-server-files/tmp.xlsx";
	public static String pathAuditsBase = "/home/bitnami/stackhat-server-files/audits/";
	public static String APIKey = "c8fb6490-26a8-46bd-88a9-ac26d7994656";

	public void start(String[] urls, ArrayList<String> log, String path, String pathDatabase, String APIKey)
			throws IOException, InvalidFormatException {

		log.add("Start audit process.");

		long start = System.currentTimeMillis();

		String sheetDatabase = "Database";
		String sheetNewTech = "NewTech Each Web";
		String sheetTechBWName = "Tech BW Name";
		String sheetCurrentTechSummary = "All Webs (Current)";
		String sheetAllTech = "All Webs (Current & Historical)";

		// fixed parameters
		int totalNumDomain = urls.length;
		int rowNum = 1; // coordinate index
		int colNum = 2; // coordinate index
		int current = 1; // current index

		if (totalNumDomain > 100) {
			log.add("Over maximum URL count. Halted.");
			throw new ArithmeticException("Max 100 URLs, please try again");
		}

		log.add("Start categories.");

		Database db = new Database();
		ArrayList<String> categories;

		try {
			categories = db.getAllCategories(pathDatabase, sheetCurrentTechSummary);
			log.add(String.format("Get all categories, total %1$s.", categories.size()));
		} catch (Exception ex) {
			log.add(String.format("Get all categories failed with exception %1$s.", ex.getMessage()));
			throw ex;
		}

		// update database
		Excel ex = new Excel();
		
		System.out.println("updating database... (step 1/4)");

		log.add("Updating database... (step 1/4)");

		XSSFWorkbook database = ex.openExcelFile(pathDatabase); // get 'database.xlsx'
		XSSFWorkbook template = ex.openExcelFile(pathTemplate); // get 'template.xlsx'
		FileOutputStream outDatabase = new FileOutputStream(new File(pathDatabase)); // output stream
		BufferedOutputStream bufferedOutTemplate = new BufferedOutputStream(
				new FileOutputStream(new File(pathTemplate)), 8 * 1024); // buffered output stream, buffer size 8*1024
																			// bytes

		ex.updateDatabase(database, sheetDatabase, sheetCurrentTechSummary, bufferedOutTemplate);// update "ALL WEBs" in
																									// database.xlsx
		ex.copyDatabase(database, template, sheetDatabase, bufferedOutTemplate);
		ex.copyTemplate(database, template, sheetCurrentTechSummary, sheetCurrentTechSummary, bufferedOutTemplate);// copy
																													// "ALL
																													// WEBs"
																													// from
																													// "database.xlsx"
																													// to
																													// "tmp.xlsx"
		ex.copyTemplate(database, template, sheetAllTech, sheetCurrentTechSummary, bufferedOutTemplate);// update "All
																										// Tech (Current
																										// &
																										// Historical)"
		ex.copyTemplateStyle(template, sheetCurrentTechSummary, categories, bufferedOutTemplate);
		ex.copyTemplateStyle(template, sheetAllTech, categories, bufferedOutTemplate);
		bufferedOutTemplate.flush();
		database.write(outDatabase);
		database.close();
		outDatabase.close();

		// template.write(bufferedOutTemplate);
		// template.close();
		// bufferedOutTemplate.close();

		System.out.println("updating complete (step 1/4)");

		log.add("Updating database complete (step 1/4)");

		// check URLs,
		// correct URLs: get all technologies used and write them into excel sheet.
		// wrong URLs: write the URLs into excel sheet under 'wrong urls' column
		ArrayList<String> wrongURLs = new ArrayList<String>();
		wrongURLs.add("Wrong URLs");
		HashMap<String, Integer> map = new HashMap<String, Integer>(); // use map to count technologies
		// XSSFWorkbook template = ex.openExcelFile(pathTemplate); // open streams
		XSSFSheet xssfsheetAllTech = template.getSheet(sheetAllTech);

		// insert domains into the tmp

		// FileOutputStream bufferedOutTemplate = new
		// FileOutputStream(pathTemplate);//output stream
		ex.insertDomain(urls, pathTemplate, template, sheetCurrentTechSummary, sheetNewTech, sheetTechBWName,
				sheetDatabase);
		ex.insertDomain(urls, pathTemplate, template, sheetAllTech);
		bufferedOutTemplate.flush();
		//
		// int colTech = 2;// fixed parameter, technologies start from column D in
		// "Database"
		// int colData = 6;// fixed parameter, data start fixedrom column G in
		// "Database"
		// int b = 1;
		ArrayList<String> newTech = new ArrayList<String>();

		@SuppressWarnings("unused")
		ArrayList<String> PensoNames = new ArrayList<String>();
		//

		log.add(String.format("Start querying domains, total %1$s", totalNumDomain));

		int tmp = rowNum;
		for (int i = 0; i < totalNumDomain; i++) {
			JSon JSon = new JSon();
			String domain = ex.ReadExcelFile(rowNum, colNum, template, sheetTechBWName);

			log.add(String.format("Process domain %1$s", domain));

			System.out.println("analysing domain " + current++ + ": " + domain + "(step 2/4)");
			String json = JSon.getJson(domain, APIKey); // get JSon of a domain
			if (JSon.checkWrongURL(json) != null) {
				wrongURLs.add(JSon.checkWrongURL(json));
				colNum++;
				continue;
			}
			ArrayList<Tech> techs = new ArrayList<Tech>(); // arraylist contains all tech cell
			techs = JSon.getInterestedTechNames(json); // return ArrayList<Tech>

			ArrayList<String> names = new ArrayList<String>();
			for (Tech t : techs) {
				names.add(t.getName());
			}

			ex.writeTechName(names, rowNum, colNum, template, sheetTechBWName); // write the tech to spreadsheet "Tech
																				// BW Name"
			JSon.countTech(names, map); // count technologies
			rowNum = tmp;
			colNum++;
			bufferedOutTemplate.flush();

			newTech = db.matchDatabase(template, i + 2, i + 6, sheetDatabase, techs); // return ArrayList<String>
																						// newTech
			// write new tech for each web in a separate sheet "NewTech Each Web"
			ex.writeNewTech(newTech, 1, i + 1, template, sheetNewTech);
			PensoNames = db.getPENSONames(template, sheetDatabase, i + 6);
			Cell cell = new Cell();
			for (Tech t : techs) {
				// if t is new tech, continue
				if (newTech.contains(t.getName()))
					continue;
				if (t.isStatus()) { // check status is current or historical
					cell = db.matchCurrentPENSOName(t.getPensoName(), i + 1, template, sheetCurrentTechSummary); // return
																													// a
																													// cell
																													// with
																													// cell
																													// row
																													// &
																													// col
																													// index
					ex.write(xssfsheetAllTech, cell, t, (3 + 4 * i));// start from column D, next domain starts 4
																		// columns after
				}
				if (t.isStatus() == false) { // historical
					cell = db.matchHistoricalPENSOName(t.getPensoName(), i + 1, template, sheetCurrentTechSummary);
					ex.write(xssfsheetAllTech, cell, t, (3 + 4 * i));
				}
			}
			// for (Tech t : techs) {
			// System.out.println(t.toString());
			// }
			bufferedOutTemplate.flush();
		}

		ArrayList<String> allNewTech = new ArrayList<String>();
		allNewTech = db.ifExistInDatabase(map, template, sheetDatabase);

		ex.writeTechCount(map, rowNum, colNum, template, sheetTechBWName);
		colNum++;
		colNum++;

		System.out.println("writing into template... (step 3/4)");

		ex.writeNewTech(allNewTech, rowNum - 1, colNum, template, sheetTechBWName);
		ex.writeWrongURLs(wrongURLs, --rowNum, ++colNum, template, sheetTechBWName);
		ex.insertFormula(totalNumDomain, 5, sheetCurrentTechSummary, template, categories); // insert formula to
																							// calculate SUM and AVERAGE

		System.out.println("deleting irrelevant rows... (step 4/4)");

		ex.deleteRow(template, sheetCurrentTechSummary); // delete irrelevant rows which has count = 0;

		// ex.closeAndWrite(wb, bufferedOutTemplate);

		template.write(bufferedOutTemplate);
		template.close();
		bufferedOutTemplate.close();
		System.out.println("success");
		long end = System.currentTimeMillis();
		System.out.println("time taken: " + (end - start) / 1000 + " s");
	}
}
