# Список с пропусками (Skip List)

## Intuition
Список с пропусками — это вероятностная структура данных, которая позволяет выполнять поиск, вставку и удаление за O(log n) в среднем, используя многоуровневые связные списки. Представьте себе обычный односвязный список, где вы можете перемещаться только последовательно. Теперь добавьте "экспресс-полосы" — дополнительные уровни, где узлы пропускают множество элементов. На верхних уровнях шаги больше, на нижних — точнее. Это похоже на метро с экспресс-линиями: вы едете быстро на экспрессе, а затем спускаетесь на локальные линии для точной остановки.

## Approach
1. Каждый узел имеет массив указателей на следующие узлы на разных уровнях
2. **Уровни**: количество уровней определяется случайно (обычно с вероятностью 1/2)
3. **Поиск**:
    - Начинаем с верхнего уровня
    - Двигаемся вперед, пока следующий узел имеет ключ меньше искомого
    - Спускаемся на уровень ниже и повторяем
4. **Вставка**:
    - Находим позицию для вставки на всех уровнях (сохраняем путь)
    - Генерируем случайный уровень для нового узла
    - Вставляем узел на всех уровнях до сгенерированного
5. **Удаление**:
    - Находим узел на всех уровнях
    - Перенаправляем указатели, пропуская удаляемый узел
6. Вероятностная природа гарантирует балансировку без сложных вращений

## Complexity
- Time complexity (поиск): **O(log n)** в среднем, O(n) в худшем
- Time complexity (вставка): **O(log n)** в среднем
- Time complexity (удаление): **O(log n)** в среднем
- Space complexity: **O(n log n)** в среднем

## Code

```java
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class SkipList<K extends Comparable<K>, V> {
    
    // Узел списка с пропусками
    private static class Node<K, V> {
        K key;
        V value;
        List<Node<K, V>> next; // Указатели на следующих узлов на каждом уровне
        
        @SuppressWarnings("unchecked")
        Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            this.next = new ArrayList<>(level + 1);
            for (int i = 0; i <= level; i++) {
                next.add(null);
            }
        }
    }
    
    private Node<K, V> head;
    private int maxLevel;
    private int currentMaxLevel;
    private double probability;
    private Random random;
    private int size;
    
    @SuppressWarnings("unchecked")
    public SkipList() {
        this(16, 0.5);
    }
    
    @SuppressWarnings("unchecked")
    public SkipList(int maxLevel, double probability) {
        this.maxLevel = maxLevel;
        this.probability = probability;
        this.random = new Random();
        this.currentMaxLevel = 0;
        this.size = 0;
        
        // Создаем головной узел с минимальным ключом
        this.head = new Node<>(null, null, maxLevel);
    }
    
    // Генерация случайного уровня
    private int randomLevel() {
        int level = 0;
        while (level < maxLevel && random.nextDouble() < probability) {
            level++;
        }
        return level;
    }
    
    // Поиск значения по ключу
    public V get(K key) {
        if (key == null) return null;
        
        Node<K, V> current = head;
        
        // Идем с верхнего уровня вниз
        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.next.get(i) != null && 
                   current.next.get(i).key.compareTo(key) < 0) {
                current = current.next.get(i);
            }
        }
        
        // current указывает на узел перед искомым
        if (current.next.get(0) != null && 
            current.next.get(0).key.compareTo(key) == 0) {
            return current.next.get(0).value;
        }
        
        return null;
    }
    
    // Вставка пары ключ-значение
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        // Массив обновлений для каждого уровня
        @SuppressWarnings("unchecked")
        Node<K, V>[] update = new Node[maxLevel + 1];
        
        Node<K, V> current = head;
        
        // Находим позицию для вставки на каждом уровне
        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.next.get(i) != null && 
                   current.next.get(i).key.compareTo(key) < 0) {
                current = current.next.get(i);
            }
            update[i] = current;
        }
        
        // Проверяем, существует ли уже такой ключ
        if (current.next.get(0) != null && 
            current.next.get(0).key.compareTo(key) == 0) {
            // Обновляем значение
            current.next.get(0).value = value;
            return;
        }
        
        // Генерируем случайный уровень
        int newLevel = randomLevel();
        
        // Обновляем currentMaxLevel если нужно
        if (newLevel > currentMaxLevel) {
            for (int i = currentMaxLevel + 1; i <= newLevel; i++) {
                update[i] = head;
            }
            currentMaxLevel = newLevel;
        }
        
        // Создаем новый узел
        Node<K, V> newNode = new Node<>(key, value, newLevel);
        
        // Вставляем узел на каждом уровне
        for (int i = 0; i <= newLevel; i++) {
            newNode.next.set(i, update[i].next.get(i));
            update[i].next.set(i, newNode);
        }
        
        size++;
    }
    
    // Удаление по ключу
    public V remove(K key) {
        if (key == null) return null;
        
        @SuppressWarnings("unchecked")
        Node<K, V>[] update = new Node[maxLevel + 1];
        
        Node<K, V> current = head;
        
        // Находим позицию для удаления на каждом уровне
        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.next.get(i) != null && 
                   current.next.get(i).key.compareTo(key) < 0) {
                current = current.next.get(i);
            }
            update[i] = current;
        }
        
        // Проверяем, существует ли такой ключ
        if (current.next.get(0) == null || 
            current.next.get(0).key.compareTo(key) != 0) {
            return null;
        }
        
        Node<K, V> nodeToRemove = current.next.get(0);
        
        // Удаляем узел на каждом уровне
        for (int i = 0; i <= currentMaxLevel; i++) {
            if (update[i].next.get(i) != nodeToRemove) {
                break;
            }
            update[i].next.set(i, nodeToRemove.next.get(i));
        }
        
        // Уменьшаем currentMaxLevel если нужно
        while (currentMaxLevel > 0 && head.next.get(currentMaxLevel) == null) {
            currentMaxLevel--;
        }
        
        size--;
        return nodeToRemove.value;
    }
    
    // Проверка наличия ключа
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    // Получение первого (минимального) ключа
    public K firstKey() {
        if (isEmpty()) return null;
        return head.next.get(0).key;
    }
    
    // Получение последнего (максимального) ключа
    public K lastKey() {
        if (isEmpty()) return null;
        
        Node<K, V> current = head;
        for (int i = currentMaxLevel; i >= 0; i--) {
            while (current.next.get(i) != null) {
                current = current.next.get(i);
            }
        }
        return current.key;
    }
    
    // Получение всех ключей в порядке возрастания
    public List<K> keys() {
        List<K> keys = new ArrayList<>();
        Node<K, V> current = head.next.get(0);
        while (current != null) {
            keys.add(current.key);
            current = current.next.get(0);
        }
        return keys;
    }
    
    // Получение всех значений в порядке ключей
    public List<V> values() {
        List<V> values = new ArrayList<>();
        Node<K, V> current = head.next.get(0);
        while (current != null) {
            values.add(current.value);
            current = current.next.get(0);
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
        head = new Node<>(null, null, maxLevel);
        currentMaxLevel = 0;
        size = 0;
    }
    
    // Печать структуры (для отладки)
    public void printSkipList() {
        System.out.println("Skip List (levels: " + (currentMaxLevel + 1) + ", size: " + size + ")");
        for (int i = currentMaxLevel; i >= 0; i--) {
            System.out.print("Level " + i + ": ");
            Node<K, V> current = head.next.get(i);
            while (current != null) {
                System.out.print(current.key + " ");
                current = current.next.get(i);
            }
            System.out.println();
        }
    }
}