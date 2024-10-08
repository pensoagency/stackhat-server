package penso.stackhat.builtwith;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import penso.stackhat.builtwith.models.Category;
import penso.stackhat.builtwith.models.Name;

public class Database {

	/**
	 * match the Tech table with database, filled "Yes" if match
	 * 
	 * @return an arraylist contains the new Tech for the current domain
	 */
	public ArrayList<String> matchDatabase(XSSFWorkbook wb, int colTech, int colData, String sheetDatabase,ArrayList<Tech> techs) throws IOException {
		ArrayList<String> newTech = new ArrayList<String>();
		XSSFSheet sheetData = wb.getSheet(sheetDatabase);
		int lastData = sheetData.getLastRowNum();
		int len = techs.size();
		outer: for (int i = 0; i < len; i++) { // tech starts from row 3 in sheet "Database"
			String name = techs.get(i).getName();
			for (int j = 2; j <= lastData; j++) { // domain starts on row 3 in sheet "Database"
				Row rowData = sheetData.getRow(j);
				Cell cellData = rowData.getCell(3);
				if (cellData == null) {
					newTech.add(name);
					continue outer;
				}
				String Data = cellData.getRichStringCellValue().toString();
				if (name.equals(Data)) {
					if (name.equals("") && Data.equals(""))
						break;
					Cell cellYes = rowData.createCell(colData);
					cellYes.setCellValue("Yes");
					techs.get(i).setPensoName(rowData.getCell(2).getRichStringCellValue().toString());
					continue outer;
				}
			}
			newTech.add(name);
		}
		return newTech;
	}

	/**
	 * if the tech does NOT exist in the database, add it to the arraylist
	 * 
	 * @return an arraylist of all the new technologies
	 * @throws IOException
	 */
	public ArrayList<String> ifExistInDatabase(HashMap<String, Integer> map, XSSFWorkbook wb, String sheetDatabase)
			throws IOException {
		ArrayList<String> newTech = new ArrayList<String>();
		XSSFSheet sheet = wb.getSheet(sheetDatabase);

		// BW names column D in sheet "Tech BW Name"
		int colnum = 3;
		int lastRow = sheet.getLastRowNum();
		ArrayList<Map.Entry<String, Integer>> lists = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		newTech.add("New Tech");
		outer: for (Map.Entry<String, Integer> list : lists) {

			for (int i = 2; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if (row.getCell(colnum) == null)
					continue;
				Cell cell = row.getCell(colnum);
				if (cell.getRichStringCellValue().getString().trim().equals(list.getKey()))
					continue outer;
			}
			newTech.add(list.getKey());

		}
		return newTech;
	}

	/**
	 * Match the "Yes" column with the database,return a list contains all the
	 * PensoNames
	 * @param colNum column number of the domain in the "Database" sheet
	 * @param databaseSheetName "Database"
	 * @return an Arraylist with only Penso Names
	 */
	public ArrayList<String> getPENSONames(XSSFWorkbook wb, String databaseSheetName, int colNum) throws IOException {
		ArrayList<String> PensoNames = new ArrayList<String>();
		XSSFSheet sheet = wb.getSheet(databaseSheetName);

		int lastRow = sheet.getLastRowNum();

		for (int i = 2; i <= lastRow; i++) {
			Row row = sheet.getRow(i);
			// column G
			Cell cell = row.getCell(colNum);
			if (cell == null || cell.getRichStringCellValue().toString().equals(""))
				continue;

			// get the PensoName cell
			Cell cell2 = row.getCell(2);
			if (cell2 == null || cell2.getRichStringCellValue().toString().equals(""))
				continue;
			// add the PensoName to list
			String PensoName = cell2.getRichStringCellValue().toString();
			if (PensoNames.contains(PensoName))
				continue;
			PensoNames.add(cell2.getRichStringCellValue().toString());
		}

//		for(String s:PensoNames)
//			System.out.println(s);
		return PensoNames;
	}

	/**
	 * insert 1 on the summary sheet
	 */
	public void matchPENSOName(ArrayList<String> PensoNames, int colNum, XSSFWorkbook wb, String sheetName)
			throws IOException {
		XSSFSheet sheetSummary = wb.getSheet(sheetName);
		int lastRow = sheetSummary.getLastRowNum();

		outer: for (String name : PensoNames) {
			for (int i = 5; i <= lastRow; i++) {
				Row row = sheetSummary.getRow(i);
				Cell cell = row.getCell(0);
				if (cell == null)
					continue;
				String Tech = cell.getRichStringCellValue().toString();
				if (Tech.equals(name)) {
					if (row.getCell(colNum + 2) == null) {
						row.createCell(colNum + 2);
					}
					Cell cell3 = row.getCell(colNum + 2);
					cell3.setCellValue(1);
					continue outer;
				}
			}
		}
		// row num : i
		// col num: colNum+2

	}

