package cn.com.sgcc.marki_with_maven.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.com.sgcc.marki_with_maven.Config;
import cn.com.sgcc.marki_with_maven.bean.Poc;
import cn.com.sgcc.marki_with_maven.bean.Task;
import cn.com.sgcc.marki_with_maven.modules.IPocBase;

public class PocConfigFrame extends JFrame {

	JFrame parent;

	Object data;

	public PocConfigFrame(JFrame parent, Object data) throws HeadlessException {
		super();
		this.parent = parent;
		this.data = data;
		if (this.data instanceof Poc) {
			initPoc(this.data);
		}
	}

	HashMap<String, Component> specialFieldController = null;
	HashMap<String, Component> baseinfoControl = null;

	public void initPoc(Object aPoc) {
		Poc poc = (Poc) aPoc;
		this.setLayout(new BorderLayout());

		// baseinfo panel generation
		Map object = (HashMap<String, Object>) IPocBase.Utils.configurable_para;

		List<String> configurable_para = new ArrayList<String>();
		for (Object obj : IPocBase.Utils.configurable_para.keySet()) {
			configurable_para.add((String) obj);
		}

		Collections.sort(configurable_para);
		
		JPanel baseInfoPanel = new JPanel(new GridLayout(( (int)Math.ceil( configurable_para.size() / 4.0)   ) * 2, 4));
		baseinfoControl = new HashMap<String, Component>();
		int nextAddIndex = 0;
		for (int i = 0; i < configurable_para.size(); i++) {
			baseInfoPanel.add(new JLabel(configurable_para.get(i)));
			baseinfoControl.put(configurable_para.get(i), new JTextField());
			if (i != 0 && i % 4 == 0) {
				// int start = (i % 4) - 1 < 0 ? 0: ((i % 4) - 1 ) ;
				for (int j = (i % 4) - 1; j < (i % 4) * 4; j++) {
					baseInfoPanel.add((JTextField) baseinfoControl.get(configurable_para.get(j)));
					
				}
				nextAddIndex = (i % 4) * 4;
			}
		}
		for(int i = nextAddIndex; i < configurable_para.size(); i ++)
		{
			baseInfoPanel.add((JTextField) baseinfoControl.get(configurable_para.get(i)));
		}

		this.add(baseInfoPanel, BorderLayout.NORTH);

		// special info panel generation
		HashMap<String, Object> object2 = (HashMap<String, Object>) poc.action.info()
				.get(Config.CONFIGURABLE_PARA_SPECIAL);
		if(object2 == null)
		{
			object2 = new HashMap<String, Object>();
		}
		List<String> specialFields = new ArrayList<String>();
		for (String str : object2.keySet()) {
			specialFields.add(str);
		}
		Collections.sort(specialFields);
		specialFieldController = new HashMap<>();
		int rows = ( (int) Math.ceil(object2.keySet().size() / 4.0)  ) * 2;
		
		JPanel specialFieldPanel = new JPanel(new GridLayout(rows, 4));
		int nextAddIndex1 = 0;
		for (int i = 0; i < specialFields.size(); i++) {
			specialFieldPanel.add(new JLabel(specialFields.get(i)));
			Component fieldController = null;
			if (object2.get(specialFields.get(i)) instanceof File) {
				fieldController = new JScrollPane(new JTextArea(10, 5));
			}

			specialFieldController.put(specialFields.get(i), fieldController);
			if (i != 0 && i % 4 == 0) {
				// int start = (i % 4) - 1 < 0 ? 0: ((i % 4) - 1 ) ;
				for (int j = (i % 4) - 1; j < (i % 4) * 4; j++) {
					specialFieldPanel.add(specialFieldController.get(specialFields.get(j)));
				}
				nextAddIndex1 = (i % 4) * 4;
			}
		}
		for(int i = nextAddIndex1; i < specialFields.size(); i ++)
		{
			specialFieldPanel.add(specialFieldController.get(specialFields.get(i)));
		}

		this.add(specialFieldPanel, BorderLayout.CENTER);

		// run button
		JPanel runButtonPanel = new JPanel(new BorderLayout());
		JButton runButton = new JButton("run");
		runButton.addActionListener(new RunButtonActionListener());
		runButtonPanel.add(runButton, BorderLayout.EAST);
		this.add(runButtonPanel, BorderLayout.SOUTH);
		
		setSize(800, 600);
//		setExtendedState(JFrame.MAXIMIZED_BOTH);
//		setDefaultCloseOperation(JFrame);
		this.setVisible(true);
	}

	class RunButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// baseinfo info get
			Map<String, Object> passValue = new HashMap<String, Object>();
			for (String str : baseinfoControl.keySet()) {
				if (baseinfoControl.get(str) instanceof JTextField) {
					passValue.put(Config.CONFIGED + str, ((JTextField) baseinfoControl.get(str)).getText());
				} else if (baseinfoControl.get(str) instanceof JTextArea) {
					passValue.put(Config.CONFIGED + str,
							new StringReader(((JTextArea) baseinfoControl.get(str)).getText()));
				}
			}

			// specail info get

			for (String str : specialFieldController.keySet()) {
				if (specialFieldController.get(str) instanceof JTextArea) {
					passValue.put(Config.CONFIGED + str,
							new StringReader(((JTextArea) specialFieldController.get(str)).getText()));
				} else if (specialFieldController.get(str) instanceof JTextField) {
					passValue.put(Config.CONFIGED + str, ((JTextField) specialFieldController.get(str)).getText());
				}
			}
			
			((Poc)data).getMytasks().add(new Task((Poc)data, passValue, true));
			((Poc)data).notifyObserver("detail");
			PocConfigFrame.this.setVisible(false);
			JOptionPane.showMessageDialog(parent, String.format("exploit %s successfully", ((Poc)data).getLocation()));
			
		}

	}

}
