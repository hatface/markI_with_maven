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

import org.sqlite.core.DB;

import cn.com.sgcc.marki_with_maven.bean.Poc;
import cn.com.sgcc.marki_with_maven.db.ClassLoader;
import cn.com.sgcc.marki_with_maven.db.DBHelper;
import javafx.scene.control.TextField;

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
		leftPanel 	= new TreeViewPanel(this);		
		

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
	
	JTextField idTextField = new JTextField();
	JTextField nameTextField = new JTextField();
	JTextField categoryField = new JTextField();
	JTextField locationField = new JTextField();
	JTextField authorField = new JTextField();
	JTextArea fixTextArea = new JTextArea(3, 5);
	
	class SaveInformationButtonAction implements ActionListener
	{
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
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	public void showPocDetail(Poc poc)
	{
		JPanel rightDownPane = new JPanel();
		JPanel rightUpPane = new JPanel();
		
		JTabbedPane rightDownContent = new JTabbedPane();
		JScrollPane rightDownContentText = new JScrollPane();
		rightDownContent.add("console for " + poc.getName(), rightDownContentText);
		rightDownPane.add(rightDownContent);
		
		rightUpPane.setLayout( new BorderLayout() );
		JLabel idLabel = new JLabel("id");
		JLabel nameLabel = new JLabel("name");
		JLabel categoryLabel = new JLabel("category");
		JLabel locationLabel = new JLabel("location");
		JLabel authorLabel = new JLabel("author");
		
		idTextField.setEditable(false);
		idTextField.setText(poc.getId()+"");
		
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
		rightUpPane.add(tmp1Panel, BorderLayout.NORTH);
		
		JLabel fixLabel = new JLabel("fix");
		
		
		JButton fixInformationButton = new JButton("save");
		fixInformationButton.addActionListener(new SaveInformationButtonAction(poc) );
		JButton configButton = new JButton("config");
		
		JPanel tmp2Panel = new JPanel();
		tmp2Panel.add(fixLabel);
		tmp2Panel.add(fixTextArea);
		
		JPanel tmp3Panel = new JPanel();
		tmp3Panel.add(fixInformationButton);
		tmp3Panel.add(configButton);
		
		rightUpPane.add(tmp2Panel, BorderLayout.CENTER);
		rightUpPane.add(tmp3Panel, BorderLayout.EAST);
		
		
		
		rightPanel.setOneTouchExpandable(true);
		rightPanel.setContinuousLayout(true);
		rightPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightPanel.setRightComponent(rightDownPane);
		rightPanel.setLeftComponent(rightUpPane);
		rightPanel.setDividerLocation(0.70);
		rightPanel.setDividerSize(3);
		this.repaint();
	}


	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException
	{
		HashMap<String, Poc> classLoader = ClassLoader.getSINGLETON().loadClasses();
		
		MainFrame mainFrame = new MainFrame();
		mainFrame.init();
		mainFrame.setVisible(true);
	}

}
