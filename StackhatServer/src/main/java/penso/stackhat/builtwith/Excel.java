package penso.stackhat.builtwith;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
	private String domain;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String ReadExcelFile(int rowNum, int colNum, XSSFWorkbook wb, String sheetName) throws IOException {
		XSSFSheet sheet = wb.getSheet(sheetName);
		Row row = sheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		this.domain = cell.toString();
		return domain;
	}

	/**
	 * write the technologies used in each website into an excel sheet
	 * 
	 * @param list      the ArrayList of Tech Names that need to be written into
	 *                  excel
	 * @param rowNum    write the list to the nth row in the sheet, start from 0
	 * @param excelPath excelPath
	 * @param sheetName name of the spreadsheet
	 */
	public void writeTechName(ArrayList<String> list, int rowNum, int colNum, XSSFWorkbook wb, String sheetName)
			throws IOException {

		// create an excel sheet "Sheet1"
		XSSFSheet sheet = wb.getSheet(sheetName);

		for (int i = 0; i < list.size(); i++) {
			if (sheet.getRow(rowNum + 1) == null) {
				Row row = sheet.createRow(rowNum + 1);
				Cell cell = row.createCell(colNum);
				cell.setCellValue(list.get(i));
				rowNum++;
				continue;
			}
			Row row = sheet.getRow(rowNum + 1);
			Cell cell = row.createCell(colNum);
			cell.setCellValue(list.get(i));
			rowNum++;
		}

		sheet.autoSizeColumn(colNum);
	}

	/**
	 * write all new technologies into excel sheet
	 */
	public void writeNewTech(ArrayList<String> list, int rowNum, int colNum, XSSFWorkbook wb, String sheetName)
			throws IOException {

		XSSFSheet sheet = wb.getSheet(sheetName);

		for (int i = 0; i < list.size(); i++) {
			if (sheet.getRow(rowNum + 1) == null) {
				Row row = sheet.createRow(rowNum + 1);
				Cell cell = row.createCell(colNum);
				cell.setCellValue(list.get(i));
				rowNum++;
				continue;
			}
			Row row = sheet.getRow(rowNum + 1);
			Cell cell = row.createCell(colNum);
			cell.setCellValue(list.get(i));
			rowNum++;
		}
		sheet.autoSizeColumn(colNum);
	}

	public void writeWrongURLs(ArrayList<String> list, int rowNum, int colNum, XSSFWorkbook wb, String sheetName)
			throws IOException {
		this.writeNewTech(list, rowNum, colNum, wb, sheetName);
	}

	/**
	 * @param map       the HashMap that need to be written into excel
	 * @param rowNum    write the list to the nth row in the sheet, start from 0
	 * @param excelPath excel path
	 * @param sheetName spreadsheet's name
	 * @throws IOException
	 */
	public void writeTechCount(HashMap<String, Integer> map, int rowNum, int colNum, XSSFWorkbook wb, String sheetName)
			throws IOException {
		int tmp = rowNum;
		// sort the map based on value
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				return o2.getValue() - o1.getValue();
			}
		});

		// create an excel sheet "Sheet1"
		XSSFSheet sheet = wb.getSheet(sheetName);

		// write all map keys
		Row keyRow = sheet.getRow(rowNum);
		keyRow.createCell(colNum).setCellValue("Technology");
		// create the nth row in "Sheet1"
		rowNum += 1;
		for (Map.Entry<String, Integer> entry : list) {
			if (sheet.getRow(rowNum) == null) {
				sheet.createRow(rowNum);
			}
			Row row = sheet.getRow(rowNum);
			Cell cell = row.createCell(colNum);
			cell.setCellValue(entry.getKey());
			rowNum++;
		}
		colNum++;
		// write all map values
		rowNum = tmp;
		Row valueRow = sheet.getRow(rowNum);
		valueRow.createCell(colNum).setCellValue("count");
		// create the nth row in "Sheet1"
		rowNum += 1;
		for (Map.Entry<String, Integer> entry : list) {
			if (sheet.getRow(rowNum) == null) {
				sheet.createRow(rowNum);
			}
			Row row = sheet.getRow(rowNum);
			Cell cell = row.createCell(colNum);
			cell.setCellValue(entry.getValue());
			rowNum++;
		}

		sheet.autoSizeColumn(colNum - 1);
		sheet.autoSizeColumn(colNum);
	}

	/**
	 * @param excelPath excelPath
	 * @param sheetName name of the spreadsheet
	 */
	public void createExcel(String excelPath, String sheetName) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		// create a spreadsheet
		workbook.createSheet(sheetName);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(excelPath);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeMyRow(XSSFSheet sheet, int rowIndex) throws IOException {
		if (rowIndex == sheet.getLastRowNum())
			sheet.removeRow(sheet.getRow(rowIndex));
		sheet.shiftRows(rowIndex + 1, sheet.getLastRowNum(), -1);
	}

	/**
	 * delete irrelevant rows
	 */
	public void deleteRow(XSSFWorkbook wb, String sheetName) throws IOException {
		// create an excel sheet "Sheet1"
		XSSFSheet sheet = wb.getSheet(sheetName);
		int cur = 5;
		int lastRowNum = sheet.getLastRowNum();

		for (int i = 0; i < lastRowNum; i++) {
			Row row = sheet.getRow(cur);
			if (row == null)
				break;
			if (row.getCell(2) == null) {
				cur++;
				continue;
			}
			if (row.getCell(2).getNumericCellValue() == 0) {
				if (cur == sheet.getLastRowNum()) {
					sheet.removeRow(row);
					break;
				}
				removeMyRow(sheet, cur);
				continue;
			}
			cur++;

			if (cur == sheet.getLastRowNum())
				break;
		}

	}

	/**
	 * The most time-consuming function in this project. insert formula to calculate
	 * SUM and AVERAGE
	 * 
	 * @param totalNumDomain total number of domains
	 * @param rowNum         first tech row number
	 * @param sheetName      summary sheet name
	 */
	public void insertFormula(int totalNumDomain, int rowNum, String sheetName, XSSFWorkbook wb,
			ArrayList<String> categories) throws IOException, InvalidFormatException {

		// create an excel sheet "Sheet1"
		XSSFSheet sheet = wb.getSheet(sheetName);
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 5; i <= lastRowNum; i++) {
			Row row = sheet.getRow(rowNum);
			if (row == null)
				continue;
			Cell cell = row.getCell(0);
			if (cell == null)
				break;
			String content = cell.getRichStringCellValue().toString();
//			if (content.equals("CMS") || content.equals("CDN") || content.equals("Analytics and Tracking")
//					|| content.equals("Mobile") || content.equals("Web Hosting Providers")
//					|| content.equals("Email Hosting Providers") || content.equals("Web Servers")
//					|| content.equals("Syndication Techniques") || content.equals("SSL certificates")
//					|| content.equals("Advertising") || content.equals("Audio/Video Media") || content.equals("Mapping")
//					|| content.equals("Payment")||content.equals("Email Marketing")
//					|| content.equals("Marketing Automation")) {
			if (categories.contains(content)) {
				rowNum++;
				continue;
			}
			if (row.getCell(2) == null)
				row.createCell(2);
			if (row.getCell(1) == null)
				row.createCell(1);
			Cell cellAverage = row.getCell(1);
			Cell cellTotal = row.getCell(2);
			cellTotal.setCellFormula("SUM(D" + (rowNum + 1) + ":DZ" + (rowNum + 1) + ")");
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);

			// set Average column
			cellAverage.setCellValue((cellTotal.getNumericCellValue() / totalNumDomain));
			// set the average column to "0.00%" format
			CellStyle style = wb.createCellStyle();
			style.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
			cellAverage.setCellStyle(style);

			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			rowNum++;
		}
		// 9.67 6.78
		sheet.setColumnWidth(1, 11 * 256);
		sheet.setColumnWidth(2, 8 * 256);
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
	}

	/**
	 * get the excel workbook
	 */
	public XSSFWorkbook openExcelFile(String path) throws IOException {
		File myFile = new File(path);
		FileInputStream in = new FileInputStream(myFile);
		XSSFWorkbook wb = new XSSFWorkbook(in);
		return wb;
	}

	/**
	 * close streams, write excel workbook
	 */
	public void closeAndWrite(XSSFWorkbook wb, String path) throws IOException {
		FileOutputStream out = new FileOutputStream(path);
		wb.write(out);
		wb.close();
		out.close();
	}

	public void closeAndWrite(XSSFWorkbook wb, FileOutputStream out) throws IOException {
		wb.write(out);
		wb.close();
		out.close();
	}

	public void insertDomain(String[] args, String path, XSSFWorkbook wb, String summary, String newTechSheet,
			String techBWName, String databaseSheet) throws IOException {
		int totalNum = args.length;

		XSSFSheet sheetSummary = wb.getSheet(summary);
		XSSFSheet sheetDatabase = wb.getSheet(databaseSheet);
		XSSFSheet sheetNewTechEachWeb = wb.getSheet(newTechSheet);
		XSSFSheet sheetTechBWName = wb.getSheet(techBWName);

		Row rowSummary = sheetSummary.getRow(2);
		Row rowDatabase = sheetDatabase.getRow(1);
		Row rowNewTechEachWeb = sheetNewTechEachWeb.getRow(1);
		Row rowTechBWName = sheetTechBWName.getRow(1);

		int index = 0;
		for (int i = 0; i < totalNum; i++) {
			if (rowSummary.getCell(index + 3) == null) { // +3 means start from column D on sheet "Summary”
				rowSummary.createCell(index + 3);
			}
			if (rowDatabase.getCell(index + 6) == null) { // start form G
				rowDatabase.createCell(index + 6);
				sheetDatabase.autoSizeColumn(index + 6);
			}
			if (rowNewTechEachWeb.getCell(index + 1) == null) { // start from B
				rowNewTechEachWeb.createCell(index + 1);
				sheetNewTechEachWeb.autoSizeColumn(index + 1);
			}
			if (rowTechBWName.getCell(index + 2) == null) { // start from C
				rowTechBWName.createCell(index + 2);
				sheetTechBWName.autoSizeColumn(index + 2);
			}
			rowSummary.getCell(index + 3).setCellValue(args[i]);
			rowDatabase.getCell(index + 6).setCellValue(args[i]);
			rowNewTechEachWeb.getCell(index + 1).setCellValue(args[i]);
			rowTechBWName.getCell(index + 2).setCellValue(args[i]);
			sheetSummary.autoSizeColumn(index + 3);
			index++;
		}
	}

	public void insertDomain(String[] args, String path, XSSFWorkbook wb, String allTechSheet) throws IOException {
		int totalNum = args.length;

		XSSFSheet sheetAllTech = wb.getSheet(allTechSheet);

		Row rowAllTech = sheetAllTech.getRow(2);
		Row rowLabel = sheetAllTech.getRow(3);

		for (int j = 3; j < 4 * totalNum + 3; j++) {
			if (rowAllTech.getCell(j) == null)
				rowAllTech.createCell(j);
			if (rowLabel.getCell(j) == null)
				rowLabel.createCell(j);
		}
		int index = 0;

		for (int i = 0; i < totalNum; i++) {
			if (rowAllTech.getCell(index + 3) == null) { // same as sheet "Summary"
				rowAllTech.createCell(index + 3);
			}
			rowAllTech.getCell(index + 3).setCellValue(args[i]);

			CellRangeAddress rangeAddress = new CellRangeAddress(2, 2, index + 3, index + 6); // merge cell
			sheetAllTech.addMergedRegion(rangeAddress);
			CellStyle style = rowLabel.getCell(3).getCellStyle();// column D as standard cell style
			
			rowLabel.getCell(index + 3).setCellValue("Status");
			rowLabel.getCell(index + 4).setCellValue("First Detected");
			rowLabel.getCell(index + 5).setCellValue("Last Detected");
			rowLabel.getCell(index + 6).setCellValue("Duration");
			
			rowLabel.getCell(index + 3).setCellStyle(style);
			rowLabel.getCell(index + 4).setCellStyle(style);
			rowLabel.getCell(index + 5).setCellStyle(style);
			rowLabel.getCell(index + 6).setCellStyle(style);

			index += 4;
		}

	}

	public void insertDomain(String[] args, String path, XSSFWorkbook wb, String summary, String newTechSheet,
			String techBWName, String databaseSheet, String allTechSheet) throws IOException {
		int totalNum = args.length;

		XSSFSheet sheetSummary = wb.getSheet(summary);
		XSSFSheet sheetDatabase = wb.getSheet(databaseSheet);
		XSSFSheet sheetNewTechEachWeb = wb.getSheet(newTechSheet);
		XSSFSheet sheetTechBWName = wb.getSheet(techBWName);
		XSSFSheet sheetAllTech = wb.getSheet(allTechSheet);

		Row rowSummary = sheetSummary.getRow(2);
		Row rowDatabase = sheetDatabase.getRow(1);
		Row rowNewTechEachWeb = sheetNewTechEachWeb.getRow(1);
		Row rowTechBWName = sheetTechBWName.getRow(1);
		Row rowAllTech = sheetAllTech.getRow(2);

		int index = 0;
		for (int i = 0; i < totalNum; i++) {
			if (rowSummary.getCell(index + 3) == null) { // +3 means start from column D on sheet "Summary”
				rowSummary.createCell(index + 3);
			}
			if (rowDatabase.getCell(index + 6) == null) { // start form G
				rowDatabase.createCell(index + 6);
				sheetDatabase.autoSizeColumn(index + 6);
			}
			if (rowNewTechEachWeb.getCell(index + 1) == null) { // start from B
				rowNewTechEachWeb.createCell(index + 1);
				sheetNewTechEachWeb.autoSizeColumn(index + 1);
			}
			if (rowTechBWName.getCell(index + 2) == null) { // start from C
				rowTechBWName.createCell(index + 2);
				sheetTechBWName.autoSizeColumn(index + 2);
			}
			if (rowAllTech.getCell(index + 3) == null) { // same as sheet "Summary"
				rowAllTech.createCell(index + 3);
			}
			rowSummary.getCell(index + 3).setCellValue(args[i]);
			rowDatabase.getCell(index + 6).setCellValue(args[i]);
			rowNewTechEachWeb.getCell(index + 1).setCellValue(args[i]);
			rowTechBWName.getCell(index + 2).setCellValue(args[i]);
			rowAllTech.getCell(index + 3).setCellValue(args[i]);
			sheetSummary.autoSizeColumn(index + 3);
			sheetAllTech.autoSizeColumn(index + 3);
			index++;
		}
	}

	/**
	 * update "ALL WEBs" in database.xlsx
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	public void updateDatabase(XSSFWorkbook database, String sheetDatabase, String sheetSummary,BufferedOutputStream bufferedOutTemplate) throws IOException {
//		String path = "C:\\Users\\myxll\\Desktop\\test\\database.xlsx";
//		File database = new File(path);
//		FileInputStream in = new FileInputStream(database);
//		XSSFWorkbook wb = new XSSFWorkbook(in);
		XSSFSheet sheetData = database.getSheet(sheetDatabase);
		XSSFSheet sheetTmp = database.getSheet(sheetSummary);

		int lastRowData = sheetData.getLastRowNum();
		Map<String, String> map = new HashMap<String, String>();
		String category = null;

		XSSFFont font = database.createFont();
		font.setBold(true);

		XSSFCellStyle style = database.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 242;
		rgb[1] = (byte) 242;
		rgb[2] = (byte) 242;
		XSSFColor color = new XSSFColor(rgb);
		style.setFillForegroundColor(color);
		style.setFillPattern(style.SOLID_FOREGROUND);

		style.setFont(font);

		outer: for (int i = 2; i <= lastRowData; i++) {
			Row rowData = sheetData.getRow(i);
//			System.out.println("i= " + i);
			Cell cellDataCategory = rowData.getCell(1);
			Cell cellDataPensoname = rowData.getCell(2);

			if (i == 2) {
				category = cellDataCategory.getStringCellValue();
			}
//			System.out.println("i: "+i);
			if (cellDataCategory.getStringCellValue() != category || i == lastRowData) {
				if (i == lastRowData)
					map.put(cellDataPensoname.getStringCellValue(), "");
				int lastRowTmp = sheetTmp.getLastRowNum();
				for (int j = 4; j <= lastRowTmp + 1; j++) {
					Row rowTmp = sheetTmp.getRow(j);
					Cell cellTmp = rowTmp.getCell(0);
					String value = cellTmp.getStringCellValue();
//					System.out.println("j: "+j);
					if (value.equals(category)) {
						for (Map.Entry<String, String> entry : map.entrySet()) {
							lastRowTmp = sheetTmp.getLastRowNum();
							if (j == lastRowTmp) {
								Row row = sheetTmp.createRow(j + 1);
								Cell cell = row.createCell(0);

								cell.setCellValue(entry.getKey());
								cell.setCellStyle(style);
								continue;
							}

							sheetTmp.shiftRows(j + 1, lastRowTmp, 1);
							Row row = sheetTmp.createRow(j + 1);
							Cell cell = row.createCell(0);

							cell.setCellValue(entry.getKey());
							cell.setCellStyle(style);
						}
						category = cellDataCategory.getStringCellValue();
						map.clear();
						map.put(cellDataPensoname.getStringCellValue(), "");
						continue outer;
					}
				}
			}

			if (!map.containsKey(cellDataPensoname.getStringCellValue())) {
				map.put(cellDataPensoname.getStringCellValue(), "");
			}

		}
//
//		database.write(outDatabase);
//		outDatabase.close();
//		database.close();
		bufferedOutTemplate.flush();
	}

	/**
	 * copy sheet "ALL webs" from database.xlsx to tmp.xlsx
	 */
	public void copyTemplate(XSSFWorkbook database, XSSFWorkbook template, String sheetTemplate, String sheetDatabase,BufferedOutputStream bufferedOutTemplate)
			throws IOException {

		XSSFSheet sheetData = database.getSheet(sheetDatabase);
		XSSFSheet sheetTmp = template.getSheet(sheetTemplate);

		int lastRow = sheetData.getLastRowNum();
		for (int i = 4; i <= lastRow; i++) {
			if (sheetData.getRow(i) == null)
				continue;
			Row rowData = sheetData.getRow(i);
			if (sheetTmp.getRow(i) == null) {
				sheetTmp.createRow(i);
//				sheetTmp.getRow(i).createCell(0);
			}
//			if(sheetTmp.getRow(i)==null)
//				sheetTmp.createRow(i);
			Row rowTmp = sheetTmp.getRow(i);
			Cell cellData = rowData.getCell(0);
			if (rowTmp.getCell(0) == null)
				rowTmp.createCell(0);
			Cell cellTmp = rowTmp.getCell(0);
			CellStyle style = cellData.getCellStyle();
			CellStyle newStyle = template.createCellStyle();
			newStyle.cloneStyleFrom(style);
			String cellValue = cellData.getStringCellValue();
			cellTmp.setCellValue(cellValue);
			cellTmp.setCellStyle(newStyle);
		}

		bufferedOutTemplate.flush();
	}

	/**
	 * copy the database from "Database.xlsx" to "template.xlsx" update the database
	 * in template
	 */
	public void copyDatabase(XSSFWorkbook database, XSSFWorkbook template, String sheetDatabase,BufferedOutputStream bufferedOutTemplate) throws IOException {
	
		XSSFSheet sheetData = database.getSheet(sheetDatabase);
		XSSFSheet sheetTmp = template.getSheet(sheetDatabase);

		int lastRow = sheetData.getLastRowNum();

		for (int i = 2; i <= lastRow; i++) {
			Row rowData = sheetData.getRow(i);
			// if row = null, create row and cells
			if (sheetTmp.getRow(i) == null) {
				sheetTmp.createRow(i);
				sheetTmp.getRow(i).createCell(1);
				sheetTmp.getRow(i).createCell(2);
				sheetTmp.getRow(i).createCell(3);
			}
			Row rowTmp = sheetTmp.getRow(i);
			if (rowTmp.getCell(1) == null)
				rowTmp.createCell(1);
			if (rowTmp.getCell(2) == null)
				rowTmp.createCell(2);
			if (rowTmp.getCell(3) == null)
				rowTmp.createCell(3);
			copyRow(rowData, rowTmp, database, template);
		}
		sheetTmp.autoSizeColumn(1);
		sheetTmp.autoSizeColumn(2);
		sheetTmp.autoSizeColumn(3);

		bufferedOutTemplate.flush();
	}

	public void copyRow(Row r1, Row r2, XSSFWorkbook wbData, XSSFWorkbook wbTmp) {
		for (int i = 0; i < 3; i++) {
			Cell cell1 = r1.getCell(i + 1);
			Cell cell2 = r2.getCell(i + 1);
			copyCell(cell1, cell2, wbData, wbTmp);
		}
	}

	public void copyCell(Cell c1, Cell c2, XSSFWorkbook wbData, XSSFWorkbook wbTmp) {
		CellStyle cellStyle = c1.getCellStyle();// get cell style
		String cellValue = c1.getStringCellValue(); // get cell value
		c2.setCellValue(cellValue);// copy cell value
		CellStyle newStyle = wbTmp.createCellStyle();
		newStyle.cloneStyleFrom(cellStyle);
		c2.setCellStyle(newStyle);// copy cell style
	}

	public void copyTemplateStyle(XSSFWorkbook template, String sheetName, ArrayList<String> categories, BufferedOutputStream bufferedOutTemplate) throws IOException {

		XSSFSheet sheet = template.getSheet(sheetName);
		int lastRow = sheet.getLastRowNum();
		for (int i = 4; i <= lastRow; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(0);
			if (cell == null)
				break;

			if (categories.contains(cell.getStringCellValue())) {
				CellStyle style = cell.getCellStyle();
				row.setRowStyle(style);
			}
		}

		bufferedOutTemplate.flush();
	}

	public void write(XSSFSheet sheet, penso.stackhat.builtwith.Cell cell, Tech tech, int colNum) {

		int rowNum = cell.getRowNum();
//		System.out.println(rowNum);
		if (sheet.getRow(rowNum) == null) {
			sheet.createRow(rowNum);
		}
		Row row = sheet.getRow(rowNum);
		Long firstDetected = tech.getFirstDetectedTime();
		Long lastDetected = tech.getLastDetectedTime();
		String duration = tech.getDuration();
		for (int i = colNum; i < colNum + 4; i++) {
			if (row.getCell(i) == null)
				row.createCell(i);
		}

		DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy"); // date format

		Calendar calendarFirst = Calendar.getInstance();
		calendarFirst.setTimeInMillis(firstDetected);
		String firstDate = formatter.format(calendarFirst.getTime());

		Calendar calendarLast = Calendar.getInstance();
		calendarLast.setTimeInMillis(lastDetected);
		String lastDate = formatter.format(calendarLast.getTime());

		if (tech.isStatus())
			row.getCell(colNum).setCellValue("Current");
		if (tech.isStatus() == false)
			row.getCell(colNum).setCellValue("Historical");
		row.getCell(colNum + 1).setCellValue(firstDate);
		row.getCell(colNum + 2).setCellValue(lastDate);
		row.getCell(colNum + 3).setCellValue(duration);

	}
}
