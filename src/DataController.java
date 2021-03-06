import org.json.JSONException;
import org.json.JSONObject;

public class DataController {

	// the addresses are currently organized by states
	// array order: bugerking, mcdonalds, pizzahut, wendys
	JunkFood[] jFoods;
	// array order: traderjoes, wholefoods
	HealthFood[] hFoods;

	private static String APIkey = "AIzaSyCUwb8Nn9rm4eAAfiZFHED5bhC2_s8ZiB8";
	private HttpsClient client = new HttpsClient();
	private static String[] templateURL = new String[6];

	//the Data Controller constructor creates two arrays that holds all the junk food and health food restaurant objects
	DataController()
	{
		jFoods = new JunkFood[4];
		hFoods = new HealthFood[2];

		jFoods[0] = new JunkFood("src/Data/burgerking.xlsx");
		jFoods[1] = new JunkFood("src/Data/mcdonalds.xlsx");
		jFoods[2] = new JunkFood("src/Data/pizzahut.xlsx");
		jFoods[3] = new JunkFood("src/Data/wendys.xlsx");

		hFoods[0] = new HealthFood("src/Data/traderjoes.xlsx");
		hFoods[1] = new HealthFood("src/Data/wholefoods.xlsx");

		templateURL[0] = "https://maps.googleapis.com/maps/api/geocode/"; //beginning of the URL
		templateURL[1] = "json"; //output format, we'll be using JSON but it can also be XML
		templateURL[2] = "?address=";
		templateURL[3] = "?latlng="; 
		templateURL[4] = "&key=";
		templateURL[5] = APIkey; //put the API key here
	}

	public JunkFood getJunk(int location)
	{return jFoods[location];}

	public HealthFood getHealth(int location)
	{return hFoods[location];}
	
	//this method takes in a zip code and geocodes it into lat and long bounds using the Google Maps API
	public double[] zipToLatLongBounds(String zipCode) throws JSONException
	{
		//bounds are [NE lat, NE long, SW lat, SW long]
		double[] latLongBounds = new double[4];
		
		String json = client.SendRequest(templateURL[0] + templateURL[1] + templateURL[2] + zipCode + templateURL[4] + templateURL[5]);
		JSONObject obj = new JSONObject(json);
		JSONObject bounds = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("bounds");
		
		latLongBounds[0] = bounds.getJSONObject("northeast").getDouble("lat");
		latLongBounds[1] = bounds.getJSONObject("northeast").getDouble("lng");
		latLongBounds[2] = bounds.getJSONObject("southwest").getDouble("lat");
		latLongBounds[3] = bounds.getJSONObject("southwest").getDouble("lng");

		return latLongBounds;
	}
	
	//this methods takes in address and geocodes it into lat and long bounds using the Google Maps API
	public double[] addressToLatLongBounds(String[] inputAddress) throws JSONException
	{
		//inputAddress are [street, city, state]
		double[] latLongBounds = new double[4];
		
		String json = client.SendRequest(templateURL[0] + templateURL[1] + templateURL[2] + inputAddress[0] + "," + inputAddress[1] + "," + inputAddress[2] + templateURL[4] + templateURL[5]);
		JSONObject obj = new JSONObject(json);
		JSONObject bounds = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("bounds");
		
		latLongBounds[0] = bounds.getJSONObject("northeast").getDouble("lat");
		latLongBounds[1] = bounds.getJSONObject("northeast").getDouble("lng");
		latLongBounds[2] = bounds.getJSONObject("southwest").getDouble("lat");
		latLongBounds[3] = bounds.getJSONObject("southwest").getDouble("lng");

		return latLongBounds;
	}
	
	//this method takes in an address and gets the lat and long of the address using the Google Maps API
	public double[] getLatLong(String[] address) throws JSONException
	{
		double[] latLong = new double[2];
		String json = client.SendRequest(templateURL[0] + templateURL[1] + templateURL[2] + address[0] + "," + address[1] + "," + address[2] + templateURL[4] + templateURL[5]);
		
		JSONObject obj = new JSONObject(json);
		JSONObject bounds = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
		
		latLong[0] = bounds.getDouble("lat");
		latLong[1] = bounds.getDouble("lng");

		return latLong;
	}

}
