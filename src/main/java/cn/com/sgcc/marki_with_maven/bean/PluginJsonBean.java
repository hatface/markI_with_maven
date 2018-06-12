package cn.com.sgcc.marki_with_maven.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import cn.com.sgcc.marki_with_maven.ui.CMSPluginGeneratorFrame.StringRelationBooleanExpress;

public 	class PluginJsonBean
{
	public String pocName;
	public String pocFix ;
	
	public PluginJsonBean() {
		super();
	}
	public String httpMethod;
	public String uri;
	public HashMap<String, String> myRequestHeaders = new HashMap<>();
	public String myRequestBody;
	public String respCompareRelation;
	public String respCompareCode;
	public String respHeaderContain;
	
	public String relation;
	public String respHeader1;
	public String respHeader1ContainsContent;
	public List<StringRelationBooleanExpress> list = new ArrayList<>();
	public String respContent;
	
}