	/**
	 * insert 1 on the summary sheet
	 * @return row index & col index
	 */
	public penso.stackhat.builtwith.Cell matchCurrentPENSOName(String PensoName, int colNum, XSSFWorkbook wb, String sheetName)
			throws IOException {
		XSSFSheet sheetSummary = wb.getSheet(sheetName);
		int lastRow = sheetSummary.getLastRowNum();
		penso.stackhat.builtwith.Cell pensoCell = new penso.stackhat.builtwith.Cell();
		for (int i = 5; i <= lastRow; i++) {
			Row row = sheetSummary.getRow(i);
			Cell cell = row.getCell(0);
			if (cell == null)
				continue;
			String Tech = cell.getRichStringCellValue().toString();
			if (Tech.equals(PensoName)) {
				if (row.getCell(colNum + 2) == null) {
					row.createCell(colNum + 2);
				}
				Cell cell3 = row.getCell(colNum + 2);
				cell3.setCellValue(1);
				pensoCell.setRowNum(i);
				pensoCell.setColNum(colNum + 2);
			}
		}
		// row num : i
		// col num: colNum+2
		return pensoCell;
	}

	
	/**
	 * 
	 */
	public penso.stackhat.builtwith.Cell matchHistoricalPENSOName(String PensoName, int colNum, XSSFWorkbook wb, String sheetName)
			throws IOException {
		XSSFSheet sheetSummary = wb.getSheet(sheetName);
		int lastRow = sheetSummary.getLastRowNum();
		penso.stackhat.builtwith.Cell pensoCell = new penso.stackhat.builtwith.Cell();
		for (int i = 5; i <= lastRow; i++) {
			Row row = sheetSummary.getRow(i);
			Cell cell = row.getCell(0);
			if (cell == null)
				continue;
			String Tech = cell.getRichStringCellValue().toString();
			if (Tech.equals(PensoName)) {
				// row num : i
				// col num: colNum+2
				pensoCell.setRowNum(i);
				pensoCell.setColNum(colNum + 2);
			}
		}
		return pensoCell;
	}	
	
	public ArrayList<String> getAllCategories(String path, String summary) throws IOException{
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(in);
		XSSFSheet sheet = wb.getSheet(summary);
		
		ArrayList<String> categories = new ArrayList<String>();
		int lastRow= sheet.getLastRowNum();
		for(int i=4;i<=lastRow;i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(0);
			categories.add(cell.getStringCellValue());
		}
//		for(String category:categories) {
//			System.out.println(category);
//		}
		wb.close();
		in.close();
		return categories;
	}

	public ArrayList<Category> getAllCategoriesDetail(String path) throws IOException{
		File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(in);
		XSSFSheet sheet = wb.getSheet("DATABASE");
		
		ArrayList<Category> categories = new ArrayList<Category>();
		int lastRow= sheet.getLastRowNum();

		Category currentCategory = null;
		Name currentName = null;
		for(int i=2;i<=lastRow;i++) {
			// get the row
			Row row = sheet.getRow(i);

			///////////////////////////////////
			// category
			///////////////////////////////////

			// get category name	
			Cell cell = row.getCell(1);
			String categoryTitle = cell.getStringCellValue();			

			// has category changed?
			if (currentCategory != null && !currentCategory.Title.equals(categoryTitle)) {
				// yes -> add current name to this cat
				currentCategory.Names.add(currentName);
				currentName = null;
				// add existing to list
				categories.add(currentCategory);
				// set null to trigger new item
				currentCategory = null;
			}

			if (currentCategory == null) {
				// create new category and set title
				currentCategory = new Category();
				currentCategory.setTitle(categoryTitle);				
			}

			///////////////////////////////////
			// name
			///////////////////////////////////

			// get name	
			cell = row.getCell(2);
			String nameTitle = cell.getStringCellValue();

			// has name changed?
			if (currentName != null && !currentName.Title.equals(nameTitle)) {
				// yes -> add existing to list
				currentCategory.Names.add(currentName);
				// set null to trigger new item
				currentName = null;
			}			

			if (currentName == null) {
				// create new category and set title
				currentName = new Name();
				currentName.setTitle(nameTitle);				
			}

			///////////////////////////////////
			// technology
			///////////////////////////////////			

			// get technology	
			cell = row.getCell(3);
			String technologyName = cell.getStringCellValue();

			// add to current name
			currentName.Technologies.add(technologyName);

		}

		// add final category + name
		if (!currentCategory.equals(null)) {
			currentCategory.Names.add(currentName);
			categories.add(currentCategory);
		}

		wb.close();
		in.close();
		return categories;
	}	
}
