import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;

public class HealthFood implements Restaurant {

	// string array set-up: [street address, state, zipcode]
	private String[][] addresses;
	private String name;
	private Boolean isChecked;

	//JunkFood constructer takes in a filename for the excel data and parses the excel workbook into data within the String Double Array addresses
	//It also stores the name of the restaurant within the String name and it also makes isChecked false
	HealthFood(String fileName) 
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

		addresses = new String[sheet.getPhysicalNumberOfRows()][3];

		for(Row row: sheet)
		{
			addresses[rowNum][0] = row.getCell(0).toString();
			addresses[rowNum][1] = row.getCell(1).toString();
			parsedName = row.getCell(2).toString().split("[.]");
			addresses[rowNum][2] = parsedName[0];
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

	//serachByZip takes in a String input of a zip code and its runs through the addresses array and compares the zip code of different locations to the zip code from the input
	//if they match, the address is stored in results
	public ArrayList<String> searchByZip(String Zip)
	{
		ArrayList<String> results = new ArrayList<>();
		for(int i = 0; i < addresses.length; i++)
		{
			if(addresses[i][2].equals(Zip))
			{
				results.add(addresses[i][0] + "\n" + addresses[i][1] + "\n" + addresses[i][2]);
			}
		}
		return results;
	}

	//searchByAddress first compares the state that the user input and only compares restaurants within that state
	//then it gets the bounds, which are created by adding 25 miles to the lat and long bounds of the users input
	//then it checks every health food within the state and sees if the restaurants lat and long are within the bounds
	//if they are, add to results
	public ArrayList<String> searchByAddress(double[] bounds, String state, DataController controller) throws JSONException
	{
		ArrayList<String> results = new ArrayList<>();
		for(int i = 0; i < addresses.length; i++)
		{
			if(addresses[i][1].equals(state))
			{
				String[] inputAddresses = addresses[i].clone();
				inputAddresses[0] = inputAddresses[0].replace(" ", "+");
				
				double[] latLong = controller.getLatLong(inputAddresses);

				if(latLong[0] <= bounds[0] && latLong[0] >= bounds[2])
				{
					if(latLong[1] <= bounds[1] && latLong[1] >= bounds[3])
					{
						results.add(addresses[i][0] + "\n" + addresses[i][1] + "\n" + addresses[i][2]);
					}
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
