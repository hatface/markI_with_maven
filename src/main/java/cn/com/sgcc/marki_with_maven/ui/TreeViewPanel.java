package cn.com.sgcc.marki_with_maven.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import cn.com.sgcc.marki_with_maven.bean.Poc;

public class TreeViewPanel extends JPanel {
	
	
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("the POCS");
	private HashMap<String, DefaultMutableTreeNode> categorys = new HashMap<>();
	
	private JTree theTree;
	
	private MainFrame mainFrame = null;
	
	
	private class TreeLeef extends DefaultMutableTreeNode 
	{
		String pocpath;

		public String getPocpath() {
			return pocpath;
		}

		public void setPocpath(String pocpath) {
			this.pocpath = pocpath;
		}

		public TreeLeef(Object userObject) {
			super(userObject);
			// TODO Auto-generated constructor stub
			this.pocpath = (String) userObject;
		}
		
	}
	
	private class TreeBrunch extends DefaultMutableTreeNode
	{

		public TreeBrunch(Object userObject) {
			super(userObject);
			// TODO Auto-generated constructor stub
		}
		
	}
	
	
	
	
	public TreeViewPanel() {
		super();
		
		ConnectionSource connectionSource = null;
		Dao<Poc, Integer> pocDao = null;
		try {
			String connectionString = "jdbc:sqlite:db/data.db";
			connectionSource = new JdbcConnectionSource(connectionString);
			pocDao = DaoManager.createDao(connectionSource, Poc.class);
			List<Poc> queryForAll = pocDao.queryBuilder().orderBy("location", true).query();
			for(Poc poc : queryForAll)
			{

				String category = poc.getCategory() + "";
				
				if(categorys.get(category) == null)
				{
					TreeBrunch treeBrunch = new TreeBrunch(category);
					rootNode.add(treeBrunch);
					categorys.put(category, treeBrunch);
				}
				
				
			}
			for(Poc poc : queryForAll)
			{
				String name = poc.getName();
				if (name == null)
				{
					name = poc.getLocation();
				}
				String category = poc.getCategory() + "";
				if(categorys.get(category) != null)
				{
					categorys.get(category).add(new TreeLeef(name));
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		
		this.theTree = new JTree(rootNode);
		this.add(theTree);
		
		this.theTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)theTree.getLastSelectedPathComponent();
				if(node == rootNode)
				{
					
				}
				else if(node instanceof TreeLeef)
				{
					String pocpath = ((TreeLeef)node).getPocpath();
				}
				else if (node instanceof TreeBrunch)
				{
					
				}
			}
		});
	}

	

	public static void main(String[] args)
	{
		return;
	}
	

}
