package fxconky;

import org.apache.poi.EncryptedDocumentException;

//Path path = Paths.get(System.getProperty("user.dir")+"/CURRENCY.XLSX");


import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ExcelCellReader {
	  static Map<String, String> stockMap = new HashMap<>();
	
    public static  void fillMap() {
    	    	
    	
    		String filePath = Paths.get(System.getProperty("user.dir")+"/CURRENCY.xlsm").toString();
    	  	
        // Define zero-indexed targets (e.g., Row 1, Column 0 is cell A2)
     int targetColumnIndex =1;
     
    // try (InputStream is = getClass().getResourceAsStream("/data/CURRENCY.xlsm");Workbook workbook = WorkbookFactory.create(is)) {  
    	 try (Workbook workbook = WorkbookFactory.create(new File(filePath))) { 
    	 
    	 for (int targetRowIndex = 1; targetRowIndex<8;targetRowIndex++) {
    	   			       			      
					        	// Get the first sheet (index 0)
					            Sheet sheet = workbook.getSheetAt(0);
					            
					            // Fetch the specific row
					            Row row = sheet.getRow(targetRowIndex);
					            if (row != null) {
					                // Fetch the specific cell
					                Cell cell = row.getCell(targetColumnIndex);
					                Cell cell1 = row.getCell(targetColumnIndex+1);
					                if (cell != null) {
					                    String cellValue = getCellValueAsString(cell);
					                    String cellValue1 =getCellValueAsString(cell1);
					                    stockMap.put(cellValue,cellValue1);
					                    
					                
					                } else {
					                    System.out.println("The cell is empty.");
					                }
					            } else {
					                System.out.println("The row does not exist.");
					            }
     } //for loop      
					        } catch (IOException e) {
					            e.printStackTrace();
					        } catch (EncryptedDocumentException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
	    
     // Print the result
     System.out.println("Copied Map: " + stockMap);
     } //function 
    /**
     * Helper method to safely read cell content based on its type.
     */
    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Evaluates the formula and returns the cached string/numeric result
                return cell.getCellFormula(); 
            case BLANK:
                return "";
            default:
                return "Unknown Type";
        }
    }
}
