import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;

public class HttpsClient
{

	public String SendRequest(String input)
	{
		String https_url = input;
		URL url;
		String output = "";
		try 
		{
			url = new URL(https_url);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			output = PrintContent(con);

		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return output;
	}

	public String PrintContent(HttpsURLConnection con)
	{
		String output = "";
		if(con!=null)
		{

			try 
			{
				BufferedReader br =new BufferedReader(new InputStreamReader(con.getInputStream()));

				String input;
				while ((input = br.readLine()) != null)
				{
					output = output + input;
				}
				br.close();

			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return output;
	}
}
