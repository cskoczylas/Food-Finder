import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class JunkFood implements Restaurant {

	// string array set-up: [latitude, longitude, street address, city, state]
	String[][] addresses;
	String name;
	Boolean isChecked;
	
	JunkFood(String fileName) 
	{
    	String[] parsedName = fileName.split("[/.]");
    	name = parsedName[2];
    	
    	isChecked = false;
		
		int rowNum = 0;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);

		addresses = new String[sheet.getPhysicalNumberOfRows()][5];
		
		for(Row row: sheet)
		{
			addresses[rowNum][0] = row.getCell(0).toString();
			addresses[rowNum][1] = row.getCell(1).toString();
			addresses[rowNum][2] = row.getCell(3).toString();
			addresses[rowNum][3] = row.getCell(4).toString();
			addresses[rowNum][4] = row.getCell(5).toString();
			rowNum++;
		}

		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> searchByZip(double[] bounds)
	{
		ArrayList<String> results = new ArrayList<>();
		for(int i = 0; i < addresses.length; i++)
		{
			if(Double.parseDouble(addresses[i][0]) <= bounds[0] && Double.parseDouble(addresses[i][0]) >= bounds[2])
			{
				if(Double.parseDouble(addresses[i][1]) <= bounds[1] && Double.parseDouble(addresses[i][1]) >= bounds[3])
				{
					results.add(addresses[i][2] + "\n" + addresses[i][3] + "\n" + addresses[i][4]);
				}
			}
		}
		return results;
	}
	
	public void check()
	{isChecked = true;}
	
	public void uncheck()
	{isChecked = false;}

	public boolean isChecked() 
	{return isChecked;}
	
}
