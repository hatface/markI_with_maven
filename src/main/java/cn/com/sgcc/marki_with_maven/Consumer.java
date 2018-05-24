package cn.com.sgcc.marki_with_maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.com.sgcc.marki_with_maven.modules.IPocBase;

public class Consumer extends Thread {

	private Map infodict = null;
	private Scheduler scheduler = null;
	
	public Consumer(Map infodict, Scheduler scheduler)
	{
		this.infodict = infodict;
		this.scheduler = scheduler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(IPocBase poc : ClassLoader.pocs)
		{
			Map pocinfo = poc.info();
			
			
			String ip = (String) infodict.get("ip");
			for(String[] service : (ArrayList<String[]>)this.infodict.get("services"))
			{
				String port = service[0];
				String service_type = service[2];
				String service_version = service[3];
				Map localpass = new HashMap<Object, Object>();
				localpass.putAll(pocinfo);
				localpass.put("success", false);
				localpass.put("ip", ip);
				localpass.put("port", port);
				localpass.put("service_type", service_type);
				localpass.put("service_version", service_version);
				if (poc.match(localpass) && poc.verify(localpass))
				{
					localpass.put("success", true);
					this.scheduler.consumerReport(localpass);
				}
			}
		}
	}
	

}
