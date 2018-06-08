package cn.com.sgcc.marki_with_maven.bean;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
	
	OutputStream out = new ByteArrayOutputStream();
	
	
	public void taskRun()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(myPoc.action.match(information))
				{
					information.put("outputStream", out);
					myPoc.action.verify(information);
				}
			}
		});
	}
	
}
