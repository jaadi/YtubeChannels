package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entities.Channel;
import entities.Video;
import main.Consts;
import model.Model;
import netconnections.HttpCallManager;
import utils.UrlBuilder;
import views.View;

public class Controller implements ActionListener, ListSelectionListener,
		MouseListener {

	private View view;
	private Model model;
	private UpdateController updateController;
	private HttpCallManager httpCallManager;

	private String actualExpression = "";

	public Controller(View view) {

		this.view = view;
		view.addListeners(this);

		httpCallManager = new HttpCallManager();

		this.model = new Model();
		this.model.addObserver(this.view);

		this.updateController = new UpdateController(httpCallManager);
		this.updateController.addObserver(this.view);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (!e.getValueIsAdjusting()) {

			if (e.getSource() instanceof JList) {

			} else if (e.getSource() instanceof JTable) {

				

			} else if (e.getSource() == view.getVideoTable()
					.getSelectionModel()) {

				int selectedRow = view.getVideoTable().getSelectedRow();
				
				if (selectedRow >= 0) {
					Video video = view.getVideoList().get(selectedRow);
					String url = UrlBuilder
							.buildVideoCallUrl(video.getYtb_id());
					HttpCallManager.openWebpage(url);
				}else{
					
					
					return;
				}

			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(Consts.SEARCH)) {

			if (!view.getSearchField().getText().trim().isEmpty()) {

				String expression = view.getSearchField().getText().trim();
				this.actualExpression = expression;
				model.searchString(expression);
			}

		} else if (e.getActionCommand().equals(Consts.UPDATE_CHANNEL)) {

			JFrame frame = new JFrame("InputDialog");
			String channelUserName = JOptionPane.showInputDialog(frame,
					"Enter the channel id", "update or add a channel",
					JOptionPane.QUESTION_MESSAGE);

			if (channelUserName != null && !channelUserName.trim().isEmpty()) {

				updateController.updateOrAddChannel(channelUserName);
			}

		} else if (e.getActionCommand().equals(Consts.UPDATE_DB)) {

			updateController.updateChannelsFromOpml();

		} else if (e.getActionCommand().equals(Consts.CLEAR)) {

			view.clearView();

		} else if (e.getActionCommand().equals(Consts.CLEAR_LOG)) {

			view.clearLog();

		} else if (e.getActionCommand().equals(Consts.PREVIEW_UPDATE)) {

			ArrayList<Channel> previewData = view.getPreviewData();
			updateController.manageVideoUpdate(previewData);
		} else if (e.getActionCommand().equals(Consts.DESELECT_ALL)) {
			
			int rowCount = view.getPreview().getMyTableModel().getRowCount();
			
			for(int i = 0; i < rowCount; i++){
				
				view.getPreview().getMyTableModel().setValueAt(false, i, 2);
				view.getPreview().getMyTableModel().setValueAt(false, i, 3);
				
			}
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.getSource().getClass() == JList.class) {
			@SuppressWarnings("unchecked")
			JList<String> theList = (JList<String>) mouseEvent.getSource();
			if (mouseEvent.getClickCount() == 1) {
				int index = theList.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) {
					theList.getModel().getElementAt(index);
					// String channelTitle = (String) o;
					Channel channel = view.getChannelObjectsList().get(index);
					model.getVideos(channel, actualExpression);
				}
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
