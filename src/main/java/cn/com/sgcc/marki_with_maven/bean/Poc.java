package cn.com.sgcc.marki_with_maven.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.com.sgcc.marki_with_maven.modules.IPocBase;

@DatabaseTable(tableName="POC")
public class Poc implements Comparable<Poc>, Observerable{
	
	
	
	
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

	public List<Task> getMytasks() {
		return mytasks;
	}

	public void setMytasks(List<Task> mytasks) {
		this.mytasks = mytasks;
	}

	@Override
	public int compareTo(Poc o) {
		// TODO Auto-generated method stub
		return location.compareTo(o.location);
	}

	
	Set<Observer> setObservers = new HashSet<>();
	@Override
	public void registerObserver(Observer o) {
		// TODO Auto-generated method stub
		setObservers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		setObservers.remove(o);
	}

	@Override
	public void notifyObserver(String message) {
		// TODO Auto-generated method stub
		for(Observer o : setObservers)
		{
			o.update(this, message);
		}
	}


	
	
	
}
