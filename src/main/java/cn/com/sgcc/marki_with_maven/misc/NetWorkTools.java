package cn.com.sgcc.marki_with_maven.misc;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class NetWorkTools {

	String proxyIp = null;
	int proxyPort = 0;

	public SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, new SecureRandom());
		return sc;
	}

	@SuppressWarnings("resource")
	public CloseableHttpClient getCustomClient() throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext sslcontext = this.createIgnoreVerifySSL();
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		connManager.setMaxTotal(400);
		connManager.setDefaultMaxPerRoute(200);

		HttpClientBuilder setConnectionManager = HttpClients.custom().setConnectionManager(connManager);
		if (proxyIp != null && proxyPort != 0) {
			HttpHost proxy = new HttpHost(proxyIp, proxyPort);
			setConnectionManager = setConnectionManager.setRoutePlanner(new DefaultProxyRoutePlanner(proxy));
		}

		return setConnectionManager.build();
	}

	@SuppressWarnings("resource")
	public CloseableHttpClient getCustomAutoRedirectClient() throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext sslcontext = this.createIgnoreVerifySSL();
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		connManager.setMaxTotal(400);
		connManager.setDefaultMaxPerRoute(200);
		HttpClients.custom().setConnectionManager(connManager);
		
		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(connManager).setRedirectStrategy(new LaxRedirectStrategy());
		HttpHost proxy = null;
		if (proxyIp != null && proxyPort != 0) {
			proxy = new HttpHost(proxyIp, proxyPort);
			builder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy));
		}		
				// .setSSLSocketFactory( )
		return builder.build();
	}

}
