package main;

public final class Consts {
	
	public static final String PROPERTIES_FILENAME = "youtube.properties";
	public static final String OPML_FILENAME = "resources/subscription_manager";
	public static final String URL_MAIN_PART = "https://www.googleapis.com/youtube/v3/";
	public static final String CHANNEL_PART = "channels?part=id%2Csnippet%2CcontentDetails%2Cstatistics";	
	public static final String PLAYLIST_ITEMS_PART = "playlistItems?part=snippet&maxResults=50";
	public static final String PLAYLIST_ID_PART = "&playlistId=";
	public static final String FORUSERNAME_PART = "&forUsername=";
	public static final String ID_PART = "&id=";
	public static final String API_KEY_PART = "&key={API_KEY}";
	public static final String NEXT_PAGE_TOCKEN_PART = "&pageToken=";
	public static final String SEARCH = "search words";
	public static final String CLEAR = "clear";
	public static final String CHANNEL = "channel";	
	public static final String CHANNEL_COUNT = "> Channel found.";
	public static final String CHANNELS_TO_DELETE = "> Select the channels that you want to delete from database.";
	public static final String VIDEOS_TO_IMPORT = "> Select the channels to import the videos.";
	public static final String VIDEO = "VIDEO";	
	public static final String IMPORT_VIDEOS = "import videos";
	public static final String DELETE_CHANNEL = "delete channel from databse";
	public static final String NOSUCHVIDEO = "no video found for this expression !";
	public static final String TYPE = "type";
	public static final String DATA = "data";
	public static final String MENU = "Menu";
	public static final String UPDATE_CHANNEL = "update or add a channel";
	public static final String UPDATE_DB = "update database using opml file";
	public static final String NO_SUCH_CHANNEL = "no Channel found.";
	public static final String NEW_CHANNEL_ADDED = "a new channel ist added to the database.";
	public static final String NEW_VIDEO_ADDED = "a new video ist added to the database.";
	public static final String SEARCH_EXPRESSION_EXAMPLE = "search example : java + (swing + awt) >>  "
			+ "that means : java swing or java awt.";
	public static final String PARSER_ERROR = "file not found or error during parsing.";
	public static final String OPML_NOT_FOUND = "file can not be found.";
	public static final String CAN_NOT_PARSE_FILE = "file can not be parsed.";
	public static final String NO_CHANNEL_IN_FILE = "the file was parsed but no channel found.";
	public static final String YOUTUBE_API_KEY_NOT_FOUND = "Youtube Api Key can not be found.";
	public static final String VIDEO_LIST_UPDATED = "the video list was updated";
	public static final String NO_VIDIO_FOUND_FOR_THIS_CHANNEL = "no video found for this channel";
	public static final String VIDEOCOUNT = "videos count";
	public static final String PREVIEW_UPDATE = "update database";
	public static final String TOSTORE = "Store";
	public static final String TODELETE = "Delete";
	public static final String DELETED_AFTER_IMPORT = "deleted after import";
	public static final String INFO = "mapInfo";
	public static final String CLEAR_LOG = "clear log";
	public static final String VIDEO_CALL_URL = "https://www.youtube.com/watch?v=";
	public static final String CHANNEL_EXISTS = "channel already exists";
	public static final String UPDATE_FINISHED_ALLCHANNEL_EXISTS = "Update fished all Channels already exists";
	public static final String DESELECT_ALL = "deselect All";
		
	private Consts() {
		// this prevents even the native class from
		// calling this ctor as well :
		
	}
	
	

}
