import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainDisplay extends Application {

	private Stage primaryStage;
	private DataController dataController = new DataController();
	
	@Override
    public void start(Stage primaryStage) throws Exception 
	{
		this.primaryStage = primaryStage;
		
		mainWindow();
    }
	
	//loads the first screen [Main] and starts the Controller for the GUI
	private void mainWindow()
	{
		try {
			FXMLLoader loader = new FXMLLoader(MainDisplay.class.getResource("Screens/Main.fxml"));
			loader.setController(new Controller());
			AnchorPane pane = loader.load();
			
			Controller controller = loader.getController();
			controller.setMain(this);
			
			Scene scene = new Scene(pane);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Food Finder");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//getters for the Data Controller and the Stage
	public DataController getDataController()
	{return dataController;}
	
	public Stage getStage()
	{return primaryStage;}

	
    public static void main(String[] args) throws IOException {
    	launch(args);

    	
    }
    
}
