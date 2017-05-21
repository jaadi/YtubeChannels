package utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

import entities.Video;

public class JsonParser {

//	public static Channel getChannel(String jsonString) {
//
//		JSONObject jsonObj = null;
//		JSONParser parser = new JSONParser();
//
//		try {
//			jsonObj = (JSONObject) parser.parse(jsonString);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//		JSONArray items = (JSONArray) jsonObj.get("items");
//
//		if (!items.isEmpty()) {
//			JSONObject item = (JSONObject) items.get(0);
//			String ytb_id = (String) item.get("id");
//			JSONObject contentDetails = (JSONObject) item.get("contentDetails");
//
//			JSONObject relatedPlaylists = (JSONObject) contentDetails
//					.get("relatedPlaylists");
//
//			String uploadsId = (String) relatedPlaylists.get("uploads");
//
//			return new Channel(0, "", ytb_id, uploadsId,0);
//		}
//		return null;
//	}
	
	public static String getUploadsId(String jsonString) {

		JSONObject jsonObj = null;
		JSONParser parser = new JSONParser();

		try {
			jsonObj = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		JSONArray items = (JSONArray) jsonObj.get("items");

		if (!items.isEmpty()) {
			JSONObject item = (JSONObject) items.get(0);
			JSONObject contentDetails = (JSONObject) item.get("contentDetails");

			JSONObject relatedPlaylists = (JSONObject) contentDetails
					.get("relatedPlaylists");

			String uploadsId = (String) relatedPlaylists.get("uploads");

			return uploadsId;
		}
		return null;

	}

	public static HashMap<String, Object> getVideoList(String jsonString) {

		HashMap<String, Object> data = new HashMap<>();
		ArrayList<Video> videos = new ArrayList<>();
		JSONObject jsonObj = null;

		JSONParser parser = new JSONParser();

		try {
			jsonObj = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		String nextPageToken = (String) jsonObj.get("nextPageToken");
		data.put("nextPageToken", nextPageToken);

		JSONArray items = (JSONArray) jsonObj.get("items");

		for (int i = 0; i < items.size(); i++) {
			// get item from array
			JSONObject item = (JSONObject) items.get(i);

			// get snippet
			JSONObject snippet = (JSONObject) item.get("snippet");
			// get title
			String title = ((String) snippet.get("title"))
					.replaceAll("'", "''");
			// get description
			String description = ((String) snippet.get("description"))
					.replaceAll("'", "''");
			// get video id
			JSONObject resourceId = (JSONObject) snippet.get("resourceId");
			String ytbVideoId = (String) resourceId.get("videoId");			

			videos.add(new Video(ytbVideoId, title, description));
		}

		data.put("videos", videos);

		return data;
	}

	public static HashMap<String, Object> getChannelInfo(String jsonString){
		
		JSONObject jsonObj = null;
		JSONParser parser = new JSONParser();
		HashMap<String,Object> channelInfo = new HashMap<>();
		try {
			jsonObj = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		JSONArray items = (JSONArray) jsonObj.get("items");

		if (!items.isEmpty()) {
			
			JSONObject item = (JSONObject) items.get(0);
			
			Object ytbChannelId = item.get("id");
			channelInfo.put("ytbChannelId", ytbChannelId);
			
			JSONObject snippet = (JSONObject) item.get("snippet");
			Object channelTitle = snippet.get("title");
			channelInfo.put("channelTitle", channelTitle);
			
			JSONObject contentDetails = (JSONObject) item.get("contentDetails");
			JSONObject relatedPlaylists = (JSONObject)contentDetails.get("relatedPlaylists");
			Object uploadsId = relatedPlaylists.get("uploads");
			channelInfo.put("uploadsId", uploadsId);
			
			JSONObject statistics = (JSONObject) item.get("statistics");
			Object videoCount = statistics.get("videoCount");
			channelInfo.put("videoCount", videoCount);			

			return channelInfo;
		}
		return null;		
	}
	public static Long getVideoNumber(String jsonString) {

		JSONObject jsonObj = null;
		JSONParser parser = new JSONParser();
		String []channelInfo = new String [2];
		try {
			jsonObj = (JSONObject) parser.parse(jsonString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		JSONObject pageInfo = (JSONObject) jsonObj.get("pageInfo");
		Long videosNumber = (Long) pageInfo.get("totalResults");
		
		return videosNumber;
	}

//	public static String videosNumber(String jsonString) {
//
//		JSONObject jsonObj = null;
//		JSONParser parser = new JSONParser();
//
//		try {
//			jsonObj = (JSONObject) parser.parse(jsonString);
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		JSONObject pageInfo = (JSONObject) jsonObj.get("pageInfo");
//		Long lVideosNumber = (Long) pageInfo.get("totalResults");
//		String videosNumber = String.valueOf(lVideosNumber);
//		return videosNumber;
//	}
}
