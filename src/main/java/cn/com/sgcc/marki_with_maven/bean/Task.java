package cn.com.sgcc.marki_with_maven.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.com.sgcc.marki_with_maven.Config;

public class Task {
	
	
	private Poc myPoc = null;
	
	



	private Map information = new HashMap<>();
	
	public Task(Poc myPoc, Map information) {
		super();
		this.myPoc = myPoc;
		this.information = information;
		try{
		taskRun();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private OutputStream out = new ByteArrayOutputStream();
	
	
	public OutputStream getOut() {
		return out;
	}


	public void setOut(OutputStream out) {
		this.out = out;
	}


	public void taskRun()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(myPoc.action.match(information))
				{
					information.put("outputStream", out);
					if(myPoc.action.verify(information)== true)
					{
						try {
							Config.getSINGLETON().getConsoleOS().write(String.format("successful!!!  ip: %s  port:%s poc:%s extra:%s\n", 
																		information.get("ip"), information.get("port"), myPoc.getLocation(),
																		information.get("extra")).getBytes());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						try {
							out.write(String.format("ip: %s  port:%s poc:%s not vulnrable\n", 
									information.get("ip"), information.get("port"), myPoc.getLocation()).getBytes());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	
}