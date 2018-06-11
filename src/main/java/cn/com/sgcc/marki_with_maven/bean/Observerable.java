package cn.com.sgcc.marki_with_maven.bean;

public interface Observerable {
	
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObserver(String message);

}
