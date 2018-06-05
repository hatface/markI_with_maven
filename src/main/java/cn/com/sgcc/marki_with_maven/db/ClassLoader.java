package cn.com.sgcc.marki_with_maven.db;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import cn.com.sgcc.marki_with_maven.bean.Poc;
import cn.com.sgcc.marki_with_maven.misc.Strings;
import cn.com.sgcc.marki_with_maven.modules.IPocBase;

public class ClassLoader {

	HashMap<String, Poc> pocs = new HashMap<>();

	public HashMap<String, Poc> loadClasses() throws IOException, SQLException {
		// 取数据库
		ConnectionSource connectionSource = null;
		try {
			String connectionString = "jdbc:sqlite:db/data.db";
			connectionSource = new JdbcConnectionSource(connectionString);
			Dao<Poc, Integer> pocDao = DaoManager.createDao(connectionSource, Poc.class);
			TableUtils.createTable(connectionSource, Poc.class);
			List<Poc> queryForAll = pocDao.queryForAll();
			for(Poc poc : queryForAll)
			{
				pocs.put(poc.getLocation(), poc);
			}
		} finally {
			// destroy the data source which should close underlying connections
			if (connectionSource != null) {
				connectionSource.close();
			}
		}

		// 取指定目录
		loadFromDirectory();

		// 取指定包
		loadFromPackage();
		
		// 保持数据库与目录和包一致

		return null;
	}

	private HashMap<String, Poc> loadFromDirectory() {
		return null;
	}

	private HashMap<String, Poc> loadFromPackage() {
		final List<Class<? extends IPocBase>> payloadClasses =
				new ArrayList<Class<? extends IPocBase>>(IPocBase.Utils.getPayloadClasses());
		Collections.sort(payloadClasses, new Strings.ToStringComparator()); // alphabetize
		
		for(Class<? extends IPocBase> payloadClass : payloadClasses)
		{
			try {
//				payloadClass.getName(), ((IPocBase)payloadClass.newInstance()).info()
			}
			catch(Throwable t)
			{
				
			}
		}
		
		return null;
	}

}
