package cn.com.sgcc.marki_with_maven;

import java.io.ByteArrayOutputStream;

public class Config {
	
	private static Config SINGLETON = null;
	
	private Config(){}
	
	public synchronized static Config getSINGLETON()
	{
		if(SINGLETON == null)
		{
			SINGLETON = new Config();
			
		}
		return SINGLETON;
	}
	
	private ByteArrayOutputStream serviceOS = new ByteArrayOutputStream();
	private ByteArrayOutputStream consoleOS = new ByteArrayOutputStream();

	public ByteArrayOutputStream getServiceOS() {
		return serviceOS;
	}

	public void setServiceOS(ByteArrayOutputStream serviceOS) {
		this.serviceOS = serviceOS;
	}

	public ByteArrayOutputStream getConsoleOS() {
		return consoleOS;
	}

	public void setConsoleOS(ByteArrayOutputStream consoleOS) {
		this.consoleOS = consoleOS;
	}
	
	public static final String CONFIGURABLE_PARA = "configurable_para";

}
