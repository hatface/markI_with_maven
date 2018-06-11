package cn.com.sgcc.marki_with_maven.bean;

public interface Observer {
	public void update(Observerable o, String message);
}
