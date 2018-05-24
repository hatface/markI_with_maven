package cn.com.sgcc.marki_with_maven;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Scheduler extends Thread {

	private MainFrame frame = null;
	private String target = null;
	
	public Scheduler(MainFrame frame) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
		this.target = frame.textTarget.getText().trim();
	}
	
	
	
	private Set<String> parseTarget(String target)
	{
		String subnet;
		int mask = 32;
		Set<String> result = new HashSet<String>();
		if (target.contains("/"))
		{
			String[] split = target.split("/");
			subnet = split[0];
			mask = Integer.valueOf(split[1]);
		}
		else
		{
			subnet = target;
		}
		try {
			InetAddress address = InetAddress.getByName(subnet);
			subnet = address.getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] split = subnet.split("\\.");
		long ipvalue = ((Long.valueOf(split[0]) << 24 )+ (Long.valueOf(split[1]) << 16) + (Long.valueOf(split[2]) << 8) + (Long.valueOf(split[3]))) & 0xffffffff;
		ipvalue &=0xffffffff << (32 - mask);
		for (int i = 1; i < Math.pow(2, 32 - mask) -1 ; i ++ )
		{
			long tmp = ipvalue + i;
			String myip = ((tmp & 0xff000000) >> 24)  + "." + ((tmp & 0x00ff0000) >> 16) + "." +( (tmp & 0x0000ff00) >> 8) + "." + (tmp & 0x000000ff);
			result.add(myip);
		}
		result.add(subnet);
		return result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Semaphore semp_producer = new Semaphore(5);
		
		ArrayList<Thread> producers = new ArrayList<>(); 
		for (String ip : this.parseTarget(target))
		{
			Thread t = new Producer(ip, semp_producer, this);
			t.start();
			producers.add(t);
		}
		
		
		for(Thread t: producers)
		{
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(Thread t : consumers)
		{
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<Thread> consumers = new ArrayList<>();
	
	public void consumer(Map infodict)
	{
		Thread t = new Consumer(infodict, this);
		consumers.add(t);
		t.start();
	}
	
	synchronized public void consumerReport(Map infodict)
	{
		if((boolean)infodict.get("success"))
		{
			String ip = (String) infodict.get("ip");
			String port = (String) infodict.get("port");
			String service_type = (String) infodict.get("service_type");
			String service_version = (String) infodict.get("service_version");
			String vulnName = (String) infodict.get("name");
			String extra = (String) infodict.get("extra");
			String outText = ip + "\t" + port + "\t" + service_type + "\t" + service_version + "\t" + vulnName + "\t" + extra + "\n";
			this.frame.rightDownPanePanelTextArea.setText(this.frame.rightDownPanePanelTextArea.getText()+ outText);
		}
	}
	
	synchronized public void producerStart(String ip)
	{
		ip = "#start scan "+ip+"\n";
		this.frame.areaServiceResult.setText(this.frame.areaServiceResult.getText() + ip);
	}
	
	
	synchronized public void producerReport(Map infodict)
	{
		String ip = (String) infodict.get("ip");
		for(String[] service : (ArrayList<String[]>) infodict.get("services"))
		{
			String port = service[0];
			String service_type = service[2];
			String service_version = service[3];
			String outText = ip + "\t" + port + "\t" + service_type + "\t" + service_version  + "\n";
			this.frame.areaServiceResult.setText(this.frame.areaServiceResult.getText() + outText);
		}
	}
	

}

