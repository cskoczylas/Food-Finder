import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;

//HttpsClient sends and receives data from the internet
//Used to get data from the Google Maps API
public class HttpsClient{

	//Returns a string object of a json output from the Google Maps API
	public String SendRequest(String https_url)
	{
		String output = "";
		try {
			URL url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			output = PrintContent(con);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	public String PrintContent(HttpsURLConnection con)
	{
		String output = "";
		if(con!=null)
		{
			try {
				BufferedReader br =new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				
				while ((input = br.readLine()) != null)
				{output = output + input;}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
}
