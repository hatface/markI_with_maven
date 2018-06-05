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

public class HttpGetAuth implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "HttpGetAuth");
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
		CloseableHttpClient customClient = null;
		try {
			String url = String.format("%s://%s:%s/", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			if ( resp.getStatusLine().getStatusCode() == 401 )
			{
				ismatch = true;
			}
			httpGet.releaseConnection();
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
		if ( serviceType.toLowerCase().contains("http") && ismatch)
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
			String url = String.format("%s://%s:%s/", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			if ( resp.getStatusLine().getStatusCode() == 404 )
			{
				success = false;
				return success;
			}
			httpGet.releaseConnection();
			BufferedReader brUserName = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/httpauth-username.txt"))));
			BufferedReader brPassWord = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/httpauth-password.txt"))));
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
					if(respLocal.getStatusLine().getStatusCode() == 200)
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

}
