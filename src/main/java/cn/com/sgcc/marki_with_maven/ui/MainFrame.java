package cn.com.sgcc.marki_with_maven.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.com.sgcc.marki_with_maven.bean.Poc;
import cn.com.sgcc.marki_with_maven.db.ClassLoader;

public class MainFrame extends JFrame {
	
	
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

	public JTextArea	rightDownPanePanelTextArea = null;
	public JTextArea areaServiceResult = null;
	public JTextField textTarget = null;
	
	
	public void init()
	{
		setSize(800, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//JMenu
		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenu menu2 = new JMenu("Edit");
		JMenu menu3 = new JMenu("Source");
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		JMenuItem item1 = new JMenuItem("open");
		JMenuItem item2 = new JMenuItem("close");
		menu1.add(item1);
		menu1.addSeparator();
		menu1.add(item2);		

		
		//JSplitPane
		wholePanel 	= new JSplitPane();
		leftPanel 	= new TreeViewPanel();		
		

		showDashBoard();
		
		setJMenuBar(menubar);
		setContentPane(wholePanel);
		setVisible(true);
		
	}
	
	
	public void showDashBoard()
	{
		rightPanel 	= new JSplitPane();
		JTabbedPane rightDownPane	= new JTabbedPane();
		rightDownPanePanelTextArea = new JTextArea();	
		JScrollPane 		rightDownPanePanel = new JScrollPane(rightDownPanePanelTextArea);	
		rightDownPanePanelTextArea.setText("            \n");
		rightDownPane.add("console", rightDownPanePanel);
		leftPanel.setMinimumSize(new Dimension(200, 100));
		
		
		JPanel rightUpPane = new JPanel(new BorderLayout());
		JPanel operationPanel = new JPanel();
		JLabel lableTarget = new JLabel("target");
		textTarget = new JTextField("                                 ");
		textTarget.setToolTipText("input target as 10.0.0.1/24");
		JButton buttonStart = new JButton("start");
//		buttonStart.addActionListener(this);
		areaServiceResult = new JTextArea();
		JTabbedPane wrapAreaServiceResult = new JTabbedPane();
		wrapAreaServiceResult.add("service", new JScrollPane(areaServiceResult));
		areaServiceResult.setText("            \n");
		operationPanel.add(lableTarget);
		operationPanel.add(textTarget);
		operationPanel.add(buttonStart);
		rightUpPane.add(operationPanel, BorderLayout.NORTH);
		rightUpPane.add(wrapAreaServiceResult, BorderLayout.CENTER);

		
		
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
		this.repaint();
	}
	
	
	public void showPocDetail(Poc poc)
	{
		JPanel rightDownPane = new JPanel();
		JPanel rightUpPane = new JPanel();
		
		JTabbedPane rightDownContent = new JTabbedPane();
		JScrollPane rightDownContentText = new JScrollPane();
		rightDownContent.add("console for " + poc.getName(), rightDownContentText);
		
		rightUpPane.setLayout( new GridLayout(6, 2) );
//		rightUp
		
		
		rightPanel.setOneTouchExpandable(true);
		rightPanel.setContinuousLayout(true);
		rightPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightPanel.setRightComponent(rightDownPane);
		rightPanel.setLeftComponent(rightUpPane);
		rightPanel.setDividerLocation(0.70);
		rightPanel.setDividerSize(3);
	}


	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException
	{
		HashMap<String, Poc> classLoader = ClassLoader.getSINGLETON().loadClasses();
		
		MainFrame mainFrame = new MainFrame();
		mainFrame.init();
		mainFrame.setVisible(true);
	}

}
