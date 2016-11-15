import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller implements Initializable{

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
	ListView<String> zipResults = new ListView<String>();
	ListView<String> addressResults = new ListView<String>();


	private MainDisplay main;

	public void setMain(MainDisplay main)
	{this.main = main;}


	@FXML
	public void toZipButtonClicked() throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/ZipCode.fxml"));
		loader.setController(this);
		AnchorPane pane = loader.load();


		Scene scene = new Scene(pane);
		main.getStage().setScene(scene);

	}

	@FXML
	public void toZipResultsClicked() throws IOException, JSONException 
	{
		ArrayList<String> results = new ArrayList<>();
		ObservableList<String> data = FXCollections.observableArrayList();

		if(isZip(zipBox.getText()) && isChecked())
		{
			//bounds are [NE lat, NE long, SW lat, SW long]
			double[] bounds = main.getDataController().zipToLatLongBounds(zipBox.getText());
			for(HealthFood r : main.getDataController().hFoods)
			{
				if(r.isChecked())
				{
					results = r.searchByZip(zipBox.getText());
					for(String loc : results)
					{
						loc = r.name + "@\n" + loc;
						data.add(loc);
					}
				}
			}
			for(JunkFood r : main.getDataController().jFoods)
			{
				if(r.isChecked())
				{
					results = r.searchByZip(bounds);
					for(String loc: results)
					{
						loc = r.name + "@\n" + loc;
						data.add(loc);
					}
				}
			}


			FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/ZipCodeResults.fxml"));
			loader.setController(this);
			AnchorPane pane = loader.load();

			zipResults.setItems(data);

			Scene scene = new Scene(pane);
			main.getStage().setScene(scene);


		}
		else if(isChecked() != true)
		{
			FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/CheckBoxError.fxml"));
			loader.setController(this);
			AnchorPane pane = loader.load();


			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		}
		else
		{
			FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/ZipError.fxml"));
			loader.setController(this);
			AnchorPane pane = loader.load();


			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.show();
		}
	}

	@FXML
	public void toAddressButtonClicked() throws IOException 
	{
		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/Address.fxml"));
		loader.setController(this);
		AnchorPane pane = loader.load();


		Scene scene = new Scene(pane);
		main.getStage().setScene(scene);
	}

	@FXML
	public void toAddressResultsClicked() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/AddressResults.fxml"));
		loader.setController(this);
		AnchorPane pane = loader.load();


		Scene scene = new Scene(pane);
		main.getStage().setScene(scene);
	}

	@FXML
	public void backFromSecondClicked() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/Main.fxml"));
		loader.setController(this);
		AnchorPane pane = loader.load();


		Scene scene = new Scene(pane);
		main.getStage().setScene(scene);
	}

	@FXML
	public void backFromZipResultsClicked() throws IOException
	{
		unCheckAll();

		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/ZipCode.fxml"));
		loader.setController(this);
		AnchorPane pane = loader.load();


		Scene scene = new Scene(pane);
		main.getStage().setScene(scene);
	}

	@FXML
	public void backFromAddressResultsClicked() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/Address.fxml"));
		loader.setController(this);
		AnchorPane pane = loader.load();


		Scene scene = new Scene(pane);
		main.getStage().setScene(scene);
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
			main.getDataController().getJunk(0).check();
			atLeastOne = true;
		}
		if(mcDonalds.isSelected())
		{
			main.getDataController().getJunk(1).check();
			atLeastOne = true;
		}
		if(pizzaHut.isSelected())
		{
			main.getDataController().getJunk(2).check();
			atLeastOne = true;
		}
		if(wendys.isSelected())
		{
			main.getDataController().getJunk(3).check();
			atLeastOne = true;
		}
		if(traderJoes.isSelected())
		{
			main.getDataController().getHealth(0).check();
			atLeastOne = true;
		}
		if(wholeFoods.isSelected())
		{
			main.getDataController().getHealth(1).check();
			atLeastOne = true;
		}

		return atLeastOne;
	}

	private void unCheckAll()
	{
		for(JunkFood r : main.getDataController().jFoods)
		{
			r.uncheck();
		}
		for(HealthFood r : main.getDataController().hFoods)
		{
			r.uncheck();
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}
