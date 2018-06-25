package cn.com.sgcc.marki_with_maven.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.com.sgcc.marki_with_maven.Config;
import cn.com.sgcc.marki_with_maven.Scheduler;
import cn.com.sgcc.marki_with_maven.bean.Observer;
import cn.com.sgcc.marki_with_maven.bean.Observerable;
import cn.com.sgcc.marki_with_maven.bean.Poc;
import cn.com.sgcc.marki_with_maven.bean.Task;
import cn.com.sgcc.marki_with_maven.db.ClassLoader;
import cn.com.sgcc.marki_with_maven.db.DBHelper;

public class MainFrame extends JFrame implements Observer {

	private JSplitPane wholePanel;
	private JPanel leftPanel;
	private JSplitPane rightPanel;

	public MainFrame() throws HeadlessException {
		super();
	}

	public MainFrame(JPanel leftPanel) throws HeadlessException {
		super();
		this.leftPanel = leftPanel;
	}

	public JTextArea rightDownPanePanelTextArea = null;
	public JTextArea areaServiceResult = null;
	public JTextArea textTarget = null;

	private SplashFrame splash = null;

	public void init() {
		setSize(800, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// JMenu
		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenu menu2 = new JMenu("Edit");
		JMenu menu3 = new JMenu("Plugin");
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		JMenuItem item1 = new JMenuItem("open");
		JMenuItem item2 = new JMenuItem("close");
		menu1.add(item1);
		menu1.addSeparator();
		menu1.add(item2);
		
		JMenuItem pluginCmsAdd = new JMenuItem("cms plugin add");
		pluginCmsAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new CMSPluginGeneratorFrame(MainFrame.this);
			}
		});
		menu3.add(pluginCmsAdd);
		// JSplitPane
		wholePanel = new JSplitPane();
		leftPanel = new TreeViewPanel(this);
		
		
		showDashBoard();
		
		
		setJMenuBar(menubar);
		setContentPane(wholePanel);

//		setVisible(true);
//		wholePanel.setOneTouchExpandable(true);
//		wholePanel.setContinuousLayout(true);
//		wholePanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
//		wholePanel.setDividerLocation(0.25);
//		wholePanel.setDividerSize(3);
//
//		rightPanel.setOneTouchExpandable(true);
//		rightPanel.setContinuousLayout(true);
//		rightPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
////		rightPanel.setRightComponent();
////		rightPanel.setLeftComponent();
//		rightPanel.setDividerLocation(0.70);
//		rightPanel.setDividerSize(3);

