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
	private String[][] addresses;
	private String name;
	private Boolean isChecked;
	
	//JunkFood constructer takes in a filename for the excel data and parses the excel workbook into data within the String Double Array addresses
	//It also stores the name of the restaurant within the String name and it also makes isChecked false
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
	
	//searchByZip takes in a double[] bounds which has the lat and long bounds from the Google Maps API
	//it compares the lat and long within the addresses array and if they are within the bounds they are stored in results
	//searchByZip is also used to search by address since they use the same method
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

	public boolean isChecked() 
	{return isChecked;}
	
	public String getName()
	{return name;}
	
	public void check()
	{isChecked = true;}
	
	public void uncheck()
	{isChecked = false;}

	
}
