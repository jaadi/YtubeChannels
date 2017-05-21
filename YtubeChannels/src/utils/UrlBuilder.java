package utils;

import main.Consts;

public  class UrlBuilder {	

	public static String getChannelInfoRequestUrl(String forUserName, String channel_id, String apikey) {

		StringBuilder sb = new StringBuilder();
		sb.append(Consts.URL_MAIN_PART);
		sb.append(Consts.CHANNEL_PART);
		if (forUserName != null && channel_id == null) {
			sb.append(Consts.FORUSERNAME_PART);
			sb.append(forUserName);
		} else if (forUserName == null && channel_id != null) {
			sb.append(Consts.ID_PART);
			sb.append(channel_id);
		}
		
		String key = Consts.API_KEY_PART.replace("{API_KEY}", apikey);
		sb.append(key);		

		return sb.toString();
	}
		
	public static String buildPlaylistItemsUrl(String uploadsId, String nextPageToken, String apiKey ) {	

		StringBuilder sb = new StringBuilder();
		sb.append(Consts.URL_MAIN_PART);
		sb.append(Consts.PLAYLIST_ITEMS_PART);
		if (nextPageToken != null && !nextPageToken.isEmpty()) {
			sb.append(Consts.NEXT_PAGE_TOCKEN_PART);
			sb.append(nextPageToken);
		}
		sb.append(Consts.PLAYLIST_ID_PART);
		sb.append(uploadsId);
		String key = Consts.API_KEY_PART.replace("{API_KEY}", apiKey);
		sb.append(key);
		
		return sb.toString();
	}
	
	public static String buildVideoCallUrl(String videoId){		
		return Consts.VIDEO_CALL_URL +videoId;
	}
//	public static String getUploadsInfoRequestUrl(String upLoadsId,String apikey) {
//
//		StringBuilder sb = new StringBuilder();
//		sb.append(Consts.URL_MAIN_PART);
//		sb.append(Consts.CHANNEL_SNIPPET_PART);
//		if (channel_User_Name != null && channel_id == null) {
//			sb.append(Consts.FORUSERNAME_PART);
//			sb.append(channel_User_Name);
//		} else if (channel_User_Name == null && channel_id != null) {
//			sb.append(Consts.ID_PART);
//			sb.append(channel_id);
//		}
//		
//		String key = Consts.API_KEY_PART.replace("{API_KEY}", apikey);
//		sb.append(key);		
//
//		return sb.toString();
//	}
}
