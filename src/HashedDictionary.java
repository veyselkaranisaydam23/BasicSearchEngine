import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashedDictionary<V> implements DictionaryInterface<String, V> {
	private TableEntry<V>[] hashTable; 
	private int numberOfEntries;
	private int locationsUsed; 
	private static final int DEFAULT_SIZE = 2477; 
	private static final double MAX_LOAD_FACTOR = 0.8;
	public int collision = 0;
	private static final int q = 31;
	public HashedDictionary() {
		this(DEFAULT_SIZE); 
	} 

	@SuppressWarnings("unchecked")
	public HashedDictionary(int tableSize)
	{
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntry[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
	}

	public boolean isPrime(int num)
	{
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++)
		{
			if ((num % i) == 0)
			{
				prime = false;
				break;
			}
		}
		return prime;
	}

	public int getNextPrime(int num)
	{
		num = num*2;
		if (num <= 1)
            return 2;
		else if(isPrime(num))
			return num;
        boolean found = false;   
        while (!found)
        {
            num++;     
            if (isPrime(num))
                found = true;
        }     
        return num;
	}

	public V add(String key, V value)
	{
		V oldValue; 
		if (isHashTableTooFull())
			rehash();
		int index = getHashIndex(key);
		index = probe(index, key); 

		if ((hashTable[index] == null) || hashTable[index].isRemoved())
		{ 
			hashTable[index] = new TableEntry<V>(key, value);
			numberOfEntries++;
			locationsUsed++;
			oldValue = null;
		} else { 
			oldValue = hashTable[index].getValue();
			hashTable[index].setValue(value);
		} 
		return oldValue;
	}

	private int getHashIndex(String key)
	{
		int hashIndex = 0;
		int test = 0;
		String j = key;
		
		
		//SSF
		for(int i = 0; i<j.length();i++)
		{
			int asciiValue = j.charAt(i);
			test = test + asciiValue;
		}
		
		
		/*
		//PAF
		
		int n = 7;
		int h = 0;
		for(int i = j.length() -1; i>=0;i--)
		{
			int asciiValue = (int) j.charAt(i);
			h = (int) Math.pow(n, i);
			h=h*asciiValue;
			test = test+h;
		}
		*/

		
		
		
		
		
		hashIndex = test % hashTable.length;
		
		if (hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}

	public boolean isHashTableTooFull()
	{
		double load_factor = (double)locationsUsed / hashTable.length;
		
		if (load_factor >= MAX_LOAD_FACTOR)
			return true;
		
		return false;
	}

	@SuppressWarnings("unchecked")
	public void rehash() {
		TableEntry<V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(2 * oldSize);
		hashTable = new TableEntry[newSize]; 
		numberOfEntries = 0; 
		locationsUsed = 0;

		for (int index = 0; index < oldSize; index++) {
			
			 
			if ( (oldTable[index] != null) && oldTable[index].isIn() )
				add(oldTable[index] .getKey(), oldTable[index] .getValue());
		}
	}

	private int probe(int index, String key) {
		boolean found = false;
		int c = 1;
		//int c = doubleHashing(index);
		int removedStateIndex = -1;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey()))
					found = true; 
				else
				{
					
					index = (index + c) % hashTable.length;
					
				}
				
			} 
			else 
			{
				if (removedStateIndex == -1)
					removedStateIndex = index;
				index = (index + c) % hashTable.length; 
				
			} 
		} 
		if (found || (removedStateIndex == -1))
			return index; 
		else
			return removedStateIndex; 
	}

	public V remove(String key) {
		V removedValue = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1) { 
			removedValue = hashTable[index].getValue();
			hashTable[index].setToRemoved();
			numberOfEntries--;
		} 
		return removedValue;
	}

	private int locate(int index, String key) {
		int c = 1;
		//int c = doubleHashing(index);
		boolean found = false;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true; 
			else 
				index = (index + c) % hashTable.length; 
		} 
		int result = -1;
		if (found)
			result = index;
		return result;
	}

	public V getValue(String key) {
		V result = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1)
			result = hashTable[index].getValue(); 
		return result;
	}
	
	public V search(String key) {
		V result = null;
		int index = getHashIndex(key);
		index = locateOfSearch(index, key);
		if (index != -1)
			result = hashTable[index].getValue(); 
		return result;
	}
	
	private int locateOfSearch(int index, String key) {
		int c = 1;
		//int c = doubleHashing(index);
		boolean found = false;
		while (!found && (hashTable[index] != null)) {
			collision+=1;
			if (hashTable[index].isIn() && key.equals(hashTable[index].getKey()))
				found = true; 
			else
				index = (index + c) % hashTable.length; 
		} 
		int result = -1;
		if (found)
			result = index;
		return result;
	}


	public boolean contains(String key) {
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1)
			return true;
		return false;
	}

	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public int getSize() {
		return numberOfEntries;
	}

	public void clear() {
		while(getKeyIterator().hasNext()) {
			remove(getKeyIterator().next());		
		}
	}
	
	public Iterator<String> getKeyIterator() {
		return new KeyIterator();
	}

	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	private class TableEntry<T> {
		private String key;
		private T value;
		private boolean inTable;

		private TableEntry(String key, T value) {
			this.key = key;
			this.value = value;
			inTable = true;
		}

		private String getKey() {
			return key;
		}

		private T getValue() {
			return value;
		}

		private void setValue(T value) {
			this.value = value;
		}

		private boolean isRemoved() {
			return inTable == false;
		}

		private void setToRemoved() {
			inTable = false;
		}

		private void setToIn() {
			inTable = true;
		}

		private boolean isIn() {
			return inTable == true;
		}
	}

	private class KeyIterator implements Iterator<String> {
		private int currentIndex; 
		private int numberLeft; 

		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		} 

		public boolean hasNext() {
			return numberLeft > 0;
		} 

		public String next() {
			String result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				} 
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		} 

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
	
	private class ValueIterator implements Iterator<V> {
		private int currentIndex; 
		private int numberLeft; 

		private ValueIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		} 

		public boolean hasNext() {
			return numberLeft > 0;
		} 

		public V next() {
			V result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				} 
				result = hashTable[currentIndex].getValue();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		} 

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
	
	private int doubleHashing(int hashIndex)
	{
		int hashedIndex1= q - (hashIndex % q);
		return hashedIndex1;
	}
	
	
}
