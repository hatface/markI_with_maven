package cn.com.sgcc.marki_with_maven.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.alibaba.fastjson.JSON;

import cn.com.sgcc.marki_with_maven.bean.PluginJsonBean;
import cn.com.sgcc.marki_with_maven.db.ClassLoader;

public class CMSPluginGeneratorFrame extends JFrame {

	JFrame parent = null;

	public CMSPluginGeneratorFrame(JFrame parent) throws HeadlessException {
		super();
		this.parent = parent;
		init();
	}

	JPanel respHeaderValuePanel = null;
	JPanel respHeaderValuePanelFreeAdd = null;

	class AddRequestHeaderAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JPanel jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.X_AXIS));
			jPanel2.add(new JLabel("header name"));
			jPanel2.add(new JTextField());
			jPanel2.add(new JLabel("header value"));
			jPanel2.add(new JTextField());

			requestHeaderEdit.add(jPanel2);
			CMSPluginGeneratorFrame.this.revalidate();
			CMSPluginGeneratorFrame.this.repaint();
		}

	}

	JComboBox<String> methodComboBox;
	JTextField uriTextField;
	HashMap<Component, Component> requestHeaders = new HashMap<>();
	JTextArea requestBody;

	JComboBox<String> respCodeComboBox;
	JTextField respCodeTextField;
	JTextField respHeaderContains;

	Relation relation1;
	BooleanExpress booleanExpress1;

	List<RelationBooleanExpress> relations = new ArrayList<>();

	JTextArea jTextArea;

	class RelationBooleanExpress {
		Relation relation1;
		BooleanExpress booleanExpress1;

		public RelationBooleanExpress(Relation relation1, BooleanExpress booleanExpress1) {
			super();
			this.relation1 = relation1;
			this.booleanExpress1 = booleanExpress1;
		}

	}

	class BooleanExpress {
		JTextField jTextField;
		JLabel jLabel;
		JTextField jTextField2;

		public BooleanExpress(JTextField jTextField, JLabel jLabel, JTextField jTextField2) {
			super();
			this.jTextField = jTextField;
			this.jLabel = jLabel;
			this.jTextField2 = jTextField2;
		}

	}

	class Relation {
		JRadioButton buttonAND;
		JRadioButton buttonOR;

		public Relation(JRadioButton button1, JRadioButton button2) {
			super();
			this.buttonAND = button1;
			this.buttonOR = button2;
		}

		public String getRelation() {
			String result = "";
			if (buttonAND.isSelected()) {
				result = "AND";
			} else {
				result = "OR";
			}
			return result;
		}

	}

	final String[] httpMethods = new String[] { "GET", "POST", "HEAD", "TRACE", "CONNECT", "PUT", "OPTIONS" };
	final String[] respCodeRelation = new String[] { "==", ">=", "<=", ">", "<" };
	JPanel requestHeaderEdit = null;
	JTextField pocName;
	JTextArea pocFix ;
	private void init() {
		this.setLayout(new BorderLayout());
		
		
		JPanel basicInfo = new JPanel(new GridLayout(2, 2));
		pocName = new JTextField();
		pocFix = new JTextArea(2, 10);
		basicInfo.add(new JLabel("name"));
		basicInfo.add(new JLabel("Fix"));
		basicInfo.add(pocName);
		basicInfo.add(new JScrollPane(pocFix));
		
		this.add(basicInfo, BorderLayout.NORTH);
		

		JPanel requestPanel = new JPanel();
		requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.Y_AXIS));
		requestPanel.setBorder(new TitledBorder("request"));

		JPanel reqestRow = new JPanel();
		reqestRow.setLayout(new BoxLayout(reqestRow, BoxLayout.X_AXIS));
		methodComboBox = new JComboBox<>(httpMethods);
		uriTextField = new JTextField();
		reqestRow.add(methodComboBox);
		reqestRow.add(uriTextField);

		requestPanel.add(reqestRow);

		JPanel requestHeaders = new JPanel();
		requestHeaders.setLayout(new BorderLayout());
		requestHeaderEdit = new JPanel();
		requestHeaderEdit.setLayout(new BoxLayout(requestHeaderEdit, BoxLayout.Y_AXIS));

		JPanel jPanel2 = new JPanel();
		jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.X_AXIS));
		jPanel2.add(new JLabel("header name"));
		jPanel2.add(new JTextField());
		jPanel2.add(new JLabel("header value"));
		jPanel2.add(new JTextField());
		requestHeaderEdit.add(jPanel2);

		JButton jButton2 = new JButton("add header");
		jButton2.addActionListener(new AddRequestHeaderAction());
		requestHeaders.add(requestHeaderEdit, BorderLayout.CENTER);
		requestHeaders.add(jButton2, BorderLayout.EAST);
		requestPanel.add(requestHeaders);

		requestBody = new JTextArea(10, 10);
		requestPanel.add(new JScrollPane(requestBody));

		this.add(new JScrollPane(requestPanel), BorderLayout.CENTER);

		// response judge

		JPanel respPanel = new JPanel();
		respPanel.setLayout(new BoxLayout(respPanel, BoxLayout.Y_AXIS));
		respPanel.setBorder(new TitledBorder("response"));

		JPanel respCodePanel = new JPanel();
		respCodePanel.setLayout(new BoxLayout(respCodePanel, BoxLayout.X_AXIS));
		respCodePanel.add(new JLabel("resp code"));
		respCodeComboBox = new JComboBox<String>(respCodeRelation);
		respCodePanel.add(respCodeComboBox);
		respCodeTextField = new JTextField();
		respCodePanel.add(respCodeTextField);
		respPanel.add(respCodePanel);

		JPanel respHeadersPanel = new JPanel();
		respHeadersPanel.setLayout(new BoxLayout(respHeadersPanel, BoxLayout.X_AXIS));
		respHeadersPanel.add(new JLabel("resp headers"));
		respHeadersPanel.add(new JLabel("contains"));
		respHeaderContains = new JTextField();
		respHeadersPanel.add(respHeaderContains);
		respPanel.add(respHeadersPanel);

		JPanel relationPanel = new JPanel();
		relationPanel.setLayout(new BoxLayout(relationPanel, BoxLayout.X_AXIS));
		ButtonGroup buttonGroup = new ButtonGroup();
		JRadioButton jRadioButton = new JRadioButton("&&");
		jRadioButton.setSelected(true);
		JRadioButton jRadioButton2 = new JRadioButton("||");
		buttonGroup.add(jRadioButton);
		buttonGroup.add(jRadioButton2);
		relationPanel.add(jRadioButton);
		relationPanel.add(jRadioButton2);
		relation1 = new Relation(jRadioButton, jRadioButton2);
		respPanel.add(relationPanel);

		JPanel wrapRespHeaderValuePanel = new JPanel(new BorderLayout());
		respHeaderValuePanel = new JPanel();
		respHeaderValuePanel.setLayout(new BoxLayout(respHeaderValuePanel, BoxLayout.X_AXIS));
		respHeaderValuePanel.add(new JLabel("header"));
		JTextField jTextField = new JTextField();
		respHeaderValuePanel.add(jTextField);
		JLabel jLabel = new JLabel("contains");
		respHeaderValuePanel.add(jLabel);
		JTextField jTextField2 = new JTextField();
		respHeaderValuePanel.add(jTextField2);
		booleanExpress1 = new BooleanExpress(jTextField, jLabel, jTextField2);
		wrapRespHeaderValuePanel.add(respHeaderValuePanel, BorderLayout.CENTER);
		JButton jButton = new JButton("add");
		jButton.addActionListener(new AddRespHeaderValueAction());
		wrapRespHeaderValuePanel.add(jButton, BorderLayout.EAST);
		respPanel.add(wrapRespHeaderValuePanel);

		respHeaderValuePanelFreeAdd = new JPanel();
		respHeaderValuePanelFreeAdd.setLayout(new BoxLayout(respHeaderValuePanelFreeAdd, BoxLayout.Y_AXIS));
		respPanel.add(respHeaderValuePanelFreeAdd);

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

		jPanel.add(new JLabel("resp body"));
		jPanel.add(new JLabel("contains"));
		jTextArea = new JTextArea(10, 10);
		jPanel.add(new JScrollPane(jTextArea));
		respPanel.add(jPanel);

		JButton buttonGenerate = new JButton("generate");
		buttonGenerate.addActionListener(new GeneratePocAction());
		respPanel.add(buttonGenerate);

		this.add(new JScrollPane(respPanel), BorderLayout.SOUTH);
		this.setSize(800, 600);
		// this.pack();
		this.setVisible(true);

	}


	class GeneratePocAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			PluginJsonBean pluginJsonBean = new PluginJsonBean();
			pluginJsonBean.pocName = pocName.getText();
			pluginJsonBean.pocFix = pocFix.getText();
			
			pluginJsonBean. httpMethod = httpMethods[methodComboBox.getSelectedIndex()];
