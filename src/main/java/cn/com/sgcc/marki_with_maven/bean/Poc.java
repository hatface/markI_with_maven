package cn.com.sgcc.marki_with_maven.bean;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.com.sgcc.marki_with_maven.modules.IPocBase;

@DatabaseTable(tableName="POC")
public class Poc {
	
	
	
	public Poc() {
		super();
	}

	@DatabaseField(generatedId = true)
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public IPocBase getAction() {
		return action;
	}

	public void setAction(IPocBase action) {
		this.action = action;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFix() {
		return fix;
	}

	public void setFix(String fix) {
		this.fix = fix;
	}

	@DatabaseField(canBeNull=true)
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DatabaseField(canBeNull=true)
	private String category;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

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

	public Poc(String name, String category,  String location, String author, String fix) {
		super();
		this.name = name;
		this.category = category;
		this.location = location;
		this.author = author;
		this.fix = fix;
	}
	
	private List<Task> mytasks = new ArrayList<Task>();	
	
	
	
	
	
}
