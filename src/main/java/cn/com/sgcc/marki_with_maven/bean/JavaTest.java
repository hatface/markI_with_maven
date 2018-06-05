package cn.com.sgcc.marki_with_maven.bean;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class JavaTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String connectionString = "jdbc:sqlite:db/data.db";  
		Dao<Poc, Integer> pocDao;

		ConnectionSource connectionSource;  
        try {  
            connectionSource = new JdbcConnectionSource(connectionString);  
            Dao<Poc, ?> createDao = DaoManager.createDao(connectionSource, Poc.class);
            
            TableUtils.createTable(connectionSource, Poc.class);
        }
        catch(Exception e)
        {
        	;
        }
	}

}
