package cn.com.sgcc.marki_with_maven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Producer extends Thread {
	
	private Semaphore semp = null;
	private String ip = null;
	private Scheduler scheduler = null;
	
	public Producer(String ip, Semaphore semp, Scheduler scheduler)
	{
		this.ip = ip;
		this.semp = semp;
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.semp.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			this.scheduler.producerStart(this.ip);
			Process process = Runtime.getRuntime ().exec ("cmd");
            SequenceInputStream sis = new SequenceInputStream (process.getInputStream (), process.getErrorStream ());
            InputStreamReader isr = new InputStreamReader (sis, "gbk");
            BufferedReader br = new BufferedReader (isr);
            OutputStreamWriter osw = new OutputStreamWriter (process.getOutputStream ());
            BufferedWriter bw = new BufferedWriter (osw);
//            bw.write ("nmap -sV --version-all -p1-65535 --open -n --host-timeout=3600s -T4 " + ip);
            bw.write ("nmap -sV --version-all -p1-65535 --open -n --host-timeout=3600s "+ip+" -T4" );
            bw.newLine ();
            bw.flush ();
            bw.close ();
            osw.close ();

			int start = 0;
			Map passvalue = new  HashMap<String, Object>();
			passvalue.put("services", new ArrayList<>());
			String line = null;
			while( (line = br.readLine()) != null )
			{
				if (line.indexOf("Nmap scan") == 0)
				{
					start = 1;
					passvalue.put("ip", this.ip);
					continue;
				}
				if( start == 1 &&  line.matches("^[0-9]+/tcp.*"))
				{
					String[] output = line.split("\\s+");
					output[0] = output[0].replace("/tcp", "");
//					ArrayList<String[]> service = new ArrayList<String[]>();
					String tmp = "";
					for(int i = 3; i < output.length; i ++)
					{
						tmp += output[i];
						tmp += " ";
					}
					((ArrayList<String[]>)passvalue.get("services")).add(new String[]{output[0], output[1], output[2], tmp});
				}
			}
			process.destroy ();
            br.close ();
            isr.close ();
			if(((ArrayList)passvalue.get("services")).size() != 0 && ((ArrayList)passvalue.get("services")).size()  < 1000)
			{
				this.scheduler.producerReport(passvalue);
				this.scheduler.consumer(passvalue);
				
			}
			else
			{
				;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.semp.release();
	}
	
	

}
