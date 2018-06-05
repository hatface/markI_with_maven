package cn.com.sgcc.marki_with_maven.modules;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

import cn.com.sgcc.marki_with_maven.misc.NetWorkTools;

public class EzUpload_2_2_index_php_path_Parameter_RFI implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "EzUpload_2_2_index_php_path_Parameter_RFI");
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
			String url = String.format("%s://%s:%s/ezupload/index.php", serviceType.contains("https") ? "https":"http", ip, port );
			HttpGet httpGet = new HttpGet(url);
			customClient = new NetWorkTools().getCustomClient();
			HttpContext httpContext = new BasicHttpContext();
			CloseableHttpResponse resp = customClient.execute(httpGet, httpContext);
			HttpHost targetHost = (HttpHost)httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
	        HttpUriRequest realRequest = (HttpUriRequest)httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK && url.equals(targetHost.toString() + realRequest.getURI()))
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
		return match(infodict);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
