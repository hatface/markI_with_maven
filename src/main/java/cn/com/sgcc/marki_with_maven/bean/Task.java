package cn.com.sgcc.marki_with_maven.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.com.sgcc.marki_with_maven.Config;
import cn.com.sgcc.marki_with_maven.modules.IPocBase;

public class Task {

	private Poc myPoc = null;

	private Boolean skipMatch = false;

	private Map information = new HashMap<>();

	public Task(Poc myPoc, Map information) {
		super();
		this.myPoc = myPoc;
		this.information = information;
		try {
			taskRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Task(Poc myPoc, Map information, Boolean skipMatch) {
		this.skipMatch = skipMatch;
		this.myPoc = myPoc;
		this.information = information;
		try {
			if (skipMatch)
				taskRunSkipTest();
			else
				taskRun();
		} catch (Exception e) {
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

	public void taskRun() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (myPoc.action.match(information)) {
					information.put("outputStream", out);
					if (myPoc.action.verify(information) == true) {
						try {
							Config.getSINGLETON().getConsoleOS().write(String
									.format("successful!!!  ip: %s  port:%s poc:%s extra:%s\n", (String) IPocBase.Utils.getParameter(information, "ip"),
											(String) IPocBase.Utils.getParameter(information, "port"),myPoc.getName().length() > 0? myPoc.getName():  myPoc.getLocation(), information.get("extra"))
									.getBytes());
							out.write(String
									.format("successful!!!  ip: %s  port:%s poc:%s extra:%s\n", (String) IPocBase.Utils.getParameter(information, "ip"),
											(String) IPocBase.Utils.getParameter(information, "port"),myPoc.getName().length() > 0? myPoc.getName():  myPoc.getLocation(), information.get("extra"))
									.getBytes());
							myPoc.notifyObserver("update dashboard");
							myPoc.notifyObserver("update detail");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							out.write(String.format("ip: %s  port:%s poc:%s not vulnrable\n", (String) IPocBase.Utils.getParameter(information, "ip"),
									(String) IPocBase.Utils.getParameter(information, "port"),myPoc.getName().length() > 0? myPoc.getName():  myPoc.getLocation()).getBytes());
							myPoc.notifyObserver("update detail");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	public void taskRunSkipTest() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				information.put("outputStream", out);
				if (myPoc.action.verify(information) == true) {
					try {
						Config.getSINGLETON().getConsoleOS().write(String
								.format("successful!!!  ip: %s  port:%s poc:%s extra:%s\n", (String) IPocBase.Utils.getParameter(information, "ip"),
										(String) IPocBase.Utils.getParameter(information, "port"),myPoc.getName().length() > 0? myPoc.getName():  myPoc.getLocation(), information.get("extra"))
								.getBytes());
						out.write(String
								.format("successful!!!  ip: %s  port:%s poc:%s extra:%s\n", (String) IPocBase.Utils.getParameter(information, "ip"),
										(String) IPocBase.Utils.getParameter(information, "port"),myPoc.getName().length() > 0? myPoc.getName():  myPoc.getLocation(), information.get("extra"))
								.getBytes());
						myPoc.notifyObserver("update dashboard");
						myPoc.notifyObserver("update detail");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						out.write(String.format("ip: %s  port:%s poc:%s not vulnrable\n", IPocBase.Utils.getParameter(information, "ip"),
								IPocBase.Utils.getParameter(information, "port"), myPoc.getName().length() > 0? myPoc.getName(): myPoc.getLocation()).getBytes());
						myPoc.notifyObserver("update detail");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}).start();
	}

}
