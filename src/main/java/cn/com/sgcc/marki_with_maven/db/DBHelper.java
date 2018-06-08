package cn.com.sgcc.marki_with_maven.db;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.sun.media.sound.PCMtoPCMCodec;

import cn.com.sgcc.marki_with_maven.bean.Poc;

public class DBHelper {
	String connectionString = "jdbc:sqlite:db/data.db";
	ConnectionSource connectionSource = null;
	
	private DBHelper()
	{
		try {
			connectionSource = new JdbcConnectionSource(connectionString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static DBHelper SINGLETON = null;
	
	public  synchronized static DBHelper getInstance()
	{
		if(SINGLETON == null)
		{
			SINGLETON = new DBHelper();
		}
		return SINGLETON;
	}
	
	public Dao<Poc, Integer> getPocDao()
	{
		Dao<Poc, Integer> pocDao = null;
		try {
			pocDao = DaoManager.createDao(connectionSource, Poc.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pocDao;
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if(connectionSource != null)
		{
			connectionSource.close();
		}
	}
}
