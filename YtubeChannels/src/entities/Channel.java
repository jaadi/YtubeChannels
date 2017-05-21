package entities;

public class Channel {

	private int dbId;
	private String title;
	private String ytbId;
	private String uploadId;
	private int videoCount;	

	public Channel(int dbId, String title, String ytbId, String uploadId, int videoCount) {

		this.dbId = dbId;
		this.title = title;
		this.ytbId = ytbId;
		this.uploadId = uploadId;
		this.videoCount = videoCount;		
	}	
	
	public Channel(String title, String ytbId, String uploadId, int videoCount) {
		
		this.title = title;
		this.ytbId = ytbId;
		this.uploadId = uploadId;
		this.videoCount = videoCount;		
	}
	
	public Channel(String title, String ytbId ){		
		this.title = title;
		this.ytbId = ytbId;
	}

	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYtbId() {
		return ytbId;
	}

	public void setYtbId(String ytbId) {
		this.ytbId = ytbId;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public int getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(int videoCount) {
		this.videoCount = videoCount;
	}

	@Override
	public String toString() {
		return "Channel [dbId=" + dbId + ", title=" + title + ", ytbId="
				+ ytbId + ", uploadId=" + uploadId + ", videoCount="
				+ videoCount + "]";
	}	
}
