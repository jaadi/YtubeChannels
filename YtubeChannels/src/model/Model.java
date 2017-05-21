package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import dbConnections.DbManager;
import entities.Channel;
import entities.Video;
import main.Consts;

public class Model extends Observable {
	//private HttpCallManager httpCallManager = null;
	private DbManager dbManager;

	public Model() {
		//this.httpCallManager = httpCallManager;
		dbManager = new DbManager();
	}

	public void searchString(String expression) {

		try {
			ArrayList<Channel> searchResult = dbManager
					.searchString(expression);
			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put(Consts.TYPE, Consts.CHANNEL);

			if (searchResult.isEmpty()) {
				resultMap.put(Consts.DATA, Consts.NOSUCHVIDEO);
			} else {
				resultMap.put(Consts.DATA, searchResult);
			}

			setChanged();
			notifyObservers(resultMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getVideos(Channel channel, String actualExpression) {
		try {
			ArrayList<Video> videos = dbManager.getVideos(channel,
					actualExpression);
			HashMap<String, Object> resultMap = new HashMap<>();
			resultMap.put(Consts.TYPE, Consts.VIDEO);

			if (videos.isEmpty()) {
				resultMap.put(Consts.DATA, Consts.NOSUCHVIDEO);
			} else {
				resultMap.put(Consts.DATA, videos);
			}
			setChanged();
			notifyObservers(resultMap);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// }
	// public ArrayList<Video> getChannelVideoList(String channelTitle) {
	//
	// String channelId = "";
	//
	// if (xmlParser.isReady()) {
	// ArrayList<Channel> channeList = xmlParser.getChanneList();
	// for (Channel channel : channeList) {
	// if (channel.getTitle().equals(channelTitle)) {
	// channelId = channel.getYtbId();
	// break;
	// }
	// }
	//
	// } else {
	//
	// error = xmlParser.getError();
	// return null;
	// }
	//
	// ArrayList<Video> videoList = getVideoList(channelId);
	//
	// return videoList;
	//
	// }

	// !!!!! Update db method todo adjust
	// public ArrayList<HashMap<String, Object>> getAllVideolists() {
	//
	// ArrayList<HashMap<String, Object>> allvideos = new ArrayList<>();
	//
	// if (xmlParser.isReady()) {
	//
	// ArrayList<Channel> channeList = xmlParser.getChanneList();
	//
	// for (Channel channel : channeList) {
	//
	// HashMap<String, Object> channelVideos = new HashMap<>();
	//
	// String channelId = channel.getYtbId();
	// String title = channel.getTitle();
	//
	// ArrayList<Video> videoList = getVideoList(channelId);
	// channelVideos.put("Chanel title", title);
	// channelVideos.put("video List", videoList);
	//
	// allvideos.add(channelVideos);
	//
	// }
	// } else {
	//
	// error = xmlParser.getError();
	// return null;
	// }
	//
	// return allvideos;
	// }

	// !!!!! Update db method todo adjust
	// private ArrayList<Video> getVideoList(String channelId) {
	//
	// if (channelId.isEmpty() || channelId == null) {
	// return null;
	// }
	//
	// String uploadsId = httpCallManager.getUploadsId(null, channelId);
	//
	// if (uploadsId == null || uploadsId.isEmpty()) {
	// return null;
	// }
	//
	// ArrayList<Video> videoList = null;
	// videoList = httpCallManager.getVideoList(uploadsId);
	//
	// if (videoList == null) {
	// return null;
	// }
	//
	// return videoList;
	//
	// }

	// public void updateDataBaseUsingXmlFile() {
	//
	// // update channel table
	// if (xmlParser.isReady()) {
	//
	// ArrayList<Channel> channeList = xmlParser.getChanneList();
	//
	// for (Channel channel : channeList) {
	//
	// int channel_Id = dbManager.storeChannel(channel);
	// if (channel_Id != 0) {
	// ArrayList<Video> videoList = getVideoList(channel
	// .getYtbId());
	//
	// if (videoList != null) {
	//
	// for (Video video : videoList) {
	// dbManager.storeVideo(video, channel_Id);
	// }
	//
	// } else {
	//
	// System.out.println("video list is null bei "
	// + channel.getTitle());
	// continue;
	// }
	//
	// } else {
	//
	// System.out.println("STORECHANEL GIVE 0 back !! "
	// + channel.getTitle());
	// continue;
	// }
	//
	// }
	//
	// } else {
	// error = xmlParser.getError();
	//
	// }
	// }

}