//		this.set
//		this.pack();
		

	}

	private Scheduler myscheduler = null;

	class buttonStartActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			myscheduler = new Scheduler(MainFrame.this);
			myscheduler.start();
		}
	}

	public void showDashBoard() {
		showStatus = SHOWOVERVIEW;
		rightPanel = new JSplitPane();
		JTabbedPane rightDownPane = new JTabbedPane();
		rightDownPanePanelTextArea = new JTextArea();
		rightDownPanePanelTextArea.setEditable(false);
		JScrollPane rightDownPanePanel = new JScrollPane(rightDownPanePanelTextArea);
		rightDownPanePanelTextArea.setText("            \n");
		rightDownPane.add("console", rightDownPanePanel);
		leftPanel.setMinimumSize(new Dimension(200, 100));

		JPanel rightUpPane = new JPanel(new BorderLayout());
		JPanel operationPanel = new JPanel();
		JLabel lableTarget = new JLabel("target");
		textTarget = new JTextArea(5, 40);
		textTarget.setToolTipText("input target as 10.0.0.1/24");
		JButton buttonStart = new JButton("start");
		buttonStart.addActionListener(new buttonStartActionListener());
		areaServiceResult = new JTextArea(20, 10);
		areaServiceResult.setEditable(false);
		JTabbedPane wrapAreaServiceResult = new JTabbedPane();
		wrapAreaServiceResult.add("service", new JScrollPane(areaServiceResult));
		areaServiceResult.setText(Config.getSINGLETON().getServiceOS().toString());
		operationPanel.add(lableTarget);
		operationPanel.add(new JScrollPane(textTarget));
		operationPanel.add(buttonStart);
		rightUpPane.add(operationPanel, BorderLayout.NORTH);
		rightUpPane.add(wrapAreaServiceResult, BorderLayout.CENTER);

		rightDownPanePanelTextArea.setText(Config.getSINGLETON().getConsoleOS().toString());
		
		
		wholePanel.setOneTouchExpandable(true);
		wholePanel.setContinuousLayout(true);
		wholePanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		wholePanel.setRightComponent(rightPanel);
		wholePanel.setLeftComponent(leftPanel);
		wholePanel.setDividerLocation(0.25);
		wholePanel.setDividerSize(3);

		rightPanel.setOneTouchExpandable(true);
		rightPanel.setContinuousLayout(true);
		rightPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightPanel.setTopComponent(rightUpPane);
//		rightPanel.setRightComponent();
//		rightPanel.setLeftComponent();
		rightPanel.setBottomComponent(rightDownPane);
		rightPanel.setDividerLocation(0.50);

		rightPanel.setDividerSize(3);
		this.validate();
		this.repaint();
//		this.pack();
		
		
	}

	JTextField idTextField = new JTextField();
	JTextField nameTextField = new JTextField();
	JTextField categoryField = new JTextField();
	JTextField locationField = new JTextField();
	JTextField authorField = new JTextField();
	JTextArea fixTextArea = new JTextArea(10, 50);

	class SaveInformationButtonAction implements ActionListener {
		Poc poc;

		public SaveInformationButtonAction(Poc poc) {
			super();
			this.poc = poc;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			poc.setId(Integer.parseInt(idTextField.getText()));
			poc.setName(nameTextField.getText());
			poc.setCategory(categoryField.getText());
			poc.setLocation(locationField.getText());
			poc.setAuthor(authorField.getText());
			poc.setFix(fixTextArea.getText());
			try {
				DBHelper.getInstance().getPocDao().update(poc);
				JOptionPane.showMessageDialog(MainFrame.this, "add successful");
				MainFrame.this.showPocDetail(poc);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
	
	class LaunchConfigPocButtonAction implements ActionListener{

		Poc poc;

		public LaunchConfigPocButtonAction(Poc poc) {
			super();
			this.poc = poc;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new PocConfigFrame(MainFrame.this, poc);
		}
		
	}
	
	private int showStatus = 0;
	private final static int SHOWOVERVIEW = 0;
	private final static int SHOWDETAIL = 1;
	private Poc lastPocShowed = null;

	public void showPocDetail(Poc poc) {
		lastPocShowed = poc;
		showStatus = SHOWDETAIL;
		JTabbedPane rightDownPane = new JTabbedPane();
		JTabbedPane rightUpPane = new JTabbedPane();

//		JTabbedPane rightDownContent = new JTabbedPane();
		JTextArea rightDownContentText = new JTextArea();
		
		rightDownContentText.setText("                 \n");
		rightDownPane.add("console for " + poc.getName(), new JScrollPane(rightDownContentText));
		int i = 1;
		for (Task t : poc.getMytasks()) {
			JTextArea jTextArea = new JTextArea();
			jTextArea.setText(t.getOut().toString());
			rightDownPane.add("console for task" + i, new JScrollPane(jTextArea));
			i++;
		}
//		rightDownPane.add(rightDownPane);

		JPanel rightUpPanecontent = new JPanel(new BorderLayout());
		JLabel idLabel = new JLabel("id");
		JLabel nameLabel = new JLabel("name");
		JLabel categoryLabel = new JLabel("category");
		JLabel locationLabel = new JLabel("location");
		JLabel authorLabel = new JLabel("author");

		idTextField.setEditable(false);
		idTextField.setText(poc.getId() + "");

		nameTextField.setText(poc.getName());

		categoryField.setText(poc.getCategory());

		locationField.setText(poc.getLocation());
		locationField.setEditable(false);

		authorField.setText(poc.getAuthor());

		JPanel tmp1Panel = new JPanel(new GridLayout(2, 5));
		tmp1Panel.add(idLabel);
		tmp1Panel.add(nameLabel);
		tmp1Panel.add(categoryLabel);
		tmp1Panel.add(locationLabel);
		tmp1Panel.add(authorLabel);

		tmp1Panel.add(idTextField);
		tmp1Panel.add(nameTextField);
		tmp1Panel.add(categoryField);
		tmp1Panel.add(locationField);
		tmp1Panel.add(authorField);
		rightUpPanecontent.add(tmp1Panel, BorderLayout.NORTH);

		JLabel fixLabel = new JLabel("fix");

		JButton fixInformationButton = new JButton("save");
		fixInformationButton.addActionListener(new SaveInformationButtonAction(poc));
		JButton configButton = new JButton("config");
		configButton.addActionListener(new LaunchConfigPocButtonAction(poc));

		JPanel tmp2Panel = new JPanel();
		tmp2Panel.add(fixLabel);
		tmp2Panel.add(fixTextArea);

		JPanel tmp3Panel = new JPanel();
		tmp3Panel.add(fixInformationButton);
		tmp3Panel.add(configButton);

		rightUpPanecontent.add(tmp2Panel, BorderLayout.CENTER);
		rightUpPanecontent.add(tmp3Panel, BorderLayout.EAST);
		rightUpPane.add("detail", rightUpPanecontent);

		wholePanel.setOneTouchExpandable(true);
		wholePanel.setContinuousLayout(true);
		wholePanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		wholePanel.setRightComponent(rightPanel);
		wholePanel.setLeftComponent(leftPanel);
		wholePanel.setDividerLocation(0.25);
		wholePanel.setDividerSize(3);
		
		rightPanel.setOneTouchExpandable(true);
		rightPanel.setContinuousLayout(true);
		rightPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightPanel.setRightComponent(rightDownPane);
		rightPanel.setLeftComponent(rightUpPane);
		rightPanel.setDividerLocation(0.70);
		rightPanel.setDividerSize(3);
		this.revalidate();
		this.repaint();
	}
	
	
	

	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		HashMap<String, Poc> classLoader = ClassLoader.getSINGLETON().loadClasses();
		
		
		MainFrame mainFrame = new MainFrame();
		
		for(Poc poc : classLoader.values())
		{
			poc.registerObserver( mainFrame);
		}
		mainFrame.splash = new SplashFrame(mainFrame);
		mainFrame.splash.start();
		
		mainFrame.init();
//		mainFrame.setVisible(true);
//		mainFrame.showDashBoard();
		
//		mainFrame.setVisible(true);
	}

	@Override
	public void update(Observerable o, String message) {
		// TODO Auto-generated method stub
		if(showStatus == SHOWOVERVIEW && message.contains("detail"))
		{
//			show
			;
		}
		else if(showStatus == SHOWOVERVIEW && message.contains("dashboard"))
		{
			showDashBoard();
		}
		else if(showStatus == SHOWDETAIL && message.contains("detail"))
		{
			if(o == lastPocShowed)
			{
				showPocDetail((Poc) o);
			}
		}
		else if(showStatus == SHOWDETAIL && message.contains("dashboard"))
		{
			;
		}
		
	}


}
