package cn.com.sgcc.marki_with_maven;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class MainFrame implements ActionListener{
	
	public static String pluginDir = "plugins";
	public JTextArea	rightDownPanePanelTextArea = null;
	public JTextArea areaServiceResult = null;
	public JTextField textTarget = null;
	private Scheduler myscheduler = null;
	
	
	
	private void init()
	{
		ClassLoader classLoader = new ClassLoader();
		//load build in poc
		classLoader.loadBuildInPoc();
		//load plugins
		classLoader.loadPlugin(pluginDir);
		
		//JFrame
		JFrame jf = new JFrame("helloworld");
		jf.setSize(800, 600);
		jf.setExtendedState(JFrame.MAXIMIZED_BOTH);  
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
		JSplitPane 	wholePane 	= new JSplitPane();
		JSplitPane 	rightPane 	= new JSplitPane();
		JPanel		leftPane 	= new JPanel();		
		
		JTabbedPane rightDownPane	= new JTabbedPane();
		rightDownPanePanelTextArea = new JTextArea();	
		JScrollPane 		rightDownPanePanel = new JScrollPane(rightDownPanePanelTextArea);	
		rightDownPanePanelTextArea.setText("            \n");
		rightDownPane.add("console", rightDownPanePanel);
		leftPane.setMinimumSize(new Dimension(200, 100));
		
		
		JPanel rightUpPane = new JPanel(new BorderLayout());
		JPanel operationPanel = new JPanel();
		JLabel lableTarget = new JLabel("target");
		textTarget = new JTextField("                                 ");
		textTarget.setToolTipText("input target as 10.0.0.1/24");
		JButton buttonStart = new JButton("start");
		buttonStart.addActionListener(this);
		areaServiceResult = new JTextArea();
		JTabbedPane wrapAreaServiceResult = new JTabbedPane();
		wrapAreaServiceResult.add("service", new JScrollPane(areaServiceResult));
		areaServiceResult.setText("            \n");
		operationPanel.add(lableTarget);
		operationPanel.add(textTarget);
		operationPanel.add(buttonStart);
		rightUpPane.add(operationPanel, BorderLayout.NORTH);
		rightUpPane.add(wrapAreaServiceResult, BorderLayout.CENTER);

		
		jf.setJMenuBar(menubar);
		jf.setContentPane(wholePane);
		jf.setVisible(true);
		

		wholePane.setOneTouchExpandable(true);
		wholePane.setContinuousLayout(true);
		wholePane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		wholePane.setRightComponent(rightPane);
		wholePane.setLeftComponent(leftPane);
		wholePane.setDividerLocation(0.25);
		wholePane.setDividerSize(3);
		
		
		rightPane.setOneTouchExpandable(true);
		rightPane.setContinuousLayout(true);
		rightPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightPane.setRightComponent(rightDownPane);
		rightPane.setLeftComponent(rightUpPane);
		rightPane.setDividerLocation(0.70);
		rightPane.setDividerSize(3);
	}
	
	
	
	public static void main(String[] args)
	{
		
		MainFrame mainFrame = new MainFrame();
		mainFrame.init();
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.myscheduler = new Scheduler(this);
		this.myscheduler.start();
	}

}
