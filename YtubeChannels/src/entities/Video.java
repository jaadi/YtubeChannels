package entities;

public class Video {

	private int id;
	private String ytb_id;
	private String title;
	private  String description;
	private int channelId;	

	public Video() {

	}

	public Video(int id, String ytb_id, String title, String description,
			int channelId) {
		super();
		this.id = id;
		this.ytb_id = ytb_id;
		this.title = title;
		this.description = description;
		this.channelId = channelId;
	}
	public Video(String ytb_id, String title, String description,
			int channelId) {
		super();		
		this.ytb_id = ytb_id;
		this.title = title;
		this.description = description;
		this.channelId = channelId;
	}
	
	public Video (String ytb_id, String title, String description){
		this.ytb_id = ytb_id;
		this.title = title;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYtb_id() {
		return ytb_id;
	}

	public void setYtb_id(String ytb_id) {
		this.ytb_id = ytb_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", ytb_id=" + ytb_id + ", title=" + title
				+ ", description=" + description + ", channelId=" + channelId
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + channelId;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((ytb_id == null) ? 0 : ytb_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		if (channelId != other.channelId)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (ytb_id == null) {
			if (other.ytb_id != null)
				return false;
		} else if (!ytb_id.equals(other.ytb_id))
			return false;
		return true;
	}
	
	

	

}
