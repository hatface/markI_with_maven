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

public class PhpInfoLeak implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "PhpInfoLeak");
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
		if ( serviceType.toLowerCase().contains("http") )
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
			for(String phpinfo : new String[]{"phpinfo.php", "p.php"})
			{
				String url = String.format("%s://%s:%s/"+phpinfo, serviceType.contains("https") ? "https":"http", ip, port );
				HttpGet httpGet = new HttpGet(url);
				customClient = new Tools().getCustomAutoRedirectClient();
				CloseableHttpResponse resp = customClient.execute(httpGet);
				if ( resp.getStatusLine().getStatusCode() == 404 )
				{
					continue;
				}
				String content = EntityUtils.toString(resp.getEntity());
				httpGet.releaseConnection();
				if(content.toLowerCase().contains("php") && content.toLowerCase().contains("version".toLowerCase()) && resp.getStatusLine().getStatusCode() == 200 &&
						content.toLowerCase().contains("system")	)
				{
					success = true;
					break;
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
		infodict.put("ip", "www.talkceltic.net");
		infodict.put("port", "80");
		infodict.put("service_type", "http");
		infodict.put("service_version", "topsec");
		System.out.println(new PhpInfoLeak().verify(infodict));
	}

}
