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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller implements Initializable{

	@FXML
	private Button toZip, toAddress, back, zipGo, addressGo, errorOK;
	@FXML
	private ImageView logoImage;
	@FXML
	private CheckBox mcDonalds, burgerKing, wendys, pizzaHut, traderJoes, wholeFoods;
	@FXML
	private TextField zipBox, streetBox, cityBox, stateBox;
	@FXML
	private Label zipBoxDesc, streetBoxDesc, cityBoxDesc, stateBoxDesc, hoverDesc;
	@FXML
	private ListView<String> zipResults = new ListView<String>();
	@FXML
	private ListView<String> addressResults = new ListView<String>();


	private MainDisplay main;

	public void setMain(MainDisplay main)
	{this.main = main;}
	
	@FXML
	public void toZipButtonClicked() throws IOException 
	{
		changeScreen("Screens/ZipCode.fxml");
		createToolTips();
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
						loc = r.getName() + "@\n" + loc;
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
						loc = r.getName() + "@\n" + loc;
						data.add(loc);
					}
				}
			}

			changeScreen("Screens/ZipCodeResults.fxml");
			zipResults.setItems(data);
		}
		else if(!isChecked())
		{createError("Screens/CheckBoxError.fxml");}
		else
		{createError("Screens/ZipError.fxml");}
	}

	@FXML
	public void toAddressButtonClicked() throws IOException 
	{
		changeScreen("Screens/Address.fxml");
		createToolTips();
	}

	@FXML
	public void toAddressResultsClicked() throws IOException, JSONException
	{
		ArrayList<String> results = new ArrayList<>();
		ObservableList<String> data = FXCollections.observableArrayList();

		if(addressIsFilled() && isChecked())
		{
	    	double km = 0.621371;
	    	double lat = (25 * km) / 110.54;
	    	double lng = (25 * km) / (111.320 * Math.cos(lat * Math.PI / 180));
			
			String[] inputAddress = new String[3];
			inputAddress[0] = streetBox.getText().replace(" ", "+");
			inputAddress[1] = cityBox.getText().replace(" ", "+");
			inputAddress[2] = stateBox.getText().replace(" ", "+");
			//bounds are [NE lat, NE long, SW lat, SW long]
			
			double[] bounds = main.getDataController().addressToLatLongBounds(inputAddress);

			bounds[0] += lat;
			bounds[1] += lng;
			bounds[2] += (-1 * lat);
			bounds[3] += (-1 * lng);
			
			for(HealthFood r : main.getDataController().hFoods)
			{
				if(r.isChecked())
				{
					results = r.searchByAddress(bounds, inputAddress[2], main.getDataController());
					for(String loc : results)
					{
						loc = r.getName() + "@\n" + loc;
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
						loc = r.getName() + "@\n" + loc;
						data.add(loc);
					}
				}
			}
			
			changeScreen("Screens/AddressResults.fxml");
			addressResults.setItems(data);
		}
		else if (addressIsFilled() && !isChecked())
		{createError("Screens/CheckBoxError.fxml");}
		else
		{createError("Screens/AddressError.fxml");}

	}

	@FXML
	public void backFromSecondClicked() throws IOException
	{changeScreen("Screens/Main.fxml");}

	@FXML
	public void backFromZipResultsClicked() throws IOException
	{
		unCheckAll();
		clearList();

		changeScreen("Screens/ZipCode.fxml");
	}

	@FXML
	public void backFromAddressResultsClicked() throws IOException
	{
		unCheckAll();
		clearList();
		
		changeScreen("Screens/Address.fxml");
	}

	@FXML
	public void okErrorClicked() throws IOException
	{
		Stage stage = (Stage) errorOK.getScene().getWindow();
		stage.close();
	}

	private boolean isZip(String zip)
	{
		String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(zip);
		return matcher.matches();
	}
	
	private boolean addressIsFilled() throws IOException
	{
		if(streetBox.getText().isEmpty() || cityBox.getText().isEmpty() || stateBox.getText().isEmpty())
		{return false;}
		else
		{return true;}
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
		{r.uncheck();}
		for(HealthFood r : main.getDataController().hFoods)
		{r.uncheck();}
	}
	
	private void clearList()
	{
		ListView<String> temp = new ListView<String>();
		zipResults = temp;
		addressResults = temp;		
	}
	
	private void changeScreen(String location) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource(location));
		loader.setController(this);
		AnchorPane pane = loader.load();

		Scene scene = new Scene(pane);
		main.getStage().setScene(scene);
	}
	
	private void createError(String location) throws IOException
	{
		FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource(location));
		loader.setController(this);
		AnchorPane pane = loader.load();

		Scene scene = new Scene(pane);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}
	
	private void createToolTips()
	{
		String[] locationNames = new String[] {"bk", "mcd", "pz", "tj", "wf", "wnd"};
		
		for(String name : locationNames)
		{
			Tooltip ttip = new Tooltip();
			Image img = new Image("Screens/" + name + ".png");
			ImageView logo = new ImageView(img);
			ttip.setGraphic(logo);
			
			if(name.equals("bk"))
			{burgerKing.setTooltip(ttip);}
			else if(name.equals("mcd"))
			{mcDonalds.setTooltip(ttip);}
			else if(name.equals("pz"))
			{pizzaHut.setTooltip(ttip);}
			else if(name.equals("tj"))
			{traderJoes.setTooltip(ttip);}
			else if(name.equals("wf"))
			{wholeFoods.setTooltip(ttip);}
			else if(name.equals("wnd"))
			{wendys.setTooltip(ttip);}
		}
		
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
}
