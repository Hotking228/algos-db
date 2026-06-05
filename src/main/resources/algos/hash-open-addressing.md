# Хеш-таблица с открытой адресацией (Hash Table with Open Addressing)

## Intuition
Хеш-таблица с открытой адресацией — это структура данных, которая хранит все элементы прямо в массиве, а не в списках (как в методе цепочек). При возникновении коллизии (два ключа хешируются в одну ячейку) мы просто ищем следующую свободную ячейку согласно определенной стратегии. Представьте парковку с фиксированным количеством мест: если ваше место занято, вы едете к следующему свободному. При поиске нужно проверять ячейки в том же порядке, пока не найдете нужный ключ или пустое место (что означает отсутствие элемента).

## Approach
1. **Хеш-функция** преобразует ключ в индекс массива: `hash(key) % capacity`
2. **Стратегии разрешения коллизий**:
    - **Линейное пробирование**: `(hash + i) % capacity`
    - **Квадратичное пробирование**: `(hash + i²) % capacity`
    - **Двойное хеширование**: `(hash1 + i * hash2) % capacity`
3. **Вставка**: вычисляем хеш, если ячейка занята и ключ не совпадает, продолжаем пробирование до нахождения свободной ячейки или удаленного маркера
4. **Поиск**: пробируем, пока не найдем ключ или пустую ячейку
5. **Удаление**: не можем просто очистить ячейку (это нарушит поиск), вместо этого ставим специальный маркер "удалено"
6. **Перехеширование (rehashing)**: при достижении определенного коэффициента загрузки (обычно 0.7) создаем массив в 2 раза больше и перевставляем все элементы

## Complexity
- Time complexity (средняя вставка): **O(1)**
- Time complexity (средний поиск): **O(1)**
- Time complexity (среднее удаление): **O(1)**
- Time complexity (худший случай): **O(n)**
- Space complexity: **O(n)**

## Code

```java
public class HashTableOpenAddressing<K, V> {
    
    // Статусы ячеек
    private enum EntryStatus {
        OCCUPIED,  // занята
        EMPTY,     // пуста
        DELETED    // удалена (маркер)
    }
    
    // Entry хранит ключ, значение и статус
    private static class Entry<K, V> {
        K key;
        V value;
        EntryStatus status;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.status = EntryStatus.OCCUPIED;
        }
        
        Entry() {
            this.status = EntryStatus.EMPTY;
        }
    }
    
    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private double loadFactor;
    
    // Стратегия пробирования
    public enum ProbingStrategy {
        LINEAR,
        QUADRATIC,
        DOUBLE_HASHING
    }
    
    private ProbingStrategy strategy;
    
    @SuppressWarnings("unchecked")
    public HashTableOpenAddressing() {
        this(16, 0.75, ProbingStrategy.LINEAR);
    }
    
    @SuppressWarnings("unchecked")
    public HashTableOpenAddressing(int initialCapacity, double loadFactor, ProbingStrategy strategy) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.strategy = strategy;
        this.size = 0;
        this.table = new Entry[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new Entry<>();
        }
    }
    
    // Основная хеш-функция
    private int hash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }
    
    // Вторая хеш-функция для двойного хеширования
    private int hash2(K key) {
        return 1 + Math.abs(key.hashCode() % (capacity - 1));
    }
    
    // Шаг пробирования
    private int probeStep(K key, int i) {
        switch (strategy) {
            case LINEAR:
                return i;
            case QUADRATIC:
                return i * i;
            case DOUBLE_HASHING:
                return i * hash2(key);
            default:
                return i;
        }
    }
    
    // Вставка пары ключ-значение
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // Проверяем необходимость перехеширования
        if ((double) size / capacity >= loadFactor) {
            rehash();
        }
        
        int index = hash(key);
        int i = 0;
        int firstDeleted = -1;
        
        while (i < capacity) {
            int probeIndex = (index + probeStep(key, i)) % capacity;
            
            if (table[probeIndex].status == EntryStatus.EMPTY) {
                // Нашли пустую ячейку
                if (firstDeleted != -1) {
                    // Используем первый удаленный маркер
                    table[firstDeleted].key = key;
                    table[firstDeleted].value = value;
                    table[firstDeleted].status = EntryStatus.OCCUPIED;
                } else {
                    table[probeIndex].key = key;
                    table[probeIndex].value = value;
                    table[probeIndex].status = EntryStatus.OCCUPIED;
                }
                size++;
                return;
            } else if (table[probeIndex].status == EntryStatus.DELETED && firstDeleted == -1) {
                // Запоминаем первый удаленный маркер
                firstDeleted = probeIndex;
            } else if (table[probeIndex].status == EntryStatus.OCCUPIED && 
                       table[probeIndex].key.equals(key)) {
                // Обновляем существующий ключ
                table[probeIndex].value = value;
                return;
            }
            i++;
        }
        
        // Если нашли удаленный маркер, используем его
        if (firstDeleted != -1) {
            table[firstDeleted].key = key;
            table[firstDeleted].value = value;
            table[firstDeleted].status = EntryStatus.OCCUPIED;
            size++;
        }
    }
    
    // Получение значения по ключу
    public V get(K key) {
        if (key == null) {
            return null;
        }
        
        int index = hash(key);
        int i = 0;
        
        while (i < capacity) {
            int probeIndex = (index + probeStep(key, i)) % capacity;
            
            if (table[probeIndex].status == EntryStatus.EMPTY) {
                return null;
            }
            
            if (table[probeIndex].status == EntryStatus.OCCUPIED &&
                table[probeIndex].key.equals(key)) {
                return table[probeIndex].value;
            }
            
            i++;
        }
        
        return null;
    }
    
    // Удаление по ключу
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        
        int index = hash(key);
        int i = 0;
        
        while (i < capacity) {
            int probeIndex = (index + probeStep(key, i)) % capacity;
            
            if (table[probeIndex].status == EntryStatus.EMPTY) {
                return null;
            }
            
            if (table[probeIndex].status == EntryStatus.OCCUPIED &&
                table[probeIndex].key.equals(key)) {
                V oldValue = table[probeIndex].value;
                table[probeIndex].status = EntryStatus.DELETED;
                size--;
                return oldValue;
            }
            
            i++;
        }
        
        return null;
    }
    
    // Проверка наличия ключа
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    // Перехеширование (увеличение размера таблицы)
    @SuppressWarnings("unchecked")
    private void rehash() {
        int oldCapacity = capacity;
        Entry<K, V>[] oldTable = table;
        
        capacity *= 2;
        size = 0;
        table = new Entry[capacity];
        
        for (int i = 0; i < capacity; i++) {
            table[i] = new Entry<>();
        }
        
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i].status == EntryStatus.OCCUPIED) {
                put(oldTable[i].key, oldTable[i].value);
            }
        }
    }
    
    // Получение всех ключей
    public java.util.List<K> keys() {
        java.util.List<K> keys = new java.util.ArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry.status == EntryStatus.OCCUPIED) {
                keys.add(entry.key);
            }
        }
        return keys;
    }
    
    // Получение всех значений
    public java.util.List<V> values() {
        java.util.List<V> values = new java.util.ArrayList<>();
        for (Entry<K, V> entry : table) {
            if (entry.status == EntryStatus.OCCUPIED) {
                values.add(entry.value);
            }
        }
        return values;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i].status = EntryStatus.EMPTY;
            table[i].key = null;
            table[i].value = null;
        }
        size = 0;
    }
    
    // Коэффициент загрузки
    public double getLoadFactor() {
        return (double) size / capacity;
    }
}