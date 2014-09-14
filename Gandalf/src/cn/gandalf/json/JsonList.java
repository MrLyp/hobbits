package cn.gandalf.json;


public interface JsonList<T extends JsonItem> extends JsonItem {
	public boolean add(T t);

	public T get(int index);
	
	public Class<T> getGenericClass();
}
