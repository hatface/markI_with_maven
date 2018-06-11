package cn.com.sgcc.marki_with_maven.ui;

import java.awt.HeadlessException;

import javax.swing.JFrame;

public class CMSPluginGenerator extends JFrame {
	
	JFrame parent = null;

	public CMSPluginGenerator(JFrame parent) throws HeadlessException {
		super();
		this.parent = parent;
		init();
	}
	
	
	
	private void init()
	{
		
	}
}
