

import java.util.HashMap;
import java.util.Map;

import cn.com.sgcc.marki_with_maven.modules.IPocBase;

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

	public boolean verify(Map infodict) {
		// TODO Auto-generated method stub
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		String serviceType = (String) infodict.get("service_type");
		String serviceVersion = (String) infodict.get("service_version");
		return new JavaUnserializaionWeblogic().testConnection(ip, port);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
