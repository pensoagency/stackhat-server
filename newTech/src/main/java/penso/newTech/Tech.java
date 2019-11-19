package penso.newTech;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Tech {


	public void addNewTech(XSSFWorkbook wbData,XSSFSheet sheetData, XSSFSheet sheetSummary,String Category, String PENSO_Name, String bwName) throws IOException {

		int lastRow = sheetData.getLastRowNum();// "DATABASE" in database.xlsx
		int lastRowSum= sheetSummary.getLastRowNum(); //"ALL WEBs" in database.xlsx
		// Category not exist ---> create new category
		// add a random background to the new category
		if (checkCategory(Category, sheetData) == -1) {
			Tech tech = new Tech();
			XSSFCellStyle cs = tech.createStyleWithBackgroundColor(wbData);
			XSSFCellStyle cc = tech.createStyleWithoutBackgroundColor(wbData);
			//add in database
			Row newRow = sheetData.createRow(lastRow + 1);
			add(newRow,Category,PENSO_Name,bwName,cs,cc);
			
			//add into Summary
			if(sheetSummary.getRow(lastRowSum+1)==null)
				sheetSummary.createRow(sheetSummary.getLastRowNum()+1);
			Row rowCategory = sheetSummary.getRow(sheetSummary.getLastRowNum());
			Cell cellCategory = rowCategory.createCell(0);
			cellCategory.setCellValue(Category);
			
			XSSFCellStyle style = tech.createCellstyle(wbData);
			cellCategory.setCellStyle(style);
//			if(sheetSummary.getRow(sheetSummary.getLastRowNum()+1)==null)
//				sheetSummary.createRow(sheetSummary.getLastRowNum()+1);
//			Row rowPensoName = sheetSummary.getRow(sheetSummary.getLastRowNum());
//			Cell cellPensoName = rowPensoName.createCell(0);
//			cellPensoName.setCellValue(PENSO_Name);
		

		}
		// Category exists
		else {
			// PENSO_Name does NOT exist
			if (checkPENSOName(PENSO_Name, sheetData) == -1) {
				// get the index of the last row in this category, category start 2
				boolean flag = true; // flag == true means the category first time show
				int end;
				for (int i = 2; i <= lastRow; i++) {
					Row row = sheetData.getRow(i);
					Cell cell = row.getCell(1);//cell Category
					if (cell.getStringCellValue().equals(Category) && flag == true) {
						flag = false;
					}
					// find the last row in this category 
					//!cell.getStringCellValue().equals(Category)&&
					if (flag==false) {
						end = i - 1;
						sheetData.shiftRows(end + 1, sheetData.getLastRowNum(), 1);
						Row newRow = sheetData.createRow(end + 1);
						add(newRow,Category,PENSO_Name,bwName,
								sheetData.getRow(end+2).getCell(1).getCellStyle(),
								sheetData.getRow(end+2).getCell(2).getCellStyle(),
								sheetData.getRow(end+2).getCell(3).getCellStyle());
						break;
					}

				}
//				// add new penso name into summary
//				for(int i=5;i<=sheetSummary.getLastRowNum();i++) {
//					Row row = sheetSummary.getRow(i);
//					Cell cell = row.getCell(0);
//					if(cell.getStringCellValue().equals(Category)) {
//						CellStyle style = sheetSummary.getRow(i+1).getCell(0).getCellStyle();
//						sheetSummary.shiftRows(i+1, sheetSummary.getLastRowNum(), 1);
//						Row newRow2 = sheetSummary.createRow(i+1);
//						Cell cell2 = newRow2.createCell(0);
//						cell2.setCellValue(PENSO_Name);
//						cell2.setCellStyle(style);
//						break;
//					}
//				}

			}
			// PENSO_Name exist, only add new tech into database
			else {
				int start = checkPENSOName(PENSO_Name, sheetData);
				sheetData.shiftRows(start, sheetData.getLastRowNum(), 1);
				Row newRow=sheetData.createRow(start);
				// add tech to database
				add(newRow,Category,PENSO_Name,bwName,sheetData.getRow(start+1).getCell(1).getCellStyle(),
						sheetData.getRow(start+1).getCell(2).getCellStyle(),
						sheetData.getRow(start+1).getCell(3).getCellStyle());
			}

		}
	}

	/**
	 * check if category exists in the database, if exists, return the row num, if
	 * not, return -1
	 * 
	 * @return rowNum of the category if exists
	 * @return -1 if not exists
	 */
	public int checkCategory(String category, XSSFSheet sheet) {
		// col B, start from row 2
		int lastRow = sheet.getLastRowNum();
		for (int i = 2; i <= lastRow; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(1);
			if (cell.getStringCellValue().equals(category))
				return i;
		}
		return -1;
	}

	/**
	 * check if PENSO name exists in the database, if exists, return the row num, if
	 * not, return -1
	 * 
	 * @return rowNum of the PENSO name if exists
	 * @return -1 if not exists
	 */
	public int checkPENSOName(String PENSOName, XSSFSheet sheet) {
		// col C, start from row 2
		int lastRow = sheet.getLastRowNum();
		for (int i = 2; i <= lastRow; i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(2);
			if (cell.getStringCellValue().equals(PENSOName))
				return i;
		}
		return -1;
	}
	
	/**add the tech into database with specific cell style
	 * */
	public void add(Row row,String Category, String PENSO_Name,String bwName, CellStyle cc, CellStyle cp, CellStyle cb) {
		Cell cellCategory = row.createCell(1);
		Cell cellPENSO_Name = row.createCell(2);
		Cell cellBW_Name = row.createCell(3);
		cellCategory.setCellValue(Category);
		cellPENSO_Name.setCellValue(PENSO_Name);
		cellBW_Name.setCellValue(bwName);
		cellCategory.setCellStyle(cc);
		cellPENSO_Name.setCellStyle(cp);
		cellBW_Name.setCellStyle(cb);
	}
	/**add the tech into database without specific cell style
	 * called when new category is added
	 * */
	public void add(Row row,String Category, String PENSO_Name,String bwName, XSSFCellStyle cs,XSSFCellStyle cc) {
		Cell cellCategory = row.createCell(1);
		Cell cellPENSO_Name = row.createCell(2);
		Cell cellBW_Name = row.createCell(3);
		cellCategory.setCellValue(Category);
		cellPENSO_Name.setCellValue(PENSO_Name);
		cellBW_Name.setCellValue(bwName);
		
		cellCategory.setCellStyle(cs);
		cellPENSO_Name.setCellStyle(cc);
		cellBW_Name.setCellStyle(cc);
	}
	
	/**create a cellstyle with a random background color, this method is called when 
	 * a new category is added into Database
	 * */
	public XSSFCellStyle createStyleWithBackgroundColor(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		// set random color
		byte[] rgb = new byte[3];
		rgb[0]=(byte)(int) (Math.random()*256);
		rgb[1]=(byte)(int) (Math.random()*256);
		rgb[2]=(byte)(int) (Math.random()*256);
		XSSFColor color = new XSSFColor(rgb);
		style.setFillForegroundColor(color);
		style.setFillPattern(style.SOLID_FOREGROUND);
		//set border
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		//set font
		XSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		style.setFont(font);
		
		return style;
	}
	/**create a cellstyle WITHOUT a random background color
	 * */
	public XSSFCellStyle createStyleWithoutBackgroundColor(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		//set border
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		//set font
		XSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		style.setFont(font);
		return style;
	}
	/**create a cellstyle for the categories in the "ALL WEBs" 
	 * */
	public XSSFCellStyle createCellstyle(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		// set random color
		byte[] rgb = new byte[3];
		rgb[0]=(byte) 102;
		rgb[1]=(byte) 102;
		rgb[2]=(byte) 102;
		XSSFColor colorBackground = new XSSFColor(rgb);
		style.setFillForegroundColor(colorBackground);
		style.setFillPattern(style.SOLID_FOREGROUND);
		
		//set font to white color
		rgb[0]=(byte) 255;
		rgb[1]=(byte) 255;
		rgb[2]=(byte) 255;
		XSSFColor colorFont = new XSSFColor(rgb);
		//set font
		XSSFFont font = wb.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		font.setColor(colorFont);
		style.setFont(font);
		
		return style;
	}
	
	public boolean checkBWName(String bwName, XSSFSheet sheet) {
		int lastRow = sheet.getLastRowNum();
		for(int i=2;i<=lastRow;i++) {
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(3);
			if(cell.getStringCellValue().equals(bwName))
				return true;
		}
		return false;
	}
}