//			System.out.println(pluginJsonBean. httpMethod);
			pluginJsonBean. uri = uriTextField.getText();
//			System.out.println(pluginJsonBean. uri);
			pluginJsonBean. myRequestHeaders = new HashMap<>();
			for (Component comp : requestHeaders.keySet()) {
				pluginJsonBean.myRequestHeaders.put(((JTextField) comp).getText(), ((JTextField) requestHeaders.get(comp)).getText());
			}
			pluginJsonBean. myRequestBody = requestBody.getText();

			pluginJsonBean. respCompareRelation = respCodeRelation[respCodeComboBox.getSelectedIndex()];
			pluginJsonBean. respCompareCode = respCodeTextField.getText();

			pluginJsonBean. respHeaderContain = respHeaderContains.getText();

			pluginJsonBean. relation = relation1.getRelation();
			pluginJsonBean. respHeader1 = booleanExpress1.jTextField.getText();
			pluginJsonBean. respHeader1ContainsContent = booleanExpress1.jTextField2.getText();

			pluginJsonBean. list = new ArrayList<>();
			for (RelationBooleanExpress rbe : relations) {
				pluginJsonBean.list.add(new StringRelationBooleanExpress(rbe.relation1.getRelation(),
						rbe.booleanExpress1.jTextField.getText(), rbe.booleanExpress1.jTextField2.getText()));
			}

			pluginJsonBean. respContent = jTextArea.getText();
			
			try {
				FileWriter fileWriter = new FileWriter(new File("plugin_Json/"+UUID.randomUUID().toString()+".json"));
				
				fileWriter.write(JSON.toJSONString(pluginJsonBean));;
				fileWriter.flush();
				fileWriter.close();
				ClassLoader.getSINGLETON().loadClasses();
//				parent
				JOptionPane.showMessageDialog(null, "add CMS Plugin Successful");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
//			System.out.println();
//			System.out.println();
		}

	}

	public class StringRelationBooleanExpress {
		public StringRelationBooleanExpress(){}
		
		public String relation;
		public String respHeader1;
		public String respHeader1ContainsContent;

		public StringRelationBooleanExpress(String relation, String respHeader1, String respHeader1ContainsContent) {
			super();
			this.relation = relation;
			this.respHeader1 = respHeader1;
			this.respHeader1ContainsContent = respHeader1ContainsContent;
		}

	}

	class AddRespHeaderValueAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			JPanel relationPanel = new JPanel();
			relationPanel.setLayout(new BoxLayout(relationPanel, BoxLayout.X_AXIS));
			ButtonGroup buttonGroup = new ButtonGroup();
			JRadioButton jRadioButton = new JRadioButton("&&");
			JRadioButton jRadioButton2 = new JRadioButton("||");
			jRadioButton.setSelected(true);
			buttonGroup.add(jRadioButton);
			buttonGroup.add(jRadioButton2);
			relationPanel.add(jRadioButton);
			relationPanel.add(jRadioButton2);
			// jPanel.add(relationPanel);

			respHeaderValuePanelFreeAdd.add(relationPanel);

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

			jPanel.add(new JLabel("resp value of:"));
			JTextField jTextField = new JTextField();
			jPanel.add(jTextField);
			JLabel jLabel = new JLabel("contains");
			jPanel.add(jLabel);
			JTextField jTextField2 = new JTextField();
			jPanel.add(jTextField2);

			JButton buttonRemove = new JButton("remove");
			jPanel.add(buttonRemove);

			RelationBooleanExpress relationBooleanExpress = new RelationBooleanExpress(
					new Relation(jRadioButton, jRadioButton2), new BooleanExpress(jTextField, jLabel, jTextField2));
			buttonRemove.addActionListener(new RelationRemoveAction(relationBooleanExpress, relationPanel, jPanel));
			relations.add(relationBooleanExpress);
			respHeaderValuePanelFreeAdd.add(jPanel);
			CMSPluginGeneratorFrame.this.revalidate();
			CMSPluginGeneratorFrame.this.repaint();
			// CMSPluginGenerator.this.pack();
		}

	}

	class RelationRemoveAction implements ActionListener {
		RelationBooleanExpress relationBooleanExpress = null;
		JPanel panel = null;
		JPanel panel1 = null;

		public RelationRemoveAction(RelationBooleanExpress relationBooleanExpress, JPanel panel, JPanel panel1) {
			super();
			this.relationBooleanExpress = relationBooleanExpress;
			this.panel = panel;
			this.panel1 = panel1;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			relations.remove(relationBooleanExpress);
			respHeaderValuePanelFreeAdd.remove(panel);
			respHeaderValuePanelFreeAdd.remove(panel1);
			CMSPluginGeneratorFrame.this.revalidate();
		}

	}

	public static void main(String[] args) {
		new CMSPluginGeneratorFrame(null);
	}
}
