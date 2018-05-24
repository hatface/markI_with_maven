package cn.com.sgcc.marki_with_maven;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.com.sgcc.marki_with_maven.misc.Strings;
import cn.com.sgcc.marki_with_maven.modules.IPocBase;

public class ClassLoader {
	
	public static ArrayList<IPocBase> pocs = new ArrayList<>();
	
	public int loadBuildInPoc()
	{
		final List<Class<? extends IPocBase>> payloadClasses =
				new ArrayList<Class<? extends IPocBase>>(IPocBase.Utils.getPayloadClasses());
		Collections.sort(payloadClasses, new Strings.ToStringComparator()); // alphabetize
		
		for(Class<? extends IPocBase> payloadClass : payloadClasses)
		{
			try {
				ClassLoader.pocs.add(payloadClass.newInstance());
			}
			catch(Throwable t)
			{
				
			}
		}
		return 0;
	}
	
	public int loadPlugin(String dir)
	{
		this.traverseFolder1(dir);
		return 0;
	}
	
	private boolean loadClass(String url) 
	{
		boolean success = true;
		try
		{
			URL url1 = new URL("file:"+url);  
	        URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()  
	                .getContextClassLoader());  
	        Class<?> myClass1 = myClassLoader1.loadClass("com.sgcc.module.Poc");  
	        IPocBase action1 = (IPocBase) myClass1.newInstance(); 
	        pocs.add(action1);
		}
		catch(Exception e)
		{
			success = false;
		}
        return success;
	}
	
	public void traverseFolder1(String path) {
		int loadPlugins = 0;
        int fileNum = 0, folderNum = 0;
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
//                    System.out.println("�ļ���:" + file2.getAbsolutePath());
                    list.add(file2);
                    folderNum++;
                } else {
                    if(this.loadClass(file2.getAbsolutePath()))
                    {
                    	loadPlugins ++;
                    }
                    fileNum++;
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
//                        System.out.println("�ļ���:" + file2.getAbsolutePath());
                        list.add(file2);
                        folderNum++;
                    } else {
                        if(this.loadClass(file2.getAbsolutePath()))
                        {
                        	loadPlugins ++;
                        }
                        fileNum++;
                    }
                }
            }
        } else {
//            System.out.println("�ļ�������!");
        }
//        System.out.println("�ļ��й���:" + folderNum + ",�ļ�����:" + fileNum);

    }

}
