import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Controller {

	@FXML
	Button toZip, toAddress, back, zipGo, addressGo, errorOK;
	@FXML
	ImageView logoImage;
	@FXML
	CheckBox mcDonalds, burgerKing, wendys, pizzaHut, traderJoes, wholeFoods;
	@FXML
	TextField zipBox, addressBox;
	@FXML
	Label zipBoxDesc, addressBoxDesc;
	@FXML
	ListView<String> zipResults, addressResults;
	
	DataController controller = new DataController();

		
	@FXML
	public void toZipButtonClicked() throws IOException 
	{

		Parent root = FXMLLoader.load(getClass().getResource("Screens/ZipCode.fxml"));
		Stage stage = (Stage) toZip.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);

	}

	@FXML
	public void toZipResultsClicked() throws IOException 
	{
		if(isZip(zipBox.getText()) && isChecked())
		{
			for(HealthFood r : controller.hFoods)
			{
				if(r.isChecked())
				{
					r.searchByZip(zipBox.getText());
				}
			}
			//load all locations into array
			//after loading screen load array into ListView
			
			Parent root = FXMLLoader.load(getClass().getResource("Screens/ZipCodeResults.fxml"));
			Stage stage = (Stage) zipGo.getScene().getWindow();
			Scene scene = new Scene(root);
			stage.setScene(scene);
		}
		else
		{
			Parent root = FXMLLoader.load(getClass().getResource("Screens/ZipError.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	@FXML
	public void toAddressButtonClicked() throws IOException 
	{
		Parent root = FXMLLoader.load(getClass().getResource("Screens/Address.fxml"));
		Stage stage = (Stage) toAddress.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}
	
	@FXML
	public void toAddressResultsClicked() throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("Screens/AddressResults.fxml"));
		Stage stage = (Stage) addressGo.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	@FXML
	public void backFromSecondClicked() throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("Screens/Main.fxml"));
		Stage stage = (Stage) back.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	@FXML
	public void backFromZipResultsClicked() throws IOException
	{
		unCheckAll();
		
		Parent root = FXMLLoader.load(getClass().getResource("Screens/ZipCode.fxml"));
		Stage stage = (Stage) back.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}
	
	@FXML
	public void backFromAddressResultsClicked() throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource("Screens/Address.fxml"));
		Stage stage = (Stage) back.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
	}

	@FXML
	public void okErrorClicked() throws IOException
	{
		Stage stage = (Stage) errorOK.getScene().getWindow();
		stage.close();
	}

	public boolean isZip(String zip)
	{
		String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(zip);
		return matcher.matches();
	}
	
	private boolean isChecked()
	{
		boolean atLeastOne = false;
		
		if(burgerKing.isSelected())
		{
			controller.getJunk(0).check();
			atLeastOne = true;
		}
		if(mcDonalds.isSelected())
		{
			controller.getJunk(1).check();
			atLeastOne = true;
		}
		if(pizzaHut.isSelected())
		{
			controller.getJunk(2).check();
			atLeastOne = true;
		}
		if(wendys.isSelected())
		{
			controller.getJunk(3).check();
			atLeastOne = true;
		}
		if(traderJoes.isSelected())
		{
			controller.getHealth(0).check();
			atLeastOne = true;
		}
		if(wholeFoods.isSelected())
		{
			controller.getHealth(1).check();
			atLeastOne = true;
		}

		return atLeastOne;
	}
	
	private void unCheckAll()
	{
		for(JunkFood r : controller.jFoods)
		{
			r.uncheck();
		}
		for(HealthFood r : controller.hFoods)
		{
			r.uncheck();
		}
	}
}
