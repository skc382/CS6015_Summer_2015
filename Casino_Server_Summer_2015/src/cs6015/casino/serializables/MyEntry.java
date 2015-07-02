package cs6015.casino.serializables;

import java.io.Serializable;

public class MyEntry<K, V> implements Serializable{
	private static final long serialVersionUID = -1872405317612079449L;
	
	private K key;
	private V value;
	
	public MyEntry(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}

	public V getValue() {
		// TODO Auto-generated method stub
		return this.value;
	}

	@Override
	public String toString() {
		return "MyEntry [key=" + key + ", value=" + value + "]";
	}
	
}
