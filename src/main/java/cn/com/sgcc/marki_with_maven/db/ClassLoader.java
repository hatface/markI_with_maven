package cn.com.sgcc.marki_with_maven.db;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import cn.com.sgcc.marki_with_maven.bean.Poc;
import cn.com.sgcc.marki_with_maven.misc.Strings;
import cn.com.sgcc.marki_with_maven.modules.IPocBase;

public class ClassLoader {
	
	private static  ClassLoader SINGLETON = null;
	
	private ClassLoader()
	{
		
	}
	
	public static synchronized ClassLoader getSINGLETON()
	{
		ClassLoader retValue = null;
		if(SINGLETON == null)
		{
			SINGLETON = new ClassLoader(); 
			retValue = SINGLETON;
		}
		else{
			retValue =  SINGLETON;
		}
		return retValue;
	}

	HashMap<String, Poc> pocs = new HashMap<>();

	public HashMap<String, Poc> getPocs() {
		return pocs;
	}

	public void setPocs(HashMap<String, Poc> pocs) {
		this.pocs = pocs;
	}

	public HashMap<String, Poc> loadClasses() throws IOException, SQLException, ClassNotFoundException {
		// 取数据库
		ConnectionSource connectionSource = null;
		Dao<Poc, Integer> pocDao = null;
		try {
			String connectionString = "jdbc:sqlite:db/data.db";
			connectionSource = new JdbcConnectionSource(connectionString);
			pocDao = DaoManager.createDao(connectionSource, Poc.class);
			try{
			TableUtils.createTable(connectionSource, Poc.class);
			}
			catch(Exception e)
			{
				;
			}
			List<Poc> queryForAll = pocDao.queryForAll();
			for(Poc poc : queryForAll)
			{
				pocs.put(poc.getLocation(), poc);
			}
		} finally {
			// destroy the data source which should close underlying connections
			
		}

		// 取指定目录
		loadFromDirectory();

		// 取指定包
		HashMap<String, Poc> loadFromPackage = loadFromPackage();
		
		// 保持数据库与目录和包一致
		if(loadFromPackage.keySet().size() != 0)
		{
			pocDao.create(loadFromPackage.values());
		}
		if(tmp.keySet().size() != 0)
		{
			pocDao.create(tmp.values());
		}
		
		
		for(String key : pocs.keySet())
		{
			if(key.endsWith(".jar"))
			{
				
				try {
					URL url1 = new URL("file:"+key);
					URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()  
			                .getContextClassLoader());  
			        Class<?> myClass1;
					myClass1 = myClassLoader1.loadClass("com.sgcc.module.Poc");
					IPocBase object  = ((IPocBase)myClass1.newInstance());
					pocs.get(key).action = object;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		        
			}
			else
			{
				Class<?> forName = Class.forName(key);
				try {
					pocs.get(key).action = (IPocBase) forName.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (connectionSource != null) {
			connectionSource.close();
		}
		return pocs;
	}

	private HashMap<String, Poc> loadFromDirectory() {
		traverseFolder2("plugin");
		
		return null;
	}
	
	private HashMap<String, Poc> tmp = new HashMap<>();

	private void loadClass(File file)
	{
		String path = file.getPath();
		if(file.getAbsolutePath().endsWith(".jar"))
		{
			URL url1;
			try {
				url1 = new URL("file:"+file.getAbsolutePath());
				URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()  
		                .getContextClassLoader());  
		        Class<?> myClass1 = myClassLoader1.loadClass("com.sgcc.module.Poc");  
		        IPocBase object  = ((IPocBase)myClass1.newInstance());
		        Poc poc = new Poc((String)object.info().get("name"), 
						(String)object.info().get("category"),
						(String)object.info().get("location"), 
						(String)object.info().get("author"), 
						(String)object.info().get("fix"));
		        if(pocs.get(poc.getLocation()) == null)
		        {
		        	tmp.put(poc.getLocation(), poc);
		        	pocs.put(poc.getLocation(), poc);
		        }
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        
		}
	}
	
	private  void traverseFolder2(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
//                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
//                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                    	loadClass(file2);
//                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
//            System.out.println("文件不存在!");
        }
    }
	
	private HashMap<String, Poc> loadFromPackage() {
		final List<Class<? extends IPocBase>> payloadClasses =
				new ArrayList<Class<? extends IPocBase>>(IPocBase.Utils.getPayloadClasses());
		Collections.sort(payloadClasses, new Strings.ToStringComparator()); // alphabetize
		
		HashMap<String, Poc> result = new HashMap<>();
		for(Class<? extends IPocBase> payloadClass : payloadClasses)
		{
			try {
				IPocBase object = ((IPocBase)payloadClass.newInstance());
				Poc poc = new Poc((String)object.info().get("name"), 
						(String)object.info().get("category"),
						(String)object.info().get("location")==null ? payloadClass.getName(): (String)object.info().get("location"), 
						(String)object.info().get("author"), 
						(String)object.info().get("fix"));
				if (this.pocs.get(poc.getLocation()) == null)
				{
					result.put(poc.getLocation(), poc);
					pocs.put(poc.getLocation(), poc);
				}
			}
			catch(Throwable t)
			{
				
			}
		}
		
		return result;
	}

}
