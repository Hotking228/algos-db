# Стек (Stack) - push/pop

## Intuition
Стек — это структура данных, работающая по принципу LIFO (Last In, First Out) — "последним пришёл, первым ушёл". Представьте себе стопку тарелок: вы всегда берёте верхнюю тарелку (pop) и кладёте новую тоже наверх (push). Нельзя достать тарелку из середины, не сняв верхние. Это ограничение делает стек идеальным для многих алгоритмических задач.

## Approach
1. **push(item)** — добавляет элемент на вершину стека
2. **pop()** — удаляет и возвращает элемент с вершины стека
3. **peek()** / top() — возвращает элемент с вершины без удаления
4. **isEmpty()** — проверяет, пуст ли стек
5. **size()** — возвращает количество элементов

## Complexity
- Time complexity: $$O(1)$$ для всех операций (push, pop, peek, isEmpty)

- Space complexity: $$O(n)$$ для хранения n элементов

## Code

### Реализация через массив
```java
public class StackArray<T> {
    private T[] array;
    private int top;
    private static final int DEFAULT_CAPACITY = 10;
    
    @SuppressWarnings("unchecked")
    public StackArray() {
        array = (T[]) new Object[DEFAULT_CAPACITY];
        top = -1;
    }
    
    @SuppressWarnings("unchecked")
    public StackArray(int capacity) {
        array = (T[]) new Object[capacity];
        top = -1;
    }
    
    // Добавление элемента на вершину
    public void push(T item) {
        if (isFull()) {
            resize(); // увеличиваем размер массива
        }
        array[++top] = item;
    }
    
    // Удаление элемента с вершины
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T item = array[top];
        array[top--] = null; // помогаем GC
        return item;
    }
    
    // Просмотр верхнего элемента без удаления
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return array[top];
    }
    
    public boolean isEmpty() {
        return top == -1;
    }
    
    public boolean isFull() {
        return top == array.length - 1;
    }
    
    public int size() {
        return top + 1;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        T[] newArray = (T[]) new Object[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }
}
```