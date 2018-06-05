package cn.com.sgcc.marki_with_maven.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.com.sgcc.marki_with_maven.modules.IPocBase;

@DatabaseTable(tableName="POC")
public class Poc {
	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField(canBeNull=true)
	private String name;
	
	@DatabaseField(canBeNull=true)
	private String category;
	
	public IPocBase action;
	
	//tasks
	
	@DatabaseField(canBeNull=false)
	private String location;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@DatabaseField(canBeNull=true)
	private String author;
	
	@DatabaseField(canBeNull=true)
	private String fix;
	
}
