package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;

import controller.UpdateController;
import entities.Channel;
import entities.Video;
import main.Consts;
import model.Model;

public class View extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	// JFrame to Display imported Channels
	private Preview preview;

	private JMenuItem mItemUpdateChannel, mItemUpdateDatabase;
	private JTextField searchField;
	// Buttons
	private JButton searchButton;
	private JButton clearButton;
	private JButton clearLogButton;
	// info field
	private JTextArea infoArea;
	// Channel list area
	private JList<String> channelList;
	private DefaultListModel<String> listModel;
	// video list area
	private JTable videoTable;
	private MyTableModel myTableModel;
	private ArrayList<String> descriptionList = new ArrayList<>();
	private EventListener listener;
	
	private ArrayList<Channel> channelObjectsList;
	private ArrayList<Video> videoList ;

	public View() {

		this.setLocation(50, 50);
		// this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Youtube search");		

		createAndAddComponents();
		createAndAddMenuBar();

		// Adjusts the size of the frame to best work for the components
		this.pack();

		this.setVisible(true);

		// Define if the user can resize the frame (true by default)
		//this.setResizable(false);
	}

	private void createAndAddComponents() {

		// hole panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.decode("#DDA185"));

		// Header Search+buttons
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(0, 1));
		headerPanel.setBackground(Color.decode("#DAE9D7"));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
		// headerPanel.setBackground(Color.green);

		// search
		JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		inputPanel.setBackground(Color.decode("#DAE9D7"));
		// inputPanel.setBackground(Color.ORANGE);

		JLabel searchLabel = new JLabel("expression");
		inputPanel.add(searchLabel);

		searchField = new JTextField(30);
		searchField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				// getInfoField().setText("");
			}
		});
		inputPanel.add(searchField);

		JLabel hintLabel = new JLabel(Consts.SEARCH_EXPRESSION_EXAMPLE);
		inputPanel.add(hintLabel);

		headerPanel.add(inputPanel);

		// buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));		
		buttonPanel.setBackground(Color.decode("#DAE9D7"));

		searchButton = new JButton(Consts.SEARCH);
		searchButton.setActionCommand(Consts.SEARCH);

		clearButton = new JButton(Consts.CLEAR);
		clearButton.setActionCommand(Consts.CLEAR);
		
		clearLogButton = new JButton(Consts.CLEAR_LOG);
		clearLogButton.setActionCommand(Consts.CLEAR_LOG);

		buttonPanel.add(searchButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(clearLogButton);
		

		headerPanel.add(buttonPanel);

		mainPanel.add(headerPanel, BorderLayout.NORTH);
		// channels
		listModel = new DefaultListModel<String>();
		channelList = new JList<String>(listModel);
		channelList.setSelectedIndex(0);
		channelList.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		JScrollPane scrollPane = new JScrollPane(channelList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(200, 380));
		scrollPane.setBorder(new CompoundBorder(new EmptyBorder(3, 3, 3, 3),
				BorderFactory.createTitledBorder("Channels")));

		mainPanel.add(scrollPane, BorderLayout.WEST);

		// videos
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("video title");
		myTableModel = new MyTableModel(new Vector<Object>(), columnNames);
		videoTable = new JTable(myTableModel) {
			private static final long serialVersionUID = 1L;

			// Implement table cell tool tips.
			public String getToolTipText(MouseEvent e) {
				ToolTipManager.sharedInstance().setInitialDelay(0);
				ToolTipManager.sharedInstance().setDismissDelay(5000);
				// ToolTipManager.sharedInstance().setInitialDelay(600);
				String tip = null;
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				if (rowIndex < 0) {
					return null;
				}

				int colIndex = columnAtPoint(p);
				int realColumnIndex = convertColumnIndexToModel(colIndex);

				if (realColumnIndex == 0) {
					tip = formatLines(descriptionList.get(rowIndex), 100,
							Locale.UK);
				}

				return tip;
			}
		};
		videoTable.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		videoTable.setFillsViewportHeight(true);

		JScrollPane scrollPane2 = new JScrollPane(videoTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setPreferredSize(new Dimension(800, 380));
		scrollPane2.setBorder(new CompoundBorder(new EmptyBorder(20, 20, 5, 5),
				BorderFactory.createTitledBorder("Videos")));

		mainPanel.add(scrollPane2, BorderLayout.CENTER);

		infoArea = new JTextArea();
		Font font = new Font("Courier", Font.PLAIN, 14);
		infoArea.setFont(font);
		infoArea.setBorder(new CompoundBorder(new EmptyBorder(3, 3, 3, 3),
				BorderFactory.createTitledBorder("log")));

		JScrollPane scrollPane3 = new JScrollPane(infoArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane3.setPreferredSize(new Dimension(800, 200));
		scrollPane3.setBorder(new CompoundBorder(new EmptyBorder(3, 3, 3, 3),
				BorderFactory.createTitledBorder("Log")));
		mainPanel.add(scrollPane3, BorderLayout.SOUTH);

		this.add(mainPanel);
	}

	private void createAndAddMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu(Consts.MENU);
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		mItemUpdateChannel = new JMenuItem(Consts.UPDATE_CHANNEL, KeyEvent.VK_T);
		mItemUpdateChannel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		mItemUpdateChannel.setActionCommand(Consts.UPDATE_CHANNEL);
		menu.add(mItemUpdateChannel);

		mItemUpdateDatabase = new JMenuItem(Consts.UPDATE_DB, KeyEvent.VK_D);
		mItemUpdateDatabase.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		mItemUpdateDatabase.setActionCommand(Consts.UPDATE_DB);
		menu.add(mItemUpdateDatabase);

		this.setJMenuBar(menuBar);
	}

	static String formatLines(String target, int maxLength, Locale currentLocale) {

		StringBuilder sb = new StringBuilder("<html>");

		BreakIterator boundary = BreakIterator.getLineInstance(currentLocale);
		boundary.setText(target);
		int start = boundary.first();
		int end = boundary.next();
		int lineLength = 0;

		while (end != BreakIterator.DONE) {

			String word = target.substring(start, end);
			sb.append(word);
			lineLength = lineLength + word.length();
			if (lineLength >= maxLength) {
				sb.append("<br>");
				lineLength = word.length();
			}
			// sb.append(word+"<br>");
			start = end;
			end = boundary.next();
		}

		return sb.toString();
	}

	public void addListeners(EventListener listener) {

		this.listener = listener;
		ActionListener al = (ActionListener) listener;
		searchButton.addActionListener(al);
		clearButton.addActionListener(al);
		clearLogButton.addActionListener(al);
		mItemUpdateChannel.addActionListener(al);
		mItemUpdateDatabase.addActionListener(al);

		ListSelectionListener lsl = (ListSelectionListener) listener;
		channelList.addListSelectionListener(lsl);
		ListSelectionModel selectionModel = videoTable.getSelectionModel();
		selectionModel.addListSelectionListener(lsl);
		videoTable.setSelectionModel(selectionModel);

		MouseListener mAdapter = (MouseListener) listener;
		channelList.addMouseListener(mAdapter);
		// ??
		videoTable.addMouseListener(mAdapter);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void update(Observable observable, Object arg) {

		// check where the info is coming from
		if (observable instanceof Model) {

			if (arg instanceof HashMap<?, ?>)

			{
				HashMap<String, Object> data = (HashMap<String, Object>) arg;

				if (((String) data.get(Consts.TYPE)).equals(Consts.CHANNEL)) {
					if (data.get(Consts.DATA) instanceof String) {
						displayInfo((String) data.get(Consts.DATA));
						listModel.clear();
						myTableModel.setRowCount(0);
					} else if (data.get(Consts.DATA) instanceof ArrayList<?>) {
						updateChannelList((ArrayList<Channel>) data
								.get(Consts.DATA));
					}

				} else if (((String) data.get(Consts.TYPE))
						.equals(Consts.VIDEO)) {

					if (data.get(Consts.DATA) instanceof String) {
						displayInfo((String) data.get(Consts.DATA));

					} else if (data.get(Consts.DATA) instanceof ArrayList<?>) {
						videoList = (ArrayList<Video>) data
								.get(Consts.DATA);
						updateDescriptionsList(videoList);
						updateVideoTable(videoList);
					}
				}

			} else if (arg instanceof String) {

				String info = (String) arg;
				displayInfo(info);
			}

		} else if (observable instanceof UpdateController) {

			if (arg instanceof String) {
				String info = (String) arg;
				displayInfo(info);
			} else if (arg instanceof ArrayList<?>) {
				ArrayList<Object> objects = (ArrayList<Object>) arg;
				if (objects.get(0) instanceof Channel) {
					preview = new Preview(objects);
					preview.addListener(listener);
				}

			} else if (arg instanceof Video) {
				Video video = (Video) arg;
				displayInfo(video.getTitle());
			} else if (arg instanceof HashMap<?, ?>){
				
				HashMap<String, Object> map = (HashMap<String, Object>) arg;
				String info = (String)map.get(Consts.INFO);
				if(info.equals(Consts.DELETED_AFTER_IMPORT)){					
					preview.removeRows((ArrayList<Channel>)map.get(Consts.DATA));					
				}
			}			
		}
	}

	private void updateDescriptionsList(ArrayList<Video> videoList) {

		descriptionList.clear();
		for (Video v : videoList) {
			descriptionList.add(v.getDescription());
		}
	}

	private void updateVideoTable(ArrayList<Video> videoList) {

		myTableModel.setRowCount(0);
		for (Video video : videoList) {
			Vector<String> v = new Vector<>();
			v.add(video.getTitle());
			myTableModel.addRow(v);
		}
	}

	private void displayInfo(final String info) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		getInfoArea().append(dateFormat.format(date) + " :" + info + "\n");
	}

	private void updateChannelList(ArrayList<Channel> result) {
		
		this.channelObjectsList = result;
		listModel.clear();
		for (Channel channel : result) {
			listModel.addElement(channel.getTitle());
		}
	}

	public JTextField getSearchField() {
		return searchField;
	}

	public JTable getVideoTable() {
		return videoTable;
	}

	public JTextArea getInfoArea() {
		return infoArea;
	}

	public JMenuItem getMenuItem() {
		return mItemUpdateChannel;
	}
	
	public ArrayList<Channel> getChannelObjectsList() {
		return channelObjectsList;
	}	

	public ArrayList<Video> getVideoList() {
		return videoList;
	}

	public void clearView() {
		getSearchField().setText("");		
		listModel.clear();
		myTableModel.setRowCount(0);
	}
	
	public void clearLog() {		
		getInfoArea().setText("");		
	}
	
	public ArrayList<Channel> getPreviewData() {
		
		ArrayList<Channel> previewData = preview.getChannelsToStore();	
	
		return previewData;
	}

	public Preview getPreview() {
		return preview;
	}
	
	

}
