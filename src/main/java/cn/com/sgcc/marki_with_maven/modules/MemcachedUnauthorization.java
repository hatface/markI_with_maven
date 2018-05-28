package cn.com.sgcc.marki_with_maven.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MemcachedUnauthorization implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		return new HashMap<String, String>(){{
			put("author", "kongzhen");
			put("vuln_name", "MemcachedUnauthorization");
			}};
	}

	@Override
	public boolean match(Map infodict) {
		// TODO Auto-generated method stub
		return ((String)infodict.get("service_type")).toLowerCase().contains("memcached") ;
	}

	@Override
	public boolean verify(Map infodict) {
		// TODO Auto-generated method stub
		String ip = (String) infodict.get("ip");
		String port = (String) infodict.get("port");
		boolean success = false;
		try {
			Socket socket = new Socket(ip, Integer.valueOf(port).intValue());
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.setSoTimeout(1000);
			pw.write("stats\r\n");
			pw.flush();
			char[] result = new char[1000];
			br.read(result, 0, 1000);
			String resultString = new String(result);
			if(resultString.contains("pid") || 
			   resultString.contains("uptime") ||
			   resultString.contains("time") ||
			   resultString.contains("version") ||
			   resultString.contains("libevent") ||
			   resultString.contains("pointer_size"))
			{
				success = true;
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
	
	
	public static void main(String[] args)
	{
		Map infodict = new HashMap<String, String>();
		infodict.put("ip", "74.207.251.51");
		infodict.put("port", "11211");
		infodict.put("service_type", "memcached");
		infodict.put("service_version", "memcached");
		System.out.println(new MemcachedUnauthorization().match(infodict));
		System.out.println(new MemcachedUnauthorization().verify(infodict));
	}
}
