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
	String[][] addresses;
	String name;
	Boolean isChecked;

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

	public void check()
	{isChecked = true;}

	public void uncheck()
	{isChecked = false;}

	public boolean isChecked() 
	{return isChecked;}

}
