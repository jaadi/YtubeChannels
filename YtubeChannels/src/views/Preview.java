package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import entities.Channel;
import main.Consts;

public class Preview extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable channelTable;
	private MyTableModel myTableModel;
	private JButton okButton;
	private JButton deselectAll;
	private ArrayList<Object> data;

	public Preview(ArrayList<Object> data) {

		this.data = data;

		this.setLocation(600, 70);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setTitle("Channels");

		createAndAddComponents();
		this.pack();
		this.setVisible(true);
	}

	private void createAndAddComponents() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		labelPanel.setBorder(new EmptyBorder(5, 5, 15, 5));

		JLabel channelCountLabel = new JLabel(Consts.CHANNEL_COUNT);
		JLabel importVideosLabel = new JLabel(Consts.VIDEOS_TO_IMPORT);
		JLabel DeleteChannelLabel = new JLabel(Consts.CHANNELS_TO_DELETE);

		Font font = new Font("Courier", Font.PLAIN, 14);

		channelCountLabel.setFont(font);
		channelCountLabel.setBorder(new EmptyBorder(3, 3, 3, 3));

		importVideosLabel.setFont(font);
		importVideosLabel.setBorder(new EmptyBorder(3, 3, 3, 3));

		DeleteChannelLabel.setFont(font);
		DeleteChannelLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		
		deselectAll = new JButton(Consts.DESELECT_ALL);
		deselectAll.setActionCommand(Consts.DESELECT_ALL);
		deselectAll.setBorder(new EmptyBorder(3, 270, 0, 3));

		labelPanel.add(channelCountLabel);
		labelPanel.add(importVideosLabel);
		labelPanel.add(DeleteChannelLabel);
		labelPanel.add(deselectAll);

		mainPanel.add(labelPanel, BorderLayout.NORTH);
		Vector<String> columnsName = getColumnsName();
		Vector<Object> tableData = getTableData();
		myTableModel = new MyTableModel(tableData, columnsName);
		myTableModel.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == 2 || e.getColumn() == 3) {
					toggleCheckbox(e.getColumn(), e.getFirstRow());
				}
			}
		});
		channelTable = new JTable(myTableModel);
		channelTable
				.setPreferredScrollableViewportSize(new Dimension(500, 300));
		channelTable.setFillsViewportHeight(true);
		// channelTable.getSelectionModel().addListSelectionListener(new
		// RowListener());
		// channelTable.getColumnModel().getSelectionModel().
		// addListSelectionListener(new ColumnListener());
		mainPanel.add(new JScrollPane(channelTable), BorderLayout.CENTER);
		okButton = new JButton(Consts.PREVIEW_UPDATE);
		okButton.setActionCommand(Consts.PREVIEW_UPDATE);
		okButton.setBorder(new EmptyBorder(3, 3, 3, 3));

		mainPanel.add(okButton, BorderLayout.SOUTH);

		mainPanel.setBorder(new EmptyBorder(15, 5, 5, 5));

		this.setContentPane(mainPanel);
	}

	private Vector<Object> getTableData() {

		Vector<Object> dataVektor = new Vector<>();

		for (int i = 0; i < data.size(); i++) {

			Channel channel = (Channel) this.data.get(i);
			Vector<Object> row = new Vector<>();
			row.add(channel.getTitle());
			row.add(channel.getVideoCount());
			row.add(new Boolean(true));
			row.add(new Boolean(false));

			dataVektor.add(row);
		}
		return dataVektor;
	}

	private Vector<String> getColumnsName() {

		Vector<String> columnsname = new Vector<>();
		columnsname.add("title");
		columnsname.add("videos count");
		columnsname.add("import videos info");
		columnsname.add("delete channel");

		return columnsname;
	}

	public void addListener(EventListener listener) {
		ActionListener al = (ActionListener) listener;
		okButton.addActionListener(al);
	}

	public MyTableModel getMyTableModel() {
		return myTableModel;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Channel> getChannelsToStore() {

		ArrayList<Channel> toStore = new ArrayList<>();		

		Vector<Object> dataVector = getMyTableModel().getDataVector();

		for (int i = 0; i < dataVector.size(); i++) {

			Vector<Object> v = (Vector<Object>) dataVector.get(i);
			Boolean store = (Boolean) v.get(2);
			if (store) {
				toStore.add((Channel) this.data.get(i));
			} 
		}

		return toStore;
	}

	private void toggleCheckbox(int column, int firstRow) {
		// Object valueAt = myTableModel.getValueAt(firstRow, column);
		// Boolean newValue = (Boolean)valueAt;
		// System.out.println("185 >> new value of row "+firstRow+" column "+column+" is"+newValue);
		boolean valueAt2 = (boolean) myTableModel.getValueAt(firstRow, 2);
		boolean valueAt3 = (boolean) myTableModel.getValueAt(firstRow, 3);

		if (column == 2) {
			if (valueAt2 == valueAt3) {
				myTableModel.setValueAt(!valueAt3, firstRow, 3);
			}
		} else if (column == 3) {
			if (valueAt2 == valueAt3) {
				myTableModel.setValueAt(!valueAt2, firstRow, 2);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void removeRows(ArrayList<Channel> channels) {

		Vector<Object> dataVector = myTableModel.getDataVector();

		for (Channel channel : channels) {

			for (int i = 0; i < dataVector.size(); i++) {
				Vector<Object> row = (Vector<Object>) dataVector.get(i);
				if (((String) row.get(0)).equals(channel.getTitle())) {
					myTableModel.removeRow(i);
				}
			}
		}

	}

	
}
