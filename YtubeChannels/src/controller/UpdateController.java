package controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import javax.swing.JFileChooser;

import dbConnections.DbManager;
import entities.Channel;
import entities.Video;
import main.Consts;
import netconnections.HttpCallManager;
import utils.XmlParser;

public class UpdateController extends Observable {

	private HttpCallManager httpCallManager;
	private DbManager dbManager;

	public UpdateController(HttpCallManager httpCallManager) {
		this.httpCallManager = httpCallManager;
		dbManager = new DbManager();
	}

	public void updateChannelsFromOpml() {

		File opmlFile = getFile();
		// C:\Users\Naim\Downloads\opml
		// File opmlFile = new
		// File("C:\\Users\\Naim\\Downloads\\opml\\subscription_manager");
		if (opmlFile == null) {
			setChanged();
			notifyObservers(Consts.OPML_NOT_FOUND);
			return;
		} else {
			ArrayList<Channel> channelList = XmlParser.getChannels(opmlFile);
			if (channelList == null) {
				setChanged();
				notifyObservers(Consts.CAN_NOT_PARSE_FILE);
				return;

			} else if (channelList.size() == 0) {
				setChanged();
				notifyObservers(Consts.NO_CHANNEL_IN_FILE);
				return;

			} else {
				ArrayList<Channel> newChannels = new ArrayList<>();

				for (Channel channel : channelList) {

					HashMap<String, Object> channelInfo = httpCallManager
							.getChannelInfo(null, channel.getYtbId());
					if (channelInfo == null) {
						setChanged();
						notifyObservers(Consts.NO_SUCH_CHANNEL + " :"
								+ channel.getTitle());
						continue;
					} else {
						try {
							String ytbChannelId = (String) channelInfo
									.get("ytbChannelId");
							if (dbManager.channelExists(ytbChannelId) == 0) {
								Channel channel1 = new Channel(
										(String) channelInfo
												.get("channelTitle"),
										(String) channelInfo
												.get("ytbChannelId"),
										(String) channelInfo.get("uploadsId"),
										(Integer.valueOf((String) channelInfo
												.get("videoCount"))));
								newChannels.add(channel1);

								// storedChannel = dbManager
								// .storeChannel(channel1);

								// setChanged();
								// notifyObservers(Consts.NEW_CHANNEL_ADDED
								// + "  title: "
								// + storedChannel.getTitle());

							} else {

								setChanged();
								notifyObservers(Consts.CHANNEL_EXISTS
										+ "  title: "
										+ (String) channelInfo
												.get("channelTitle"));
							}

						} catch (SQLException e) {
							e.printStackTrace();
							return;
						}
					}
				}

				if (newChannels.isEmpty()) {
					setChanged();
					notifyObservers(Consts.UPDATE_FINISHED_ALLCHANNEL_EXISTS);

				} else {
					setChanged();
					notifyObservers(newChannels);
				}

			}

		}
	}

	public void updateOrAddChannel(String userInput) {

		HashMap<String, Object> channelInfo = httpCallManager.getChannelInfo(
				userInput, null);
		if (channelInfo == null) {
			channelInfo = httpCallManager.getChannelInfo(null, userInput);
		}

		if (channelInfo == null) {
			setChanged();
			notifyObservers(Consts.NO_SUCH_CHANNEL);
			return;
		} else {

			try {

				String ytbChannelId = (String) channelInfo.get("ytbChannelId");
				int channelDbId = dbManager.channelExists(ytbChannelId);

				if (channelDbId == 0) {
					String titel = (String) channelInfo.get("channelTitle");
					String yid = (String) channelInfo.get("ytbChannelId");
					String uid = (String) channelInfo.get("uploadsId");
					int count = Integer.valueOf((String)channelInfo.get("videoCount")) ;

					Channel channel = new Channel(titel, yid, uid, count);
					Channel storedChannel = dbManager.storeChannel(channel);
					setChanged();
					notifyObservers(Consts.NEW_CHANNEL_ADDED + "  title: "
							+ storedChannel.getTitle());

					updateVideos(storedChannel.getDbId(),
							(String) channelInfo.get("uploadsId"));

				} else {

					updateVideos(channelDbId,
							(String) channelInfo.get("uploadsId"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private void updateVideos(int channeldbId, String uploadsId) {

		ArrayList<Video> videoList = httpCallManager.getVideoList(uploadsId);
		if (videoList != null) {
			for (Video video : videoList) {
				try {
					if (!dbManager.videoExists(video.getYtb_id())) {
						Video video1 = new Video(video.getYtb_id(),
								video.getTitle(), video.getDescription(),
								channeldbId);
						Video storedVideo = dbManager.storeVideo(video1);
						setChanged();
						notifyObservers(Consts.NEW_VIDEO_ADDED + " :"
								+ storedVideo.getTitle());
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			setChanged();
			notifyObservers(Consts.NO_VIDIO_FOUND_FOR_THIS_CHANNEL);
		}

	}

	/**
	 * displays a file chooser. get input and create a file
	 * 
	 * @return created file
	 */
	private File getFile() {

		String absolutePath = "";
		File file = null;

		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int choosen = chooser.showOpenDialog(null);
		if (choosen == JFileChooser.APPROVE_OPTION) {
			absolutePath = chooser.getSelectedFile().getAbsolutePath();
			file = new File(absolutePath);

			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	public void manageVideoUpdate(
			ArrayList<Channel> previewData) {			

		for (Channel channel : previewData) {
			
			 try {
				Channel storedChannel = dbManager.storeChannel(channel);
				setChanged();
				notifyObservers(Consts.NEW_CHANNEL_ADDED+ "   title :"+storedChannel.getTitle());
				updateVideos(storedChannel.getDbId(), storedChannel.getUploadId());
			} catch (SQLException e) {				
				e.printStackTrace();
			}			
		}		
	}
	
	public void deleteChannelFromDb(){
		
//		int[] toDeleteArray = new int[toDelete.size()];
//
//		for (int i = 0; i < toDelete.size(); i++) {
//			toDeleteArray[i] = toDelete.get(i).getDbId();
//		}
//		try {
//			dbManager.deleteChannels(toDeleteArray);
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put(Consts.INFO, Consts.DELETED_AFTER_IMPORT);
//			map.put(Consts.DATA, toDelete);
//			setChanged();
//			notifyObservers(map);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
	}
}
