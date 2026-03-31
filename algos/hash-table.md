# Хеш-таблица (поиск)

## Intuition
Хеш-таблица — это структура данных, которая обеспечивает поиск элементов за константное время в среднем случае. Идея заключается в использовании хеш-функции, которая преобразует ключ в индекс массива, где хранится значение. Представьте себе гардеробную с ячейками, где номер ячейки вычисляется из вашей фамилии — вы сразу знаете, где искать.

## Approach
1. Создаётся массив фиксированного размера (bucket array)
2. Хеш-функция преобразует ключ в индекс массива: `index = hash(key) % array_size`
3. При вставке элемент помещается по вычисленному индексу
4. При поиске снова вычисляется индекс и проверяется наличие элемента
5. Коллизии (когда разные ключи дают одинаковый индекс) решаются методом цепочек (связные списки) или открытой адресацией

## Complexity
- Time complexity:
    - В среднем: $$O(1)$$ для поиска, вставки, удаления
    - В худшем случае: $$O(n)$$ (при плохой хеш-функции или множестве коллизий)

- Space complexity: $$O(n)$$ где n — количество элементов

## Code
```java
import java.util.LinkedList;

public class HashTable<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private LinkedList<Entry<K, V>>[] buckets;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 16;
    
    @SuppressWarnings("unchecked")
    public HashTable() {
        buckets = new LinkedList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            buckets[i] = new LinkedList<>();
        }
    }
    
    private int hash(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }
    
    // Вставка
    public void put(K key, V value) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value; // обновляем существующий
                return;
            }
        }
        
        bucket.add(new Entry<>(key, value));
        size++;
    }
    
    // Поиск
    public V get(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        
        return null; // не найден
    }
    
    // Удаление
    public V remove(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V value = entry.value;
                bucket.remove(entry);
                size--;
                return value;
            }
        }
        
        return null;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public int size() {
        return size;
    }
}