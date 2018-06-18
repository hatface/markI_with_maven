package cn.com.sgcc.marki_with_maven.misc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import cn.com.sgcc.marki_with_maven.bean.PluginJsonBean;
import cn.com.sgcc.marki_with_maven.modules.IPocBase;
import cn.com.sgcc.marki_with_maven.ui.CMSPluginGeneratorFrame.StringRelationBooleanExpress;

public class IPocBaseGenerator {

	private static IPocBaseGenerator SKELETON = null;

	private IPocBaseGenerator() {
	}

	public static synchronized IPocBaseGenerator getINSTANCE() {
		if (SKELETON == null) {
			SKELETON = new IPocBaseGenerator();
		}
		return SKELETON;
	}
	
	class innerClass
	{
		PluginJsonBean plugin;

		public innerClass(PluginJsonBean plugin) {
			super();
			this.plugin = plugin;
		}
		
		public IPocBase generate()
		{
			return new IPocBase() {

				@Override
				public Map info() {
					// TODO Auto-generated method stub
					Map info = new HashMap<String, String>();
					
					return info;
				}

				@Override
				public boolean match(Map infodict) {
					// TODO Auto-generated method stub
					String ip = (String) IPocBase.Utils.getParameter(infodict, "ip");
					String port = (String) IPocBase.Utils.getParameter(infodict, "port");
					String serviceType = (String) IPocBase.Utils.getParameter(infodict, "service_type");
					String serviceVersion = (String) IPocBase.Utils.getParameter(infodict, "service_version");
					return serviceType.contains("http");
				}

				@Override
				public boolean verify(Map infodict) {
					// TODO Auto-generated method stub
					String ip = (String) IPocBase.Utils.getParameter(infodict, "ip");
					String port = (String) IPocBase.Utils.getParameter(infodict, "port");
					String serviceType = (String) IPocBase.Utils.getParameter(infodict, "service_type");
					String serviceVersion = (String) IPocBase.Utils.getParameter(infodict, "service_version");

					String httpMethod = innerClass.this.plugin.httpMethod;
					HttpRequestBase requestbase = null;
					if (httpMethod.equals("GET")) {
						requestbase = new HttpGet();
					} else if (httpMethod.equals("POST")) {
						requestbase = new HttpPost();
					} else if (httpMethod.equals("TRACE")) {
						requestbase = new HttpTrace();

					} else if (httpMethod.equals("CONNECT")) {

					} else if (httpMethod.equals("PUT")) {
						requestbase = new HttpPut();

					} else if (httpMethod.equals("HEAD")) {
						requestbase = new HttpHead();
					} else if (httpMethod.equals("OPTIONS")) {
						requestbase = new HttpOptions();
					}

					String uri = innerClass.this.plugin.uri;
					HashMap<String, String> myRequestHeaders = innerClass.this.plugin.myRequestHeaders;
					String myRequestBody = innerClass.this.plugin.myRequestBody;
					String respCompareRelation = innerClass.this.plugin.respCompareRelation;
					String respCompareCode = innerClass.this.plugin.respCompareCode;

					String respHeaderContain = innerClass.this.plugin.respHeaderContain;

					boolean success = false;
					CloseableHttpClient customClient = null;
					try {
						String url = String.format("%s://%s:%s/", serviceType.contains("https") ? "https" : "http", ip,
								port);
						URL url1 = new URL(new URL(url), uri);
//						System.out.println(url1.getQuery());
						requestbase.setURI( new URI(url1.getProtocol(), url1.getHost(), url1
								.getPath(),URLEncoder.encode( url1.getQuery()), null)  );
						customClient = new NetWorkTools().getCustomClient();
						for (String str : myRequestHeaders.keySet()) {
							requestbase.addHeader(str, myRequestHeaders.get(str));
						}
						if (!myRequestBody.equals("")) {
							Method method = requestbase.getClass().getMethod("setEntity", HttpEntity.class);
							if (method != null) {
								method.invoke(requestbase, new StringEntity(myRequestBody));
							}
						}

						CloseableHttpResponse resp = customClient.execute(requestbase);

						String resultBooleanExpress = "var output = ";
						Boolean bool1 = false;
						// respCompareCodeInt
						if (!respCompareCode.equals("")) {
							int respCompareCodeInt = 0;
							try {
								respCompareCodeInt = Integer.parseInt(respCompareCode);
							} catch (Exception e) {

							}

							if (respCompareRelation.equals("==")) {
								bool1 = resp.getStatusLine().getStatusCode() == respCompareCodeInt;
							} else if (respCompareRelation.equals("<=")) {
								bool1 = resp.getStatusLine().getStatusCode() <= respCompareCodeInt;
							} else if (respCompareRelation.equals(">=")) {
								bool1 = resp.getStatusLine().getStatusCode() >= respCompareCodeInt;
							} else if (respCompareRelation.equals(">")) {
								bool1 = resp.getStatusLine().getStatusCode() > respCompareCodeInt;
							} else if (respCompareRelation.equals("<")) {
								bool1 = resp.getStatusLine().getStatusCode() < respCompareCodeInt;
							}
							resultBooleanExpress += bool1 + " ";
						}

						Boolean bool2 = false;
						if (!respHeaderContain.equals("")) {
							bool2 = resp.getFirstHeader(respHeaderContain) == null;
							resultBooleanExpress += "&& " + bool2 + " ";
						}

						if (!innerClass.this.plugin.respHeader1.equals("")
								&& !innerClass.this.plugin.respHeader1ContainsContent.equals("")) {
							resultBooleanExpress += innerClass.this.plugin.relation + " "
									+ resp.getFirstHeader(innerClass.this.plugin.respHeader1).getValue()
											.contains(innerClass.this.plugin.respHeader1ContainsContent) + " ";
						}

						for (StringRelationBooleanExpress sbe : innerClass.this.plugin.list) {
							if (!sbe.respHeader1.equals("") && !sbe.respHeader1ContainsContent.equals("")) {
								resultBooleanExpress += sbe.relation + " " + resp.getFirstHeader(sbe.respHeader1).getValue()
										.contains(sbe.respHeader1ContainsContent) + " ";
							}
						}

						String string = EntityUtils.toString(resp.getEntity());

						if (!string.equals("")
								&& !innerClass.this.plugin.respContent.equals("")) {
							resultBooleanExpress += " && " + string
									.contains(innerClass.this.plugin.respContent);
						}
						ScriptEngineManager manager = new ScriptEngineManager();   
						ScriptEngine engine = manager.getEngineByName("nashorn");
						
						engine.eval(resultBooleanExpress);
						success = (Boolean)engine.get("output");
						
						requestbase.releaseConnection();

					} catch (KeyManagementException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ScriptException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							if (customClient != null)
								customClient.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return success;

				}
			};
		}
	}

	
	
	

	public IPocBase generateIPocBase(PluginJsonBean plugin) {
		return new innerClass(plugin).generate();
	}

}
