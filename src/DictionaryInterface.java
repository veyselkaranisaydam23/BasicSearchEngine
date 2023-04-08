import java.util.Iterator;

public interface DictionaryInterface<K, V>
{
	public boolean isPrime(int num);
	public int getNextPrime(int num);
	public V add(K key, V value);
	public boolean isHashTableTooFull();
	public void rehash();
	public V remove(K key);
	public V getValue(K key);
	public boolean contains(K key);
	public boolean isEmpty();
	public int getSize();
	public void clear();
	public Iterator<K> getKeyIterator();
	public Iterator<V> getValueIterator();
}
