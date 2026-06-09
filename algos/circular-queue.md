# Кольцевая очередь (Circular Queue)

## Intuition
Кольцевая очередь (циклический буфер) — это структура данных FIFO (первый пришел - первый ушел), которая эффективно использует память, соединяя конец массива с его началом. Представьте круглую карусель с местами: когда последнее место занято, следующий элемент попадает на первое место, если оно свободно. Это позволяет избежать сдвига элементов при удалении и повторно использовать освободившиеся ячейки без увеличения размера массива.

## Approach
1. Очередь реализуется на массиве фиксированного размера
2. Два указателя: `front` (индекс первого элемента) и `rear` (индекс последнего элемента)
3. Пустая очередь: `front == -1`
4. Полная очередь: `(rear + 1) % capacity == front`
5. **enqueue (добавление)**:
    - Если очередь пуста, устанавливаем front = 0
    - Сдвигаем rear: `rear = (rear + 1) % capacity`
    - Вставляем элемент на позицию rear
6. **dequeue (удаление)**:
    - Сохраняем элемент на позиции front
    - Если front == rear (один элемент), устанавливаем front = -1, rear = -1
    - Иначе сдвигаем front: `front = (front + 1) % capacity`
7. Кольцевая структура обеспечивает использование всех ячеек массива

## Complexity
- Time complexity (enqueue): **O(1)**
- Time complexity (dequeue): **O(1)**
- Time complexity (peek): **O(1)**
- Space complexity: **O(n)**, где n — емкость очереди

## Code

```java
public class CircularQueue<T> {
    private T[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;
    
    @SuppressWarnings("unchecked")
    public CircularQueue(int capacity) {
        this.capacity = capacity;
        this.queue = (T[]) new Object[capacity];
        this.front = -1;
        this.rear = -1;
        this.size = 0;
    }
    
    // Добавление элемента в конец очереди
    public void enqueue(T element) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }
        
        if (isEmpty()) {
            front = 0;
        }
        
        rear = (rear + 1) % capacity;
        queue[rear] = element;
        size++;
    }
    
    // Удаление и возврат элемента из начала очереди
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        
        T element = queue[front];
        
        if (front == rear) {
            // Был только один элемент
            front = -1;
            rear = -1;
        } else {
            front = (front + 1) % capacity;
        }
        
        size--;
        return element;
    }
    
    // Просмотр первого элемента без удаления
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[front];
    }
    
    // Проверка на пустоту
    public boolean isEmpty() {
        return front == -1;
    }
    
    // Проверка на заполненность
    public boolean isFull() {
        return (rear + 1) % capacity == front;
    }
    
    // Текущий размер очереди
    public int size() {
        return size;
    }
    
    // Емкость очереди
    public int getCapacity() {
        return capacity;
    }
    
    // Очистка очереди
    @SuppressWarnings("unchecked")
    public void clear() {
        queue = (T[]) new Object[capacity];
        front = -1;
        rear = -1;
        size = 0;
    }
    
    // Вывод всех элементов (для отладки)
    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return;
        }
        
        System.out.print("Queue elements: ");
        int i = front;
        while (true) {
            System.out.print(queue[i] + " ");
            if (i == rear) break;
            i = (i + 1) % capacity;
        }
        System.out.println();
    }
}