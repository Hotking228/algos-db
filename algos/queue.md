# Очередь (Queue) - enqueue/dequeue

## Intuition
Очередь — это структура данных, работающая по принципу FIFO (First In, First Out) — "первым пришёл, первым ушёл". Представьте себе очередь в магазине: люди подходят в конец очереди (enqueue), а обслуживаются те, кто стоит в начале (dequeue). Это естественная модель для многих реальных процессов, где важен порядок обработки.

## Approach
1. **enqueue(item)** — добавляет элемент в конец очереди
2. **dequeue()** — удаляет и возвращает элемент из начала очереди
3. **front()** / peek() — возвращает первый элемент без удаления
4. **rear()** — возвращает последний элемент
5. **isEmpty()** — проверяет, пуста ли очередь
6. **size()** — возвращает количество элементов

## Complexity
- Time complexity: $$O(1)$$ для всех основных операций (enqueue, dequeue, peek, isEmpty)

- Space complexity: $$O(n)$$ для хранения n элементов

## Code

### Реализация через массив (кольцевая очередь)
```java
public class QueueArray<T> {
    private T[] array;
    private int front;
    private int rear;
    private int size;
    private int capacity;
    
    @SuppressWarnings("unchecked")
    public QueueArray(int capacity) {
        this.capacity = capacity;
        array = (T[]) new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }
    
    // Добавление элемента в конец
    public void enqueue(T item) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }
        rear = (rear + 1) % capacity;
        array[rear] = item;
        size++;
    }
    
    // Удаление элемента из начала
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T item = array[front];
        array[front] = null; // помогаем GC
        front = (front + 1) % capacity;
        size--;
        return item;
    }
    
    // Просмотр первого элемента
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return array[front];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public boolean isFull() {
        return size == capacity;
    }
    
    public int size() {
        return size;
    }
}