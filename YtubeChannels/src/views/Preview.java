package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Vector;

import javax.swing.Box;
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
		//mainPanel.setLayout(new BorderLayout());
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

//		JPanel labelPanel = new JPanel();
//		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
//		labelPanel.setBorder(new EmptyBorder(5, 5, 15, 5));

		Font font = new Font("Courier", Font.PLAIN, 14);
		
		JLabel channelCountLabel = new JLabel(Consts.CHANNEL_COUNT);		
		channelCountLabel.setFont(font);
		channelCountLabel.setBorder(new EmptyBorder(3, 3, 3, 3));		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;		 
		mainPanel.add(channelCountLabel,c);		 
		 
		JLabel importVideosLabel = new JLabel(Consts.VIDEOS_TO_IMPORT);
		importVideosLabel.setFont(font);
		importVideosLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;		 
		mainPanel.add(importVideosLabel,c);
		
		JLabel DeleteChannelLabel = new JLabel(Consts.CHANNELS_TO_DELETE);
		DeleteChannelLabel.setFont(font);
		DeleteChannelLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 2;		 
		mainPanel.add(DeleteChannelLabel,c);		
		
		
		deselectAll = new JButton(Consts.DESELECT_ALL);
		deselectAll.setPreferredSize(new Dimension(10, 50));
		deselectAll.setActionCommand(Consts.DESELECT_ALL);
		deselectAll.setFont(font);
		deselectAll.setBorder(new EmptyBorder(3, 3, 3, 3));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 3;		 
		mainPanel.add(deselectAll,c);	
		
		
		//Dimension minSize = new Dimension(10, 10);
		//Dimension prefSize = new Dimension(10, 10);
		//Dimension maxSize = new Dimension(10,10);
		//container.add(new Box.Filler(minSize, prefSize, maxSize));

		
		//labelPanel.add(importVideosLabel);
		//labelPanel.add(DeleteChannelLabel);
		//labelPanel.add(new Box.Filler(minSize, prefSize, maxSize));
		//labelPanel.add(deselectAll);

		//mainPanel.add(labelPanel, BorderLayout.NORTH);
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
		c.fill = GridBagConstraints.HORIZONTAL;			
		c.weightx = 0.0;
		c.gridwidth = 5;
		c.gridheight = 5;
		c.gridx = 0;
		c.gridy = 4;		
		mainPanel.add(new JScrollPane(channelTable), c);
		
		
		okButton = new JButton(Consts.PREVIEW_UPDATE);
		//deselectAll.setPreferredSize(new Dimension(10, 50));
		okButton.setActionCommand(Consts.DESELECT_ALL);
		okButton.setFont(font);
		okButton.setBorder(new EmptyBorder(3, 3, 3, 3));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 3;
		c.gridy = 9;		 
		mainPanel.add(okButton,c);		

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
		deselectAll.addActionListener(al);
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
		// .println("185 >> new value of row "+firstRow+" column "+column+" is"+newValue);
		boolean valueAt2 = (boolean) myTableModel.getValueAt(firstRow, 2);
		boolean valueAt3 = (boolean) myTableModel.getValueAt(firstRow, 3);
		
		if(column == 2){
			if ( valueAt2 ) {
				myTableModel.setValueAt(false, firstRow, 3);
			} 
		}else if (column == 3) {
			if (valueAt3) {
				myTableModel.setValueAt(false, firstRow, 2);
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
