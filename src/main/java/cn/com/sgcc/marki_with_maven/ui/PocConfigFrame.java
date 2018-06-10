package cn.com.sgcc.marki_with_maven.ui;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon;

import cn.com.sgcc.marki_with_maven.Config;
import cn.com.sgcc.marki_with_maven.bean.Poc;
import cn.com.sgcc.marki_with_maven.modules.IPocBase;

public class PocConfigFrame extends JFrame {
	
	JFrame parent;
	
	Object data;

	public PocConfigFrame(JFrame parent, Object data) throws HeadlessException {
		super();
		this.parent = parent;
		this.data = data;
		if(this.data instanceof Poc)
		{
			initPoc(this.data);
		}
	}
	
	
	public void  initPoc(Object aPoc)
	{
		Poc poc = (Poc) aPoc;
		Map object = (HashMap<String, Object>) poc.action.info().get(Config.CONFIGURABLE_PARA);
		
		Collections.sort(IPocBase.Utils.configurable_para.keySet().toArray());
		
		
		
		
		JPanel jPanel = new JPanel(new GridLayout( (object.keySet().size() / 5 + 1) * 2, 5));
		for(Object obj : object.keySet())
		{
			
		}
	}
	

}
