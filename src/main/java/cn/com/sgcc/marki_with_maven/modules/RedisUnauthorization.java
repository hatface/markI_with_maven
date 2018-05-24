package cn.com.sgcc.marki_with_maven.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RedisUnauthorization implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, String>();
		info.put("name", "redis unauthorizaion");
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
		if (Integer.valueOf(port) == 6379 || serviceVersion.toLowerCase().contains("redis"))
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
		try {
			Socket socket = new Socket(ip, Integer.valueOf(port));
			PrintWriter pw = new PrintWriter( socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.setSoTimeout(1000);
			char[] payload = new char[]{0x2a,0x31,0x0d,0x0a,0x24,0x34,0x0d,0x0a,0x69,0x6e,0x66,0x6f,0x0d,0x0a};
			pw.write(payload);
			pw.flush();
			char[] result = new char[1000];
			br.read(result, 0, 1000);
			if (new String(result).contains("redis_version"))
			{
				success= true;
			}
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	
	public static void main(String [] args)
	{
		Map infodict = new HashMap<String, String>();
		infodict.put("ip", "119.60.26.110");
		infodict.put("port", "6379");
		infodict.put("service_type", "redis");
		infodict.put("service_version", "redis");
		System.out.println(new RedisUnauthorization().verify(infodict));
	}
}
