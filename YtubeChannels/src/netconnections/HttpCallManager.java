package netconnections;

import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import main.Consts;
import utils.*;

import entities.Video;

public class HttpCallManager {

	private String apiKey = "";
	private String nextPageToken = null;
	private boolean ready = false;

	public HttpCallManager() {
		SetApiKey();
	}

	// read google Api key from properties file
	private void SetApiKey() {
		Properties properties = new Properties();
		try {
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream(Consts.PROPERTIES_FILENAME);
			properties.load(in);

		} catch (Exception e) {
			System.err.println("There was an error reading "
					+ Consts.PROPERTIES_FILENAME + ": " + e.getCause() + " : "
					+ e.getMessage());
			e.printStackTrace();
		}

		this.apiKey = properties.getProperty("youtube.apikey");

		setReady(true);
	}

	/*
	 * take any kind of url as string, makes http call and return json Object as
	 * string
	 */

	public String makeCall(String s_url) {

		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(s_url);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), "UTF8");
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:" + s_url,
					e);
		}

		String request_result = sb.toString();

		return request_result;

	}

	/*
	 * use constant, argument and apikey to build an Url(for id)
	 * channel_User_Name can be retrieved from browser channel url channel_id ca
	 * be retrieved from opml file (to get this file export all your abo
	 * channels) from this request url you can get the uploads id and use it to
	 * get the videos
	 */
	
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static void openWebpage(String urlString) {
	    try {
	    	URL url = new URL(urlString);
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }catch(MalformedURLException mue){
	    	mue.printStackTrace();
	    }
	}
	
	public HashMap<String, Object> getChannelInfo(String forUserName, String channelId) {		
		
		String uRequestResult = "";
		HashMap<String, Object> channelInfo = null;
		
		if (forUserName == null && channelId != null) {			
			String channelInfoRequestUrl = UrlBuilder.getChannelInfoRequestUrl(null, channelId, apiKey);
			uRequestResult = makeCall(channelInfoRequestUrl);
			
		} else if (channelId == null && forUserName != null) {			
			String channelInfoRequestUrl = UrlBuilder.getChannelInfoRequestUrl(forUserName, null, apiKey);
			uRequestResult = makeCall(channelInfoRequestUrl);
		}
		
		if(uRequestResult != null && !uRequestResult.isEmpty()){
			channelInfo = JsonParser.getChannelInfo(uRequestResult);
			
			
		}


		return channelInfo;
	}
	
	public ArrayList<Video> getVideoList(String uploadsId) {

		ArrayList<Video> videoList = new ArrayList<>();

		do {
			String s_url = UrlBuilder.buildPlaylistItemsUrl(uploadsId,
					nextPageToken, apiKey);
			String request_result = makeCall(s_url);

			if (request_result != null && !request_result.isEmpty()) {

				HashMap<String, Object> data = JsonParser
						.getVideoList(request_result);

				nextPageToken = (String) data.get("nextPageToken");

				@SuppressWarnings("unchecked")
				ArrayList<Video> newList = (ArrayList<Video>) data
						.get("videos");
				for (Video video : newList) {
					videoList.add(video);
				}
			}

		} while (nextPageToken != null);

		nextPageToken = null;

		return videoList;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}
