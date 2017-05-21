package dbConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import entities.Channel;
import entities.Video;

public class DbManager {

	private static final String DB_DRIVER = "org.sqlite.JDBC";
	private static final String DB_CONNECTION = "jdbc:sqlite:resources\\youtubeChannels.sqlite";

	public static Connection getDBConnection() {

		Connection con = null;

		try {

			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {

			JOptionPane.showMessageDialog(null, e);
		}

		try {

			con = DriverManager.getConnection(DB_CONNECTION);

			return con;

		} catch (SQLException sqle) {

			JOptionPane.showMessageDialog(null, sqle);
		}

		return con;
	}

	public ArrayList<Channel> searchString(String expression)
			throws SQLException {

		ArrayList<Channel> channels = new ArrayList<Channel>();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT distinct channel.channelId, channel.title, "
				+ "channel.ytb_id, channel.uploadsId, channel.videoCount FROM channel "
				+ "INNER JOIN video "
				+ "ON channel.channelId = video.channelId "
				+ "WHERE video.title LIKE ? ;";
		
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setString(1, "%" + expression + "%");
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int channelId = rs.getInt(1);				
				String channelTitle = rs.getString(2);
				String ytb_id = rs.getString(3);
				String uploadsId = rs.getString(4);
				int videoCount = rs.getInt(1);
				channels.add(new Channel(channelId,channelTitle, ytb_id, uploadsId, videoCount));
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return channels;
	}

	public ArrayList<Video> getVideos(Channel channel,
			String actualExpression) throws SQLException {

		ArrayList<Video> videos = new ArrayList<Video>();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT * FROM video INNER JOIN channel "
				+ "ON channel.channelId = video.channelId "
				+ "WHERE channel.channelId = ? AND video.title LIKE ? ;";

		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, channel.getDbId());
			preparedStatement.setString(2, "%" + actualExpression + "%");

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				int videoId = rs.getInt(1);
				String ytb_id = rs.getString(2);
				String title = rs.getString(3);
				String description = rs.getString(4);
				int channelId = rs.getInt(5);

				videos.add(new Video(videoId, ytb_id, title, description,
						channelId));
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return videos;
	}

	public int channelExists(String channelId) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String selectSQL = "SELECT * FROM channel WHERE channel.ytb_Id = ? ;";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setString(1, channelId);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.isBeforeFirst()) {
				return 0;
			}else {
				
				int channelDbId = rs.getInt(1);
				return channelDbId;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return 0;
	}

	public Channel storeChannel(Channel channel) throws SQLException {

		int channel_Db_Id = 0;
		Connection dbConnection = null;
		Statement stmt = null;
		PreparedStatement preparedStatement = null;
		String insertSQL = "INSERT INTO channel (title, ytb_id, uploadsId, videoCount)"
				+ "VALUES (?,?,?,?);";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(insertSQL);
			String channelTitle = channel.getTitle().replaceAll("'", "''");
			preparedStatement.setString(1, channelTitle);
			preparedStatement.setString(2, channel.getYtbId());
			preparedStatement.setString(3, channel.getUploadId());
			preparedStatement.setInt(4, channel.getVideoCount());

			preparedStatement.executeUpdate();

			String selectSql = "SELECT last_insert_rowid();";
			stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(selectSql);
			channel_Db_Id = rs.getInt(1);

			return new Channel(channel_Db_Id, channel.getTitle(),
					channel.getYtbId(), channel.getUploadId(),
					channel.getVideoCount());

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
			return null;
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (stmt != null) {
				stmt.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

	}

	public boolean videoExists(String ytb_id) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String selectSQL = "SELECT * FROM video WHERE video.ytb_id = ? ;";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setString(1, ytb_id);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.isBeforeFirst()) {
				return false;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		return true;
	}

	public Video storeVideo(Video video) throws SQLException {

		int video_DbId = 0;
		Connection dbConnection = null;
		Statement stmt = null;
		PreparedStatement preparedStatement = null;
		String insertSQL = "INSERT INTO video (ytb_id, title, description, channelId)"
				+ "VALUES (?,?,?,?);";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(insertSQL);
			preparedStatement.setString(1, video.getYtb_id());
			preparedStatement.setString(2, video.getTitle());
			preparedStatement.setString(3, video.getDescription());
			preparedStatement.setInt(4, video.getChannelId());

			preparedStatement.executeUpdate();
			
			String selectSql = "SELECT last_insert_rowid();";
			stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(selectSql);
			video_DbId = rs.getInt(1);
			
			return new Video(video_DbId, video.getYtb_id(),video.getTitle(),video.getDescription(),video.getChannelId());

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
			return null;
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	public int getChannelDbId(Channel channel) throws SQLException {
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String selectSQL = "SELECT channelId FROM channel WHERE channel.ytb_id = ? ;";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			preparedStatement.setString(1, channel.getYtbId());
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.isBeforeFirst()) {
				return 0;
			} else {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

		return 0;
	}

	public void deleteChannels(int[] toDeleteArray) throws SQLException {
		
		Connection dbConnection = null;		
		PreparedStatement preparedStatement = null;
		
		String deleteSQLpart1 = "DELETE FROM channel WHERE channelId IN (" ;
		StringBuffer deleteSQLpart2 = new StringBuffer();
		
		if(toDeleteArray.length == 1){
			deleteSQLpart2.append(" ? );") ;
		}else {			
			for(int i = 0; i< toDeleteArray.length; i++){
				if(i<toDeleteArray.length -1){
					deleteSQLpart2.append("?,");
				}else {
					deleteSQLpart2.append("? );");
				}
			}			
		}				
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(deleteSQLpart1 + deleteSQLpart2);			
			for(int i = 0; i< toDeleteArray.length; i++){
				preparedStatement.setInt(i+1, toDeleteArray[i]);
			}	
			preparedStatement.executeUpdate();	

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
			
		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}
		deleteVideos(toDeleteArray);
	}

	private void deleteVideos(int[] toDeleteArray) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String deleteSQLpart1 = "DELETE FROM Video WHERE channelId IN (";
		StringBuffer deleteSQLpart2 = new StringBuffer();

		if (toDeleteArray.length == 1) {
			deleteSQLpart2.append(" ? );");
		} else {
			for (int i = 0; i < toDeleteArray.length; i++) {
				if (i < toDeleteArray.length - 1) {
					deleteSQLpart2.append("?,");
				} else {
					deleteSQLpart2.append("? );");
				}
			}
		}
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(deleteSQLpart1
					+ deleteSQLpart2);
			for (int i = 0; i < toDeleteArray.length; i++) {
				preparedStatement.setInt(i + 1, toDeleteArray[i]);
			}
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}
		}

	}	
}

/*
 * notes = notes .replace("!", "!!") .replace("%", "!%") .replace("_", "!_")
 * .replace("[", "!["); PreparedStatement pstmt = con.prepareStatement(
 * "SELECT * FROM analysis WHERE notes LIKE ? ESCAPE '!'"); pstmt.setString(1,
 * notes + "%");
 */
